package home.mutant.dl.utils.kmeans.smooth;

import home.mutant.dl.utils.kmeans.model.ListClusterable;

public class RunSmoothieOpenCl {
	public static final int FRAMES = 100;
	public static void main(String[] args) {
		
		ListClusterable filters = ListClusterable.load("clusters4_256");
		filters.clusterables = filters.clusterables.subList(0, 256);
		LinkedClusterablesOpenCl sm = new LinkedClusterablesOpenCl(filters);
		long t0 = System.currentTimeMillis();
		for(int i=0;i<FRAMES;i++){
			sm.stepV();
			//sm.stepX();
			//if(i%1==0)
			//	sm.show();
			//System.out.println(i);
		}
		System.out.println("FPS:" + (1000.*FRAMES/(System.currentTimeMillis()-t0)));
		//sm.listDistances();
		sm.release();
	}

}
