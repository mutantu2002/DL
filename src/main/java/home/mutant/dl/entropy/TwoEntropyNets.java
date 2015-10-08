package home.mutant.dl.entropy;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

import java.util.List;

public class TwoEntropyNets {
	public Layer layer;
	public Layer layer2;
	private double[] targets;
	private double[] targets2;
	public static void main(String[] args) throws Exception {
		new TwoEntropyNets(10, 784).train();

	}

	public TwoEntropyNets(int size, int neuronSize){
		layer = new Layer(size, neuronSize);
		targets = new double[size];
		layer2 = new Layer(size, neuronSize);
		targets2 = new double[size];
	}
	private void train() throws Exception {
		MnistDatabase.loadImagesNormalized();
		ResultFrame frame = new ResultFrame(500, 300);
		for(int steps=0;steps<40000;steps++)
		{
			double entropy=0;
			for (int i=0;i<600;i++){
				int s = (int) (Math.random()*6000);
				int s2 = (int) (Math.random()*6000);
				layer.calculateActivations(MnistDatabase.trainImages.get(s).getDataDouble());
				for (int j2 = 0; j2 < targets.length; j2++) {
					targets[j2]=layer.neurons[j2].y;
				}
				layer2.calculateActivations(MnistDatabase.trainImages.get(s2).getDataDouble());
				for (int j2 = 0; j2 < targets2.length; j2++) {
					targets2[j2]=layer2.neurons[j2].y;
				}
				layer.updateErrors(MnistDatabase.trainImages.get(s).getDataDouble(), targets2);
				layer2.updateErrors(MnistDatabase.trainImages.get(s2).getDataDouble(), targets);
			}
			System.out.println(entropy);
//			test();
			layer.updateWeights();
			layer2.updateWeights();
			List<Image> images = layer.getImages();
			images.addAll(layer2.getImages());
			frame.showImages(images);
		}
		
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

	private int getMaxOutputIndex(Image image) {
		double max=-1*Double.MAX_VALUE;
		int indexMax=-1;
		for (int i = 0; i < layer.neurons.length; i++) {
			layer.neurons[i].calculateActivation(image.getDataDouble());
		}
		for (int i = 0;i<layer.neurons.length;i++){
			double activation = layer.neurons[i].z;
			if (activation>max) {
				max=activation;
				indexMax=i;
			}
		}
		return indexMax;
	}
}
