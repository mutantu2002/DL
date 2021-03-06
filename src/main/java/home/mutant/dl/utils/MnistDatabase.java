package home.mutant.dl.utils;


import home.mutant.dl.models.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MnistDatabase
{
	public enum TYPE{FLOAT,DOUBLE,SHORT};
	public static TYPE IMAGE_TYPE = TYPE.DOUBLE;
	public static List<Image> trainImages = new ArrayList<Image>();
	public static List<Integer> trainLabels  = new ArrayList<Integer>();

	public static List<Image> testImages = new ArrayList<Image>();
	public static List<Integer> testLabels  = new ArrayList<Integer>();
	
	public static List<List<Image>> getImagesByType(){
		List<List<Image>> trainImagesByType = new ArrayList<>();
		for(int i=0;i<10;i++){
			trainImagesByType.add(new ArrayList<>());
		}
		for(int i=0;i<trainImages.size();i++){
			trainImagesByType.get(trainLabels.get(i)).add(trainImages.get(i));
		}
		return trainImagesByType;
	}
	public static void loadImages() throws IOException
	{
		ImageUtils.loadImages(trainImages, testImages, trainLabels, testLabels);
	}
	
	public static void loadImagesNormalized() throws IOException
	{
		ImageUtils.loadImages(trainImages, testImages, trainLabels, testLabels);
		normalize(MnistDatabase.trainImages);
		normalize(MnistDatabase.testImages);
	}
	
	public static void loadImagesCrop(int newImageSize) throws IOException
	{
		trainLabels.addAll(ImageUtils.readMinstLabels("/mnist/train-labels.idx1-ubyte"));
		testLabels.addAll(ImageUtils.readMinstLabels("/mnist/t10k-labels.idx1-ubyte"));
		trainImages.addAll(ImageUtils.readMnistAsImage("/mnist/train-images.idx3-ubyte", newImageSize));
		testImages.addAll(ImageUtils.readMnistAsImage("/mnist/t10k-images.idx3-ubyte", newImageSize));			
	}
	
	public static void loadImagesScaled(double scale) throws IOException
	{
		MnistDatabase.loadImages();
		replaceImagesWithScaled(trainImages, scale);
		replaceImagesWithScaled(testImages, scale);
	}
	public static void loadImagesPadded(int padding) throws IOException
	{
		MnistDatabase.loadImages();
		replaceImagesWithPadded(trainImages, padding);
		replaceImagesWithPadded(testImages, padding);
	}
	public static void loadImagesBlured() throws IOException
	{
		MnistDatabase.loadImages();
		replaceImagesWithBlured(trainImages);
		replaceImagesWithBlured(testImages);
	}
	
	private static void replaceImagesWithBlured(List<Image> images)
	{
		for(int i=0;i<images.size();i++)
		{
			images.set(i, ImageUtils.blurImage(images.get(i)));
		}
	}
	
	public static void loadImagesGradient() throws IOException
	{
		MnistDatabase.loadImages();
		replaceImagesWithGradient(trainImages);
		replaceImagesWithGradient(testImages);
	}
	private static void replaceImagesWithGradient(List<Image> images)
	{
		for(int i=0;i<images.size();i++)
		{
			images.set(i, ImageUtils.gradientImage(images.get(i)));
		}
	}
	private static void replaceImagesWithScaled(List<Image> images, double scale)
	{
		for(int i=0;i<images.size();i++)
		{
			images.set(i, images.get(i).scaleImage(scale));
		}
	}
	private static void normalize(List<Image> images)
	{
		for(int i=0;i<images.size();i++)
		{
			images.get(i).normalize();
		}
	}
	public static void replaceImagesWithPadded(List<Image> images,int padding){
		for(int i=0;i<images.size();i++)
		{
			images.set(i, images.get(i).padImage(padding));
		}
	}
}
