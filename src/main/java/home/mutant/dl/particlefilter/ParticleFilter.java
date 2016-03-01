package home.mutant.dl.particlefilter;

import java.util.ArrayList;
import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.utils.MathUtils;

public class ParticleFilter {
	Image mapImage;
	Image inputImage;
	int dimMap;
	int dimImage;
	int imageSize;
	int mapSize;
	List<Particle> particles = new ArrayList<>();
	int noParticles;
	float xInput;
	float yInput;
	
	public ParticleFilter(Image mapImage, Image inputImage, int noParticles){
		this.mapImage=mapImage;
		this.inputImage=inputImage;
		this.imageSize = inputImage.getDataFloat().length;
		this.dimImage = (int) Math.sqrt(imageSize);
		this.mapSize = mapImage.getDataFloat().length;
		this.dimMap = (int) Math.sqrt(mapSize);
		this.noParticles = noParticles;
		initParticles();
		xInput=(float) (Math.random()*dimImage);
		yInput=(float) (Math.random()*dimImage);
	}

	private void initParticles() {
		for(int i=0;i<noParticles;i++){
			Particle p = new Particle();
			p.x=(float) (Math.random()*dimMap);
			p.y=(float) (Math.random()*dimMap);
			p.weight=(float) (1./noParticles);
			particles.add(p);
		}
	}
	private void estimateParticlesWeights(float measurement){
		for(int i=0;i<particles.size();i++){
			Particle particle = particles.get(i);
			float diff = mapImage.getDataFloat()[(int) (particle.y*dimMap+particle.x)]-measurement;
			particle.weight=(float) MathUtils.gaussian(diff, 100);
		}
	}
	private void normalizeWeights(){
		float sum=0;
		for(int i=0;i<particles.size();i++){
			sum+= particles.get(i).weight;
		}
		for(int i=0;i<particles.size();i++){
			particles.get(i).weight/=sum;
		}
	}
	private void recreateParticles(){
		List<Particle> newParticles = new ArrayList<>();
		while(newParticles.size()<particles.size()){
			
		}
	}
}
