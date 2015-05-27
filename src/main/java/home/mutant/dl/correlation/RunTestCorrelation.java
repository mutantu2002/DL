package home.mutant.dl.correlation;

public class RunTestCorrelation {

	public static void main(String[] args) {
		Feature f1 = new Feature();
		Feature f2 = new Feature();
		Correlation corr = new Correlation(f1,f2);
		
		for (int i=0;i<1000;i++)
		{
			double random = Math.random();
			if(random<0.1){
				f1.values.add(-0.1);
			}else
			{
				if (random>0.9){
					f1.values.add(0.1);
				}else{
					f1.values.add(0.);
				}
			}
			
			random = Math.random();
			if(random<0.1){
				f2.values.add(-0.1);
			}else
			{
				if (random>0.9){
					f2.values.add(0.1);
				}else{
					f2.values.add(0.);
				}
			}
		}
		System.out.println(corr.calculateCorrelation());
	}

}
