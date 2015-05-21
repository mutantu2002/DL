package home.mutant.dl.utils.kmeans;

public class SimpleClusterable implements Clusterable {
	double weights[];
	
	public SimpleClusterable(int size) {
		weights = new double[size];
	}
	@Override
	public double d(Clusterable clusterable) {
		double d=0;
		for (int i = 0; i < weights.length; i++) {
			d+=(weights[i] - clusterable.getWeights()[i])*(weights[i] - clusterable.getWeights()[i]);
		}
		return Math.sqrt(d);
	}

	@Override
	public double[] getWeights() {
		// TODO Auto-generated method stub
		return weights;
	}

	@Override
	public Clusterable randomize() {
		SimpleClusterable m = new SimpleClusterable(weights.length);
		m.randomizeWeights();
		return m;
	}
	public void randomizeWeights(){
		for (int i = 0; i < weights.length; i++) {
			weights[i] = Math.random()*255;
		}
	}
	@Override
	public Clusterable copy() {
		SimpleClusterable m = new SimpleClusterable(weights.length);
		System.arraycopy(weights, 0, m.getWeights(), 0, weights.length);
		return m;
	}
}
