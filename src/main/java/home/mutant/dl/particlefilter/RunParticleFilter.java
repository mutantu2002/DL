package home.mutant.dl.particlefilter;

import java.io.IOException;

import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;
import home.mutant.dl.utils.MnistDatabase.TYPE;

public class RunParticleFilter {

	public static void main(String[] args) throws IOException {
		MnistDatabase.IMAGE_TYPE = TYPE.FLOAT;
		MnistDatabase.loadImages();
		Map map = new Map(MnistDatabase.trainImages, 5);
		ParticleFilter pf = new ParticleFilter(map.map, MnistDatabase.trainImages.get(8), 4000);
		ResultFrame frame = new ResultFrame(600, 600);
		frame.showImage(pf.getImageParticles());
		for(int step=0;step<100;step++){
			pf.step();
			frame.showImage(pf.getImageParticles());
		}
		System.out.println("Done");
	}

}
