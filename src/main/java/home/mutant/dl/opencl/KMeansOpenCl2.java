package home.mutant.dl.opencl;

import java.util.ArrayList;
import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.models.ImageFloat;
import home.mutant.dl.opencl.model.Kernel;
import home.mutant.dl.opencl.model.MemoryFloat;
import home.mutant.dl.opencl.model.MemoryInt;
import home.mutant.dl.opencl.model.Program;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;
import home.mutant.dl.utils.MnistDatabase.TYPE;

public class KMeansOpenCl2 {
	public static final int NO_CLUSTERS = 256;
	public static final int WORK_ITEMS = 2560;
	public static final int NO_ITERATIONS = 5;
	public static final int IMAGE_SIZE = 784;
	
	public static void main(String[] args) throws Exception {
		
		MnistDatabase.IMAGE_TYPE = TYPE.FLOAT;
		MnistDatabase.loadImages();
		
		float[] images= new float[IMAGE_SIZE*WORK_ITEMS];
		
		float[] clustersCenters = new float[IMAGE_SIZE*NO_CLUSTERS];
		for (int i = 0; i < clustersCenters.length; i++) {
			clustersCenters[i] = (float) (Math.random()*256);
		}
		int[] clustersUpdates = new int[WORK_ITEMS];
		
		Program program = new Program(Program.readResource("/opencl/Kmeans2.cl"));
		
		MemoryFloat memClusters = new MemoryFloat(program);
		memClusters.addReadWrite(clustersCenters);
		
		MemoryFloat memImages = new MemoryFloat(program);
		memImages.addReadOnly(images);
		
		MemoryInt memUpdates = new MemoryInt(program);
		memUpdates.addReadWrite(clustersUpdates);
		
		Kernel updateCenters = new Kernel(program, "updateCenters");
		updateCenters.setArgument(memClusters,0);
		updateCenters.setArgument(memImages,1);
		updateCenters.setArgument(memUpdates,2);
		
		Kernel reduceCenters = new Kernel(program, "reduceCenters");
		reduceCenters.setArgument(memClusters,0);
		reduceCenters.setArgument(memImages,1);
		reduceCenters.setArgument(memUpdates,2);
		
		long tTotal=0;

		for (int iteration=0;iteration<NO_ITERATIONS;iteration++){
			for (int i=0;i<WORK_ITEMS;i++){
				System.arraycopy(MnistDatabase.trainImages.get(i).getDataFloat(), 0, images, i*(IMAGE_SIZE), IMAGE_SIZE);
			}
			long t0 = System.currentTimeMillis();
			memImages.copyHtoD();
			updateCenters.run(WORK_ITEMS, 256);
			program.finish();
			reduceCenters.run(NO_CLUSTERS, 256);
			program.finish();
			tTotal+=System.currentTimeMillis()-t0;

			System.out.println("Iteration "+iteration);
		}
		memUpdates.copyDtoH();

		System.out.println("Time in kernel per iteration " + tTotal/1000./NO_ITERATIONS);
		memClusters.copyDtoH();
		List<Image> imagesClusters = new ArrayList<Image>();
		for (int i=0;i<NO_CLUSTERS;i++) {
			Image image = new ImageFloat(IMAGE_SIZE);
			System.arraycopy(memClusters.getSrc(), i*IMAGE_SIZE, image.getDataFloat(), 0, IMAGE_SIZE);
			imagesClusters.add(image);
		}
		ResultFrame frame = new ResultFrame(600, 600);
		frame.showImages(imagesClusters);
		memClusters.release();
		memImages.release();
		memUpdates.release();
		updateCenters.release();
		program.release();
	}
}
