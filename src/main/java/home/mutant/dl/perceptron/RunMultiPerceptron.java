package home.mutant.dl.perceptron;

import home.mutant.dl.utils.MnistDatabase;

public class RunMultiPerceptron {

	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImages();
		MultiPerceptron p = new MultiPerceptron(10, 28*28);
		for (int i=0;i<6000000;i++){
			int j = (int) (Math.random()*60000);
			p.addClassData(MnistDatabase.trainImages.get(j).getDataDouble(), MnistDatabase.trainLabels.get(j));
		}
//		for (int i=0;i<60000;i++){
//			p.addClassData(MnistDatabase.trainImages.get(i).data, MnistDatabase.trainLabels.get(i));
//		}
		int count=0;
		int total=0;
		for (int i=0;i<10000;i++){
			if(MnistDatabase.testLabels.get(i)==p.getClassForData(MnistDatabase.testImages.get(i).getDataDouble())) count++;
			total++;
		}
		
//		for (int i=0;i<10000;i++){
//			System.out.println(MnistDatabase.testLabels.get(i));
//			if(MnistDatabase.testLabels.get(i)==6){
//				if (p.getClassForData(MnistDatabase.testImages.get(i).data)==6) count++;
//			}else{
//				if (p.getClassForData(MnistDatabase.testImages.get(i).data)!=6) count++;
//			}
//			total++;
//			
//		}
		System.out.println((double)count/total);
	}

}
