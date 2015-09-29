package home.mutant.dl.utils.multithreading;

import home.mutant.dl.gaussian.pattern.TransformImagesPatternNeurons;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher {
	List<Runnable> runnables = new ArrayList<Runnable>();
	List<Thread> threads = new ArrayList<Thread>();
	
	public Launcher(){
	}
	
	public Launcher(List<Runnable> runnables) {
		this.runnables = runnables;
	}
	public void addRunnable(Runnable runnable){
		runnables.add(runnable);
	}
	public void run(){
		init();
		join();
	}
	public void init(){
		for (int i = 0; i < runnables.size(); i++) {
			threads.add(new Thread(runnables.get(i)));
			threads.get(i).start();
		}
	}
	public void join(){
		for (int i = 0; i < runnables.size(); i++) {
			try {
				threads.get(i).join();
			} catch (InterruptedException ex) {
				Logger.getLogger(TransformImagesPatternNeurons.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
