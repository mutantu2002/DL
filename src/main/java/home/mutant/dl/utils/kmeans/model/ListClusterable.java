package home.mutant.dl.utils.kmeans.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import home.mutant.dl.models.Image;
import home.mutant.dl.ui.ResultFrame;
import home.mutant.dl.utils.Utils;

public class ListClusterable implements Serializable {
	private static final long serialVersionUID = -3585637517611698740L;
	public List<Clusterable> clusterables = new ArrayList<Clusterable>();
	public ListClusterable(){
	}
	public ListClusterable(Image[] images){
		for (int i = 0; i < images.length; i++) {
			clusterables.add(new SimpleClusterable(images[i].getDataFloat()));
		}
	}
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
		Object obj = Utils.load(fileName);
		if (obj instanceof ListClusterable){
			return (ListClusterable)obj;
		}else if (obj instanceof Image[]){
			return new ListClusterable((Image[])obj);
		}
		return null;
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
