package lt.rg.rpr.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.jcodec.api.awt.AWTSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

public class RenderLogic {
	
	public static int COLOR_RGB = 0;
	public static int COLOR_BW = 1;
	
	private int imageWidth = 100;
	private int imageHeight = 100;
	private int imageColor = 1;
	
	private int videoWidth = 100;
	private int videoHeight = 100;
	private int videoFps = 30;
	private int videoLength_ms = 7000;
	private int videoColor = 1;
	
	public void createImage(LogicNote logicNote) {
		//Start
		logicNote.runing(true);

		String format = "png";
		boolean usAlpha = false;
		
		int imageType = usAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
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
					logicNote.display("Time left: "+  (end + end*(imageHeight - y))/1000l +"s");
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
		
		//End
		logicNote.display("Done");
		logicNote.displayAlert();
		logicNote.runing(false);
	}
	
	public void createVideo(LogicNote logicNote) {
		//Start
		logicNote.runing(true);
		
		checkRenderFolder();
		SeekableByteChannel out = null;
		try {
			 out = NIOUtils.writableFileChannel("render/video.mp4");
			
			AWTSequenceEncoder se = new AWTSequenceEncoder(out, new Rational(videoFps, 1));

			int length = (int)(( (double)videoLength_ms / 1000 ) * videoFps);
			
			for (int j = 0; j < length; j++) {
				
				// Time			
				long start = System.currentTimeMillis();
				
				se.encodeImage(getImage());
				
				// Time
				long end = System.currentTimeMillis() - start;
				
				if(logicNote.isCancel()) {
					
					//On Cancel
					logicNote.display("Cancel");
					logicNote.runing(false);
					logicNote.setCancel(false);
					System.out.println("Loop Cancel");
					se.finish();
					NIOUtils.closeQuietly(out);
					return;
				}else {
					if(end > 0) {
						// Time
						logicNote.display("Time left: "+  (end + end*(length - j))/1000l +"s");
					}
				}
			}
			
			logicNote.display("Wraiting file...");
			
			se.finish();
		} catch (Exception e) {
			System.out.println(e);
			logicNote.setStatus(e);
		}finally {
			NIOUtils.closeQuietly(out);
		}
		
		//End
		System.out.println("Done");
		logicNote.display("Done");
		logicNote.displayAlert();
		logicNote.runing(false);
		
//		testVideoSupport();
	}
	
	private BufferedImage getImage() {
		BufferedImage img = new BufferedImage(videoWidth, videoHeight, BufferedImage.TYPE_INT_RGB);
		
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

	public void testVideoSupport() {
		for (int i = 1; i <= 1000; i++) {
			videoWidth = videoHeight = i;
			SeekableByteChannel out = null;
			try {
				out = NIOUtils.writableFileChannel("render/support_test_video.mp4");
				AWTSequenceEncoder se = new AWTSequenceEncoder(out, new Rational(1, 1));
				se.encodeImage(getImage());
				se.finish();
				System.out.println("OK: "+ videoWidth + "x"+ videoHeight);
			} catch (Exception e) {
//					System.out.println("FAIL: "+ width + "x"+ height);
			} finally {
				NIOUtils.closeQuietly(out);
			}
		}
		
		System.out.println("Support test done");
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

	public int getVideoLength_ms() {
		return videoLength_ms;
	}

	public void setVideoLength_ms(int length) {
		this.videoLength_ms = length;
	}

	public int getVideoColor() {
		return videoColor;
	}

	public void setVideoColor(int videoColor) {
		this.videoColor = videoColor;
	}	
}
