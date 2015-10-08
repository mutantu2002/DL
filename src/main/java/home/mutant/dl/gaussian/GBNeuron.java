/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.mutant.dl.gaussian;

import home.mutant.dl.models.Image;
import home.mutant.dl.models.ImageDouble;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cipi
 */
public class GBNeuron {
	List<GBSynapse> synapses = new ArrayList<GBSynapse>();
	public GBNeuron(int size){
		for (int i = 0; i < size; i++) {
			synapses.add(new GBSynapse());
		}
	}
	public void trainImage(Image image){
		double[] pixels = image.getDataDouble();
		for (int i = 0; i < pixels.length; i++) {
			synapses.get(i).addTarget(pixels[i]);
		}
	}
	public void trainImage(Image image, int weight){
		double[] pixels = image.getDataDouble();
		for (int i = 0; i < pixels.length; i++) {
			synapses.get(i).addTarget(pixels[i]);
		}
	}
	
	public void trainPixels(float[] pixels){
		for (int i = 0; i < pixels.length; i++) {
			synapses.get(i).addTarget(pixels[i]);
		}
	}
	
	public void trainInput(int[] input){
		for (int i = 0; i < input.length; i++) {
			synapses.get(i).addTarget(input[i]);
		}
	}
	
	public void trainImages(List<Image> images){
		for (Image image : images) {
			trainImage(image);
		}
	}
	public double getPosterior(Image image){
		double posterior = 0;
		double[] pixels = image.getDataDouble();
		for (int i = 0; i < pixels.length; i++) {
			posterior+=synapses.get(i).getPosterior(pixels[i]);
		}
		return posterior;
	}
	public double getPosterior(int[] input){
		double posterior = 0;
		for (int i = 0; i < input.length; i++) {
			posterior+=synapses.get(i).getPosterior(input[i]);
		}
		return posterior;
	}
	
	
	public double countSimilar(float[] input) {
		double count=0;
		for (int i=0;i<synapses.size();i++) {
			double diff =Math.abs(input[i]-synapses.get(i).mean);
			count+=Globals.MAX_PIXEL_VALUE-diff;
		}
		return count/synapses.size();
	}
	public boolean isSimilar(float[] input){
		double count = countSimilar(input);
		//System.out.println(count);
		if (count/Globals.MAX_PIXEL_VALUE>=Globals.SIMILARITY) return true;
		return false;
	}
	public Image generateImage(){
		double[] pixels=new double[synapses.size()];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] =  (float) (synapses.get(i).mean);
		}
		return new ImageDouble(pixels);
	}
}
