package home.mutant.dl.particlefilter;

import java.util.ArrayList;
import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.models.ImageFloat;
import home.mutant.dl.utils.MathUtils;

public class ParticleFilter {
	public static final int NO_RND=1000;
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
	int step=0;
	int[] rnd = new int[NO_RND];
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
		for (int i=0;i<NO_RND;i++){
			rnd[i]=2-(int)(5*Math.random());
		}
	}

	private void initParticles() {
		for(int i=0;i<noParticles;i++){
			Particle p = new Particle();
			p.x=(int) (Math.random()*dimMap);
			p.y=(int) (Math.random()*dimMap);
			p.weight=1./noParticles;
			particles.add(p);
		}
	}
	public void step(){
		float measurement = inputImage.getDataFloat()[yInput*dimImage+xInput];
		//estimateParticlesWeights(measurement);
		normalizeWeights();
		recreateParticles();
		int xNew=xInput;
		int yNew=yInput;
		
		boolean test;
		do{
			xNew=(int) (dimImage-dimImage*Math.random());
			if(xNew>=dimImage)xNew=dimImage-1;
			if(xNew<0)xNew=0;
			yNew=(int) (dimImage-dimImage*Math.random());
			if(yNew>=dimImage)yNew=dimImage-1;
			if(yNew<0)yNew=0;
			
			if(step%2==0){
				test=inputImage.getDataFloat()[yNew*dimImage+xNew]==0;
			}else{
				test=inputImage.getDataFloat()[yNew*dimImage+xNew]!=0;
			}
		}while(test);
		step++;
		moveParticles(xNew-xInput, yNew-yInput);
		xInput=xNew;
		yInput=yNew;
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
	}
	@SuppressWarnings("unused")
	private void recreateParticles2(){
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
	
	private void recreateParticles(){
		List<Particle> newParticles = new ArrayList<>();
		for(int i=0;i<particles.size();i++){
			Particle p = particles.get(i);
			int noNew = (int) (p.weight*noParticles);
			for(int newI=0;newI<noNew;newI++){
				Particle pNew = new Particle();
				pNew.x=p.x;
				pNew.y=p.y;
				pNew.weight=1./noParticles;
				newParticles.add(pNew);
			}
		}
		particles=newParticles;
	}
	private void moveParticles(int x, int y){
		int index = (int) (NO_RND * Math.random());
		for(int i=0;i<particles.size();i++){
			Particle p = particles.get(i);
			p.x+=x+rnd[index++];
			index%=NO_RND;
			if(p.x>=dimMap)p.x=dimMap-1;
			if(p.x<0)p.x=0;
			p.y+=y+rnd[index];
			index%=NO_RND;
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
