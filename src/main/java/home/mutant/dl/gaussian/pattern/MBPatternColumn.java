package home.mutant.dl.gaussian.pattern;

import home.mutant.dl.gaussian.Globals;
import home.mutant.dl.models.Image;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MBPatternColumn implements Runnable, Comparable<MBPatternColumn>{
	List<MBPatternNeuron> neurons = new ArrayList<MBPatternNeuron>();
	public Set<Integer> indexes = new LinkedHashSet<Integer>();
	public int neuronSize;
	public int inputSize;
	List<Image> images ;
	
	public MBPatternColumn(int neuronSize, int inputSize, List<Image> images) {
		super();
		this.neuronSize = neuronSize;
		this.inputSize = inputSize;
		this.images = images;
		switch(Globals.FILL){
		case ROUND:
			fillIndexesRound();
			break;
		case UNIFORM:
			fillIndexesUniform();
			break;
		case SQUARE:
			fillIndexesSquares();
			break;
		}
		fillIndexesUniform();

	}
	
	public void fillIndexesSquares(){
		int inputX = (int) Math.sqrt(inputSize);
		int neuronX = (int) Math.sqrt(neuronSize);
		int x=(int) (Math.random()*(inputX-neuronX));
		int y=(int) (Math.random()*(inputX-neuronX));
		for (int i=x;i<x+neuronX;i++)
			for (int j=y;j<y+neuronX;j++){
				indexes.add((int)((i+j*inputX)));
			}
	}
	public void fillIndexesRound(){
		int inputX = (int) Math.sqrt(inputSize);
		int neuronX = (int) (2*Globals.RADIUS);
		int xMiddle = (int)(Globals.RADIUS+ (Math.random()*(inputX-neuronX)));
		int yMiddle = (int)(Globals.RADIUS+ (Math.random()*(inputX-neuronX)));
		while(indexes.size()<neuronSize){
			double alfa=2*Math.PI*Math.random();
			double r = Globals.RADIUS*Math.sqrt(Math.random());
			int dx = xMiddle+(int) (r*Math.cos(alfa));
			int dy = yMiddle+(int) (r*Math.sin(alfa));
			if (dx>=0 && dx<inputX && dy>=0 && dy<inputX)
			{
				indexes.add((int)((dx+dy*inputX)));
			}
		}
	}
	public void fillIndexesUniform(){
		while(indexes.size()<neuronSize){
			indexes.add((int)(Math.random()*inputSize));
		}
	}
	public void addImage(int indexImage){
		for (MBPatternNeuron neuron:neurons){
			if(neuron.isSimilar(indexImage)) return;
		}
		neurons.add(new MBPatternNeuron(neuronSize, indexImage, indexes));
	}
	public int getMaxSimilar(double[] pixels)
	{
		int max=-1;
		int indexMax=-1;
		for (int i=0;i<neurons.size();i++){
			int similar = (int) neurons.get(i).countSimilar(pixels);
			if (similar>max)
			{
				max=similar;
				indexMax =i;
			}
		}
		return indexMax;
	}
	@Override
	public void run() {
		for (int i=0;i<images.size();i++){
			addImage((int) (Math.random()*images.size()));
		}
		for (int i=0;i<images.size();i++){
			for (MBPatternNeuron neuron:neurons){
				neuron.trainImage(i);
			}
		}
	}

	@Override
	public int compareTo(MBPatternColumn o) {
		// TODO Auto-generated method stub
		return neurons.size()-o.neurons.size();
	}
}
