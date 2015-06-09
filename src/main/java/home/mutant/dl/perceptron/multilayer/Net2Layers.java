package home.mutant.dl.perceptron.multilayer;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

import java.util.Arrays;
import java.util.List;

public class Net2Layers {
	public HiddenLayer hidden;
	public OutputLayer output;
	public double[] targets;
	
	public static void main(String[] args) throws Exception {
		new Net2Layers(784, 64,10).train();
	}
	
	private void train() throws Exception {
		MnistDatabase.loadImagesNormalized();

		ResultFrame frame = new ResultFrame(1000, 200);

		
		for(int steps=0;steps<400;steps++)
		{
			for (int i=0;i<60000;i++){
				int j = (int) (Math.random()*60000);
				step(MnistDatabase.trainImages.get(j), MnistDatabase.trainLabels.get(j));
			}
//			if (steps%10==9) {
//				increaseLearningRate();
//				System.out.println("Increase");
//			}
//			else 
			decreaseLearningRate();
			test();
			List<Image> images = hidden.getImages();
			frame.showImages(images);
		}

	}

	private void increaseLearningRate() {
		hidden.increaseLearningRate();
		output.increaseLearningRate();
		
	}

	private void decreaseLearningRate() {
		hidden.decreaseLearningRate();
		output.decreaseLearningRate();
	}

	private void test() {
		int count=0;
		int total=0;
		for (int i=0;i<10000;i++){
			int trainLabel = getMaxOutputIndex(MnistDatabase.testImages.get(i));
			if(MnistDatabase.testLabels.get(i)==trainLabel){
				count++;
			}
			total++;
		}
		System.out.println((double)count/total);
		
	}

	public Net2Layers(int inputSize, int hiddenSize, int outputSize){
		hidden = new HiddenLayer(inputSize, hiddenSize);
		output = new OutputLayer(hiddenSize, outputSize);
		targets = new double[outputSize];
	}
	
	public void step(Image image, int label){
		hidden.forward(image.data);
		Arrays.fill(targets, 0);
		targets[label]=1;
		output.trainData(hidden.activations, targets);
		hidden.backward(output.errors, image.data);
	}
	
	public int getMaxOutputIndex(Image image){
		hidden.forward(image.data);
		output.forward(hidden.activations);
		double max=-1*Double.MAX_VALUE;
		int indexMax=-1;
		for (int i = 0;i<output.activations.length;i++){
			double activation = output.activations[i];
			if (activation>max) {
				max=activation;
				indexMax=i;
			}
		}
		return indexMax;
	}
}
