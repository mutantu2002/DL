package home.mutant.dl.perceptron;

import home.mutant.dl.utils.MnistDatabase;

public class RunVotingPerceptron {

	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImages();
		VotingPerceptron p = new VotingPerceptron(110,28*28);
		for (int i=0;i<60000;i++){
			if(MnistDatabase.trainLabels.get(i)==5){
				p.trainData(MnistDatabase.trainImages.get(i).getDataDouble(), true);
			}else {
				p.trainData(MnistDatabase.trainImages.get(i).getDataDouble(), false);
			}
		}
		
		int count=0;
		int total=0;
		for (int i=0;i<10000;i++){
			if(MnistDatabase.testLabels.get(i)==5){
				if (p.correctlyClassified(MnistDatabase.testImages.get(i).getDataDouble()))count++;
				total++;
			}else{
				if (!p.correctlyClassified(MnistDatabase.testImages.get(i).getDataDouble()))count++;
				total++;
			}
		}
		System.out.println((double)count/total);
	}

}
