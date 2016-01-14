package home.mutant.dl.gaussian;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;
import home.mutant.dl.utils.MnistDatabase.TYPE;
import home.mutant.dl.utils.Utils;

import java.util.Arrays;

public class TransformImageClusters {

	public static void main(String[] args) throws Exception{
		MnistDatabase.IMAGE_TYPE = TYPE.FLOAT;
		MnistDatabase.loadImages();
		Image[] subImages = (Image[]) Utils.load("clusters7_64");
		ResultFrame frame = new ResultFrame(600, 600);
		frame.showImages(Arrays.asList(subImages),(int) Math.sqrt(subImages.length));
		Image transformed = MnistDatabase.trainImages.get(57).transformGauss(subImages);
		ResultFrame frame1 = new ResultFrame(600, 600);
		frame1.showImage(transformed);
	}

}
