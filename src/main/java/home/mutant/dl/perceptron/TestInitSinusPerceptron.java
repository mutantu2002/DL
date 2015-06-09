package home.mutant.dl.perceptron;

import home.mutant.dl.ui.ResultFrame;

public class TestInitSinusPerceptron {

	public static void main(String[] args) throws Exception {
		Perceptron p = new Perceptron(28*28);
		ResultFrame frame = new ResultFrame(200, 200);
		frame.showImage(p.getImage());
	}

}
