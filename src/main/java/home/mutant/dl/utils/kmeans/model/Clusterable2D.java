package home.mutant.dl.utils.kmeans.model;

import java.util.List;

import home.mutant.dl.models.Image;

public class Clusterable2D extends SimpleClusterable {
	private static final long serialVersionUID = -996388060421011310L;
	private static final int DIM=16;
	public short[][] weights2d;
	public Clusterable2D(double[] weights) {
		super(weights);
		syncWeights2D();
	}
	public Clusterable2D(int size) {
		super(size);
	}
	public Clusterable2D(double[] newImage, int label) {
		super(newImage, label);
		syncWeights2D();
	}
	public void syncWeights2D(){
		weights2d = new short[2][weights.length];
		for (int i = 0; i < weights.length; i++) {
			short ww = (short) weights[i];
			weights2d[0][i]=(short) (ww/DIM);
			weights2d[1][i]=(short) (ww%DIM);
		}
	}
	public void syncWeights(){
		for (int i = 0; i < weights.length; i++) {
			weights[i]=weights2d[0][i]*DIM+weights2d[1][i];
		}
	}
	@Override
	public double d(Clusterable clusterable) {
		Clusterable2D clusterable2d=(Clusterable2D) clusterable;
		
		double d=0;
		for (int i = 0; i < weights.length; i++) {
			d+=(weights2d[0][i]-clusterable2d.weights2d[0][i])*(weights2d[0][i]-clusterable2d.weights2d[0][i])+
					(weights2d[1][i]-clusterable2d.weights2d[1][i])*(weights2d[1][i]-clusterable2d.weights2d[1][i]);
		}
		return Math.sqrt(d);
	}
	@Override
	public Clusterable copy() {
		Clusterable2D m = new Clusterable2D(weights.length);
		System.arraycopy(weights, 0, m.getWeights(), 0, weights.length);
		m.syncWeights2D();
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
				w0+=((Clusterable2D)allList.get(cluster.get(i))).weights2d[0][w];
				w1+=((Clusterable2D)allList.get(cluster.get(i))).weights2d[1][w];
			}
			weights2d[0][w]=(short) (w0/cluster.size());
			weights2d[1][w]=(short) (w1/cluster.size());
		}
	}
	@Override
	public Image getImage(){
		syncWeights();
		return super.getImage();
	}
}
