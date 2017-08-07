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
	public static final String TEST_FILE = TEST_FOLDER_DIRECTORY + "drop_good.txt";

	public static void main(String[] args) throws IOException, OverlappingPolyStylesException {

		List<MyPointForConverter> points = getPointsFromTxt(new File(TEST_FILE));
		MyKmlFactoryForConverter kml = new MyKmlFactoryForConverter("test");

		kml.addPointsToKml(points);

		kml.saveKmlFile(new File("testoutput_deleteme.kml"));
	}

	public static final List<MyPointForConverter> getPointsFromTxt(File file) throws IOException {
		if (file == null) {
			return Collections.emptyList();
		}
		Path path = Paths.get(file.getAbsolutePath());
		List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
		// System.out.println(lines);
		lines = lines.stream().filter(l -> isValidLine(l)).map(line -> line.substring(0, line.length()))
				.collect(Collectors.toList());
		List<MyPointForConverter> allPoints = new ArrayList<>();

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);

			String northDegree = line.substring(9, 35);

			String eastDegree = line.substring(36, 60);

			String speed = line.substring(70, 90);
			String delay = line.substring(90, line.length() - 7);

			System.out.println(northDegree + " xxx " + eastDegree + " xxx " + speed + " xxx " + delay + " xxx");
			double d = Double.parseDouble(delay) / 1000;
			double sp = Double.parseDouble(speed);
			String name = Utils.numberToString(sp, 8, 2) + " " + Utils.numberToString(d, 8, 3);

			DegreeCoordinate north = new DegreeCoordinate(Double.parseDouble(northDegree));
			DegreeCoordinate east = new DegreeCoordinate(Double.parseDouble(eastDegree));
			
			if (allPoints.size()>1) {
				double dis = EarthCalc.getDistance(new MyPointForConverter(north, east, name), allPoints.get(allPoints.size()-1));
				name += " "+Utils.numberToString(dis,7,3);
			}

			allPoints.add(new MyPointForConverter(north, east, name));

		}

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
