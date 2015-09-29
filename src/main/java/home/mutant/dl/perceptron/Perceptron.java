package home.mutant.dl.perceptron;

import home.mutant.dl.models.Image;

public class Perceptron {
	public double[] coefficients;
	public double learningRate = 0.0008;//Math.random()/100;
	public double regularizationRate =  0.000001;
	public double activation = 0;
	public enum InitType{RANDOM, SINUS, GAUSS, NO_INIT, CONSTANT}
	public Perceptron(int size){
		this(size, 1, InitType.RANDOM);
	}
	
	public Perceptron(int size, double scale){
		this(size, scale, InitType.RANDOM);
	}
	
	public Perceptron(int size, double scale, InitType type){
		coefficients = new double[size+1];
		switch(type){
			case RANDOM: randomize(scale);break;
			case SINUS: initSinus(scale);break;
			case GAUSS: initGauss(scale);break;
			case CONSTANT: initConstant(scale);break;
			case NO_INIT: break;
			default: randomize(scale);
		}

	}
	
	private void initConstant(double scale) {
		for (int i = 0; i < coefficients.length; i++) {
			coefficients[i] = scale;
		}
	}

	private void randomize(double scale) {
		for (int i = 0; i < coefficients.length; i++) {
			coefficients[i] = scale - 2*Math.random()*scale;
		}
	}
	
	public void initGauss(double scale){
		double sqr = Math.sqrt(coefficients.length-1);
		double meanX=Math.random()*sqr;
		double meanY=Math.random()*sqr;
		double devX=sqr/4;//Math.random()*sqr;
		double devY=sqr/4;//Math.random()*sqr;
		for(int x=0;x<sqr;x++){
			for(int y=0;y<sqr;y++){
				double d = scale - 2*scale*Math.random();
				coefficients[(int) (x*sqr+y)] = d*Math.exp(-((x-meanX)*(x-meanX)/2/devX/devX)-((y-meanY)*(y-meanY)/2/devY/devY));
			}
		}
		coefficients[coefficients.length-1]=Math.random()*scale;
	}
	private void initSinus(double scale){
		double freq = 2*Math.PI/(200);
		double phase = Math.random()*2*Math.PI;
		for (int i = 0; i < coefficients.length; i++) {
			coefficients[i] = scale*Math.sin(phase+freq*i);
		}
	}
	
	public double calculateActivation(float[] data){
		activation=0;
		for (int i = 0; i < data.length; i++) {
			activation+=data[i]*coefficients[i];
		}
		activation+=coefficients[coefficients.length-1];
		return activation;
	}
	
	public double calculateActivation(double[] data){
		activation=0;
		for (int i = 0; i < data.length; i++) {
			activation+=data[i]*coefficients[i];
		}
		activation+=coefficients[coefficients.length-1];
		return activation;
	}
	
	public boolean output(float[] data){
		return calculateActivation(data)>0;
	}

	public boolean output(double[] data){
		return calculateActivation(data)>0;
	}
	
	public double outputDouble(float[] data){
		double activation = calculateActivation(data);
		return activation>0?1:0;
	}
	
	public double outputDouble(double[] data){
		double activation = calculateActivation(data);
		return activation>0?1:0;
	}
	
	public void modifyWeights(float[] data, double sign){
		for (int i = 0; i < data.length; i++) {
			coefficients[i]+=sign*data[i]*learningRate-coefficients[i]*regularizationRate;
		}
		coefficients[coefficients.length-1]+=sign*learningRate-coefficients[coefficients.length-1]*regularizationRate;
	}
	
	public void modifyWeights(double[] data, double sign){
		for (int i = 0; i < data.length; i++) {
			coefficients[i]+=sign*data[i]*learningRate-coefficients[i]*regularizationRate;
		}
		coefficients[coefficients.length-1]+=sign*learningRate-coefficients[coefficients.length-1]*regularizationRate;
	}
	
	public void trainData(double[] data, boolean isClass){
		boolean isCorrect = output(data);
		if (isCorrect!=isClass){
			if(isClass)
				modifyWeights(data, 1);
			else
				modifyWeights(data, -1);
		}
	}
	
	public double trainData(float[] data, double target){
		double error = outputDouble(data)-target;
		modifyWeights(data, -error);
		return error;
	}
	
	public double trainData(double[] data, double target){
		double error = outputDouble(data)-target;
		modifyWeights(data, -error);
		return error;
	}
	
	public Image getImage(){
		Image image = new Image(coefficients.length-1);
		double max = -1*Double.MAX_VALUE;
		double min = Double.MAX_VALUE;
		for (int i = 0; i < coefficients.length-1; i++) {
			if (coefficients[i]>max)max=coefficients[i];
			if (coefficients[i]<min)min=coefficients[i];
		}
		max=255/(max-min);
		for (int i = 0; i < coefficients.length-1; i++) {
			image.data[i]=(float) ((coefficients[i]-min)*max);
		}
		return image;
	}

	public void decreaseLearningRate() {
		learningRate/=1.03;
		regularizationRate/=1.03;
	}

	public void increaseLearningRate() {
		learningRate*=10;
	}
}
