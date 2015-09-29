package home.mutant.dl.utils.kmeans.runnables;

import home.mutant.dl.utils.kmeans.model.Cluster;
import home.mutant.dl.utils.kmeans.model.Clusterable;

import java.util.Arrays;
import java.util.List;

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
			double[] centerWeights = center.getWeights();
			Arrays.fill(centerWeights, 0);
			
			for(int w=0; w<centerWeights.length; w++)
			{
				for (int i = 0; i<cluster.size(); i++)
				{
					centerWeights[w]+=list.get(cluster.get(i)).getWeights()[w];
				}
				centerWeights[w]/=cluster.size();
			}
		}

	}

}
