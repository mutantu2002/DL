#pragma OPENCL EXTENSION cl_khr_fp64 : enable
#define DIM_FILTER  4
#define FILTER_SIZE  16
#define NO_CLUSTERS  256
#define DIM_IMAGE  28
#define WORK_ITEMS 20000

__kernel void updateCenters(__global double *centers, __global double *images, __global double *updates)
{
	int imagesOffset = get_global_id(0)*IMAGE_SIZE;
	int updatesOffset = get_global_id(0)*(FILTER_SIZE+1)*NO_CLUSTERS;

	int centersIndex=0;
	
	double sum=0;
	int index=0;
	double weight;
	double min;
	int minCenterIndex;
	
	int imageX;
	int imageY;
	int filterX;
	int filterY;
	
	double imageBuffer[FILTER_SIZE];
	
	for(imageX=0;imageX<=DIM_IMAGE-DIM_FILTER;imageX++)
	{
		for(imageY=0;imageY<=DIM_IMAGE-DIM_FILTER;imageY++)
		{
			index=0;
			for(filterX=0;filterX<DIM_FILTER;filterX++)
			{
				for(filterY=0;filterY<DIM_FILTER;filterY++)
				{
					imageBuffer[index++] = images[imagesOffset+(imageY+filterY)*DIM_IMAGE+(imageX+filterX);
				}
			}
			min=IMAGE_SIZE*1000000;
			minCenterIndex=0;
			for(centersIndex=0;centersIndex<NO_CLUSTERS;centersIndex++)
			{
				sum = 0;
				for(index=0;index<IMAGE_SIZE;index++)
				{
					imageBuffer[index]=images[imagesOffset+imageIndex*IMAGE_SIZE+index];
					weight = centers[centersIndex*IMAGE_SIZE+index]-imageBuffer[index];
					sum = sum+weight*weight;
				}
				if (sum<min)
				{
					min = sum;
					minCenterIndex = centersIndex;
				}
			}
			for(index=0;index<IMAGE_SIZE;index++)
			{
				updates[updatesOffset+(IMAGE_SIZE+1)*minCenterIndex+index] = updates[updatesOffset+(IMAGE_SIZE+1)*minCenterIndex+index]+imageBuffer[index];
			}
			updates[updatesOffset+(IMAGE_SIZE+1)*minCenterIndex+IMAGE_SIZE] = updates[updatesOffset+(IMAGE_SIZE+1)*minCenterIndex+IMAGE_SIZE]+1;
		}
	}
}

__kernel void reduceCenters(__global double *updates)
{
	int offsetCenter = get_global_id(0);
	int indexWorkItem=0;
	double centerBuffer[IMAGE_SIZE+1];
	int centerIndex;
	for(centerIndex=0;centerIndex<IMAGE_SIZE+1;centerIndex++)
	{
		centerBuffer[centerIndex]=0;
	}
	for(indexWorkItem=0;indexWorkItem<WORK_ITEMS;indexWorkItem++)
	{
		for(centerIndex=0;centerIndex<IMAGE_SIZE+1;centerIndex++)
		{
			centerBuffer[centerIndex]=centerBuffer[centerIndex]+updates[(indexWorkItem*NO_CLUSTERS+offsetCenter)*(IMAGE_SIZE+1)+centerIndex];
		}
	}
	if (centerBuffer[IMAGE_SIZE]>0)
	{
		for(centerIndex=0;centerIndex<IMAGE_SIZE;centerIndex++)
		{
			updates[offsetCenter*(IMAGE_SIZE+1)+centerIndex]=centerBuffer[centerIndex]/centerBuffer[IMAGE_SIZE];
		}
	}
}

__kernel void mixCenters(__global double *centers,  __global double *updates)
{
	int offsetCenter = get_global_id(0);
	int centerIndex;

	if (offsetCenter>0 && offsetCenter<NO_CLUSTERS-1)
	{
		for(centerIndex=0;centerIndex<IMAGE_SIZE;centerIndex++)
		{
			centers[offsetCenter*IMAGE_SIZE+centerIndex]=(updates[offsetCenter*(IMAGE_SIZE+1)+centerIndex]+updates[(offsetCenter+1)*(IMAGE_SIZE+1)+centerIndex]+updates[(offsetCenter-1)*(IMAGE_SIZE+1)+centerIndex])/3;
		}
	}
}
