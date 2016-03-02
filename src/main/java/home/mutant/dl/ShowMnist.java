package home.mutant.dl;

import home.mutant.dl.models.Image;
import home.mutant.dl.models.ImageFloat;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.ImageUtils;
import home.mutant.dl.utils.MathUtils;
import home.mutant.dl.utils.MnistDatabase;
import home.mutant.dl.utils.MnistDatabase.TYPE;

import java.util.ArrayList;
import java.util.List;

public class ShowMnist
{
	public static void main(String[] args) throws Exception
	{
		System.out.println(MathUtils.gaussian(50, 20));
		ResultFrame frame = new ResultFrame(1200, 600);
		MnistDatabase.IMAGE_TYPE = TYPE.FLOAT;
		MnistDatabase.loadImages();
		List<float[]> dividedImages = ImageUtils.divideImage(MnistDatabase.trainImages.get(10).getDataFloat(), 20, 20, 
				28, 28, 1, 1); 
		List<Image> images = new ArrayList<Image>();
		for (float[] image : dividedImages) {
			images.add(new ImageFloat(image));
		}
		frame.showImages(MnistDatabase.trainImages.subList(0, 25),5);
		//frame.showImage(ImageUtils.scaleImage(ImageUtils.gradientImage(MnistDatabase.trainImages.get(0)), 10));
	}
}
