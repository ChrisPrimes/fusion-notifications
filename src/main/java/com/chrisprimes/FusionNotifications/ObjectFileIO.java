package com.chrisprimes.FusionNotifications;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ObjectFileIO {
	public static <T> void writeObjectToFile(ArrayList<T> obj, String fileName) throws IOException {
		FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(obj);
        oos.close();
        fos.close();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> readObjectFromFile(String fileName) throws IOException, ClassNotFoundException {
		ArrayList<T> phones = new ArrayList<T>();
		FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);

        phones = (ArrayList<T>) ois.readObject();
        
        ois.close();
        fis.close();
        
        return phones;
	}
}