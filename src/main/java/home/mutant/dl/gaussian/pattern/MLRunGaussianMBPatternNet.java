package home.mutant.dl.gaussian.pattern;

import home.mutant.dl.gaussian.GBNeuron;
import home.mutant.dl.gaussian.Globals;
import home.mutant.dl.gaussian.Globals.FILL_TYPE;
import home.mutant.dl.models.Image;
import home.mutant.dl.models.ImageDouble;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MLRunGaussianMBPatternNet {

	public static void main(String[] args) throws IOException {
		MnistDatabase.loadImages();
		MBPatternNet net  = train();
		List<MBPatternNeuron> allNeurons = new ArrayList<MBPatternNeuron>();
		for (MBPatternColumn column:net.columns)
		{
			allNeurons.addAll(column.neurons);
		}
		Collections.sort(allNeurons, Collections.reverseOrder());
		System.out.println(allNeurons.size());
		test(allNeurons.subList(0, Globals.SUB_LIST_NEURONS));
		for (int i=0;i<Globals.NO_TRAIN;i++)
		{
			replaceImage(allNeurons.subList(0, Globals.SUB_LIST_NEURONS), i, MnistDatabase.trainImages);
		}
		for (int i=0;i<Globals.NO_TEST;i++)
		{
			replaceImage(allNeurons.subList(0, Globals.SUB_LIST_NEURONS), i, MnistDatabase.testImages);
		}
		ResultFrame frame = new ResultFrame(600, 500);
		frame.showImages(MnistDatabase.trainImages.subList(0, 100));
//		MAX_PIXEL_VALUE = 16;
		Globals.NO_COLUMNS = 1000;
//		Globals.SUBIMG_SIZE=49;
//		Globals.SIMILARITY = 0.95;
//		Globals.SUBIMG_SIZE = 4;
		Globals.FILL = FILL_TYPE.UNIFORM;
		net  = train();
		test(net);
		allNeurons.clear();
		for (MBPatternColumn column:net.columns)
		{
			allNeurons.addAll(column.neurons);
		}
		Collections.sort(allNeurons, Collections.reverseOrder());
		test(allNeurons.subList(0, allNeurons.size()/10));
		test(allNeurons.subList(0, allNeurons.size()/9));
		test(allNeurons.subList(0, allNeurons.size()/8));
		test(allNeurons.subList(0, allNeurons.size()/7));
		test(allNeurons.subList(0, allNeurons.size()/6));
		test(allNeurons.subList(0, allNeurons.size()/5));
		test(allNeurons.subList(0, allNeurons.size()/4));
		test(allNeurons.subList(0, allNeurons.size()/3));
		test(allNeurons.subList(0, allNeurons.size()/2));
		
	}

	public static void replaceImage(List<MBPatternNeuron> neurons, int indexImage, List<Image> images){
		double[] newImage = new double[neurons.size()];
		double[] pixels = images.get(indexImage).getDataDouble();
		for (int j = 0; j < neurons.size(); j++) {
			int countSimilar = (int) neurons.get(j).countSimilar(pixels);
			newImage[j] = (byte) (countSimilar>100?countSimilar:0);
		}
		//images.set(indexImage, ImageUtils.scaleImage(new Image(newImage),0.5));
		images.set(indexImage, new ImageDouble(newImage));
	}
	public static void main2(String[] args) throws IOException {
		MnistDatabase.loadImages();
		MBPatternNet subBest = new MBPatternNet(0, Globals.SUBIMG_SIZE, MnistDatabase.trainImages.get(0).getDataDouble().length,Globals.NO_TRAIN);
		MBPatternNet theNet = new MBPatternNet(0, Globals.SUBIMG_SIZE, MnistDatabase.trainImages.get(0).getDataDouble().length,Globals.NO_TRAIN);
		for (int j=0;j<5;j++){
			double max=0;
			for (int i=0;i<50;i++)
			{
				MBPatternNet net  = train();
				double p = test(net);
				if (p>max)
				{
					subBest = net;
					max=p;
				}
				System.out.println();
				
			}
			System.out.println(max);
			theNet.columns.addAll(subBest.columns);
		}
		System.out.println("*************");
		test(theNet);
		
	}
	
	public static void main5(String[] args) throws IOException {
		MnistDatabase.loadImages();
		MBPatternNet theNet = new MBPatternNet(0, Globals.SUBIMG_SIZE, MnistDatabase.trainImages.get(0).getDataDouble().length,Globals.NO_TRAIN);
		MBPatternNet net  = train();
		test(net);
		int size = net.columns.size();
		for (int j=0;j<50;j++){
			theNet.columns.clear();
			
			while (theNet.columns.size()<size*0.5)
			{
				theNet.columns.add(net.columns.get((int) (Math.random()*size)));
			}
			test(theNet);
		}
	}

	public static void main7(String[] args) throws IOException {
		MnistDatabase.loadImages();
		List<MBPatternNeuron> theNeurons = new ArrayList<MBPatternNeuron>();
		MBPatternNet net  = train();
		List<MBPatternNeuron> allNeurons = new ArrayList<MBPatternNeuron>();
		for (MBPatternColumn column:net.columns)
		{
			allNeurons.addAll(column.neurons);
		}
		test(allNeurons);
		int size = (int) (allNeurons.size()*0.3);
		Collections.sort(allNeurons, Collections.reverseOrder());
		for (int j=0;j<20;j++){
			theNeurons.clear();
			size+=50;
			for(int s=0;s<size;s++)
			{
				theNeurons.add(allNeurons.get(s));
			}
			test(theNeurons);
		}
		
	}
	
	public static void main6(String[] args) throws IOException {
		MnistDatabase.loadImages();
		List<MBPatternNeuron> theNeurons = new ArrayList<MBPatternNeuron>();
		MBPatternNet net  = train();
		List<MBPatternNeuron> allNeurons = new ArrayList<MBPatternNeuron>();
		for (MBPatternColumn column:net.columns)
		{
			allNeurons.addAll(column.neurons);
		}
		test(allNeurons);
		int size = allNeurons.size();
		for (int j=0;j<50;j++){
			theNeurons.clear();
			
			while (theNeurons.size()<size*0.2)
			{
				theNeurons.add(allNeurons.get((int) (Math.random()*size)));
			}
			test(theNeurons);
		}
	}
	
	public static void main4(String[] args) throws IOException {
		MnistDatabase.loadImages();
		MBPatternNet theNet = new MBPatternNet(0, Globals.SUBIMG_SIZE, MnistDatabase.trainImages.get(0).getDataDouble().length,Globals.NO_TRAIN);
		MBPatternNet net  = train();
		test(net);
		Collections.sort(net.columns, Collections.reverseOrder());
		int size = (int) (net.columns.size()*0.05);
		for (int j=0;j<50;j++){
			theNet.columns.clear();
			size+=1;
			for(int s=0;s<size;s++)
			{
				theNet.columns.add(net.columns.get(s));
			}
			test(theNet);
		}
	}
	private static MBPatternNet train() {
		MBPatternNet net = new MBPatternNet(Globals.NO_COLUMNS, Globals.SUBIMG_SIZE, MnistDatabase.trainImages.get(0).getDataDouble().length,Globals.NO_TRAIN);
		net.trainImages();
		return net;
	}

	private static double test(MBPatternNet net) {
		List<MBPatternNeuron> allNeurons = new ArrayList<MBPatternNeuron>();
		for(MBPatternColumn column:net.columns){
			for (MBPatternNeuron neuron:column.neurons){
					allNeurons.add(neuron);
			}
		}
		//System.out.println(initCount+" - "+ allNeurons.size());
		int[] input = new int[allNeurons.size()];
		List<GBNeuron> b10 = new ArrayList<GBNeuron>();
		for (int i=0;i<10;i++){
			b10.add(new GBNeuron(allNeurons.size()));
		}
		//MBPatternNeuron.similarityRate = 0.75;
		for (int i=0;i<Globals.NO_TRAIN;i++)
		{
			double[] pixels = MnistDatabase.trainImages.get(i).getDataDouble();
			for (int j = 0; j < allNeurons.size(); j++) {
				int countSimilar = (int) allNeurons.get(j).countSimilar(pixels);
				input[j] = countSimilar>=Globals.SUBIMG_SIZE*Globals.SIMILARITY?countSimilar:0;
			}
			int classIndex = MnistDatabase.trainLabels.get(i);
			b10.get(classIndex).trainInput(input);
		}

		//ResultFrame frame = new ResultFrame(600, 600);
		List<Image> imgs = new ArrayList<Image>();
		for (int i=0;i<10;i++)
		{
			imgs.add(b10.get(i).generateImage());
		}
		//frame.showImages(imgs);
		int count=0;
		for (int i=0;i<Globals.NO_TEST;i++)
		{
			double[] pixels = MnistDatabase.testImages.get(i).getDataDouble();
			for (int j = 0; j < allNeurons.size(); j++) {
				int countSimilar = (int) allNeurons.get(j).countSimilar(pixels);
				input[j] = countSimilar>=Globals.SUBIMG_SIZE*Globals.SIMILARITY?countSimilar:0;
			}
			if (getMax(b10,input)==MnistDatabase.testLabels.get(i))
			{
				count++;
			}
		}
		double percent = (count*100.)/Globals.NO_TEST;
		double percentPerFeature = percent/allNeurons.size();
		System.out.println(percent +" - "+allNeurons.size()+" features, "+percentPerFeature+" percent per feature");
		return percentPerFeature;
	}
	
	private static double test(List<MBPatternNeuron> allNeurons) {
		int[] input = new int[allNeurons.size()];
		List<GBNeuron> b10 = new ArrayList<GBNeuron>();
		for (int i=0;i<10;i++){
			b10.add(new GBNeuron(allNeurons.size()));
		}
		//MBPatternNeuron.similarityRate = 0.75;
		for (int i=0;i<Globals.NO_TRAIN;i++)
		{
			double[] pixels = MnistDatabase.trainImages.get(i).getDataDouble();
			for (int j = 0; j < allNeurons.size(); j++) {
				int countSimilar = (int) allNeurons.get(j).countSimilar(pixels);
				input[j] = countSimilar>=Globals.SUBIMG_SIZE*Globals.SIMILARITY?countSimilar:0;
			}
			int classIndex = MnistDatabase.trainLabels.get(i);
			b10.get(classIndex).trainInput(input);
		}

		//ResultFrame frame = new ResultFrame(600, 600);
		List<Image> imgs = new ArrayList<Image>();
		for (int i=0;i<10;i++)
		{
			imgs.add(b10.get(i).generateImage());
		}
		//frame.showImages(imgs);
		int count=0;
		for (int i=0;i<Globals.NO_TEST;i++)
		{
			double[] pixels = MnistDatabase.testImages.get(i).getDataDouble();
			for (int j = 0; j < allNeurons.size(); j++) {
				int countSimilar = (int) allNeurons.get(j).countSimilar(pixels);
				input[j] = countSimilar>=Globals.SUBIMG_SIZE*Globals.SIMILARITY?countSimilar:0;
			}
			if (getMax(b10,input)==MnistDatabase.testLabels.get(i))
			{
				count++;
			}
		}
		double percent = (count*100.)/Globals.NO_TEST;
		double percentPerFeature = percent/allNeurons.size();
		System.out.println(percent +" - "+allNeurons.size()+" features, "+percentPerFeature+" percent per feature");
		return percentPerFeature;
	}
	
	public static int getMax(List<GBNeuron> neurons, int[] input)
	{
		double max = -1*Double.MAX_VALUE;
		int indexMax=-1;
		for (int b = 0; b < 10; b++)
		{
			final double output = neurons.get(b).getPosterior(input);
			if (output>max){
				max=output;
				indexMax=b;
			}
		}
		return indexMax;
	}
}
