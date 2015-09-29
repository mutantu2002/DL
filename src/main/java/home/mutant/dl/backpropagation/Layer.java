package home.mutant.dl.backpropagation;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	public double[] z;
	public double[] errors;
	public List<Relu> relus = new ArrayList<Relu>();
	public Layer downLayer;
	public Layer(int sizeIn, int sizeOut, Layer downLayer){
		for (int i = 0; i < sizeOut; i++) {
			relus.add(new Relu(sizeIn,1, this, i));
		}
		z = new double [sizeOut];
		errors = new double [sizeOut];
		this.downLayer = downLayer;
	}
	
	public Layer( int sizeOut){
		z = new double [sizeOut];
	}
	
	public void forward(){
		for (Relu relu : relus) {
			relu.forward();
		}
	}
}
