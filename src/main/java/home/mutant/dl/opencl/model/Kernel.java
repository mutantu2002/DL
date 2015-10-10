package home.mutant.dl.opencl.model;

import static org.jocl.CL.*;

import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_kernel;

public class Kernel {
	Program program;
	cl_kernel clKernel;
	
	public Kernel(Program program, String functionName) {
		super();
		this.program = program;
		clKernel = clCreateKernel(program.getProgram(), functionName, null);
	}
	
	public void setArgument(Memory memory, int index){
		clSetKernelArg(clKernel, index, Sizeof.cl_mem, Pointer.to(memory.gemMemObject()));
	}
	
	public void setArgument(int value, int index){
		clSetKernelArg(clKernel, index, Sizeof.cl_int, Pointer.to(new int[]{ value }));
	}	
	public void setArguments(Memory ... memories){
		for (int i = 0; i < memories.length; i++) {
			clSetKernelArg(clKernel, i, Sizeof.cl_mem, Pointer.to(memories[i].gemMemObject()));
		}
	}
	
	public int run(long globalworkSize, long localWorksize)
	{
        return clEnqueueNDRangeKernel(program.getCommandQueue(), clKernel, 1, null, new long[]{globalworkSize}, new long[]{localWorksize}, 0, null, null);
	}
	public void release()
	{
		clReleaseKernel(clKernel);
	}
}
