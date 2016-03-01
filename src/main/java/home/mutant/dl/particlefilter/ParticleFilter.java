package home.mutant.dl.particlefilter;

import java.util.List;

import home.mutant.dl.models.Image;

public class ParticleFilter {
	Image mapImage;
	Image inputImage;
	float[][] probability;
	int dimMap;
	int dimImage;
	int imageSize;
	int mapSize;
	List<Particle> particles;
	
	public ParticleFilter(Image mapImage, Image inputImage){
		this.mapImage=mapImage;
		this.inputImage=inputImage;
		this.imageSize = inputImage.getDataFloat().length;
		this.dimImage = (int) Math.sqrt(imageSize);
		this.mapSize = mapImage.getDataFloat().length;
		this.dimMap = (int) Math.sqrt(mapSize);
		probability = new float[dimMap][dimMap];
		initProbability();
	}
	
	private void initProbability(){
		for(int i=0;i<dimMap;i++){
			for(int j=0;j<dimMap;j++){
				probability[i][j]=(float) (1./(dimMap*dimMap));
			}
		}
	}
}
