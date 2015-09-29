package home.mutant.dl.gaussian.pattern;

import home.mutant.dl.gaussian.ClassifyGBColumn;
import home.mutant.dl.gaussian.ClassifyGBNeuron;
import home.mutant.dl.gaussian.GBNeuron;
import home.mutant.dl.gaussian.Globals;
import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RunGaussianMBPatternNet {
	
	public static void main(String[] args) throws IOException {
		MnistDatabase.loadImages();
		MBPatternNet net  = train();
		List<MBPatternNeuron> allNeurons = new ArrayList<MBPatternNeuron>();
		for (MBPatternColumn column:net.columns)
		{
			allNeurons.addAll(column.neurons);
		}
		System.out.println(allNeurons.size());
		Collections.sort(allNeurons, Collections.reverseOrder());
		
		int offset = 0;
		switch (Globals.OFFSET) {
		case MIDDLE:
			offset = (allNeurons.size()-Globals.SUB_LIST_NEURONS)/2;
			break;
		case LEAST_FREQUENT:
			offset = allNeurons.size()-Globals.SUB_LIST_NEURONS;
			break;
		default:
			offset = 0;
			break;
		}
		
		List<Image> imgs = new ArrayList<Image>();
		for(int i=offset;i<Globals.SUB_LIST_NEURONS+offset;i++){
			imgs.add(allNeurons.get(i).generateImage());
		}
		ResultFrame frame = new ResultFrame(1300, 800);
		frame.showImages(imgs);
		
		new TransformImagesPatternNeurons(MnistDatabase.trainImages.subList(0, Globals.NO_TRAIN), allNeurons.subList(offset, Globals.SUB_LIST_NEURONS+offset)).transform();
		new TransformImagesPatternNeurons(MnistDatabase.testImages.subList(0, Globals.NO_TEST), allNeurons.subList(offset, Globals.SUB_LIST_NEURONS+offset)).transform();
		ClassifyGBNeuron bn = new ClassifyGBNeuron(MnistDatabase.trainImages.subList(0, Globals.NO_TRAIN), MnistDatabase.trainLabels.subList(0, Globals.NO_TRAIN));
		System.out.println(bn.getRate(MnistDatabase.testImages.subList(0, Globals.NO_TEST), MnistDatabase.testLabels.subList(0, Globals.NO_TEST)));

	}
	public static void main1(String[] args) throws IOException {
		MnistDatabase.loadImages();
		MBPatternNet net  = train();
		List<MBPatternNeuron> allNeurons = new ArrayList<MBPatternNeuron>();
		for (MBPatternColumn column:net.columns)
		{
			allNeurons.addAll(column.neurons);
		}
		System.out.println(allNeurons.size());
		
		test(allNeurons);
		Collections.sort(allNeurons, Collections.reverseOrder());
		List<Image> imgs = new ArrayList<Image>();
		for(int i=0;i<800;i++){
			imgs.add(allNeurons.get(i).generateImage());
		}
		ResultFrame frame = new ResultFrame(600, 600);
		frame.showImages(imgs);
//		test(allNeurons.subList(0, 2000));
//		test(allNeurons.subList(0, 4000));
//		test(allNeurons.subList(0, 6000));
//		test(allNeurons.subList(0, 8000));
	}
	
	private static MBPatternNet train() {
		MBPatternNet net = new MBPatternNet(Globals.NO_COLUMNS, Globals.SUBIMG_SIZE, MnistDatabase.trainImages.get(0).data.length,Globals.NO_TRAIN);
		net.trainImages();
		return net;
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
			double[] pixels = MnistDatabase.trainImages.get(i).data;
			for (int j = 0; j < allNeurons.size(); j++) {
				int countSimilar = (int) allNeurons.get(j).countSimilar(pixels);
				input[j] = countSimilar>=Globals.MAX_PIXEL_VALUE*Globals.SIMILARITY?countSimilar:0;
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
			double[] pixels = MnistDatabase.testImages.get(i).data;
			for (int j = 0; j < allNeurons.size(); j++) {
				int countSimilar = (int) allNeurons.get(j).countSimilar(pixels);
				input[j] = countSimilar>=Globals.MAX_PIXEL_VALUE*Globals.SIMILARITY?countSimilar:0;
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
