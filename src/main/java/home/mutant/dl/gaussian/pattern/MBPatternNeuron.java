package home.mutant.dl.gaussian.pattern;

import home.mutant.dl.gaussian.Globals;
import home.mutant.dl.models.Image;
import home.mutant.dl.utils.MnistDatabase;

import java.util.Set;


public class MBPatternNeuron implements Comparable<MBPatternNeuron>{
	public Set<Integer> indexes ;
	public int imagePatternIndex = 0;
	float[] pattern;
	public int sumPositive=0;
	
	public MBPatternNeuron(int size, int imagePatternIndex, Set<Integer> indexes) {
		this.imagePatternIndex = imagePatternIndex;
		this.indexes = indexes;
		pattern = new float[size];
		int i=0;
		for (Integer index : indexes) { 
			pattern[i++] = MnistDatabase.trainImages.get(imagePatternIndex).data[index];
		}
		//initRandom();
	}
	
	public Image generateImage(){
		return new Image(pattern);
	}
	public boolean isSimilar(float[] input){
		double count = countSimilar(input);
		//System.out.println(count);
		if (count/Globals.MAX_PIXEL_VALUE>=Globals.SIMILARITY) return true;
		return false;
	}
//	public double countSimilar(float[] input) {
//		double count=0;
//		int i=0;
//		for (Integer index : indexes) {
//			if (input[index]==0 && pattern[i]==0){count++;}
//			if (input[index]!=0 && pattern[i]!=0){count++;}
//			i++;
//		}
//		return count/pattern.length*Globals.MAX_PIXEL_VALUE;
//	}
	public double countSimilar(float[] input) {
		double count=0;
		int i=0;
		for (Integer index : indexes) {
			double diff =Math.abs(input[index]-pattern[i++]);
			count+=Globals.MAX_PIXEL_VALUE-diff;
		}
		return count/pattern.length;
	}
	public boolean isSimilar(int indexImage){
		return isSimilar(MnistDatabase.trainImages.get(indexImage).data);
	}
	public boolean trainImage(int indexImage) {
		Image image = MnistDatabase.trainImages.get(indexImage);
		boolean similar = isSimilar(image.data);
		if (similar){
			updatePattern(image);
		}
		return similar;
	}
	public void updatePattern(Image image){
		int i=0;
		sumPositive++;
//		for (Integer index : indexes) {
//			pattern[i] = (float) (pattern[i]*((double)sumPositive-1)/((double)sumPositive) + image.data[index]/((double)sumPositive));
//			i++;
//		}
	}
	@Override
	public int compareTo(MBPatternNeuron o) {
		// TODO Auto-generated method stub
		return (int) (sumPositive-o.sumPositive);
	}
}
