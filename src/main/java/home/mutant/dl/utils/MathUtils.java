package home.mutant.dl.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MathUtils
{
	public static double standardDev(int[] values)
	{
		double mean = mean(values);
		double sum=0;
		for (int val:values)
		{
			sum+=(mean-val)*(mean-val);
		}
		return Math.sqrt(sum/values.length);
	}
	public static double mean(int[] values)
	{
		double sum=0;
		for (int val:values)
		{
			sum+=val;
		}
		return sum/values.length;
	}
	
	public static long max(long[] array)
	{
		Long max = Long.MIN_VALUE;
		for (int out=0;out<array.length;out++)
		{
			if (array[out]>max)
			{
				max = array[out];
			}
		}
		return max;
	}
	
	public static String printArray(Double[] array)
	{
		String out = "Values:";
		for (int i=0;i<array.length;i++)
		{
			out+=array[i] + " ";
		}
		return out;
	}
	
	public static int indexMax(double[] array)
	{
		double max = Double.NEGATIVE_INFINITY;
		int indexMax=-1;
		for (int out=0;out<array.length;out++)
		{
			if (array[out]>max)
			{
				max = array[out];
				indexMax = out;
			}
		}
		return indexMax;
	}
	
	public static int indexMax(long[] array)
	{
		long max = Long.MIN_VALUE;
		int indexMax=-1;
		for (int out=0;out<array.length;out++)
		{
			if (array[out]>max)
			{
				max = array[out];
				indexMax = out;
			}
		}
		return indexMax;
	}
	
	public static int indexMax(List<Integer> array)
	{
		long max = Long.MIN_VALUE;
		int indexMax=-1;
		for (int out=0;out<array.size();out++)
		{
			if (array.get(out)>max)
			{
				max = array.get(out);
				indexMax = out;
			}
		}
		return indexMax;
	}
	
	public static double max(double[] array)
	{
		double max = Double.NEGATIVE_INFINITY;
		for (int out=0;out<array.length;out++)
		{
			if (array[out]>max)
			{
				max = array[out];
			}
		}
		return max;
	}
	
	public static int sigmoidBinary(double totalInput)
	{
		return (Math.random()<= sigmoidFunction(totalInput))?1:0;
	}
	
	public static double sigmoidFunction(double totalInput)
	{
		return  1./(1+Math.exp(-totalInput));
	}
	
	public static double sigmoidFunctionGamma(double totalInput, double gamma)
	{
		return  1./(1+Math.exp(-totalInput*gamma));
	}
	
	public static double sigmoidLikeInverse(double totalInput, double gamma)
	{
		return  (totalInput*gamma)/(1+Math.abs(totalInput*gamma)) +1;
	}
	
	public static Double sigmoidBinary(double totalInput, double threshhold)
	{
		return (Math.random()<= sigmoidFunction(-totalInput+threshhold))?1.:0;
	}	
	public static Double asymptoticToZero(double x)
	{
		return 10000*Math.exp(-1*x/1000.);
	}
	public static void sigmoidBinary(double[] input)
	{
		for (int i=0; i<input.length; i++)
		{
			input[i] = MathUtils.sigmoidBinary(input[i]);
		}
	}
	public static void sigmoidBinary(double[] input, double threshhold)
	{
		for (int i=0; i<input.length; i++)
		{
			input[i] = MathUtils.sigmoidBinary(input[i],threshhold);
		}
	}
	
	public static int coincidence(byte[] test, double[] sample)
	{
		int coincidence=0;
		for (int i = 0; i < sample.length; i++)
		{
			if (test[i]==sample[i])
			{
				coincidence+=1;
			}
			else
			{
				coincidence-=1;
			}
		}
		return coincidence;
	}
	public static int coincidence(byte[] test, byte[] sample)
	{
		int coincidence=0;
		for (int i = 0; i < sample.length; i++)
		{
			if (test[i]==sample[i])
			{
				coincidence+=1;
			}
			else
			{
				coincidence-=1;
			}
		}
		return coincidence;
	}
	public static List<Integer> indexMaxMultiple(int numberMax, double[] output) 
	{
		List<Integer> resIndex = new ArrayList<Integer>();
		List<Double> resValues = new ArrayList<Double>();
		for (int i = 0; i < output.length; i++)
		{
			int index = insertSortedValue(output[i], resValues, numberMax);
			if (index<numberMax)
			{
				resIndex.add(index, i);
			}
		}
		
		return resIndex.subList(0, numberMax);
	}
	public static int insertSortedValue(double value,List<Double> arr, int maxCapacity)
	{
		int indexToInsert = 0;
		for (Double integer : arr) 
		{
			if (value>integer)
			{
				break;
			}
			indexToInsert++;

		}
		arr.add(indexToInsert, value);
		if (arr.size()>maxCapacity)
		{
			arr.remove(maxCapacity);
		}
		return indexToInsert;
	}
	
	public static int getKeyForMaxValue(Map<Integer, Integer> map)
	{
		int max=Integer.MIN_VALUE;
		int label=-1;
		for (Integer key : map.keySet()) 
		{
			if (max<map.get(key))
			{
				max = map.get(key);
				label = key;
			}
		}
		return label;
	}

	public static double abruptDistrib()
	{
		return abruptDistrib(0.075);
	}
	
	public static double abruptDistrib(double exp)
	{
		return Math.pow(Math.random(), exp);
	}
	
	public static double sum(double[] array)
	{
		double sum=0;
		for (int i = 0; i < array.length; i++)
		{
			sum+=array[i];
		}
		return sum;
	}
	public static double sumSquared(double[] array)
	{
		double sum=0;
		for (int i = 0; i < array.length; i++)
		{
			sum+=array[i]*array[i];
		}
		return sum;
	}
	
	public static double gaussian(double x, double mean)
	{
		return Math.exp(-1*(x*x)/2/mean/mean);
	}
}
