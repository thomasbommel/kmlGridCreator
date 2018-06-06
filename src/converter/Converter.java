package converter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.GPSCoordinate;

import main.java.kmlGridCreator.exceptions.OverlappingPolyStylesException;
import main.java.kmlGridCreator.utils.StringUtils;
import main.java.kmlGridCreator.utils.TxtUtil;

public class Converter {

	public static final String TEST_FOLDER_DIRECTORY = Paths.get(".").toAbsolutePath().normalize().toString()
			+ "/testdata/";
	public static final String TEST_FILE = TEST_FOLDER_DIRECTORY + "TEST.txt";

	public static void main(String[] args) throws IOException, OverlappingPolyStylesException {
		int min = 47;
		int max = 53;
		
		if(args.length==2){
			min= Integer.parseInt(args[0]);
			max = Integer.parseInt(args[1]);
		}
		
		List<MyPointForConverter> points = getPointsFromTxt(new File(TEST_FILE), min, max);
		MyKmlFactoryForConverter kml = new MyKmlFactoryForConverter("test");

		kml.addPointsToKml(points);

		kml.saveKmlFile(new File("testoutput_deleteme.kml"));
	}

	public static final List<MyPointForConverter> getPointsFromTxt(File file, int min, int max) throws IOException {
		if (file == null) {
			return Collections.emptyList();
		}
		Path path = Paths.get(file.getAbsolutePath());
		List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
		// System.out.println(lines);
		lines = lines.stream().filter(l -> isValidLine(l)).map(line -> line.substring(0, line.length()))
				.collect(Collectors.toList());
		List<MyPointForConverter> allPoints = new ArrayList<>();
		double count = 0;
		double sumDist = 0;

		double lastSpeed = 0;

		for (int i = 0; i < lines.size(); i++) {
			
			String line = lines.get(i);
			
			if(line.split(" ").length<3){
				continue;
			}
			
			String newLine = "";
			int index = line.lastIndexOf(" ");
			for (int a = 0; a < line.length(); a++) {
				boolean found = false;

				if (a == index) {
					newLine += "  ";
				} else {
					newLine += line.charAt(a);
				}

			}
			line = newLine;

			try {
				// String line = lines.get(i);
				// System.out.println(line);

				String northDegree = line.split("  ")[1];

				String eastDegree = line.split("  ")[2];

				//String speed = line.split(" ")[1];
				//String delay = line.split(" ")[4];
				//System.out.println(eastDegree);
				// System.out.println(i+" "+northDegree + " xxx " + eastDegree +
				// " xxx " + speed + " xxx " + delay + " xxx");
			//	double d = Double.parseDouble(delay) / 1000;
			//	double sp = Double.parseDouble(speed);
				String name = "";
				name += "sp:" + Utils.numberToString(1, 6, 1);
				//name += ", del:" + Utils.numberToString(d, 7, 3);

				DegreeCoordinate north = new DegreeCoordinate(Double.parseDouble(northDegree));
				DegreeCoordinate east = new DegreeCoordinate(Double.parseDouble(eastDegree));

				if (allPoints.size() >= 1) {
					double dis = EarthCalc.getDistance(new MyPointForConverter(north, east, name),
							allPoints.get(allPoints.size() - 1));
					name += ", dis:" + Utils.numberToString(dis, 5, 1);

					
					
					if (dis>min && dis<max) {
						sumDist += dis;
						//System.out.println(sp);
						System.out.print(Utils.numberToString(i,6,0)+" ");
						System.out.println("  Abstand " + Utils.numberToString(dis, 10, 3));
						count++;
						allPoints.add(new MyPointForConverter(north, east, name));
						//System.out.println(Utils.numberToString(dis,10,5));
					} else {
						allPoints.add(new MyPointForConverter(north, east, "inv. " + name));
					//	System.out.println(" inv Abst " + Utils.numberToString(dis, 10, 2));
					}

				} else {
					allPoints.add(new MyPointForConverter(north, east, "START " + name));
				}
				//lastSpeed = sp;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		System.out.println("Durchschnittlicher Abstand " + Utils.numberToString(sumDist / count, 10, 3));
		return allPoints;
	}

	private static boolean isValidLine(String line) {
		if (line.contains("Fehler") || line.contains("Error")) {
			return false;
		} else {
			return true;
		}
	}

}
