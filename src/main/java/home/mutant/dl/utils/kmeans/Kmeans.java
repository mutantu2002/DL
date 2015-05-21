package home.mutant.dl.utils.kmeans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kmeans
{
	private static final int NO_ITERATIONS = 40;

	public static List<Clusterable> run(List<Clusterable> list, int noClusters)
	{
		List<List<Integer>> clusters = new ArrayList<List<Integer>>();
		List<Clusterable> centers = initializeCenters(list, noClusters);
		for (int i=0;i<noClusters;i++)
		{
			clusters.add(new ArrayList<Integer>());
		}
		
		for (int i=0;i<NO_ITERATIONS;i++)
		{
			populateClusters(list, clusters, centers);
			recalculateClustersCenters(list, clusters, centers);
		}
		return centers;
	}
	
	private static List<Clusterable> initializeCenters(List<Clusterable> list, int noCenters){
		List<Clusterable> centers = new ArrayList<Clusterable>();
		for (int i = 0; i < noCenters; i++) {
			centers.add(list.get((int) (Math.random()*list.size())).copy());
		}
		return centers;
	}
	
	private static void populateClusters(List<Clusterable> list,List<List<Integer>> clusters, List<Clusterable> centers)
	{
		for(int i=0 ; i<clusters.size(); i++)
		{
			clusters.get(i).clear();
		}
		
		for (int i = 0; i<list.size(); i++)
		{
			double minDistance = Double.MAX_VALUE;
			int minCluster=-1;
			for (int j = 0; j<centers.size(); j++)
			{
				Clusterable centre = centers.get(j);
				double distance = centre.d(list.get(i));
				if (distance<minDistance)
				{
					minDistance = distance;
					minCluster = j;
				}
			}
			clusters.get(minCluster).add(i);
		}
	}
	
	private static void recalculateClustersCenters(List<Clusterable> list,List<List<Integer>> clusters, List<Clusterable> centers)
	{
		for (int j = 0; j<clusters.size(); j++)
		{
			List<Integer> cluster = clusters.get(j);
			Clusterable center = centers.get(j);
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
