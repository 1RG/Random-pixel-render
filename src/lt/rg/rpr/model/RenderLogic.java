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
	private int width = 100;
	private int height = 100;
	private int fps = 30;
	private int time_ms = 7000;
	
	public void createImage() {
		checkRenderFolder();
		
		String format = "png";
		boolean usAlpha = false;
		
		int imageType = usAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
		BufferedImage img = new BufferedImage(width, height, imageType);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
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
			
			AWTSequenceEncoder se = new AWTSequenceEncoder(out, new Rational(fps, 1));

			for (int j = 0; j < ( time_ms/1000 ) * fps; j++) {
//				se.encodeImage(getImage());
				se.encodeImage(getImageX2());	
			}

			se.finish();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		System.out.println("Done");
	}
	
	private BufferedImage getImage() {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int a = (int)(Math.random()*256);

				img.setRGB(x, y, new Color(a, a, a).getRGB());
			}
		}
		
		return img;
	}
	
	private BufferedImage getImageX2() {
		BufferedImage img = new BufferedImage(width*2, height*2, BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < height*2; y+=2) {
			for (int x = 0; x < width*2; x+=2) {
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
			width = height = i;
			try {
				SeekableByteChannel out = NIOUtils.writableFileChannel("render/video.mp4");
				AWTSequenceEncoder se = new AWTSequenceEncoder(out, new Rational(1, 1));
				se.encodeImage(getImage());
				se.finish();
				System.out.println("OK: "+ width + "x"+ height);
			} catch (Exception e) {
//					System.out.println("FAIL: "+ width + "x"+ height);
			}
		}
	}
	
	private void checkRenderFolder() {
		File file = new File("render");
		if(!file.isDirectory()) {
			file.mkdirs();
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
