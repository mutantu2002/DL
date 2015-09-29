/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.mutant.dl.gaussian.pattern;

import home.mutant.dl.models.Image;
import home.mutant.dl.utils.multithreading.Launcher;

import java.util.List;

/**
 *
 * @author cipi
 */
public class TransformImagesPatternNeurons extends Launcher{
	List<Image> images;
	List<MBPatternNeuron> neurons;	
	public static final int NO_THREADS=4;
	
	public TransformImagesPatternNeurons(List<Image> images, List<MBPatternNeuron> neurons) {
		this.images = images;
		this.neurons = neurons;
	}

	public void transform() {
		int step = images.size() / NO_THREADS;
		
		for (int i = 0; i < NO_THREADS; i++) {
			addRunnable(new TransformImagesRunnable(images.subList(i*step, (i+1)*step), neurons));
		}
		run();
	}
}
