package home.mutant.dl.opencl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.opencl.model.Kernel;
import home.mutant.dl.opencl.model.Memory;
import home.mutant.dl.opencl.model.Program;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.MnistDatabase;

public class SubImageKMeansOpenCl {
	public static final int DIM_FILTER = 4;
	public static final int NO_CLUSTERS = 256;
	public static final int WORK_ITEMS = 10000;
	public static final int DIM_IMAGE = 28;
	public static final int NO_ITERATIONS = 100;
	
	public static void main(String[] args) throws Exception {
		MnistDatabase.loadImages();
		double[] inputImages= new double[(DIM_IMAGE*DIM_IMAGE)*WORK_ITEMS];
		
		double[] clustersCenters = new double[DIM_FILTER*DIM_FILTER*NO_CLUSTERS];
		for (int i = 0; i < clustersCenters.length; i++) {
			clustersCenters[i] = Math.random()*256;
		}
		double[] clustersUpdates = new double[(DIM_FILTER*DIM_FILTER+1)*NO_CLUSTERS*WORK_ITEMS];
		
		Program program = new Program(Program.readResource("/opencl/SubImageKmeans.cl"));
		
		Memory memClusters = new Memory(program);
		memClusters.addReadWrite(clustersCenters);
		
		Memory memImages = new Memory(program);
		memImages.addReadOnly(inputImages);
		
		Memory memUpdates = new Memory(program);
		memUpdates.addReadWrite(clustersUpdates);
		
		Kernel updateCenters = new Kernel(program, "updateCenters");
		updateCenters.setArguments(memClusters,memImages,memUpdates);
		
		Kernel reduceCenters = new Kernel(program, "reduceCenters");
		reduceCenters.setArguments(memUpdates);
		
		Kernel mixCenters = new Kernel(program, "mixCenters");
		mixCenters.setArguments(memClusters, memUpdates);
		
		long tTotal=0;

		
		for (int iteration=0;iteration<NO_ITERATIONS;iteration++){
			Arrays.fill(clustersUpdates, 0);
			memUpdates.copyHtoD();
			for (int batch=0 ;batch<60000/WORK_ITEMS;batch++){
				for (int i=0;i<WORK_ITEMS;i++){
					System.arraycopy(MnistDatabase.trainImages.get(batch*WORK_ITEMS+i).data, 0, inputImages, i*(DIM_IMAGE*DIM_IMAGE), DIM_IMAGE*DIM_IMAGE);
				}
				long t0 = System.currentTimeMillis();
				memImages.copyHtoD();
				updateCenters.run(WORK_ITEMS, 256);
				program.finish();
				tTotal+=System.currentTimeMillis()-t0;
			}
			reduceCenters.run(NO_CLUSTERS, 256);
			program.finish();
//			mixCenters.run(NO_CLUSTERS, 256);
//			program.finish();
			System.out.println("Iteration "+iteration);
		}
		memUpdates.copyDtoH();
		double sum = 0;
		int noZero=0;
		for (int i =16;i<(DIM_FILTER*DIM_FILTER+1)*NO_CLUSTERS*WORK_ITEMS;i+=17){
			if (memUpdates.getSrc()[i]==0)noZero++;
			sum+=memUpdates.getSrc()[i];
		}
		System.out.println("Threaded Clusters not assigned "+noZero);
		System.out.println("Total updates "+sum);
		System.out.println("Time in kernel per iteration " + tTotal/1000./NO_ITERATIONS);
		memClusters.copyDtoH();
		List<Image> imgClusters = new ArrayList<Image>();
		for (int i=0;i<NO_CLUSTERS;i++) {
			Image image = new Image(DIM_FILTER*DIM_FILTER);
			System.arraycopy(memClusters.getSrc(), i*DIM_FILTER*DIM_FILTER, image.data, 0, DIM_FILTER*DIM_FILTER);
			imgClusters.add(image);
		}
		
		ResultFrame frame = new ResultFrame(600, 600);
		frame.showImages(imgClusters);
		
		memClusters.release();
		memImages.release();
		memUpdates.release();
		updateCenters.release();
		reduceCenters.release();
		mixCenters.release();
		program.release();
	}
}
