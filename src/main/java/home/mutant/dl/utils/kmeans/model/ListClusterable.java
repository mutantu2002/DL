package home.mutant.dl.utils.kmeans.model;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListClusterable implements Serializable {
	private static final long serialVersionUID = -3585637517611698740L;
	public List<Clusterable> clusterables = new ArrayList<Clusterable>();
	
	public void save(String fileName){
		try{
			FileOutputStream fileOut =
					new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static ListClusterable load(String fileName){
		try
		{
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
	        ListClusterable list = (ListClusterable) in.readObject();
	        in.close();
	        fileIn.close();
	        return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public void show(){
		List<Image> imgs = new ArrayList<Image>();
		for (int i = 0; i < clusterables.size(); i++) {
			Clusterable c =  clusterables.get(i);
			imgs.add(c.getImage());
		}
		ResultFrame frame = new ResultFrame(600, 600);
		frame.showImages(imgs);
	}
	
	public int getClosestClusterIndex( Clusterable clusterable){
		double minDistance = Double.MAX_VALUE;
		int minCluster=-1;
		for (int j = 0; j<clusterables.size(); j++){
			Clusterable centre = clusterables.get(j);
			double distance = centre.d(clusterable);
			if (distance<minDistance){
				minDistance = distance;
				minCluster = j;
			}
		}
		return minCluster;
	}
	
}
