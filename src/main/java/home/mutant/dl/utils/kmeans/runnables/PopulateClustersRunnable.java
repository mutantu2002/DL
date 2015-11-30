package home.mutant.dl.utils.kmeans.runnables;

import home.mutant.dl.utils.kmeans.model.Cluster;
import home.mutant.dl.utils.kmeans.model.Clusterable;

import java.util.List;

public class PopulateClustersRunnable implements Runnable {
	List<Clusterable> list;
	List<Cluster> clusters;

	int offset;
	public PopulateClustersRunnable(List<Clusterable> list, List<Cluster> clusters, int offset) {
		super();
		this.list = list;
		this.clusters = clusters;
		this.offset = offset;
	}

	@Override
	public void run() {
		for (int i = 0; i<list.size(); i++){
			int closestCLusterIndex = Cluster.getClosestCLusterIndex(clusters, list.get(i));
			List<Integer> clusterList = clusters.get(closestCLusterIndex).members;
			synchronized(clusterList){
				clusterList.add(i+offset);
			}
		}
	}
}
