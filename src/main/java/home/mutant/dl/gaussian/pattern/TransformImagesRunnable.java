/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.mutant.dl.gaussian.pattern;

import home.mutant.dl.gaussian.Globals;
import home.mutant.dl.models.Image;

import java.util.List;

/**
 *
 * @author cipi
 */
public class TransformImagesRunnable implements Runnable{
	List<Image> images;
	List<MBPatternNeuron> neurons;

	public TransformImagesRunnable(List<Image> images,List<MBPatternNeuron> neurons) {
		this.images = images;
		this.neurons = neurons;
	}
	
	@Override
	public void run() {
		for (int i=0;i<images.size();i++) {
			double[] pixels = new double[neurons.size()];
			for (int j = 0; j < neurons.size(); j++) {
				int countSimilar = (int) neurons.get(j).countSimilar(images.get(i).data);
				pixels[j] = countSimilar>=Globals.MAX_PIXEL_VALUE*Globals.SIMILARITY?countSimilar:0;
			}
			images.set(i, new Image(pixels));

		}
	}
	
}
