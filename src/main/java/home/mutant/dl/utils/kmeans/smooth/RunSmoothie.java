package home.mutant.dl.utils.kmeans.smooth;

import home.mutant.dl.utils.kmeans.model.ListClusterable;

public class RunSmoothie {

	public static void main(String[] args) {
		ListClusterable filters = ListClusterable.load("clusters4_256");
		filters.clusterables = filters.clusterables.subList(0, 256);
		LinkedClusterables sm = new LinkedClusterables(filters);
		for(int i=0;i<200000;i++){
			sm.stepV();
			sm.stepX();
			sm.show();
			if(i%1000==0)System.out.println(i);
		}
		sm.listDistances();
	}

}
