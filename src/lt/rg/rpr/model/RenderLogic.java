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
	private int imageWidth = 100;
	private int imageHeight = 100;
	
	private int videoWidth = 100;
	private int videoHeight = 100;
	private int videoFps = 30;
	private int videoLength_ms = 7000;
	
	public void createImage() {
		checkRenderFolder();
		
		String format = "png";
		boolean usAlpha = false;
		
		int imageType = usAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
		BufferedImage img = new BufferedImage(imageWidth, imageHeight, imageType);
		
		for (int y = 0; y < imageHeight; y++) {
			for (int x = 0; x < imageWidth; x++) {
				int a = (int)(Math.random()*256);	//alpha
				int r = a;//(int)(Math.random()*256);	//red
				int g = a;//(int)(Math.random()*256);	//green
				int b = a;//(int)(Math.random()*256);	//blue
				
				if(usAlpha) {
					img.setRGB(x, y, new Color(r, g, b, a).getRGB());
				}else {
					img.setRGB(x, y, new Color(r, g, b).getRGB());
				}
			}
		}
		
		try {
			File f = new File("render/image." + format);
			ImageIO.write(img, format, f);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		System.out.println("Done");
	}
	
	public void createVideo() {
		checkRenderFolder();
		
		try {
			SeekableByteChannel out = NIOUtils.writableFileChannel("render/video.mp4");
			
			AWTSequenceEncoder se = new AWTSequenceEncoder(out, new Rational(videoFps, 1));

			for (int j = 0; j < ( videoLength_ms/1000 ) * videoFps; j++) {
				se.encodeImage(getImage());
//				se.encodeImage(getImageX2());	
			}

			se.finish();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		System.out.println("Done");
	}
	
	private BufferedImage getImage() {
		BufferedImage img = new BufferedImage(videoWidth, videoHeight, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < videoHeight; y++) {
			for (int x = 0; x < videoWidth; x++) {
				int a = (int)(Math.random()*256);

				img.setRGB(x, y, new Color(a, a, a).getRGB());
			}
		}
		
		return img;
	}
	
	private BufferedImage getImageX2() {
		BufferedImage img = new BufferedImage(videoWidth*2, videoHeight*2, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < videoHeight*2; y+=2) {
			for (int x = 0; x < videoWidth*2; x+=2) {
				int a = (int)(Math.random()*256);

				img.setRGB(x, y, new Color(a, a, a).getRGB());
				img.setRGB(x, y+1, new Color(a, a, a).getRGB());
				img.setRGB(x+1, y, new Color(a, a, a).getRGB());
				img.setRGB(x+1, y+1, new Color(a, a, a).getRGB());
			}
		}
		
		return img;
	}
	
	public void testVideoSupport() {
		for (int i = 1; i <= 1000; i++) {
			videoWidth = videoHeight = i;
			SeekableByteChannel out = null;
			try {
				out = NIOUtils.writableFileChannel("render/video.mp4");
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
}
