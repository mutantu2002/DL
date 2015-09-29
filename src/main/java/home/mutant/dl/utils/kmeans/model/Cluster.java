package home.mutant.dl.utils.kmeans.model;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;

import java.util.ArrayList;
import java.util.List;

public class Cluster {
	public Clusterable center;
	public List<Integer> members = new ArrayList<Integer>();
	
	public static int getClosestCLusterIndex(List<Cluster> clusters, Clusterable clusterable){
		double minDistance = Double.MAX_VALUE;
		int minCluster=-1;
		for (int j = 0; j<clusters.size(); j++){
			Clusterable centre = clusters.get(j).center;
			double distance = centre.d(clusterable);
			if (distance<minDistance){
				minDistance = distance;
				minCluster = j;
			}
		}
		return minCluster;
	}
	
	public static void showCenters(List<Cluster> clusters){
		List<Image> imgs = new ArrayList<Image>();
		for (int i = 0; i < clusters.size(); i++) {
			Clusterable c = clusters.get(i).center;
			imgs.add(c.getImage());
		}
		ResultFrame frame = new ResultFrame(600, 600);
		frame.showImages(imgs);
	}
}
