package home.mutant.dl.correlation;

public class Correlation {
	public int x;
	public int y;
	public Feature f1;
	public Feature f2;
	
	public Correlation(Feature f1, Feature f2) {
		super();
		this.f1 = f1;
		this.f2 = f2;
	}

	public double calculateCorrelation(){
		f1.calculateMeanDeviation();
		f2.calculateMeanDeviation();
		double correlation=0;
		for (int i=0;i< f1.values.size();i++) {
			correlation+=(f1.values.get(i)-f1.mean)*(f2.values.get(i)-f2.mean);
		}
		correlation/=f1.values.size();
		correlation/=f1.deviation;
		correlation/=f2.deviation;
		return correlation;
	}
}
