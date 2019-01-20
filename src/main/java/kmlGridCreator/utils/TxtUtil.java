package main.java.kmlGridCreator.utils;

import java.awt.Dimension;
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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.GPSCoordinate;
import com.peertopark.java.geocalc.Point;

import converter.MyPointForConverter;
import converter.MyPointForConverterWithDateAndDistance;
import main.java.kmlGridCreator.model.MyPoint;

public class TxtUtil {
	
	public static List<String> errorLines = new ArrayList<>();

	public static final List<MyPointForConverterWithDateAndDistance> getPointsFromTxt(File file) throws IOException {
		if (file == null) {
			return Collections.emptyList();
		}
		Path path = Paths.get(file.getAbsolutePath());
		List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
		// System.out.println(lines);
		lines = lines.stream().filter(l -> isValidLine(l))
				.collect(Collectors.toList());
		List<MyPointForConverterWithDateAndDistance> allPoints = new ArrayList<>();

		double sum = 0;
		double count = 0;

		MyPoint old = null;

		String northDegree = null;
		String northMinutes= null;
		String eastDegree= null;
		String eastMinutes= null;
		for (int i = 0; i < lines.size(); i++) {
			String completeLine =  lines.get(i);
			String line = completeLine.substring(18, completeLine.length());
			try {
				northDegree = line.substring(0, 3);
				northMinutes = line.substring(3, 5) + "." + line.substring(5, 8);
//				System.out.println(northDegree + " " + northMinutes);
				eastDegree = line.substring(10, 13);
				eastMinutes = line.substring(13, 15) + "." + line.substring(15, 18);
//				System.out.println(eastDegree + " " + eastMinutes + "\n");
				GPSCoordinate north = new GPSCoordinate(Double.parseDouble(northDegree),
						Double.parseDouble(northMinutes));
				GPSCoordinate east = new GPSCoordinate(Double.parseDouble(eastDegree), Double.parseDouble(eastMinutes));

				double dis = 0;
				if (old != null) {
					dis = EarthCalc.getDistance(old, new MyPoint(north, east));
//					if (dis >= 0 && dis <= 5000) {
//						count++;
//						sum += dis;
						// System.out.println("sum " + sum + " count " + count);
						// System.out.println("Abstand: " + dis);
						// System.out.println("Durchschn: " + sum /
						// Math.max(count, 1) + " \n");
//					} else {
//						old = null;
//					}
				}
				
				String hour = completeLine.substring(0,5);
				String date = completeLine.substring(9,17);
//				System.out.println("hour: "+hour+", date: "+date+".");
				
				if(i != 0){
					allPoints.add(new MyPointForConverterWithDateAndDistance(north, east,dis,date,hour));
				}else{
					allPoints.add(new MyPointForConverterWithDateAndDistance(north, east,null,date,hour));
				}
				
			
				old = new MyPointForConverter(north, east, eastMinutes);

			} catch (Exception e) {
				System.out.println(i + " Error adding point for line:\n" + line+"\n northDegree:"+northDegree+" northMinutes:"+northMinutes+" eastDegree:"+eastDegree+" eastMinutes:"+eastMinutes);
				System.out.println();
				errorLines.add(completeLine);
			}
		}

		// lines.forEach(line -> {
		//
		// });
		return allPoints;
	}

	
	public static final List<MyPoint> getPointsFromTxt2(File file) throws IOException {
		if (file == null) {
			return Collections.emptyList();
		}
		Path path = Paths.get(file.getAbsolutePath());
		List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));
		// System.out.println(lines);
		lines = lines.stream().filter(l -> isValidLine(l)).map(line -> line.substring(18, line.length()))
				.collect(Collectors.toList());
		List<MyPoint> allPoints = new ArrayList<>();

		double sum = 0;
		double count = 0;

		MyPoint old = null;

		String northDegree = null;
		String northMinutes= null;
		String eastDegree= null;
		String eastMinutes= null;
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			try {
				northDegree = line.substring(0, 3);
				northMinutes = line.substring(3, 5) + "." + line.substring(5, 8);
//				System.out.println(northDegree + " " + northMinutes);
				eastDegree = line.substring(10, 13);
				eastMinutes = line.substring(13, 15) + "." + line.substring(15, 18);
//				System.out.println(eastDegree + " " + eastMinutes + "\n");
				GPSCoordinate north = new GPSCoordinate(Double.parseDouble(northDegree),
						Double.parseDouble(northMinutes));
				GPSCoordinate east = new GPSCoordinate(Double.parseDouble(eastDegree), Double.parseDouble(eastMinutes));

				double dis = 0;
				if (old != null) {
					dis = EarthCalc.getDistance(old, new MyPoint(north, east));
//					if (dis >= 0 && dis <= 5000) {
//						count++;
//						sum += dis;
						// System.out.println("sum " + sum + " count " + count);
						// System.out.println("Abstand: " + dis);
						// System.out.println("Durchschn: " + sum /
						// Math.max(count, 1) + " \n");
//					} else {
//						old = null;
//					}

				}
				allPoints.add(new MyPointForConverter(north, east, " " ));
				old = new MyPointForConverter(north, east, eastMinutes);

			} catch (Exception e) {
				System.out.println(i + " Error adding point for line:\n" + line+"\n northDegree:"+northDegree+" northMinutes:"+northMinutes+" eastDegree:"+eastDegree+" eastMinutes:"+eastMinutes);
				System.out.println();
			}
		}

		// lines.forEach(line -> {
		//
		// });
		return allPoints;
	}
	
	private static boolean isValidLine(String line) {
		if (line.contains("Fehler") || line.contains("Error")) {
			System.out.println("Error in line content:\n" + line);
			return false;
		} else {
			return true;
		}
		// if (line.length() != 37 || line.contains("Fehler") ||
		// line.contains("Error")) {
		// System.out.println("Error in line content:\n" + line);
		// return false;
		// } else {
		// return true;
		// }
	}

	@Deprecated
	public static final List<Point> getTestPoints() {
		try {
			String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()
					.getAbsolutePath();

			Path path = Paths.get(selectFilePath(desktopPath));
			// Path path = Paths.get(desktopPath + "/test_input.txt");

			List<String> lines = Files.readAllLines(path, Charset.forName("UTF-8"));

			// System.out.println(lines);

			lines = lines.stream().map(line -> line.substring(18, line.length())).collect(Collectors.toList());

			List<Point> allPoints = new ArrayList<>();
			lines.forEach(line -> {
				String northDegree = line.substring(0, 3);
				String northMinutes = line.substring(3, 5) + "." + line.substring(5, 8);
				// System.out.println(northDegree+" "+northMinutes);

				String eastDegree = line.substring(10, 13);
				String eastMinutes = line.substring(13, 15) + "." + line.substring(15, 18);
				// System.out.println(eastDegree+" "+eastMinutes);

				GPSCoordinate north = new GPSCoordinate(Double.parseDouble(northDegree),
						Double.parseDouble(northMinutes));
				GPSCoordinate east = new GPSCoordinate(Double.parseDouble(eastDegree), Double.parseDouble(eastMinutes));

				allPoints.add(new Point(north, east));

			});
			return allPoints;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Deprecated
	private static final String selectFilePath(String desktopPath) {
		JFileChooser chooser = new JFileChooser();
		chooser.setPreferredSize(new Dimension(1000, 600));
		FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("text files (*.txt)", "txt");
		chooser.setFileFilter(pdfFilter);
		chooser.setCurrentDirectory(new java.io.File(desktopPath));
		chooser.setDialogTitle("choosertitle");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		String path = null;
		while (path == null) {
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				System.out.println("getCurrentDirectory(): " + chooser.getCurrentDirectory());
				System.out.println("getSelectedFile() : " + chooser.getSelectedFile());
				if (chooser.getSelectedFile().getName().toLowerCase().endsWith(".txt")) {
					if (JOptionPane.showConfirmDialog(null,
							"willst du wirklich " + chooser.getSelectedFile().getName() + " als Pfad auswählen?", "",
							JOptionPane.CANCEL_OPTION) == 0) {
						path = chooser.getSelectedFile().getAbsolutePath();
					}
				}
			} else {
				System.out.println("No Selection ");
				if (JOptionPane.showConfirmDialog(null, "willst du wirklich abbrechen", "",
						JOptionPane.CANCEL_OPTION) == 0) {
					System.exit(0);
				}
			}
		}
		return path;
	}

	public static List<String> getLinesFromTextFile(String path) throws IOException {
		return Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
	}

}
