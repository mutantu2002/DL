package home.mutant.dl.particlefilter;

import java.util.ArrayList;
import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.models.ImageFloat;
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
	int xInput;
	int yInput;
	
	public ParticleFilter(Image mapImage, Image inputImage, int noParticles){
		this.mapImage=mapImage;
		this.inputImage=inputImage;
		this.imageSize = inputImage.getDataFloat().length;
		this.dimImage = (int) Math.sqrt(imageSize);
		this.mapSize = mapImage.getDataFloat().length;
		this.dimMap = (int) Math.sqrt(mapSize);
		this.noParticles = noParticles;
		initParticles();
		xInput=dimImage/2;
		yInput=dimImage/2;
	}

	private void initParticles() {
		for(int i=0;i<noParticles;i++){
			Particle p = new Particle();
			p.x=(int) (Math.random()*dimMap);
			p.y=(int) (Math.random()*dimMap);
			p.weight=(float) (1./noParticles);
			particles.add(p);
		}
	}
	public void step(){
		float measurement = inputImage.getDataFloat()[yInput*dimImage+xInput];
		estimateParticlesWeights(measurement);
		normalizeWeights();
		recreateParticles();
		int xNew=xInput;
		int yNew=yInput;
		xNew=(int) (dimImage-dimImage*Math.random());
		if(xNew>=dimImage)xNew=dimImage-1;
		if(xNew<0)xNew=0;
		yNew=(int) (dimImage-dimImage*Math.random());
		if(yNew>=dimImage)yNew=dimImage-1;
		if(yNew<0)yNew=0;
		moveParticles(xNew-xInput, yNew-yInput);
		xInput=xNew;
		yInput=yNew;
		System.out.println(xInput+";"+yInput);
	}
	private void estimateParticlesWeights(float measurement){
		for(int i=0;i<particles.size();i++){
			Particle particle = particles.get(i);
			float diff = mapImage.getDataFloat()[(int) (particle.y*dimMap+particle.x)]-measurement;
			particle.weight=MathUtils.gaussian(diff, 40);
		}
	}
	private void normalizeWeights(){
		double sum=0;
		for(int i=0;i<particles.size();i++){
			sum+= particles.get(i).weight;
		}
		for(int i=0;i<particles.size();i++){
			particles.get(i).weight/=sum;
		}
		System.out.println("sum "+sum);
	}
	private void recreateParticles(){
		List<Particle> newParticles = new ArrayList<>();
		int i=0;
		while(newParticles.size()<particles.size()){
			Particle p = particles.get(i);
			if(Math.random()<p.weight){
				Particle pNew = new Particle();
				pNew.x=p.x;
				pNew.y=p.y;
				pNew.weight=(float) (1./noParticles);
				newParticles.add(pNew);
			}
			i++;
			i=i%noParticles;
		}
		particles=newParticles;
	}
	
	private void moveParticles(int x, int y){
		for(int i=0;i<particles.size();i++){
			Particle p = particles.get(i);
			p.x+=x+2-4*Math.random();
			if(p.x>=dimMap)p.x=dimMap-1;
			if(p.x<0)p.x=0;
			p.y+=y+2-4*Math.random();
			if(p.y>=dimMap)p.y=dimMap-1;
			if(p.y<0)p.y=0;
		}
	}
	
	public Image getImageParticles(){
		Image imgParticles = new ImageFloat(mapImage.getDataFloat().length);
		for(int i=0;i<particles.size();i++){
			Particle p = particles.get(i);
			double value = imgParticles.getPixel(p.x, p.y)+1;
			if(value>255)value=255;
			imgParticles.setPixel(p.x, p.y, value);
		}
		return imgParticles;
	}
}
