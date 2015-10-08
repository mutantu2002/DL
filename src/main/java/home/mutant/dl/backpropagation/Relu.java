package home.mutant.dl.backpropagation;

import home.mutant.dl.models.Image;
import home.mutant.dl.models.ImageDouble;

public class Relu{
	public double[] weights;
	public Layer layer;
	public int index;
	public Relu(int size, double scale, Layer layer, int index){
		weights = new double[size+1];
		randomize(scale);
		this.layer = layer;
		this.index = index;
	}
	
	private void randomize(double scale) {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = scale - 2*Math.random()*scale;
		}
	}
	
	public Image getImage(){
		Image image = new ImageDouble(weights.length-1);
		double max = -1*Double.MAX_VALUE;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < weights.length-1; i++) {
			if (weights[i]>max)max=weights[i];
			if (weights[i]<min)min=weights[i];
		}
		max=255/(max-min);
		for (int i = 0; i < weights.length-1; i++) {
			image.getDataDouble()[i]=(float) ((weights[i]-min)*max);
		}
		return image;
	}

	public void forward() {
		layer.z[index]=0;
		for (int i = 0; i < layer.downLayer.z.length; i++) {
			layer.z[index]+=layer.downLayer.z[i]*weights[i];
		}
		layer.z[index]+=weights[weights.length-1];
		//System.out.println(index + " done");
	}
}
