package home.mutant.dl.gaussian.pattern;

import home.mutant.dl.utils.MnistDatabase;

import java.util.ArrayList;
import java.util.List;

public class MBPatternNet {
	List<MBPatternColumn> columns = new ArrayList<MBPatternColumn>();
	public int inputSize;
	public int neuronSize;
	
	public MBPatternNet(int size, int neuronSize,int inputSize, int maxSize) {
		super();
		this.inputSize = inputSize;
		this.neuronSize = neuronSize;
		for (int i = 0;i<size;i++){
			columns.add(new MBPatternColumn(neuronSize, inputSize,MnistDatabase.trainImages.subList(0,maxSize)));
		}
	}
	public void trainImages(){
		List<Thread> lt = new ArrayList<Thread>();
		for (MBPatternColumn column:columns){
			Thread t = new Thread(column);
			lt.add(t);
			t.start();
		}
		for(Thread t:lt){
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
