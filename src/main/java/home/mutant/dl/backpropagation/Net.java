package home.mutant.dl.backpropagation;

import home.mutant.dl.utils.MnistDatabase;

public class Net {
	public Layer[] layers;
	
	public static void main(String[] args) throws Exception {
		Net net = new Net(new int[]{784, 4});
		MnistDatabase.loadImagesNormalized();
		long t0=System.currentTimeMillis();
		for(int i=0;i<100000;i++){
			net.layers[0].z = MnistDatabase.trainImages.get(i%60000).getDataDouble();
			net.forward();
			//System.out.println("layer " + i);
		}
		
		System.out.println(System.currentTimeMillis()-t0);
	}
	
	public Net(int[] perLayer){
		layers = new Layer[perLayer.length];
		layers[0] = new Layer(perLayer[0]);
		for (int i = 1; i < perLayer.length; i++) {
			layers[i] = new Layer(perLayer[i-1], perLayer[i], i==0?null:layers[i-1]);
		}
	}
	public void forward() throws InterruptedException{
		for (int i = 1; i < layers.length; i++) {
			layers[i].forward();
		}
	}
}
