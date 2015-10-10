#pragma OPENCL EXTENSION cl_khr_fp64 : enable

#define DIM_FILTER  7
#define WORK_ITEMS 1280
#define NO_CLUSTERS  128

#define DIM_IMAGE  28
#define IMAGE_SIZE  (DIM_IMAGE*DIM_IMAGE)
#define FILTER_SIZE  (DIM_FILTER*DIM_FILTER)


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
	
	double subImageBuffer[FILTER_SIZE];

	for(imageX=0;imageX<=DIM_IMAGE-DIM_FILTER;imageX++)
	{
		for(imageY=0;imageY<=DIM_IMAGE-DIM_FILTER;imageY++)
		{
			index=0;
			for(filterX=0;filterX<DIM_FILTER;filterX++)
			{
				for(filterY=0;filterY<DIM_FILTER;filterY++)
				{
					subImageBuffer[index++] = images[imagesOffset+(imageY+filterY)+(imageX+filterX)*DIM_IMAGE];
				}
			}
			min=FILTER_SIZE*1000000;
			minCenterIndex=0;
			for(centersIndex=0;centersIndex<NO_CLUSTERS;centersIndex++)
			{
				sum = 0;
				for(index=0;index<FILTER_SIZE;index++)
				{
					weight = centers[centersIndex*FILTER_SIZE+index]-subImageBuffer[index];
					sum = sum+weight*weight;
				}
				if (sum<min)
				{
					min = sum;
					minCenterIndex = centersIndex;
				}
			}
			for(index=0;index<FILTER_SIZE;index++)
			{
				updates[updatesOffset+(FILTER_SIZE+1)*minCenterIndex+index] = updates[updatesOffset+(FILTER_SIZE+1)*minCenterIndex+index]+subImageBuffer[index];
			}
			updates[updatesOffset+(FILTER_SIZE+1)*minCenterIndex+FILTER_SIZE] = updates[updatesOffset+(FILTER_SIZE+1)*minCenterIndex+FILTER_SIZE]+1;
		}
	}
}

__kernel void reduceCenters(__global double *updates)
{
	int offsetCenter = get_global_id(0);
	int indexWorkItem=0;
	double centerBuffer[FILTER_SIZE+1];
	int centerIndex;
	for(centerIndex=0;centerIndex<FILTER_SIZE+1;centerIndex++)
	{
		centerBuffer[centerIndex]=0;
	}
	for(indexWorkItem=0;indexWorkItem<WORK_ITEMS;indexWorkItem++)
	{
		for(centerIndex=0;centerIndex<FILTER_SIZE+1;centerIndex++)
		{
			centerBuffer[centerIndex]=centerBuffer[centerIndex]+updates[(indexWorkItem*NO_CLUSTERS+offsetCenter)*(FILTER_SIZE+1)+centerIndex];
		}
	}
	if (centerBuffer[FILTER_SIZE]>0)
	{
		for(centerIndex=0;centerIndex<FILTER_SIZE;centerIndex++)
		{
			updates[offsetCenter*(FILTER_SIZE+1)+centerIndex]=centerBuffer[centerIndex]/centerBuffer[FILTER_SIZE];
		}
	}
}

__kernel void mixCenters(__global double *centers,  __global double *updates)
{
	int offsetCenter = get_global_id(0);
	int centerIndex;
	double noMean=1;
	double influence=0;

	for(centerIndex=0;centerIndex<FILTER_SIZE;centerIndex++)
	{
		noMean=1;
		influence=0;
		if (offsetCenter>0)
		{
			influence = influence + updates[(offsetCenter-1)*(FILTER_SIZE+1)+centerIndex];
			noMean=noMean+1;
		}
		if (offsetCenter<NO_CLUSTERS-1)
		{
			influence = influence + updates[(offsetCenter+1)*(FILTER_SIZE+1)+centerIndex];
			noMean=noMean+1;
		}
		centers[offsetCenter*FILTER_SIZE+centerIndex]=(updates[offsetCenter*(FILTER_SIZE+1)+centerIndex]+influence)/noMean;
		//centers[offsetCenter*FILTER_SIZE+centerIndex]=updates[offsetCenter*(FILTER_SIZE+1)+centerIndex];
	}
}

__kernel void mixCenters2D(__global double *centers,  __global double *updates)
{
	int offsetCenter = get_global_id(0);
	int centerIndex;
	double noMean=1;
	double influence=0;
	int offsetCenterX=offsetCenter%16;
	int offsetCenterY=offsetCenter/16;
	for(centerIndex=0;centerIndex<FILTER_SIZE;centerIndex++)
	{
		noMean=1;
		influence=0;
		if (offsetCenterX>0)
		{
			influence = influence + updates[(offsetCenterY*16+offsetCenterX-1)*(FILTER_SIZE+1)+centerIndex];
			noMean=noMean+1;
		}
		if (offsetCenterX<16-1)
		{
			influence = influence + updates[(offsetCenterY*16+offsetCenterX+1)*(FILTER_SIZE+1)+centerIndex];
			noMean=noMean+1;
		}
		if (offsetCenterY>0)
		{
			influence = influence + updates[((offsetCenterY-1)*16+offsetCenterX)*(FILTER_SIZE+1)+centerIndex];
			noMean=noMean+1;
		}
		if (offsetCenterY<16-1)
		{
			influence = influence + updates[((offsetCenterY+1)*16+offsetCenterX)*(FILTER_SIZE+1)+centerIndex];
			noMean=noMean+1;
		}
		centers[offsetCenter*FILTER_SIZE+centerIndex]=(updates[offsetCenter*(FILTER_SIZE+1)+centerIndex]+influence)/noMean;
	}
}
