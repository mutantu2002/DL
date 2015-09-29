package home.mutant.dl.perceptron;

import java.util.ArrayList;
import java.util.List;
public class MultiPerceptron {
	public List<Perceptron> perceptrons = new ArrayList<Perceptron>();
	public MultiPerceptron(int noClasses, int size)
	{
		for (int i = 0;i<noClasses;i++){
			perceptrons.add(new Perceptron(size));
		}
	}
	
	public void addClassData(double[] data, int noClass){
		for (int i = 0;i<perceptrons.size();i++){
			if (i==noClass) perceptrons.get(i).trainData(data, true);
			else perceptrons.get(i).trainData(data, false);
		}
	}
	
	public int getClassForData(double[] data){
		double max=-1*Double.MAX_VALUE;
		int indexMax=-1;
		for (int i = 0;i<perceptrons.size();i++){
			double activation = perceptrons.get(i).calculateActivation(data);
			if (activation>max) {
				max=activation;
				indexMax=i;
			}
		}
		return indexMax;
	}
	
//	public int getClassForData(float[] data){
//		List<Integer> res = new ArrayList<Integer>();
//		for (int i = 0;i<perceptrons.size();i++){
//			if (perceptrons.get(i).correctlyClassified(data)) res.add(i);
//		}
//		//System.out.println(res.toString());
//		if(res.size()>0) return res.get(0);
//		return -1;
//	}
}
