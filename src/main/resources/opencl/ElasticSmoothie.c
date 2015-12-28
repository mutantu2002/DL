__kernel void stepV(__global double *x, __global double *y,__global double *vx, __global double *vy, __global double *preDistances)
{
	__local double lx[NO_PARTICLES];
	__local double ly[NO_PARTICLES];

	int i = get_global_id(0);
	double fx=0;
	double fy=0;
	int j;
	double dx;
	double dy;
	int f;
	lx[i]=x[i];
	ly[i]=y[i];

	barrier(CLK_LOCAL_MEM_FENCE);

	for (f=0;f<1000;f++)
	{
	for (j=0;j<NO_PARTICLES;j++){
		if (i==j)continue;
		dx=lx[i]-lx[j];
		dy=ly[i]-ly[j];
		double d = sqrt(dx*dx+dy*dy);
		fx+=dx/d*(preDistances[i*NO_PARTICLES+j]-d)*K;
		fy+=dy/d*(preDistances[i*NO_PARTICLES+j]-d)*K;
	}
	fx-=vx[i]*FRICTION;
	fy-=vy[i]*FRICTION;
	vx[i]+=fx*DT;
	vy[i]+=fy*DT;
	}
}

__kernel void stepX(__global double *x, __global double *y,__global double *vx, __global double *vy)
{
	int i = get_global_id(0);
	x[i]+=vx[i]*DT;
	y[i]+=vy[i]*DT;
}
