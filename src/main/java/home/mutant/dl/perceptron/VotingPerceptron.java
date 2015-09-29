package home.mutant.dl.perceptron;

import java.util.ArrayList;
import java.util.List;

public class VotingPerceptron {
	List<Perceptron> voters = new ArrayList<Perceptron>();
	
	public VotingPerceptron(int noVoters, int size){
		for (int i=0;i<noVoters;i++){
			voters.add(new Perceptron(size));
		}
	}
	
	public void trainData(double[] data, boolean isClass){
		for (Perceptron p :voters){
			p.trainData(data, isClass);
		}
	}
	
	public int noCorrectlyClassified(double[] data){
		int correct=0;
		for (Perceptron p :voters){
			if (p.output(data)) correct++;
		}
		return correct;
	}
	
	public double getTotalActivation(double[] data){
		double activation = 0;
		for (Perceptron p :voters){
			activation+=p.calculateActivation(data);
		}
		return activation;
	}
	
	public boolean correctlyClassified(double[] data){
		return noCorrectlyClassified(data)>voters.size()/2;
	}

	public void decreaseLearningRate() {
		for (Perceptron p :voters){
			p.learningRate/=1.1;
		}
		
	}
}
