package main.java.kmlGridCreator.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVCreatorUtils {

	public static void savePointCountToBoundingAreaCountMapToCSV(Map<Integer,Integer> map, File file) throws IOException{
		//String csvFile = TestUtil.TEST_FOLDER_DIRECTORY+"testcsv"+System.currentTimeMillis()+".csv";
		List<String> lines = new ArrayList<>();
		lines.add("Anzahl an Punkten;Felder");
		lines.addAll(map.entrySet().stream().map(e->getCSVLinefromPointCountToBoundingAreasCountMapEntry(e)).collect(Collectors.toList()));
		Files.write(Paths.get(file.getAbsolutePath()), lines, Charset.forName("UTF-8"));
	}
	
	private static String getCSVLinefromPointCountToBoundingAreasCountMapEntry(Map.Entry<Integer, Integer> entry){
		return entry.getKey()+";"+entry.getValue();
	}
}
