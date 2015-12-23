package home.mutant.dl.utils.kmeans.model;

import java.util.List;

public class ClusterablePreDistance extends SimpleClusterable {
	private static final long serialVersionUID = 8458664894066029818L;
	public static double preDistances[][];
	
	public ClusterablePreDistance(double[] weights) {
		super(weights);
	}
	public ClusterablePreDistance(int size) {
		super(size);
	}
	public ClusterablePreDistance(double[] newImage, int label) {
		super(newImage, label);
	}
	@Override
	public Clusterable copy() {
		ClusterablePreDistance m = new ClusterablePreDistance(weights.length);
		System.arraycopy(weights, 0, m.getWeights(), 0, weights.length);
		return m;
	}
	@Override
	public double d(Clusterable clusterable) {
		double d=0;
		for (int i = 0; i < weights.length; i++) {
			d+=preDistances[(int) weights[i]][(int) clusterable.getWeights()[i]];
		}
		return d;
	}
	@Override
	public void updateCenterFromMembers(List<Clusterable> allList, List<Integer> cluster) {
		for(int w=0; w<weights.length; w++)
		{
			weights[w]=0;
			double min=Double.MAX_VALUE;
			for (int allIndex=0;allIndex<preDistances.length;allIndex++)
			{
				double sum=0;
				for (int i = 0; i<cluster.size(); i++)
				{
					sum+=preDistances[allIndex][(int) allList.get(cluster.get(i)).getWeights()[w]];
				}
				if(sum<min){
					min=sum;
					weights[w]=allIndex;
				}
			}
		}
	}
}
