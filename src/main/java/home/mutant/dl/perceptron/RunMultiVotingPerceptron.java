package home.mutant.dl.perceptron;

import home.mutant.dl.utils.MnistDatabase;

public class RunMultiVotingPerceptron {

	public static void main(String[] args) throws Exception {
		//MnistDatabase.loadImagesScaled(0.5);
		MnistDatabase.loadImages();
		MultiVotingPerceptron p = new MultiVotingPerceptron(10, 28*28);
		for (int i=0;i<5;i++){
			p.trainImages(MnistDatabase.trainImages, MnistDatabase.trainLabels);
			p.decreaseLearningRate();
			System.out.println(i);
		}

		int count=0;
		int total=10000;
		for (int i=0;i<total;i++){
			
			int classForData = p.getClassForData(MnistDatabase.trainImages.get(i).data);
			if(MnistDatabase.trainLabels.get(i)==classForData) count++;
		}
		System.out.println((double)count/total);
	}

}
