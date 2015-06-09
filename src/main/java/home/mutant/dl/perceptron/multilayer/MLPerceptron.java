package home.mutant.dl.perceptron.multilayer;


import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.perceptron.Perceptron;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

public class MLPerceptron implements Runnable{
	public HiddenLayer layer ;
	public Perceptron perceptron;
	public int trainLabel=5;
	public double[] errors;
	
	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImagesNormalized();
		new MLPerceptron(16).train();
	}
	
	public MLPerceptron(int size){
		layer = new HiddenLayer(784, size);
		perceptron = new Perceptron(size, 1);
		errors = new double[size];
	}
	public MLPerceptron(int size, int label){
		this(size);
		trainLabel = label;
	}
	
	public void train(){
		for(int steps=0;steps<40;steps++)
		{
			run();
			test();
			decreaseLearningRate();
		}
		
		ResultFrame frame = new ResultFrame(1000, 200);
		List<Image> images = layer.getImages();
		images.add(perceptron.getImage());
		frame.showImages(images);
	}

	public void run() {
		for (int i=0;i<60000;i++){
			int j = (int) (Math.random()*60000);
			step(MnistDatabase.trainImages.get(j), MnistDatabase.trainLabels.get(j));
		}
	}

	public void decreaseLearningRate() {
		layer.decreaseLearningRate();
		perceptron.decreaseLearningRate();
	}
	
	public void step(Image image, int label){
		layer.forward(image.data);
		double error;
		if(trainLabel==label){
			error = perceptron.trainData(layer.activations, 1);
		}else{
			error = perceptron.trainData(layer.activations, 0);
		}
		for(int i=0;i<errors.length;i++){
			errors[i] = -(perceptron.coefficients[i])*error;
		}
		layer.backward(errors, image.data);
	}
	
	public void test(){
		int count=0;
		int total=0;
		for (int i=0;i<10000;i++){
			layer.forward(MnistDatabase.testImages.get(i).data);
			boolean output = perceptron.output(layer.activations);
			if(MnistDatabase.testLabels.get(i)==trainLabel){
				if (output)count++;
				total++;
			}else{
				if (!output)count++;
				total++;
			}
		}
		System.out.println((double)count/total);

	}
	public double getActivation(float[] data){
		layer.forward(data);
		return perceptron.calculateActivation(layer.activations);
	}
}
