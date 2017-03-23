package utils;

import java.awt.Dimension;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.peertopark.java.geocalc.GPSCoordinate;
import com.peertopark.java.geocalc.Point;

public class TxtUtil {

	public static final List<Point> getTestPoints() {
		try {
			String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

			// Path path = Paths.get(getFilePath(desktopPath));
			Path path = Paths.get(desktopPath + "/test_input.txt");

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

				GPSCoordinate north = new GPSCoordinate(Double.parseDouble(northDegree), Double.parseDouble(northMinutes));
				GPSCoordinate east = new GPSCoordinate(Double.parseDouble(eastDegree), Double.parseDouble(eastMinutes));

				allPoints.add(new Point(north, east));

			});
			return allPoints;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final String selectFilePath(String desktopPath) {
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
					if (JOptionPane.showConfirmDialog(null, "willst du wirklich " + chooser.getSelectedFile().getName() + " als Pfad auswählen?", "",
							JOptionPane.CANCEL_OPTION) == 0) {
						path = chooser.getSelectedFile().getAbsolutePath();
					}
				}
			} else {
				System.out.println("No Selection ");
				if (JOptionPane.showConfirmDialog(null, "willst du wirklich abbrechen", "", JOptionPane.CANCEL_OPTION) == 0) {
					System.exit(0);
				}
			}
		}
		return path;
	}

}
