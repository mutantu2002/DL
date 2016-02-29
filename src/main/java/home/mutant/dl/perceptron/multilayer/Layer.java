package home.mutant.dl.perceptron.multilayer;

import home.mutant.dl.models.Image;
import home.mutant.dl.perceptron.Perceptron;
import home.mutant.dl.perceptron.Perceptron.InitType;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	public List<Perceptron> perceptrons = new ArrayList<Perceptron>();
	public double[] activations;
	public double[] errors;
	public Layer(int sizeIn, int sizeOut){
		this(sizeIn, sizeOut, InitType.RANDOM);
	}
	
	public Layer(int sizeIn, int sizeOut, InitType initType){
		for (int i = 0; i < sizeOut; i++) {
			perceptrons.add(new Perceptron(sizeIn,1, initType));
		}
		activations = new double [sizeOut];
		errors = new double [sizeIn];
	}
	
	public void forward(double[] data){
		for (int i = 0; i < perceptrons.size(); i++) {
			activations[i] = perceptrons.get(i).outputDouble(data);
		}
	}
	
	public void decreaseLearningRate() {
		for (int i = 0;i<perceptrons.size();i++){
			perceptrons.get(i).decreaseLearningRate();
		}
	}
	
	public void increaseLearningRate() {
		for (int i = 0;i<perceptrons.size();i++){
			perceptrons.get(i).increaseLearningRate();
		}
	}
	
	public List<Image> getImages(){
		List<Image> images = new ArrayList<Image>();
		for (int i = 0; i < perceptrons.size(); i++) {
			images.add(perceptrons.get(i).getImage());
		}
		return images;
	}
}
