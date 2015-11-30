package home.mutant.dl.utils.kmeans.model;

import java.util.List;

import home.mutant.dl.models.Image;

public interface Clusterable {
	public void setLabel(int label);
	public int getLabel();
	public double d(Clusterable clusterable);
	public double[] getWeights();
	public void setWeights(double[] weights);
	public Clusterable randomize();
	public Clusterable copy();
	public Image getImage();
	public void updateCenterFromMembers(List<Clusterable> allList, List<Integer> members);
}
