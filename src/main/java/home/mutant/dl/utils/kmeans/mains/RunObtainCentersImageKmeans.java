package home.mutant.dl.utils.kmeans.mains;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;
import home.mutant.dl.utils.kmeans.Kmeans;
import home.mutant.dl.utils.kmeans.model.Cluster;
import home.mutant.dl.utils.kmeans.model.Clusterable;
import home.mutant.dl.utils.kmeans.model.ListClusterable;
import home.mutant.dl.utils.kmeans.model.SimpleClusterable;

import java.util.ArrayList;
import java.util.List;


public class RunObtainCentersImageKmeans {
	public static final int SUBIMAGE_SIZE = 4;
	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImages();
		List<Clusterable> clusterables = new ArrayList<Clusterable>();
		for (int i = 0; i < 60000; i++) {
				for (int j=0;j<100;j++)
					clusterables.add(new SimpleClusterable(MnistDatabase.trainImages.get(i).extractImage((int)((28-SUBIMAGE_SIZE)*Math.random()), (int)((28-SUBIMAGE_SIZE)*Math.random()), SUBIMAGE_SIZE, SUBIMAGE_SIZE).getDataDouble()));
		}
		List<Cluster> clusters = Kmeans.run(clusterables, 100,false,false);
		System.out.println(clusters.size());
		List<Image> imgs = new ArrayList<Image>();
		ListClusterable list = new ListClusterable();
		for (int i = 0; i < clusters.size(); i++) {
			SimpleClusterable c = (SimpleClusterable) clusters.get(i).center;
			list.clusterables.add(c);
			imgs.add(c.getImage());
		}
		ResultFrame frame = new ResultFrame(600, 600);
		frame.showImages(imgs);
		list.save("clusters"+SUBIMAGE_SIZE+".ser");
	}
}
