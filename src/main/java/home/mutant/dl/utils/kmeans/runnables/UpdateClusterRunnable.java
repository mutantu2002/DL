package home.mutant.dl.utils.kmeans.runnables;

import java.util.List;

import home.mutant.dl.utils.kmeans.model.Cluster;
import home.mutant.dl.utils.kmeans.model.Clusterable;

public class UpdateClusterRunnable implements Runnable {
	List<Clusterable> list;
	List<Cluster> clusters;
	
	public UpdateClusterRunnable(List<Cluster> clusters,List<Clusterable> list) {
		super();
		this.list = list;
		this.clusters = clusters;
	}
	@Override
	public void run() {
		for (int j = 0; j<clusters.size(); j++)
		{
			List<Integer> cluster = clusters.get(j).members;
			if (cluster.size()==0) continue;
			Clusterable center = clusters.get(j).center;
			center.updateCenterFromMembers(list, cluster);
		}

	}

}
