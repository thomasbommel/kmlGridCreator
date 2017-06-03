package main.java.kmlGridCreator.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import main.test.kmlGridCreator.TestUtil;

public class CSVCreatorUtils {

	public static void savePointCountToBoundingAreaCountMapToCSV(Map<Integer,Integer> map, File file) throws IOException{
        //String csvFile = TestUtil.TEST_FOLDER_DIRECTORY+"testcsv"+System.currentTimeMillis()+".csv";
        FileWriter writer = new FileWriter(file);

        writer.append("Anzahl an Punkten;Felder\n");
        
        map.entrySet().stream().forEach(e->{
			try {
				writer.append(e.getKey()+";"+e.getValue()+"\n");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

        writer.flush();
        writer.close();
	}
	
	
	
	
}
