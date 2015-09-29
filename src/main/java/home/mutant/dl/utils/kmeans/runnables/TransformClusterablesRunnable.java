package home.mutant.dl.utils.kmeans.runnables;

import home.mutant.dl.utils.ImageUtils;
import home.mutant.dl.utils.kmeans.model.Clusterable;
import home.mutant.dl.utils.kmeans.model.ListClusterable;
import home.mutant.dl.utils.kmeans.model.SimpleClusterable;

import java.util.List;

public class TransformClusterablesRunnable implements Runnable{
	List<Clusterable> toTransform;
	ListClusterable filters;
	
	public TransformClusterablesRunnable(List<Clusterable> toTransform, ListClusterable filters) {
		super();
		this.toTransform = toTransform;
		this.filters = filters;
	}

	@Override
	public void run() {
		int sizeSubImage = (int) Math.sqrt(filters.clusterables.get(0).getWeights().length);
		int imageSize = (int) Math.sqrt(toTransform.get(0).getWeights().length);
		int newImageSize = imageSize - sizeSubImage+1;
		for(int i = 0;i<toTransform.size();i++){
			Clusterable current = toTransform.get(i);
			List<double[]> dividedImages = ImageUtils.divideImage(current.getWeights(), sizeSubImage, sizeSubImage, 
					imageSize, imageSize, 1, 1);
			double[] newImage = new double[newImageSize*newImageSize];
			for (int j = 0; j < newImage.length; j++) {
				SimpleClusterable sc = new SimpleClusterable(dividedImages.get(j));
				newImage[j] = filters.getClosestClusterIndex( sc);
			}
			toTransform.set(i, new SimpleClusterable(newImage,current.getLabel()));
		}
		
	}

}
