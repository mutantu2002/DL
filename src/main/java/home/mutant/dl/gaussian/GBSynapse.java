/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.mutant.dl.gaussian;


/**
 *
 * @author cipi
 */
public class GBSynapse {
	public double mean = 0;
	public double variance = 0;
	
	public double sum = 0;
	public double sumSquares=0;
	public double noInstances = 1;
	
	
	public GBSynapse() {
		super();
		for (int i=0;i<0;i++)
			addTarget((float) (Math.random()*255));
	}
	public void addTarget(double pixel, int weight){
		sum+=pixel*weight;
		sumSquares+=weight*pixel*pixel;
		noInstances+=weight;
		
	}
	public void addTarget(double pixel){
		addTarget(pixel,1);
	}
	
	public void addTarget(int pixel){
		addTarget(pixel,1);
	}
	
	public void calculateMeanDeviationBySum(){
		mean=sum/noInstances;
		variance = sumSquares/noInstances-mean*mean;
	}

	public double getPosterior(double pixel){
		calculateMeanDeviationBySum();
		if(variance==0){
			if(mean==pixel)return 0;
			else 
				return -10;
		}
		else{
			double d = -(pixel-mean)*(pixel-mean)/2/variance;
			return d;
		}
	}
}
