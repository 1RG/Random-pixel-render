package lt.rg.rpr.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import io.humble.video.Codec;
import io.humble.video.Encoder;
import io.humble.video.MediaPacket;
import io.humble.video.MediaPicture;
import io.humble.video.Muxer;
import io.humble.video.MuxerFormat;
import io.humble.video.PixelFormat;
import io.humble.video.Rational;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;

public class RenderLogic {
	
	public static int COLOR_RGB = 0;
	public static int COLOR_BW = 1;
	
	private int imageWidth = 100;
	private int imageHeight = 100;
	private int imageColor = 1;
	
	private int videoWidth = 100;
	private int videoHeight = 100;
	private int videoFps = 30;
	private int videoLength = 7;
	private int videoColor = 1;
	
	public void createImage(LogicNote logicNote) {
		//Start
		logicNote.runing(true);

		String format = "png";
		boolean usAlpha = false;
		
		try {
			
			int imageType = usAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_3BYTE_BGR;
			BufferedImage img = new BufferedImage(imageWidth, imageHeight, imageType);
			
			for (int y = 0; y < imageHeight; y++) {
				
				// Time			
				long start = System.currentTimeMillis();
				
				for (int x = 0; x < imageWidth; x++) {
					int r;
					int g;
					int b;
					
					if(imageColor == 0) {
						r = (int)(Math.random()*256);	//red
						g = (int)(Math.random()*256);	//green
						b = (int)(Math.random()*256);	//blue
					}else {
						r = g = b = (int)(Math.random()*256);
					}
					
					if(usAlpha) {
						int a = (int)(Math.random()*256);	//alpha
						img.setRGB(x, y, new Color(r, g, b, a).getRGB());
					}else {
						img.setRGB(x, y, new Color(r, g, b).getRGB());
					}
				}
				
				// Time
				long end = System.currentTimeMillis() - start;
				
				if(logicNote.isCancel()) {
					
					//On Cancel
					logicNote.display("Cancel");
					logicNote.runing(false);
					logicNote.setCancel(false);
					System.out.println("Loop Cancel");
					return;
				}else {
					if(end > 0) {
						// Time
						logicNote.display("Done: "+(100*y/imageHeight)+"% Time left: "+  (end + end*(imageHeight - y))/1000l +"s");
					}
				}
			}
			
			try {
				checkRenderFolder();
				File f = new File("render/image." + format);
				
				logicNote.display("Wraiting file...");
				
				ImageIO.write(img, format, f);
			} catch (Exception e) {
				System.out.println(e);
				logicNote.setStatus(e);
			}
		
		} catch (Exception e) {
			System.out.println(e);
			logicNote.setStatus(e);
		} catch (OutOfMemoryError e) {
			System.out.println(e);
			logicNote.setStatus(new Exception(e));
		}
		
		//End
		logicNote.display("Done");
		logicNote.displayAlert();
		logicNote.runing(false);
	}
	
	public void createVideo(LogicNote logicNote) {
		String filename = "render/test.mp4";
		String formatname = "mp4";
		int duration = videoLength;
		
		//Start
		logicNote.runing(true);
		
		checkRenderFolder();
		
		try {
			logicNote.display("Rendering...");
			
			Muxer muxer = Muxer.make(filename, null, formatname);
			
			MuxerFormat format = muxer.getFormat();
			Codec codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());
			
			Encoder encoder = Encoder.make(codec);

			Rational framerate = Rational.make(1, videoFps);
			
			encoder.setWidth(videoWidth);
			encoder.setHeight(videoHeight);
			PixelFormat.Type pixelformat = PixelFormat.Type.PIX_FMT_YUV420P;
			encoder.setPixelFormat(pixelformat);
			encoder.setTimeBase(framerate);
			
			if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER)) {
				encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, true);
			}
			
			encoder.open(null, null);
			
			muxer.addNewStream(encoder);
			
			muxer.open(null, null);
			
			MediaPictureConverter converter = null;
			MediaPicture picture = MediaPicture.make(videoWidth, videoHeight, pixelformat);
			picture.setTimeBase(framerate);
			
			MediaPacket packet = MediaPacket.make();
			
			int length = (int)(duration / framerate.getDouble());
			
			for (int i = 0; i < length; i++) {
				// Time			
				long start = System.currentTimeMillis();
				
				BufferedImage image = getImage();
				
				if (converter == null) {
					converter = MediaPictureConverterFactory.createConverter(image, picture);
				}
				converter.toPicture(picture, image, i);
				
				do {
					encoder.encode(packet, picture);
					if (packet.isComplete()) {
						muxer.write(packet, false);
					}
				} while (packet.isComplete());
				
				// Time
				long end = System.currentTimeMillis() - start;
				
				if(logicNote.isCancel()) {
					
					//On Cancel
					logicNote.display("Cancel");
					logicNote.runing(false);
					logicNote.setCancel(false);
					System.out.println("Loop Cancel");
					muxer.close();
					return;
				}else {
					if(end > 0) {
						// Time
						logicNote.display("Done: "+(100*i/length)+"% Time left: "+  (end + end*(length - i))/1000l +"s");
					}
				}
			}
			
			logicNote.display("Wraiting cache...");
			
			do {
				encoder.encode(packet, null);
				if (packet.isComplete()) {
					muxer.write(packet, false);
				}
			} while (packet.isComplete());
			
			muxer.close();
		} catch (Exception e) {
			System.out.println(e);
			logicNote.setStatus(e);
		} catch (OutOfMemoryError e) {
			System.out.println(e);
			logicNote.setStatus(new Exception(e));
		}
		
		//End
		System.out.println("Done");
		logicNote.display("Done");
		logicNote.displayAlert();
		logicNote.runing(false);
	}
	
	private BufferedImage getImage() {
		BufferedImage img = new BufferedImage(videoWidth, videoHeight, BufferedImage.TYPE_3BYTE_BGR);
		
		for (int y = 0; y < videoHeight; y++) {
			for (int x = 0; x < videoWidth; x++) {
				int r;
				int g;
				int b;
				
				if(videoColor == 0) {
					r = (int)(Math.random()*256);
					g = (int)(Math.random()*256);
					b = (int)(Math.random()*256);
				}else {
					r = g = b = (int)(Math.random()*256);
				}
				
				img.setRGB(x, y, new Color(r, g, b).getRGB());
			}
		}
		
		return img;
	}
	
	private void checkRenderFolder() {
		File file = new File("render");
		if(!file.isDirectory()) {
			file.mkdirs();
		}
	}

	public int getImageWidth() {
		return imageWidth;
	}

	public void setImageWidth(int width) {
		this.imageWidth = width;
	}

	public int getImageHeight() {
		return imageHeight;
	}

	public void setImageHeight(int height) {
		this.imageHeight = height;
	}

	public int getImageColor() {
		return imageColor;
	}

	public void setImageColor(int imageColor) {
		this.imageColor = imageColor;
	}

	public int getVideoWidth() {
		return videoWidth;
	}

	public void setVideoWidth(int width) {
		this.videoWidth = width;
	}

	public int getVideoHeight() {
		return videoHeight;
	}

	public void setVideoHeight(int height) {
		this.videoHeight = height;
	}

	public int getVideoFps() {
		return videoFps;
	}

	public void setVideoFps(int fps) {
		this.videoFps = fps;
	}

	public int getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(int length) {
		this.videoLength = length;
	}

	public int getVideoColor() {
		return videoColor;
	}

	public void setVideoColor(int videoColor) {
		this.videoColor = videoColor;
	}	
}
