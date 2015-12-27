__kernel void stepV(__global double *x, __global double *y,__global double *vx, __global double *vy, __global double *preDistances)
{
	int i = get_global_id(0);
	double fx=0;
	double fy=0;
	int j;
	double dx;
	double dy;
	for (j=0;j<NO_PARTICLES;j++){
		if (i==j)continue;
		dx=x[i]-x[j];
		dy=y[i]-y[j];
		double d = sqrt(dx*dx+dy*dy);
		fx+=dx/d*(preDistances[i*NO_PARTICLES+j]-d)*K;
		fy+=dy/d*(preDistances[i*NO_PARTICLES+j]-d)*K;
	}
	fx-=vx[i]*FRICTION;
	fy-=vy[i]*FRICTION;
	vx[i]+=fx*DT;
	vy[i]+=fy*DT;
}

__kernel void stepX(__global double *x, __global double *y,__global double *vx, __global double *vy)
{
	int i = get_global_id(0);
	x[i]+=vx[i]*DT;
	y[i]+=vy[i]*DT;
}
