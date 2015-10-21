package home.mutant.dl.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utils {
	public static void save(String fileName, Object obj){
		try{
			FileOutputStream fileOut =
					new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(obj);
			out.close();
			fileOut.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static Object load(String fileName){
		try
		{
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
	        Object list = in.readObject();
	        in.close();
	        fileIn.close();
	        return list;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	

}
