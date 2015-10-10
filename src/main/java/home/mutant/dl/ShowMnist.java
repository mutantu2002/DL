package home.mutant.dl;

import java.util.ArrayList;
import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.models.ImageDouble;
import home.mutant.dl.models.ImageFloat;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.ImageUtils;
import home.mutant.dl.utils.MnistDatabase;
import home.mutant.dl.utils.MnistDatabase.TYPE;

public class ShowMnist
{
	public static void main(String[] args) throws Exception
	{
		ResultFrame frame = new ResultFrame(1200, 600);
		MnistDatabase.IMAGE_TYPE = TYPE.FLOAT;
		MnistDatabase.loadImages();
		List<float[]> dividedImages = ImageUtils.divideImage(MnistDatabase.trainImages.get(0).getDataFloat(), 20, 20, 
				28, 28, 1, 1); 
		List<Image> images = new ArrayList<Image>();
		for (float[] image : dividedImages) {
			images.add(new ImageFloat(image));
		}
		frame.showImages(images);
		//frame.showImage(ImageUtils.scaleImage(ImageUtils.gradientImage(MnistDatabase.trainImages.get(0)), 10));
	}
}
