package home.mutant.dl.entropy;

import java.util.Arrays;
import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

public class EntropyNet {
	public Layer layer;
	private double[] targets;
	public static void main(String[] args) throws Exception {
		new EntropyNet(100, 784).train();

	}

	public EntropyNet(int size, int neuronSize){
		layer = new Layer(size, neuronSize);
		targets = new double[size];
	}
	private void train() throws Exception {
		MnistDatabase.loadImagesNormalized();
		ResultFrame frame = new ResultFrame(500, 300);
		for(int steps=0;steps<40;steps++)
		{
			double entropy=0;
			for (int i=0;i<600;i++){
				int j = (int) (Math.random()*6000);
				Arrays.fill(targets, 0);
				Integer label =MnistDatabase.trainLabels.get(j);
//				targets[(label+9)%10]=0.2;
				targets[label]=1;
//				targets[(label+1)%10]=0.2;
//				targets[(int) (Math.random()*10)]=1;
				layer.stepBatch(MnistDatabase.trainImages.get(j).getDataDouble(), targets);
				entropy+=layer.ylogy;
//				for (int n=0;n<layer.neurons.length;n++){
//					System.out.print(layer.neurons[n].error+"; ");
//				}
				
			}
			System.out.println(entropy);
			//test();
			//layer.updateWeights();
			List<Image> images = layer.getImages();
			frame.showImages(images);
		}
		System.out.println();
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
