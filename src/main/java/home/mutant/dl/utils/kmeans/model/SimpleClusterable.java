package home.mutant.dl.utils.kmeans.model;

import java.io.Serializable;
import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.models.ImageDouble;

public class SimpleClusterable implements Clusterable,Serializable {
	private static final long serialVersionUID = 6582606937177300533L;
	protected double weights[];
	int label;
	
	public SimpleClusterable(int size) {
		weights = new double[size];
	}
	public SimpleClusterable(double[] weights) {
		this.weights = weights;
	}
	public SimpleClusterable(float[] weights) {
		this(weights.length);
		for (int i = 0; i < weights.length; i++) {
			this.weights[i]=weights[i];
		}
	}
	public SimpleClusterable(double[] weights, int label) {
		this(weights);
		setLabel(label);
	}
	@Override
	public double d(Clusterable clusterable) {
		double d=0;
		for (int i = 0; i < weights.length; i++) {
			d+=(weights[i] - clusterable.getWeights()[i])*(weights[i] - clusterable.getWeights()[i]);
		}
		return Math.sqrt(d);
	}

	@Override
	public double[] getWeights() {
		// TODO Auto-generated method stub
		return weights;
	}

	@Override
	public Clusterable randomize() {
		SimpleClusterable m = new SimpleClusterable(weights.length);
		m.randomizeWeights();
		return m;
	}
	public void randomizeWeights(){
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Math.random()*256;
		}
	}
	
	@Override
//	public Image getImage(){
//		return new Image(weights);
//	}
	
	public Image getImage(){
		Image image = new ImageDouble(weights.length);
		double max = -1*Double.MAX_VALUE;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < weights.length; i++) {
			if (weights[i]>max)max=weights[i];
			if (weights[i]<min)min=weights[i];
		}
		max=255/(max-min);
		for (int i = 0; i < weights.length; i++) {
			image.getDataDouble()[i]= (weights[i]-min)*max;
		}
		return image;
	}
	@Override
	public Clusterable copy() {
		SimpleClusterable m = new SimpleClusterable(weights.length);
		System.arraycopy(weights, 0, m.getWeights(), 0, weights.length);
		return m;
	}
	@Override
	public void setLabel(int label) {
		this.label=label;
	}
	@Override
	public int getLabel() {
		return label;
	}
	@Override
	public void setWeights(double[] weights) {
		this.weights = weights;
	}
	@Override
	public void updateCenterFromMembers(List<Clusterable> allList, List<Integer> cluster) {
		for(int w=0; w<weights.length; w++)
		{
			weights[w]=0;
			for (int i = 0; i<cluster.size(); i++)
			{
				weights[w]+=allList.get(cluster.get(i)).getWeights()[w];
			}
			weights[w]/=cluster.size();
		}
	}
}
