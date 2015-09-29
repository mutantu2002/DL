package home.mutant.dl.entropy;

import home.mutant.dl.models.Image;

public class Neuron {
	public double[] weights;
	public double[] errors;
	public double y;
	public double z;
	public double dEdZ;
	public double learningRate = 0.01;//Math.random()/100;
	public double regularizationRate =0;//  0.000001;
	public int noSamples=0;
	public Neuron(int size) {
		weights = new double[size+1];
		errors = new double[size+1];
		randomize(0.001);
	}

	private void randomize(double scale) {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = scale - 2*Math.random()*scale;
		}
	}
	
	public double calculateActivation(double[] data){
		z=0;
		for (int i = 0; i < data.length; i++) {
			z+=data[i]*weights[i];
		}
		z+=weights[weights.length-1];
		y=Math.exp(z);
		return y;
	}

	public void updateWeights() {
		for (int i = 0; i < weights.length; i++) {
			weights[i]-=errors[i]*learningRate/noSamples-weights[i]*regularizationRate;
			errors[i]=0;
		}
		noSamples=0;
	}
	
	public void updateErrors(double[] data){
		for (int i = 0; i < data.length; i++) {
			errors[i]+=dEdZ*data[i];
		}
		errors[errors.length-1]+=dEdZ;
		noSamples++;
	}
	
	public Image getImage(){
		Image image = new Image(weights.length-1);
		double max = -1*Double.MAX_VALUE;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < weights.length-1; i++) {
			if (weights[i]>max)max=weights[i];
			if (weights[i]<min)min=weights[i];
		}
		max=255/(max-min);
		for (int i = 0; i < weights.length-1; i++) {
			image.data[i]=(float) ((weights[i]-min)*max);
		}
		return image;
	}
}
