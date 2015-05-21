/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.mutant.dl.gaussian.pattern;

import home.mutant.dl.models.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cipi
 */
public class TransformImagesPatternNeurons {
	List<Image> images;
	List<MBPatternNeuron> neurons;
	List<Thread> threads = new ArrayList<Thread>();
	
	public static final int NO_THREADS=4;
	
	public TransformImagesPatternNeurons(List<Image> images, List<MBPatternNeuron> neurons) {
		this.images = images;
		this.neurons = neurons;
	}

	public void transform() {
		int step = images.size() / NO_THREADS;
		for (int i = 0; i < NO_THREADS; i++) {
			threads.add(new Thread(new TransformImagesRunnable(images.subList(i*step, (i+1)*step), neurons)));
			threads.get(i).start();
		}
		for (int i = 0; i < NO_THREADS; i++) {
			try {
				threads.get(i).join();
			} catch (InterruptedException ex) {
				Logger.getLogger(TransformImagesPatternNeurons.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
