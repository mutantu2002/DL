/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.mutant.dl.gaussian;


import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cipi
 */
public class ClassifyGBNeuron {
	List<GBNeuron> neurons = new ArrayList<GBNeuron>();
	public ClassifyGBNeuron(List<Image> images, List<Integer> labels){
		int size = images.get(0).data.length;
		for (int i = 0; i < 10; i++) {
			neurons.add(new GBNeuron(size));
		}
		for (int i=0;i<images.size();i++) {
			int classIndex = labels.get(i);
			neurons.get(classIndex).trainImage(images.get(i));
		}
	}
	
	public double getRate(List<Image> images, List<Integer> labels){
		int count=0;
		for (int i=0;i<images.size();i++) {
			if(getMax(neurons, images.get(i))== labels.get(i)){
				count++;
			}
		}
		ResultFrame frame = new ResultFrame(600, 600);
		List<Image> imgs = new ArrayList<Image>();
		for (int i=0;i<10;i++)
		{
			imgs.add(neurons.get(i).generateImage());
		}
		frame.showImages(imgs);
		return (count*100.)/images.size();
	}
	public int getMax(List<GBNeuron> neurons, Image image)
	{
		double max = -1*Double.MAX_VALUE;
		int indexMax=-1;
		for (int b = 0; b < 10; b++)
		{
			final double output = neurons.get(b).getPosterior(image);
			if (output>max){
				max=output;
				indexMax=b;
			}
		}
		return indexMax;
	}
}
