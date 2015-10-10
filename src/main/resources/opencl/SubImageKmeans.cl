#define DIM_IMAGE  28
#define IMAGE_SIZE  784

__kernel void updateCenters(__global float *centers, __global float *images, __global float *updates, const int dimFilter,const int noClusters)
{
	int imagesOffset = get_global_id(0)*IMAGE_SIZE;
	int filterSize=dimFilter*dimFilter;
	
	int updatesOffset = get_global_id(0)*(filterSize+1)*noClusters;

	int centersIndex=0;
	
	float sum=0;
	int index=0;
	float weight;
	float min;
	int minCenterIndex;
	
	int imageX;
	int imageY;
	int filterX;
	int filterY;
	
	float subImageBuffer[IMAGE_SIZE];

	for(imageX=0;imageX<=DIM_IMAGE-dimFilter;imageX++)
	{
		for(imageY=0;imageY<=DIM_IMAGE-dimFilter;imageY++)
		{
			index=0;
			for(filterX=0;filterX<dimFilter;filterX++)
			{
				for(filterY=0;filterY<dimFilter;filterY++)
				{
					subImageBuffer[index++] = images[imagesOffset+(imageY+filterY)+(imageX+filterX)*DIM_IMAGE];
				}
			}
			min=filterSize*1000000;
			minCenterIndex=0;
			for(centersIndex=0;centersIndex<noClusters;centersIndex++)
			{
				sum = 0;
				for(index=0;index<filterSize;index++)
				{
					weight = centers[centersIndex*filterSize+index]-subImageBuffer[index];
					sum = sum+weight*weight;
				}
				if (sum<min)
				{
					min = sum;
					minCenterIndex = centersIndex;
				}
			}
			for(index=0;index<filterSize;index++)
			{
				updates[updatesOffset+(filterSize+1)*minCenterIndex+index] = updates[updatesOffset+(filterSize+1)*minCenterIndex+index]+subImageBuffer[index];
			}
			updates[updatesOffset+(filterSize+1)*minCenterIndex+filterSize] = updates[updatesOffset+(filterSize+1)*minCenterIndex+filterSize]+1;
		}
	}
}

__kernel void reduceCenters(__global float *updates, const int dimFilter,const int noClusters, const int workItems)
{
	int offsetCenter = get_global_id(0);
	int indexWorkItem=0;
	int filterSize=dimFilter*dimFilter;
	
	float centerBuffer[IMAGE_SIZE+1];
	int centerIndex;
	for(centerIndex=0;centerIndex<filterSize+1;centerIndex++)
	{
		centerBuffer[centerIndex]=0;
	}
	for(indexWorkItem=0;indexWorkItem<workItems;indexWorkItem++)
	{
		for(centerIndex=0;centerIndex<filterSize+1;centerIndex++)
		{
			centerBuffer[centerIndex]=centerBuffer[centerIndex]+updates[(indexWorkItem*noClusters+offsetCenter)*(filterSize+1)+centerIndex];
		}
	}
	if (centerBuffer[filterSize]>0)
	{
		for(centerIndex=0;centerIndex<filterSize;centerIndex++)
		{
			updates[offsetCenter*(filterSize+1)+centerIndex]=centerBuffer[centerIndex]/centerBuffer[filterSize];
		}
	}
}

__kernel void mixCenters(__global float *centers,  __global float *updates, const int dimFilter, const int noClusters)
{
	int offsetCenter = get_global_id(0);
	int filterSize=dimFilter*dimFilter;
	int centerIndex;
	float noMean=1;
	float influence=0;

	for(centerIndex=0;centerIndex<filterSize;centerIndex++)
	{
		noMean=1;
		influence=0;
		if (offsetCenter>0)
		{
			influence = influence + updates[(offsetCenter-1)*(filterSize+1)+centerIndex];
			noMean=noMean+1;
		}
		if (offsetCenter<noClusters-1)
		{
			influence = influence + updates[(offsetCenter+1)*(filterSize+1)+centerIndex];
			noMean=noMean+1;
		}
		centers[offsetCenter*filterSize+centerIndex]=(updates[offsetCenter*(filterSize+1)+centerIndex]+influence)/noMean;
		//centers[offsetCenter*filterSize+centerIndex]=updates[offsetCenter*(filterSize+1)+centerIndex];
	}
}

__kernel void mixCenters2D(__global float *centers,  __global float *updates, const int dimFilter, const int dimNoClusters)
{
	int offsetCenter = get_global_id(0);
	int filterSize=dimFilter*dimFilter;
	int centerIndex;
	float noMean=1;
	float influence=0;
	
	int offsetCenterX=offsetCenter%dimNoClusters;
	int offsetCenterY=offsetCenter/dimNoClusters;
	
	for(centerIndex=0;centerIndex<filterSize;centerIndex++)
	{
		noMean=1;
		influence=0;
		if (offsetCenterX>0)
		{
			influence = influence + updates[(offsetCenterY*dimNoClusters+offsetCenterX-1)*(filterSize+1)+centerIndex];
			noMean=noMean+1;
		}
		if (offsetCenterX<dimNoClusters-1)
		{
			influence = influence + updates[(offsetCenterY*dimNoClusters+offsetCenterX+1)*(filterSize+1)+centerIndex];
			noMean=noMean+1;
		}
		if (offsetCenterY>0)
		{
			influence = influence + updates[((offsetCenterY-1)*dimNoClusters+offsetCenterX)*(filterSize+1)+centerIndex];
			noMean=noMean+1;
		}
		if (offsetCenterY<dimNoClusters-1)
		{
			influence = influence + updates[((offsetCenterY+1)*dimNoClusters+offsetCenterX)*(filterSize+1)+centerIndex];
			noMean=noMean+1;
		}
		centers[offsetCenter*filterSize+centerIndex]=(updates[offsetCenter*(filterSize+1)+centerIndex]+influence)/noMean;
	}
}
