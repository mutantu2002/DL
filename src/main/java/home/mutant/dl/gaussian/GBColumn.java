package home.mutant.dl.gaussian;

import home.mutant.dl.models.Image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class GBColumn {
	public List<GBNeuron> neurons = new ArrayList<GBNeuron>();
	
	public GBColumn(int size, int neuronSize) {
		for(int i=0;i<size;i++){
			neurons.add(new GBNeuron(neuronSize));
		}
	}
	public void trainImage(Image image){
		neurons.get(getIndexMax(image)).trainImage(image);
	}
	
	public void trainImages(List<Image> images){
		for (Image image : images) {
			trainImage(image);
		}
	}
	public int getIndexMax(Image image)
	{
		double max = -1*Double.MAX_VALUE;
		int indexMax=-1;
		for (int b = 0; b < neurons.size(); b++)
		{
			final double output = neurons.get(b).getPosterior(image);
			if (output>max){
				max=output;
				indexMax=b;
			}
		}
		return indexMax;
	}
	
	public double getPosterior(Image image) {
		double max = -1*Double.MAX_VALUE;
		for (int b = 0; b < neurons.size(); b++)
		{
			final double output = neurons.get(b).getPosterior(image);
			if (output>max){
				max=output;
			}
		}
		return max;
	}
	public Collection<? extends Image> generateImages() {
		List<Image> imgs = new ArrayList<Image>();
		for (int b = 0; b < neurons.size(); b++){
			imgs.add(neurons.get(b).generateImage());
		}
		return imgs;
	}
}
