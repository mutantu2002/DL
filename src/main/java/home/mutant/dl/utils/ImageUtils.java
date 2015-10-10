package home.mutant.dl.utils;

import home.mutant.dl.models.Image;
import home.mutant.dl.models.ImageDouble;
import home.mutant.dl.models.ImageFloat;
import home.mutant.dl.utils.MnistDatabase.TYPE;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageUtils 
{
	public static void readResourceImage(String imageResourcePath, byte[] inValues) throws IOException 
	{
		readImage(ImageIO.read(ImageUtils.class.getResourceAsStream(imageResourcePath)), inValues);
	}
	public static void readImage(BufferedImage image, byte[] inValues) throws IOException 
	{
		int offset=0;
		for (int y = 0; y < image.getHeight(); y++) 
		{
			for (int x = 0; x < image.getWidth(); x++) 
			{
                int c = image.getRGB(x,y);
                int  color = (c & 0x00ffffff);
                
                inValues[offset]=0;
                if (color>0)
                {
                	inValues[offset]=1;
                }
                offset++;
			}
		}
	}
	
	public static List<Integer> readMinstLabels(String labelsResourcePath)
	{
		List<Integer> labels = new ArrayList<Integer>();
		InputStream stream = ImageUtils.class.getResourceAsStream(labelsResourcePath);
		byte[] bRead = new byte[8];
		try
		{
			stream.read(bRead);
		} 
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		bRead = new byte[1];
		while(true)
		{
			try
			{
				if (stream.read(bRead)!=1)
				{
					break;
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
				break;
			}
			labels.add((int)bRead[0]);
		}
		return labels;
	}
	
	public static List<Image> readMnistAsImage(String imageResourcePath)
	{
		return readMnistAsImage(imageResourcePath, 28);
	}

	public static List<Image> readMnistAsImage(String imageResourcePath, int newImageSize)
	{
		List<byte[][]> bytes = readMnist(imageResourcePath, newImageSize);
		List<Image> images = new ArrayList<Image>();
		for (byte[][] bs : bytes) 
		{
			Image newImage;
			if (MnistDatabase.IMAGE_TYPE==TYPE.DOUBLE)
			{
				newImage = new ImageDouble(bs);
			}
			else
			{
				newImage = new ImageFloat(bs);
			}
			images.add(newImage);
		}
		return images;
	}
	
	public static List<byte[][]> readMnist(String imageResourcePath)
	{
		return readMnist(imageResourcePath, 28);
	}
	public static List<byte[][]> readMnist(String imageResourcePath, int size)
	{
		List<byte[][]> images = new ArrayList<byte[][]>();
		InputStream stream = ImageUtils.class.getResourceAsStream(imageResourcePath);
		byte[] bRead;
		try
		{
			readExactly(stream, 16);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		int crop = (28 - size)/2;
		while(true)
		{
			byte[][] image = new byte[size][size];
			
			try
			{
				bRead = readExactly(stream, 28*28);
				if (bRead == null)
				{
					break;
				}
			} 
			catch (IOException e)
			{
				e.printStackTrace();
				break;
			}
			for (int i = 0;i<size;i++)
			{
				for (int j = 0;j<size;j++)
				{
					image[j][i] = bRead[(j+crop)*28+i+crop];
				}
			}
			images.add(image);
		}
		return images;
	}
	
	static byte[] readExactly(InputStream is, int noBytes) throws IOException
	{
		byte[] res = new byte[noBytes];
		int offset=0;
		int read=0;
		while(offset<noBytes)
		{
			read = is.read(res,offset,noBytes-offset);
			offset+=read;
			if (read == -1)
			{
				return null;
			}
		}
		return res;
	}
	
	public static List<double[]> divideImage(double[] image, int newX, int newY, int actualX, int actualY)
	{
		return divideImage(image, newX, newY, actualX, actualY, newX, newY);
	}
	
	public static List<double[]> divideSquareImage(double[] image, int newDim, int step)
	{
		int actualX = (int) Math.sqrt(image.length);
		return divideImage(image, newDim, newDim, actualX, actualX, step, step);
	}
	public static List<double[]> divideSquareImage(double[] image, int newDim)
	{
		int actualX = (int) Math.sqrt(image.length);
		return divideImage(image, newDim, newDim, actualX, actualX, 1, 1);
	}
	
	public static double[] divideSquareImageUnidimensional(double[] image, int newDim)
	{
		int actualX = (int) Math.sqrt(image.length);
		return divideImageUnidimensional(image, newDim, newDim, actualX, actualX, 1, 1);
	}
	
	public static double[] divideImageUnidimensional(double[] image, int newX, int newY, int actualX, int actualY, int stepX, int stepY)
	{
		double[] dividedImages = new double[newX*newY*((actualY-newY+1)/stepY)*((actualX-newX+1)/stepX)];
		int offsetImage = 0;
		for (int y=0; y<=actualY-newY; y+=stepY)
		{
			for (int x=0; x<=actualX-newX; x+=stepX)
			{
				for (int imageY = y; imageY<y+newY; imageY++)
				{
					for (int imageX = x; imageX<x+newX; imageX++)
					{
						dividedImages[offsetImage++] = image[imageY*actualX+imageX];
					}
				}
			}		
		}
		return dividedImages;
	}
	
	public static List<double[]> divideImage(double[] image, int newX, int newY, int actualX, int actualY, int stepX, int stepY)
	{
		List<double[]> dividedImages = new ArrayList<double[]>();
		for (int y=0; y<=actualY-newY; y+=stepY)
		{
			for (int x=0; x<=actualX-newX; x+=stepX)
			{
				double[] newImage = new double[newX*newY];
				int offsetImage = 0;
				for (int imageY = y; imageY<y+newY; imageY++)
				{
					for (int imageX = x; imageX<x+newX; imageX++)
					{
						newImage[offsetImage++] = image[imageY*actualX+imageX];
					}
				}
				dividedImages.add(newImage);
			}		
		}
		return dividedImages;
	}
	public static List<float[]> divideImage(float[] image, int newX, int newY, int actualX, int actualY, int stepX, int stepY)
	{
		List<float[]> dividedImages = new ArrayList<float[]>();
		for (int y=0; y<=actualY-newY; y+=stepY)
		{
			for (int x=0; x<=actualX-newX; x+=stepX)
			{
				float[] newImage = new float[newX*newY];
				int offsetImage = 0;
				for (int imageY = y; imageY<y+newY; imageY++)
				{
					for (int imageX = x; imageX<x+newX; imageX++)
					{
						newImage[offsetImage++] = image[imageY*actualX+imageX];
					}
				}
				dividedImages.add(newImage);
			}		
		}
		return dividedImages;
	}
	public static Image affineTransform(Image image, int offsetX, int offsetY, double theta)
	{
		AffineTransform tx = new AffineTransform();
		tx.translate(offsetX, offsetY);
		tx.rotate(theta, image.imageX/2, image.imageY/2);
		Image dest = image.createImage(image.imageX, image.imageY);
		for (int x=0;x<dest.imageX;x++)
		{
			for (int y=0;y<dest.imageY;y++)
			{
				Point ptDst = new Point(x, y);
				try 
				{
					tx.inverseTransform(new Point(x, y), ptDst);
					if (ptDst.x>=0 && ptDst.x<image.imageX && ptDst.y>=0 && ptDst.y<image.imageY)
					{
						dest.setPixel(x, y, image.getPixel(ptDst.x, ptDst.y));
					}
					else
					{
						dest.setPixel(x, y, (byte)0);
					}
				} 
				catch (NoninvertibleTransformException e) 
				{
					dest.setPixel(x, y, (byte)0);
				}
			}
		}
		return dest;
	}
	
	public static void loadImages(List<Image> trainImages, List<Image> testImages, List<Integer> trainLabels, List<Integer> testLabels) throws IOException
	{
		trainLabels.addAll(ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte"));
		testLabels.addAll(ImageUtils.readMinstLabels("/mnist/t10k-labels.idx1-ubyte"));
		trainImages.addAll(ImageUtils.readMnistAsImage("/mnist/train-images.idx3-ubyte"));
		testImages.addAll(ImageUtils.readMnistAsImage("/mnist/t10k-images.idx3-ubyte"));			
	}
	
	public static Image blurImage(Image image)
	{
		Image dest = image.createImage(image.imageX, image.imageY);
		for (int x=1;x<image.imageX-1;x++)
		{
			for (int y=1;y<image.imageY-1;y++)
			{
				double newPixel=0;
				for(int xo=-1;xo<2;xo++)
				{
					for(int yo=-1;yo<2;yo++)
					{
						newPixel+=image.getPixel(x+xo, y+yo);
					}
				}
				dest.setPixel(x,y,(float) newPixel/9);
			}
		}

		return dest;
		
	}
	
	public static Image gradientImage(Image image)
	{
		Image dest = image.createImage(image.imageX, image.imageY);
		for (int x=1;x<image.imageX-1;x++)
		{
			for (int y=1;y<image.imageY-1;y++)
			{
				double y1 = image.getPixel(x, y-1);
				y1-= image.getPixel(x, y+1);
				double x1 = image.getPixel(x-1, y);
				x1-= image.getPixel(x+1, y);
				dest.setPixel(x,y,(float) Math.sqrt((y1*y1+x1*x1)/2.));
			}
		}
		for (int x=1;x<image.imageX-1;x++)
		{
			double y1 = -1*image.getPixel(x, 1);
			double x1 = image.getPixel(x-1, 0);
			x1-= image.getPixel(x+1, 0);
			dest.setPixel(x,0,(float)Math.sqrt((y1*y1+x1*x1)/2.));
			
			y1 = image.getPixel(x, image.imageY-2);
			x1 = image.getPixel(x-1, image.imageY-1);
			x1-= image.getPixel(x+1, image.imageY-1);
			dest.setPixel(x,image.imageY-1,(float)Math.sqrt((y1*y1+x1*x1)/2.));
		}
		
		for (int y=1;y<image.imageY-1;y++)
		{
			double y1 = -1*image.getPixel(1, y);
			double x1 = image.getPixel(0,y-1);
			x1-= image.getPixel(0,y+1);
			dest.setPixel(0,y,(float)Math.sqrt((y1*y1+x1*x1)/2.));
			
			y1 = image.getPixel(image.imageX-2,y);
			x1 = image.getPixel(image.imageX-1, y-1);
			x1-= image.getPixel(image.imageX-1, y+1);
			dest.setPixel(image.imageX-1,y,(byte)Math.sqrt((y1*y1+x1*x1)/2.));
		}
		double x1=-1*image.getPixel(0,1);
		double y1=-1*image.getPixel(1,0);
		dest.setPixel(0,0,(float)Math.sqrt((y1*y1+x1*x1)/2.));
		
		x1=image.getPixel(image.imageX-2,0);
		y1=-1*image.getPixel(image.imageX-1,1);
		dest.setPixel(image.imageX-1,0,(float)Math.sqrt((y1*y1+x1*x1)/2.));
		
		x1=image.getPixel(0,image.imageY-2);
		y1=-1*image.getPixel(1,image.imageY-1);
		dest.setPixel(0,image.imageY-1,(float)Math.sqrt((y1*y1+x1*x1)/2.));
		
		x1=image.getPixel(image.imageX-1,image.imageY-2);
		y1=-1*image.getPixel(image.imageX-2,image.imageY-1);
		dest.setPixel(image.imageX-1,image.imageY-1,(float)Math.sqrt((y1*y1+x1*x1)/2.));
		
		return dest;
	}
	
	public static BufferedImage createBufferedImage(Image image)
	{
		BufferedImage originalImage = new BufferedImage(image.imageX, image.imageY,BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < originalImage.getHeight(); y++) 
		{
			for (int x = 0; x < originalImage.getWidth(); x++) 
			{
				float pixel = (float)image.getPixel(x, y);
				pixel/=256;
				originalImage.setRGB(x, y, new Color(pixel, pixel, pixel).getRGB());
			}
		}
		return originalImage;
	}
	
	public static BufferedImage scaleImage(BufferedImage before, double scale)
	{
		int w = before.getWidth();
		int h = before.getHeight();
		BufferedImage after = new BufferedImage((int)(w*scale), (int)(h*scale), BufferedImage.TYPE_INT_RGB);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		after = scaleOp.filter(before, after);
		return after;
	}
}
