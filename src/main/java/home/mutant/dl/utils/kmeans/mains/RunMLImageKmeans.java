package home.mutant.dl.utils.kmeans.mains;

import home.mutant.dl.utils.MnistDatabase;
import home.mutant.dl.utils.kmeans.model.Clusterable;
import home.mutant.dl.utils.kmeans.model.ListClusterable;
import home.mutant.dl.utils.kmeans.model.SimpleClusterable;
import home.mutant.dl.utils.kmeans.runnables.TransformClusterablesRunnable;
import home.mutant.dl.utils.multithreading.Launcher;

import java.util.ArrayList;
import java.util.List;


public class RunMLImageKmeans {
	private static final int NO_THREADS = 8;
	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImages();
		List<Clusterable> clusterables = new ArrayList<Clusterable>();
		for (int i = 0; i < 60000; i++) {
			clusterables.add(new SimpleClusterable(MnistDatabase.trainImages.get(i).data,MnistDatabase.trainLabels.get(i)));
		}
		ListClusterable filters = ListClusterable.load("clusters4.ser");
		System.out.println(filters.clusterables.size());
		filters.show();
		Launcher launcher = new Launcher();
		int step = clusterables.size() / NO_THREADS;
		
		for (int i = 0; i < NO_THREADS; i++) {
			launcher.addRunnable(new TransformClusterablesRunnable(clusterables.subList(i*step, (i+1)*step), filters));
		}
		launcher.run();
		ListClusterable results = new ListClusterable();
		results.clusterables = clusterables.subList(0, 100);
		results.show();
		
		List<Clusterable> clusterablesTest = new ArrayList<Clusterable>();
		for (int i = 0; i < 10000; i++) {
			clusterablesTest.add(new SimpleClusterable(MnistDatabase.testImages.get(i).data));
		}
		
		launcher = new Launcher();
		step = clusterablesTest.size() / NO_THREADS;
		
		for (int i = 0; i < NO_THREADS; i++) {
			launcher.addRunnable(new TransformClusterablesRunnable(clusterablesTest.subList(i*step, (i+1)*step), filters));
		}
		launcher.run();
		
		System.out.println(RunImageKmeans.run(clusterables,clusterablesTest));
	}
}
