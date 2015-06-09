package home.mutant.dl.perceptron.multilayer;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SeparatedMLPerceptron {
	List<MLPerceptron> mlPerceptrons = new ArrayList<MLPerceptron>();
	
	
	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImagesNormalized();
		new SeparatedMLPerceptron().train();
	}
	
	public SeparatedMLPerceptron(){
		for (int i = 0;i<10;i++){
			mlPerceptrons.add(new MLPerceptron(36,i));
		}
	}
	
	public void train() throws IOException{
		for(int steps=0;steps<40;steps++)
		{
			step();
			test();
			decreaseLearningRate();
		}
		
		ResultFrame frame = new ResultFrame(1000, 200);
		List<Image> images = mlPerceptrons.get(0).layer.getImages();
		frame.showImages(images);
	}

	private void test() {
		int count=0;
		int total=0;
		for (int i=0;i<10000;i++){
			int trainLabel = getClassForData(MnistDatabase.testImages.get(i).data);
			if(MnistDatabase.testLabels.get(i)==trainLabel){
				count++;
			}
			total++;
		}
		System.out.println((double)count/total);
		
	}

	public int getClassForData(float[] data){
		double max = -1*Double.MAX_VALUE;
		int indexMax=-1;
		for (int i = 0;i<mlPerceptrons.size();i++){
			double activation = mlPerceptrons.get(i).getActivation(data);
			if(activation>max){
				max = activation;
				indexMax=i;
			}
		}
		return indexMax;
	}
	private void decreaseLearningRate() {
		for (int i = 0;i<mlPerceptrons.size();i++){
			mlPerceptrons.get(i).decreaseLearningRate();
		}
		
	}

	private void step() {
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0;i<mlPerceptrons.size();i++){
			Thread thread = new Thread(mlPerceptrons.get(i));
			threads.add(thread);
			thread.start();
		}
		for (int i = 0;i<mlPerceptrons.size();i++){
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
