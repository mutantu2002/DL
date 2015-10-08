package home.mutant.dl.utils.kmeans.mains;

import home.mutant.dl.utils.MnistDatabase;
import home.mutant.dl.utils.kmeans.Kmeans;
import home.mutant.dl.utils.kmeans.model.Cluster;
import home.mutant.dl.utils.kmeans.model.Clusterable;
import home.mutant.dl.utils.kmeans.model.SimpleClusterable;

import java.util.ArrayList;
import java.util.List;


public class RunImageKmeans {
	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImages();
		List<Clusterable> clusterables = new ArrayList<Clusterable>();
		for (int i = 0; i < 60000; i++) {
			clusterables.add(new SimpleClusterable(MnistDatabase.trainImages.get(i).getDataDouble(),MnistDatabase.trainLabels.get(i)));
		}
		
		List<Clusterable> clusterablesTest = new ArrayList<Clusterable>();
		for (int i = 0; i < 10000; i++) {
			clusterablesTest.add(new SimpleClusterable(MnistDatabase.testImages.get(i).getDataDouble()));
		}

		System.out.println(run(clusterables,clusterablesTest));
	}
	
	public static int run(List<Clusterable> train, List<Clusterable> test){
		List<Cluster> clusters = Kmeans.run(train, 1000,true);
		Cluster.showCenters(clusters);
		System.out.println("No centers "+clusters.size());
		Kmeans.updateClusterableLabelFromCenters(test, clusters);
		int count=0;
		for (int i = 0; i < test.size(); i++) {
			if (test.get(i).getLabel()==MnistDatabase.testLabels.get(i)){
				count++;
			}
		}
		return count;
	}
}
