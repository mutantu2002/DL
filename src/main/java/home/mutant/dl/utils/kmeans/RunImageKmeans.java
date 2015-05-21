package home.mutant.dl.utils.kmeans;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

import java.util.ArrayList;
import java.util.List;


public class RunImageKmeans {
	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImagesCrop(20);
		List<Clusterable> clusterables = new ArrayList<Clusterable>();
		for (int i = 0; i < 10000; i++) {
			clusterables.add(new ImageClusterable(MnistDatabase.trainImages.get(i)));
		}
		List<Clusterable> centers = Kmeans.run(clusterables, 50);
		List<Image> imgs = new ArrayList<Image>();
		for (int i = 0; i < centers.size(); i++) {
			ImageClusterable c = (ImageClusterable) centers.get(i);
			imgs.add(c.getImage());
		}
		ResultFrame frame = new ResultFrame(600, 600);
		frame.showImages(imgs);
	}
}
