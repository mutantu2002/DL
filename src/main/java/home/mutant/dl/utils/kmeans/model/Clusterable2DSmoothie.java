package home.mutant.dl.utils.kmeans.model;

import java.util.List;

import home.mutant.dl.models.Image;

public class Clusterable2DSmoothie extends SimpleClusterable {
	private static final long serialVersionUID = -996388060421011310L;
	public double[][] weights2d;
	public Clusterable2DSmoothie(double[] weights) {
		super(weights);
	}
	public Clusterable2DSmoothie(int size) {
		super(size);
	}
	public Clusterable2DSmoothie(double[] newImage, int label) {
		super(newImage, label);
	}

	@Override
	public double d(Clusterable clusterable) {
		Clusterable2DSmoothie clusterable2d=(Clusterable2DSmoothie) clusterable;
		
		double d=0;
		for (int i = 0; i < weights.length; i++) {
			d+=(weights2d[0][i]-clusterable2d.weights2d[0][i])*(weights2d[0][i]-clusterable2d.weights2d[0][i])+
					(weights2d[1][i]-clusterable2d.weights2d[1][i])*(weights2d[1][i]-clusterable2d.weights2d[1][i]);
		}
		return Math.sqrt(d);
	}
	@Override
	public Clusterable copy() {
		Clusterable2DSmoothie m = new Clusterable2DSmoothie(weights.length);
		System.arraycopy(weights, 0, m.getWeights(), 0, weights.length);
		return m;
	}
	@Override
	public void updateCenterFromMembers(List<Clusterable> allList, List<Integer> cluster) {
		for(int w=0; w<weights.length; w++)
		{
			int w0=0;
			int w1=0;
			for (int i = 0; i<cluster.size(); i++)
			{
				w0+=((Clusterable2DSmoothie)allList.get(cluster.get(i))).weights2d[0][w];
				w1+=((Clusterable2DSmoothie)allList.get(cluster.get(i))).weights2d[1][w];
			}
			weights2d[0][w]=(short) (w0/cluster.size());
			weights2d[1][w]=(short) (w1/cluster.size());
		}
	}
	@Override
	public Image getImage(){
		return super.getImage();
	}
}
