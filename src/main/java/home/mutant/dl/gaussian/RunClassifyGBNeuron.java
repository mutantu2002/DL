/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home.mutant.dl.gaussian;

import home.mutant.dl.utils.MnistDatabase;

/**
 *
 * @author cipi
 */
public class RunClassifyGBNeuron {
	public static void main(String[] args) throws Exception 
	{
		MnistDatabase.loadImages();
//		ClassifyBNeuron bn = new ClassifyBNeuron(MnistDatabase.trainImages, MnistDatabase.trainLabels);
//		System.out.println(bn.getRate(MnistDatabase.testImages, MnistDatabase.testLabels));
		ClassifyGBNeuron bn = new ClassifyGBNeuron(MnistDatabase.trainImages.subList(0, 6000), MnistDatabase.trainLabels.subList(0, 6000));
		System.out.println(bn.getRate(MnistDatabase.testImages.subList(0, 1000), MnistDatabase.testLabels.subList(0, 1000)));
	}
}
