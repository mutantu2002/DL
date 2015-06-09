package home.mutant.dl.perceptron.multilayer;

public class OutputLayer extends Layer {
	double topErrors[] ;
	public OutputLayer(int sizeIn, int sizeOut) {
		super(sizeIn, sizeOut);
		topErrors = new double[sizeOut];
	}
	public void trainData(double[] data, double[] targets){
		for (int i=0;i<perceptrons.size();i++){
			topErrors[i] = perceptrons.get(i).trainData(data, targets[i]);
		}
		backErrors();
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
