package home.mutant.dl.models;

import java.awt.image.BufferedImage;

public class Image 
{
	public int imageX,imageY;
	public double[] data = null ;

	
	public Image(double[] data, int x, int y)
	{
		this.imageX=x;
		this.imageY=y;
		this.data = data;
	}
	public Image(double[] data)
	{
		this(data, (int)Math.sqrt(data.length), (int)Math.sqrt(data.length));
	}

	public Image(int x, int y)
	{
		this.data = new double[x*y];
		this.imageX = x;
		this.imageY = y;
	}
	
	public Image(int size)
	{
		this((int)Math.sqrt(size),(int)Math.sqrt(size));
	}
	
	public Image(BufferedImage bufferedImage)
	{
		this(bufferedImage.getWidth(),bufferedImage.getHeight());
		int offset=0;
		for (int y = 0; y < bufferedImage.getHeight(); y++) 
		{
			for (int x = 0; x < bufferedImage.getWidth(); x++) 
			{
                int c = bufferedImage.getRGB(x,y);
                int  color = (c & 0xff);
                data[offset] = color;
                offset++;
			}
		}
	}
	
	public Image(byte[][] bs) 
	{
		this.imageX = bs.length;
		this.imageY = bs[0].length;
		this.data = new double[imageX*imageY];
		int offset=0;
		for (int x=0;x<imageX;x++) 
		{
			for (int y=0;y<imageY;y++)
			{
				data[offset] = bs[x][y];
				if (data[offset]<0)data[offset]+=256;
				offset++;
			}
		}
	}

	public void setPixel(int setX, int setY, double value)
	{
		data[setY*imageX+setX] = value;
	}
	public double getPixel(int getX, int getY)
	{
		return data[getY*imageX+getX];
	}
	
	public void pasteImage(Image smallImage, int origX, int origY)
	{
		for(int x=0;x<smallImage.imageX;x++)
		{
			for(int y=0;y<smallImage.imageY;y++)
			{
				setPixel(origX+x, origY+y, smallImage.getPixel(x, y));
			}
		}
	}
	public Image extractImage(int origX, int origY, int sizeX, int sizeY)
	{
		Image extracted = new Image(sizeX, sizeY);
		for(int x=0;x<sizeX;x++)
		{
			for(int y=0;y<sizeY;y++)
			{
				extracted.setPixel(x, y, getPixel(origX+x, origY+y));
			}
		}
		return extracted;
	}
	public void normalize(){
		for(int i=0;i<data.length;i++){
			data[i] = data[i]/128-1;
		}
	}
}
