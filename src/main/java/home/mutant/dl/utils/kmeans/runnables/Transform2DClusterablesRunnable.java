package home.mutant.dl.utils.kmeans.runnables;

import home.mutant.dl.utils.ImageUtils;
import home.mutant.dl.utils.kmeans.model.Clusterable;
import home.mutant.dl.utils.kmeans.model.Clusterable2D;
import home.mutant.dl.utils.kmeans.model.Clusterable2DSmoothie;
import home.mutant.dl.utils.kmeans.model.ClusterablePreDistance;
import home.mutant.dl.utils.kmeans.model.ListClusterable;
import home.mutant.dl.utils.kmeans.model.SimpleClusterable;
import home.mutant.dl.utils.kmeans.smooth.LinkedClusterablesOpenCl;

import java.util.List;

public class Transform2DClusterablesRunnable implements Runnable{
	List<Clusterable> toTransform;
	LinkedClusterablesOpenCl clusters;
	
	public Transform2DClusterablesRunnable(List<Clusterable> toTransform, LinkedClusterablesOpenCl clusters) {
		super();
		this.toTransform = toTransform;
		this.clusters = clusters;
	}

	@Override
	public void run() {
		int sizeSubImage = (int) Math.sqrt(clusters.filters.clusterables.get(0).getWeights().length);
		int imageSize = (int) Math.sqrt(toTransform.get(0).getWeights().length);
		int newImageSize = imageSize - sizeSubImage+1;
		for(int i = 0;i<toTransform.size();i++){
			Clusterable current = toTransform.get(i);
			List<double[]> dividedImages = ImageUtils.divideImage(current.getWeights(), sizeSubImage, sizeSubImage, 
					imageSize, imageSize, 1, 1);
			double[][] newImage = new double[2][newImageSize*newImageSize];
			for (int j = 0; j < newImage[0].length; j++) {
				SimpleClusterable sc = new SimpleClusterable(dividedImages.get(j));
				int indexCluster = clusters.filters.getClosestClusterIndex( sc);
				newImage[0][j]=clusters.x[indexCluster];
				newImage[1][j]=clusters.y[indexCluster];
			}
			toTransform.set(i, new Clusterable2DSmoothie(newImage,current.getLabel()));
		}
		
	}

}
