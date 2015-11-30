#define IMAGE_SIZE  784
#define NO_CLUSTERS  256
#define NO_IMAGES 6000
__kernel void updateCenters(__global float *centers, __global float *images, __global int *updates)
{
	int gid = get_global_id(0);
	int imagesOffset = gid*IMAGE_SIZE;

	int centersIndex=0;
	int imageIndex=0;
	
	float sum=0;
	int index=0;
	float weight;
	float min;
	int minCenterIndex=0;
	
	min=IMAGE_SIZE*1000000;
	for(centersIndex=0;centersIndex<NO_CLUSTERS;centersIndex++)
	{
		sum = 0;
		for(index=0;index<IMAGE_SIZE;index++)
		{
			weight = centers[centersIndex*IMAGE_SIZE+index]-images[imagesOffset+index];
			sum = sum+weight*weight;
		}
		if (sum<min)
		{
			min = sum;
			minCenterIndex = centersIndex;
		}
	}
	updates[gid]=minCenterIndex;
}

__kernel void reduceCenters(__global float *centers, __global float *images, __global int *updates)
{
	int offsetCenter = get_global_id(0);
	float centerBuffer[IMAGE_SIZE];
	int centerIndex;
	int indexImage;
	int noMembers=0;
	for(centerIndex=0;centerIndex<IMAGE_SIZE+;centerIndex++)
	{
		centerBuffer[centerIndex]=0;
	}
	
	for(indexImage=0;indexImage<NO_IMAGES;indexImage++)
	{
		if(updates[indexImage]==offsetCenter)
		{
			noMembers=noMembers+1;
			for(centerIndex=0;centerIndex<IMAGE_SIZE+;centerIndex++)
			{
				centerBuffer[centerIndex]=centerBuffer[centerIndex]+images[indexImage*IMAGE_SIZE+centerIndex];
			}
		}
	}
	if (noMembers>0)
	{
		for(centerIndex=0;centerIndex<IMAGE_SIZE;centerIndex++)
		{
			centers[offsetCenter*IMAGE_SIZE+centerIndex]=centerBuffer[centerIndex]/noMembers;
		}
	}
}