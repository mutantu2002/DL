package home.mutant.dl.utils.kmeans.smooth;

import java.util.HashMap;
import java.util.Map;

import home.mutant.dl.opencl.model.Kernel;
import home.mutant.dl.opencl.model.MemoryDouble;
import home.mutant.dl.opencl.model.Program;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.kmeans.model.Clusterable;
import home.mutant.dl.utils.kmeans.model.ListClusterable;

public class LinkedClusterablesOpenCl {
	public double preDistances[];
	ListClusterable filters;
	double[] x;
	double[] y;
	double[] vx;
	double[] vy;
	double dt=0.0002;
	double K=1;
	double friction=0.3;
	
	ResultFrame frame;
	private MemoryDouble memX;
	private MemoryDouble memY;
	private MemoryDouble memVx;
	private MemoryDouble memVy;
	private MemoryDouble memPredistances;
	private Kernel stepV;
	private Kernel stepX;
	private Program program;
	
	public LinkedClusterablesOpenCl(ListClusterable clusterables) {
		super();
		this.filters = clusterables;
		fillPreDistances();
		x = new double[filters.clusterables.size()];
		y = new double[filters.clusterables.size()];
		vx = new double[filters.clusterables.size()];
		vy = new double[filters.clusterables.size()];
		frame = new ResultFrame(800, 800);
		randDistances();
		
		Map<String, Object> params = new HashMap<>();
		params.put("NO_PARTICLES", filters.clusterables.size());
		params.put("DT", dt);
		params.put("K", K);
		params.put("FRICTION", friction);
		program = new Program(Program.readResource("/opencl/ElasticSmoothie.c"),params);
		
		memX = new MemoryDouble(program);
		memX.addReadWrite(x);
		
		memY = new MemoryDouble(program);
		memY.addReadWrite(y);
		
		memVx = new MemoryDouble(program);
		memVx.addReadWrite(vx);
		
		memVy = new MemoryDouble(program);
		memVy.addReadWrite(vy);
		
		memPredistances = new MemoryDouble(program);
		memPredistances.addReadOnly(preDistances);
		
		stepV = new Kernel(program, "stepV");
		stepV.setArguments(memX,memY,memVx, memVy, memPredistances);
		
		stepX = new Kernel(program, "stepX");
		stepX.setArguments(memX,memY,memVx, memVy);
		
	}
	private  void fillPreDistances(){
		int size = filters.clusterables.size();
		preDistances = new double[size*size];
		for (int i=0;i<size;i++){
			for (int j=0;j<size;j++){
				preDistances[i*size+j]=filters.clusterables.get(i).d(filters.clusterables.get(j))/5;
			}
		}
	}
	private void randDistances(){
		for (int i=0;i<filters.clusterables.size();i++){
			x[i]=Math.random()*200-100;
			y[i]=Math.random()*200-100;
		}
	}
	public void show(){
		copyDtoH();
		frame.drawingPanel.empty();
		for (int i=0;i<filters.clusterables.size();i++){
			Clusterable clusterable = filters.clusterables.get(i);
			int x1=(int) (x[i]+400);
			int y1=(int) (y[i]+400);
			if(x1>=0 && x1<796 && y1>=0 && y1<796){
				frame.putImage(clusterable.getImage(), x1, y1);
			}
		}
		frame.repaint();
	}
	
	public void stepX(){
		stepX.run(filters.clusterables.size(), 256);
		program.finish();
	}
	
	public void stepV(){
		stepV.run(filters.clusterables.size(), 256);
		program.finish();
	}
	
	public void listDistances(){
		copyDtoH();
		memPredistances.copyDtoH();
		int size = filters.clusterables.size();
		for (int i=0;i<size;i++){
			for (int j=0;j<size;j++){
				System.out.println(Math.sqrt((x[i]-x[j])*(x[i]-x[j])+(y[i]-y[j])*(y[i]-y[j]))+" - "+preDistances[i*size+j]);
			}
		}
	}
	private void copyDtoH(){
		memX.copyDtoH();
		memY.copyDtoH();
		memVx.copyDtoH();
		memVy.copyDtoH();
	}
	public void release(){
		memX.release();
		memY.release();
		memVx.release();
		memVy.release();
		memPredistances.release();
		stepV.release();
		stepX.release();
		program.release();
	}
}
