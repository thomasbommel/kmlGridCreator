package converter;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.peertopark.java.geocalc.Coordinate;
import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.GPSCoordinate;
import com.peertopark.java.geocalc.Point;
import com.sun.tools.xjc.reader.Util;

import main.java.kmlGridCreator.model.MyPoint;

/**
 * converts the file from the sd to the file used by the KMLGridCreator
 * 
 * @author Sallaberger
 *
 */
public class DataFormatConverterForNewDevices {

	private static final String FILE_PATH = Paths.get("./").toAbsolutePath().normalize().toString() + "";

	private static File file;

	public static void main(String[] args) {
		file = selectInputFile();
		Path path = Paths.get(file.getAbsolutePath());
		try {
			List<String> lines = getLinesFromFile(path);

			List<MyPointForConverter> points = getPointsFromLines(lines);

			List<String> convertedLines = new ArrayList<>();

			points.stream().forEach(point -> {
				try {
					//

					String fullLat = String.format("%03d", (int) point.getLatitude());
					String fullLng = String.format("%03d", (int) point.getLongitude());
					int fulllat = (int) point.getLatitude();
					int fulllng = (int) point.getLongitude();
					double lat = point.getLatitude() - fulllat;
					lat = lat * 0.6;
					String latDegree = Double.toString(lat).replace("0.", "").substring(0, 5);

					double lng = point.getLongitude() - fulllng;
					lng = lng * 0.6;
					String lngDegree = Double.toString(lng).replace("0.", "").substring(0, 5);

					convertedLines.add(
							point.toString() + "" + fullLat + "" + latDegree + "N," + fullLng + "" + lngDegree + "E");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			});

			String time = Long.toString(System.currentTimeMillis());
			JOptionPane.showMessageDialog(null, "started conversion, of " + points.size() + " lines");

			convertedLines.forEach(line -> {
				addToTxt(file.getName() + "_converted.txt", line);
			});

			JOptionPane.showMessageDialog(null, "completed conversion, converted " + convertedLines.size() + " lines, "
					+ (lines.size() - convertedLines.size()) + " were not converted");

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "error opening file \n" + path.toAbsolutePath().toString());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "error \n" + e.getMessage());
		}
	}

	private static List<String> getLinesFromFile(Path path) throws IOException {
		return Files.readAllLines(path, Charset.forName("UTF-8"));
	}

	private static List<MyPointForConverter> getPointsFromLines(List<String> lines) {

		List<MyPointForConverter> points = new ArrayList<>();

		double sum = 0;
		int count = 0;

		MyPointForConverter old = null;

		for (int i = 0; i < lines.size(); i++) {

			try {
				MyPointForConverter point = getPointFromline(lines.get(i));
				if (old != null) {
					double dist = EarthCalc.getHarvesineDistance(point, old);
					if (dist > 2 && dist < 500) {
						sum += dist;
						count++;
						System.out.println(Utils.numberToString(i, 8, 0)+ " distance "+Utils.numberToString(dist, 8, 3));
					}
				}
				old = point;
				points.add(point);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		System.out.println("average distance "+Utils.numberToString(sum / count,10,3));
		System.out.println("count " + count);

		return points;
	}

	private static MyPointForConverter getPointFromline(String line) throws Exception {

		if (line.length() <40 || line.length() >60) {
			System.out.println("line length incorrect: " + line + "; " + line.length());
		} else {
			System.out.println("line: "+line);
			String date = line.substring(0, 18);

			String latDegree = line.split(",")[2];
			String lngDegree = line.split(",")[3];

			return new MyPointForConverter(new DegreeCoordinate(Double.parseDouble(latDegree)),
					new DegreeCoordinate(Double.parseDouble(lngDegree)), date);
		}
		throw new Exception("not valid");
	}

	public static void addToTxt(String filename, String text) {
		try (FileWriter fw = new FileWriter(
				(file.getAbsoluteFile().getParent().replace(".TXT", "") + "/" + filename).replace(".TXT", ""), true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(text);
			out.flush();

			// System.out.println((file.getAbsoluteFile().getParent().replace(".TXT","")
			// + "/" + filename ).replace(".TXT",""));
			// LOGGER.info("added: '"+text+"' to: "+filename);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// LOGGER.severe("wasn't able to apped to txt file " +
			// e.getMessage());
		}
	}

	protected static File selectInputFile() {
		String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()
				.getAbsolutePath();

		JFileChooser inputFileChooser = new JFileChooser();
		inputFileChooser.setPreferredSize(new Dimension(1000, 600));
		FileNameExtensionFilter fileExtensionFilter = new FileNameExtensionFilter("text files (*.txt)", "txt");
		inputFileChooser.setFileFilter(fileExtensionFilter);
		inputFileChooser.setCurrentDirectory(new java.io.File(desktopPath));
		inputFileChooser.setDialogTitle("Eingabedatei auswählen.");
		inputFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		inputFileChooser.setAcceptAllFileFilterUsed(false);

		if (inputFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			if (!inputFileChooser.getSelectedFile().getName().toLowerCase().endsWith(".txt")
					|| !Paths.get(inputFileChooser.getSelectedFile().getAbsolutePath()).toFile().exists()) {
				JOptionPane.showMessageDialog(null, "Datei existiert nicht oder ist ein ungültiges Format");
			} else {
				return inputFileChooser.getSelectedFile();
			}
		}
		return null;
	}

}
