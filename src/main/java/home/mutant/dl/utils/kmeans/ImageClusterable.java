package home.mutant.dl.utils.kmeans;

import home.mutant.dl.models.Image;

public class ImageClusterable extends SimpleClusterable{

	public ImageClusterable(Image image) {
		super(image.data.length);
		for (int i = 0; i < weights.length; i++) {
			double pixel = image.data[i];
			if(pixel<0)pixel+=256;
			weights[i]=pixel;
		}
	}
	public ImageClusterable(int size) {
		super(size);
	}
	
	public Image getImage(){
		float[] pixels = new float[weights.length];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i]=(float) weights[i];
		}
		return new Image(pixels);
	}
	@Override
	public Clusterable randomize() {
		ImageClusterable m = new ImageClusterable(weights.length);
		m.randomizeWeights();
		return m;
	}
	@Override
	public Clusterable copy() {
		ImageClusterable m = new ImageClusterable(weights.length);
		System.arraycopy(weights, 0, m.getWeights(), 0, weights.length);
		return m;
	}
}
