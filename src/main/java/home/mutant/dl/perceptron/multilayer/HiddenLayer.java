package home.mutant.dl.perceptron.multilayer;

import home.mutant.dl.perceptron.Perceptron.InitType;


public class HiddenLayer extends Layer{
	public HiddenLayer(int sizeIn, int sizeOut){
		super(sizeIn, sizeOut, InitType.RANDOM);
	}
	
	public void forward(double[] data){
		super.forward(data);
		for (int i = 0; i < activations.length; i++) {
			//if(activations[i]<0)activations[i]=0;
			if(activations[i]<0)activations[i]/=5;
			//if(activations[i]>1)activations[i]=1;
			
		}
		//normalizeActivations();
	}
	
//	public void normalizeActivations(){
//		double max = -1*Double.MAX_VALUE;
//		double min = Double.MAX_VALUE;
//		for (int i = 0; i < activations.length; i++) {
//			if(activations[i]>max)max=activations[i];
//			if(activations[i]<min)min=activations[i];
//		}
//		max=255/(max-min);
//		for (int i = 0; i < activations.length; i++) {
//			activations[i]=(activations[i]-min)*max;
//		}
//	}
	public void normalizeActivations(){
		double max = -1*Double.MAX_VALUE;
		for (int i = 0; i < activations.length; i++) {
			if(activations[i]>max)max=activations[i];
			
		}
		if(max>0){
			max=1/(max);
			for (int i = 0; i < activations.length; i++) {
				activations[i]=(activations[i])*max;
			}
		}
	}
	
	public void backward(double[] errors, double[] data){
		for (int i = 0; i < perceptrons.size(); i++) {
			if (errors[i]!=0) 
			{
				if (activations[i]>0)perceptrons.get(i).modifyWeights(data, errors[i]);
				else perceptrons.get(i).modifyWeights(data, errors[i]/5);
			}
		}
	}
	
	public void backwardTarget(double[] targets, float[] data){
		for (int i = 0; i < perceptrons.size(); i++) {
			perceptrons.get(i).trainData(data, targets[i]);
		}
	}
}
