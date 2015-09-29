package home.mutant.dl.perceptron.multilayer;

import home.mutant.dl.perceptron.Perceptron.InitType;

public class OutputLayer extends Layer {
	double topErrors[] ;
	public OutputLayer(int sizeIn, int sizeOut) {
		super(sizeIn, sizeOut, InitType.NO_INIT);
		topErrors = new double[sizeOut];
	}
	public boolean trainData(double[] data, double[] targets){
		boolean toBeTrained = false;
		for (int i=0;i<perceptrons.size();i++){
			topErrors[i] = perceptrons.get(i).trainData(data, targets[i]);
			if (topErrors[i]!=0)
				toBeTrained=true;
		}
		if (!toBeTrained)
		{
			return false;
		}
		backErrors();
		return true;
	}
	public void backErrors(){
		for (int i=0;i<errors.length;i++){
			errors[i]=0;
			for (int t=0;t<topErrors.length;t++){
				errors[i]+=-(perceptrons.get(t).coefficients[i])*topErrors[t];
			}
			errors[i]/=perceptrons.size();
		}
	}
}
