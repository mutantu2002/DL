package home.mutant.dl.utils.kmeans;

import home.mutant.dl.utils.kmeans.model.Cluster;
import home.mutant.dl.utils.kmeans.model.Clusterable;
import home.mutant.dl.utils.kmeans.runnables.PopulateClustersRunnable;
import home.mutant.dl.utils.kmeans.runnables.UpdateClusterRunnable;
import home.mutant.dl.utils.multithreading.Launcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Kmeans
{
	private static final int NO_ITERATIONS = 10;
	private static final int NO_THREADS = 8;
	private static final int INFLUENCE = 1;
	private static final double MIN_DISTANCE_TO_ADD = 10;
	public static List<Cluster> run(List<Clusterable> list, int noClusters)
	{
		return run(list, noClusters, false);
	}
	public static List<Cluster> run(List<Clusterable> list, int noClusters, boolean updateClustersLabels)
	{
		return run(list, noClusters, updateClustersLabels, false);
	}
	public static List<Cluster> run(List<Clusterable> list, int noClusters, boolean updateClustersLabels, boolean influenceClusters)
	{
		List<Cluster> clusters = initializeClusters(list, noClusters);
	
		for (int i=0;i<NO_ITERATIONS;i++)
		{
			if (influenceClusters)
				influenceNeighboursCenters(clusters);
			populateClusters(list, clusters);
			//deleteZeroClusters(clusters);
			updateCenters(list, clusters);
			System.out.println("iteration "+i);
		}
		updateClustersLabels(list, clusters);
		return clusters;
	}
	private static void updateClustersLabels(List<Clusterable> list,List<Cluster> clusters) {
		for(int i=0 ; i<clusters.size(); i++)
		{
			Clusterable centre = clusters.get(i).center;
			List<Integer> clusterPopulation = clusters.get(i).members;
			HashMap<Integer, Integer> numbers = new HashMap<Integer, Integer>();
			for (Integer integer : clusterPopulation) {
				int label = list.get(integer).getLabel();
				if(numbers.get(label)==null){
					numbers.put(label, 0);
				}
				numbers.put(label, numbers.get(label)+1);
			}
			centre.setLabel(getMaxKeyHash(numbers));
		}
		
	}
	private static int getMaxKeyHash(HashMap<Integer, Integer> numbers){
		int max = Integer.MIN_VALUE;
		int maxKey=-1;
		for (Integer key:numbers.keySet()) {
			if (numbers.get(key)>max){
				max=numbers.get(key);
				maxKey = key;
			}
		}
		return maxKey;
	}
	public static void deleteZeroClusters(List<Cluster> clusters){
		for(int i=clusters.size()-1 ; i>=0; i--)
		{
			List<Integer> cluster = clusters.get(i).members;
			if (cluster.size()==0){
				clusters.remove(i);
			}
		}
	}
	
	private static List<Cluster> initializeClusters(List<Clusterable> list, int noCenters){
		List<Cluster> mClusters = new ArrayList<Cluster>();
		for (int i = 0; i < noCenters; ) {
			Clusterable newCenter = list.get((int) (Math.random()*list.size()));
			if (okToAddCenter(newCenter, mClusters)){
				Cluster cluster = new Cluster();
				mClusters.add(cluster);
				cluster.center = newCenter.copy();
				i++;
			}
		}
		return mClusters;
	}
	private static boolean okToAddCenter(Clusterable newCenter, List<Cluster> centers){
		for (Cluster cluster : centers) {
			if(newCenter.d(cluster.center)<MIN_DISTANCE_TO_ADD){
				return false;
			}
		}
		return true;
	}
	private static void populateClusters(List<Clusterable> list,List<Cluster> clusters)
	{
		for(int i=0 ; i<clusters.size(); i++)
		{
			clusters.get(i).members.clear();
		}

		Launcher launcher = new Launcher();
		int step = list.size() / NO_THREADS;
		
		for (int i = 0; i < NO_THREADS; i++) {
			launcher.addRunnable(new PopulateClustersRunnable(list.subList(i*step, (i+1)*step), clusters, i*step));
		}
		launcher.run();
	}
	
	private static void updateCenters(List<Clusterable> list,List<Cluster> clusters)
	{
		Launcher launcher = new Launcher();
		int step = clusters.size() / NO_THREADS;
		
		for (int i = 0; i < NO_THREADS-1; i++) {
			launcher.addRunnable(new UpdateClusterRunnable(clusters.subList(i*step, (i+1)*step), list));
		}
		launcher.addRunnable(new UpdateClusterRunnable(clusters.subList((NO_THREADS-1)*step, clusters.size()), list));
		launcher.run();
	}

	private static void influenceNeighboursCenters(List<Cluster> clusters) {
		List<Clusterable> updated = new ArrayList<Clusterable>();
		for (int j = 0; j<clusters.size(); j++)
		{
			Clusterable toUpdate = clusters.get(j).center.copy();
			updated.add(toUpdate);
			double[] centerWeights = clusters.get(j).center.getWeights();
			double[] centerWeights2 = clusters.get((j+1)%clusters.size()).center.getWeights();
			double[] centerWeights3 = clusters.get((j-1+clusters.size())%clusters.size()).center.getWeights();
			for(int w=0; w<centerWeights.length; w++)
			{
				toUpdate.getWeights()[w] = (INFLUENCE*centerWeights[w]+centerWeights2[w]+centerWeights3[w])/(INFLUENCE+2);
				if (Double.isNaN(toUpdate.getWeights()[w]) || Double.isInfinite(toUpdate.getWeights()[w]))
					System.out.println(toUpdate.getWeights()[w]);
			}
		}
		for (int j = 0; j<clusters.size(); j++)
		{
			clusters.get(j).center.setWeights(updated.get(j).getWeights());
		}
	}
	
	public static void updateClusterableLabelFromCenters(List<Clusterable> list,List<Cluster> clusters){
		for (int i = 0; i<list.size(); i++)
		{
			double minDistance = Double.MAX_VALUE;
			int minCluster=-1;
			for (int j = 0; j<clusters.size(); j++)
			{
				Clusterable centre = clusters.get(j).center;
				double distance = centre.d(list.get(i));
				if (distance<minDistance)
				{
					minDistance = distance;
					minCluster = j;
				}
			}
			list.get(i).setLabel(clusters.get(minCluster).center.getLabel());
		}
	}
}
