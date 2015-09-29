package home.mutant.dl.perceptron;

import home.mutant.dl.models.Image;

import java.util.ArrayList;
import java.util.List;
public class MultiVotingPerceptron {
	
	public static class VotingPerceptronRunnable implements Runnable{
		public int noClass;
		public List<Image> images;
		public List<Integer> labels;
		public VotingPerceptron perceptron;
		
		public VotingPerceptronRunnable(int noClass, List<Image> images,
				List<Integer> labels, VotingPerceptron perceptron) {
			super();
			this.noClass = noClass;
			this.images = images;
			this.labels = labels;
			this.perceptron = perceptron;
		}

		@Override
		public void run() {
			for (int i=0;i<images.size();i++)
			{
				int j = (int) (Math.random()*images.size());
				if (labels.get(j)==noClass)perceptron.trainData(images.get(j).data, true);
				else perceptron.trainData(images.get(j).data, false);
			}
		}
	}
	public List<VotingPerceptron> perceptrons = new ArrayList<VotingPerceptron>();
	public MultiVotingPerceptron(int noClasses, int size)
	{
		for (int i = 0;i<noClasses;i++){
			perceptrons.add(new VotingPerceptron(50,size));
		}
	}
	
	public void trainImages(List<Image> images, List<Integer> labels){
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0;i<perceptrons.size();i++){
			Thread thread = new Thread(new VotingPerceptronRunnable(i, images, labels, perceptrons.get(i)));
			threads.add(thread);
			thread.start();
		}
		for (int i = 0;i<perceptrons.size();i++){
			try {
				threads.get(i).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void addClassData(double[] data, int noClass){
		for (int i = 0;i<perceptrons.size();i++){
			if (i==noClass) perceptrons.get(i).trainData(data, true);
			else perceptrons.get(i).trainData(data, false);
		}
	}


	public int getClassForData(double[] data){
		double max = -1*Double.MAX_VALUE;
		int indexMax=-1;
		for (int i = 0;i<perceptrons.size();i++){
			double activation = perceptrons.get(i).getTotalActivation(data);
			if(activation>max){
				max = activation;
				indexMax=i;
			}
		}
		return indexMax;
	}

	public void decreaseLearningRate() {
		for (int i = 0;i<perceptrons.size();i++){
			perceptrons.get(i).decreaseLearningRate();
		}
	}
	
//	public int getClassForData(float[] data){
//		int max=-1;
//		int indexMax=-1;
//		for (int i = 0;i<perceptrons.size();i++){
//			int noCorrectlyClassified = perceptrons.get(i).noCorrectlyClassified(data);
//			if(noCorrectlyClassified>max){
//				max = noCorrectlyClassified;
//				indexMax=i;
//			}
//		}
//		return indexMax;
//	}
}
