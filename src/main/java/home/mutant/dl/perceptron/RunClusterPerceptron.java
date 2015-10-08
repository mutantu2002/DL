package home.mutant.dl.perceptron;

import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

public class RunClusterPerceptron {

	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImages();
		Perceptron p = new Perceptron(28*28);
		for (int i=0;i<6000;i++){
			if(p.output(MnistDatabase.trainImages.get(i).getDataDouble())){
				p.modifyWeights(MnistDatabase.trainImages.get(i).getDataDouble(), 1);
			}else {
				p.modifyWeights(MnistDatabase.trainImages.get(i).getDataDouble(), -1);
			}
		}
		
//		int count=0;
//		int total=0;
//		for (int i=0;i<10000;i++){
//			if(MnistDatabase.testLabels.get(i)==1){
//				if (p.correctlyClassified(MnistDatabase.testImages.get(i).data))count++;
//				total++;
//			}else{
//				if (!p.correctlyClassified(MnistDatabase.testImages.get(i).data))count++;
//				total++;
//			}
//		}
//		System.out.println((double)count/total);
		ResultFrame frame = new ResultFrame(200, 200);
		frame.showImage(p.getImage());
	}

}
