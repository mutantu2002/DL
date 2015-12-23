package home.mutant.dl.ui;


import home.mutant.dl.models.Image;

import java.util.List;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ResultFrame extends JFrame
{
	private int width = 800;
    private int height = 600;
    
    public RasterPanel drawingPanel;
    
    public ResultFrame(int width, int height,int x, int y, String title)
    {
    	this.width = width;
    	this.height = height;
    	setTitle(title);
    	setSize(this.width+20, this.height+50);
    	setLocation(x, y);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	drawingPanel = new RasterPanel (this.width,this.height);
        add (drawingPanel);
        drawingPanel.init();
        setVisible(true);
    }
    public ResultFrame(int width, int height, String title)
    {
    	this(width, height,0,0, title);
    }
    public ResultFrame(int width, int height)
    {
    	this(width, height, "Title");
    }
    
	public void buildFrame()
	{
		repaint();
	}
	


	
	public void showImage(Image image)
	{
		putImage(image, 0, 0);
		repaint();
	}
	public void showImage(Image image, int x, int y)
	{
		putImage(image, x, y);
		repaint();
	}
	
	public void showMnist(List<byte[][]> images, int index)
	{
		drawingPanel.empty();
		for (int n1=0;n1<20;n1++)
		{
			for (int i =0;i<28;i++)
			{
				for (int j=0;j<28;j++)
				{
					drawingPanel.setPixel(i+n1*28,j,images.get(index+n1)[i][j]);
				}
			}
		}
		repaint();
	}
	
	public void showMnist2(List<byte[]> images, int index)
	{
		drawingPanel.empty();
		for (int n1=0;n1<20;n1++)
		{
			int offset = 0;
			for (int i =0;i<28;i++)
			{
				for (int j=0;j<28;j++)
				{
					byte color = images.get(index+n1)[offset++];
					if (color ==1)
					{
						color = (byte)255;
					}
					drawingPanel.setPixel(j+n1*28,i,color);
				}
			}
		}
		repaint();
	}
	
	public void showImages(List<Image> images)
	{
		showImages(images, 0);
	}
	public void showImages(List<Image> images, int perLine)
	{
		final int imageX = images.get(0).imageX+2;
		final int imageY = images.get(0).imageY+2;
		if(perLine==0) perLine = width/imageX;
		int index=0;
		drawingPanel.empty();
		for (Image image : images) {
			int y=index/perLine;
			int x = index%perLine;
			putImage(image, x*imageX, y*imageY);
			index++;
		}
		repaint();
	}
	
	public void putImage(Image image, int xOffset, int yOffset)
	{
		for (int x=0;x<image.imageX;x++)
		{
			for (int y=0;y<image.imageY;y++)
			{
				drawingPanel.setPixel(xOffset+x,yOffset+y,(byte)image.getPixel(x, y));
			}
		}
	}
}
