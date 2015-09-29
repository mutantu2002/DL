package home.mutant.dl;

import java.util.ArrayList;
import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.ImageUtils;
import home.mutant.dl.utils.MnistDatabase;

public class ShowMnist
{
	public static void main(String[] args) throws Exception
	{
		//List<Integer> labels = ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte");
		ResultFrame frame = new ResultFrame(1200, 600);
		//frame.showMnist2(ImageUtils.convertToBW(images), 0);
		//frame.showImages(ImageUtils.readMnistAsBWImage("/mnist/train-images.idx3-ubyte"), 0, 20);
		MnistDatabase.loadImages();
		List<double[]> dividedImages = ImageUtils.divideImage(MnistDatabase.trainImages.get(0).data, 20, 20, 
				28, 28, 1, 1);
		List<Image> images = new ArrayList<Image>();
		for (double[] image : dividedImages) {
			images.add(new Image(image));
		}
		frame.showImages(images);
		//frame.showImage(ImageUtils.scaleImage(ImageUtils.gradientImage(MnistDatabase.trainImages.get(0)), 10));
	}
}
