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
public class ClassifyGBColumn {
	List<GBColumn> columns = new ArrayList<GBColumn>();
	public ClassifyGBColumn(List<Image> images, List<Integer> labels){
		int neuronSize = images.get(0).getDataDouble().length;
		for (int i = 0; i < 10; i++) {
			columns.add(new GBColumn(11, neuronSize));
		}
		for (int i=0;i<images.size();i++) {
			int classIndex = labels.get(i);
			columns.get(classIndex).trainImage(images.get(i));
		}
	}
	
	public double getRate(List<Image> images, List<Integer> labels){
		int count=0;
		for (int i=0;i<images.size();i++) {
			if(getMax(columns, images.get(i))== labels.get(i)){
				count++;
			}
		}
		ResultFrame frame = new ResultFrame(600, 600);
		List<Image> imgs = new ArrayList<Image>();
		for (int i=0;i<10;i++)
		{
			imgs.addAll(columns.get(i).generateImages());
		}
		frame.showImages(imgs);
		return (count*100.)/images.size();
	}
	public int getMax(List<GBColumn> columns, Image image)
	{
		double max = -1*Double.MAX_VALUE;
		int indexMax=-1;
		for (int b = 0; b < 10; b++)
		{
			final double output = columns.get(b).getPosterior(image);
			if (output>max){
				max=output;
				indexMax=b;
			}
		}
		return indexMax;
	}
}
