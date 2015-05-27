package home.mutant.dl.correlation;

import java.util.ArrayList;
import java.util.List;

public class Feature {
	public List<Double> values = new ArrayList<Double>();
	public double mean = 0;
	public double deviation = 0;
	
	public void calculateMeanDeviation(){
		mean=0;
		deviation=0;
		for (Double double1 : values) {
			mean+=double1;
			deviation+=double1*double1;
		}
		mean/=values.size();
		deviation/=values.size();
		deviation-=mean*mean;
		deviation = Math.sqrt(deviation);
	}
}
