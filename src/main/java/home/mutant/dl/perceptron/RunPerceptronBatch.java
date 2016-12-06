package home.mutant.dl.perceptron;

import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

public class RunPerceptronBatch {
	public static int trainLabel=0;
	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImages();
		Perceptron p = new Perceptron(28*28,1,Perceptron.InitType.NO_INIT);
		for(int steps=0;steps<20;steps++)
		{
			p.clearDelta();
			for (int i=0;i<60000;i++){
				int j = i;//(int) (Math.random()*60000);
				if(MnistDatabase.trainLabels.get(j)==trainLabel){
					p.accumulateDelta(MnistDatabase.trainImages.get(j).getDataDouble(), 1);
				}else {
					p.accumulateDelta(MnistDatabase.trainImages.get(j).getDataDouble(), 0);
				}
			}
			p.modifyWeightsFromDelta(60000);
			System.out.println(steps);
			p.learningRate/=1.0005;
			System.out.println(p.learningRate);
		}
		int count=0;
		int total=0;
		for (int i=0;i<10000;i++){
			if(MnistDatabase.testLabels.get(i)==trainLabel){
				if (p.output(MnistDatabase.testImages.get(i).getDataDouble()))count++;
			}else{
				if (!p.output(MnistDatabase.testImages.get(i).getDataDouble()))count++;
			}
			total++;
		}
		System.out.println((double)count/total);
		ResultFrame frame = new ResultFrame(200, 200);
		frame.showImage(p.getImage());
	}

}
