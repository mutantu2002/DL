package home.mutant.dl;

import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

public class ShowMnist
{
	public static void main(String[] args) throws Exception
	{
		//List<Integer> labels = ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte");
		
		ResultFrame frame = new ResultFrame(1200, 600);
		//frame.showMnist2(ImageUtils.convertToBW(images), 0);
		//frame.showImages(ImageUtils.readMnistAsBWImage("/mnist/train-images.idx3-ubyte"), 0, 20);
		MnistDatabase.loadImagesScaled(0.5);
		frame.showImages(MnistDatabase.trainImages.subList(0, 100));
		//frame.showImage(ImageUtils.scaleImage(ImageUtils.gradientImage(MnistDatabase.trainImages.get(0)), 10));
	}
}
