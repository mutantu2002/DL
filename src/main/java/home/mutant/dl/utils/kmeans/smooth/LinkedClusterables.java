package home.mutant.dl.utils.kmeans.smooth;

import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.kmeans.model.Clusterable;
import home.mutant.dl.utils.kmeans.model.ClusterablePreDistance;
import home.mutant.dl.utils.kmeans.model.ListClusterable;

public class LinkedClusterables {
	public double preDistances[][];
	ListClusterable filters;
	int[] x;
	int[] y;
	double[] vx;
	double[] vy;
	ResultFrame frame;
	public LinkedClusterables(ListClusterable clusterables) {
		super();
		this.filters = clusterables;
		fillPreDistances();
		x = new int[filters.clusterables.size()];
		y = new int[filters.clusterables.size()];
		vx = new double[filters.clusterables.size()];
		vy = new double[filters.clusterables.size()];
		frame = new ResultFrame(800, 800);
	}
	private  void fillPreDistances(){
		preDistances = new double[filters.clusterables.size()][filters.clusterables.size()];
		for (int i=0;i<filters.clusterables.size();i++){
			for (int j=0;j<filters.clusterables.size();j++){
				ClusterablePreDistance.preDistances[i][j]=filters.clusterables.get(i).d(filters.clusterables.get(j));
			}
		}
	}
	public void show(){
		frame.drawingPanel.empty();
		for (int i=0;i<filters.clusterables.size();i++){
			Clusterable clusterable = filters.clusterables.get(i);
			int x1=x[i]+400;
			int y1=y[i]+400;
			if(x1>=0 && x1<396 && y1>=0 && y1<396){
				frame.putImage(clusterable.getImage(), x1, y1);
			}
		}
		frame.repaint();
	}
}
