package home.mutant.dl.entropy;

import home.mutant.dl.models.Image;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	public Neuron[] neurons;
	public double ylogy;
	int maxValues[];
	public Layer(int size, int neuronSize){
		maxValues = new int[size];
		neurons = new Neuron[size];
		for (int i = 0; i < neurons.length; i++) {
			neurons[i] = new Neuron(neuronSize);
		}
	}
	
	public void stepBatch(double[] data, double[] targets){
		calculateActivations(data);
		updateErrors(data, targets);
	}

	public void updateErrors(double[] data, double[] targets) {
		for (int i = 0; i < neurons.length; i++) {
			neurons[i].dEdZ = neurons[i].y-targets[i];
			neurons[i].updateErrors(data);
		}
	}

	public void calculateActivations(double[] data) {
		double norm=0;
		double max = -Double.MAX_VALUE;
		int indexMax=1;
		for (int i = 0; i < neurons.length; i++) {
			norm+=neurons[i].calculateActivation(data);
			if (neurons[i].y>max)
			{
				max=neurons[i].y;
				indexMax = i;
			}
		}
		maxValues[indexMax]++;
		for (int i = 0; i < neurons.length; i++) {
			neurons[i].y/=norm;
		}
	}
	
	public void stepBatch_1(double[] data, double[] targets){
		double norm=0;
		for (int i = 0; i < neurons.length; i++) {
			norm+=neurons[i].calculateActivation(data);
		}
		ylogy=0;
		for (int i = 0; i < neurons.length; i++) {
			neurons[i].y/=norm;
			neurons[i].z = Math.log(neurons[i].y);
			ylogy+=neurons[i].y*neurons[i].z;
		}
		for (int i = 0; i < neurons.length; i++) {
			neurons[i].dEdZ= neurons[i].y*(neurons[i].z - ylogy);
			//neurons[i].dEdZ = neurons[i].y-targets[i];
			neurons[i].updateErrors(data);
		}
	}

	public void updateWeights(){
		for (int i = 0; i < neurons.length; i++) {
			neurons[i].updateWeights();
		}
	}
	public List<Image> getImages() {
		List<Image> images = new ArrayList<Image>();
		for (int i = 0; i < neurons.length; i++) {
			images.add(neurons[i].getImage());
		}
		return images;
	}
}
