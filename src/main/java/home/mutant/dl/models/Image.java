package home.mutant.dl.models;

import home.mutant.dl.utils.ImageUtils;

import java.awt.image.BufferedImage;

public abstract class Image
{
	public int imageX,imageY;
	
	public Image(int x, int y)
	{
		this.imageX=x;
		this.imageY=y;
	}
	
	public Image(int size)
	{
		this((int)Math.sqrt(size),(int)Math.sqrt(size));
	}
	
	public void convert(BufferedImage bufferedImage)
	{
		int offset=0;
		for (int y = 0; y < bufferedImage.getHeight(); y++) 
		{
			for (int x = 0; x < bufferedImage.getWidth(); x++) 
			{
                int c = bufferedImage.getRGB(x,y);
                int  color = (c & 0xff);
                setValue(offset, color);
                offset++;
			}
		}
	}
	
	public void convert(byte[][] bs) 
	{
		this.imageX = bs.length;
		this.imageY = bs[0].length;
		int offset=0;
		for (int x=0;x<imageX;x++) 
		{
			for (int y=0;y<imageY;y++)
			{
				setValue(offset,bs[x][y]);
				double value = getValue(offset);
				if (value<0)setValue(offset,value+256);
				offset++;
			}
		}
	}

	public void setPixel(int setX, int setY, double value)
	{
		setValue(setY*imageX+setX, value);
	}
	public double getPixel(int getX, int getY)
	{
		return getValue(getY*imageX+getX);
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
		Image extracted = createImage(sizeX, sizeY);
		for(int x=0;x<sizeX;x++)
		{
			for(int y=0;y<sizeY;y++)
			{
				extracted.setPixel(x, y, getPixel(origX+x, origY+y));
			}
		}
		return extracted;
	}
	public Image scaleImage(double scale) {
		Image scaled= createImage(this.imageX,this.imageY);
		BufferedImage bufferedImage = ImageUtils.createBufferedImage(this);
		scaled.convert(ImageUtils.scaleImage(bufferedImage, scale));
		return scaled;
	}
	
	public abstract void normalize();
	public abstract void setValue(int offset, double value);
	public abstract double getValue(int offset);
	public abstract double[] getDataDouble();
	public abstract float[] getDataFloat();
	public abstract Image createImage(int X, int Y);

}
