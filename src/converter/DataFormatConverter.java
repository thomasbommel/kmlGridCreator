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
import com.peertopark.java.geocalc.GPSCoordinate;
import com.peertopark.java.geocalc.Point;

import main.java.kmlGridCreator.model.MyPoint;

@Deprecated
public class DataFormatConverter {

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
//				try{
//				String convertedLine = point.toString();
//
//				String fullLat = String.format("%03d", (int) Math.floor(point.getLatitude()));
//				convertedLine += fullLat;
//				double comma = point.getLatitude() - Math.floor(point.getLatitude());
//				int minutes = (int) (comma*60000);
//				convertedLine += minutes + "N,";
//				
//				String fullLng = String.format("%03d", (int) Math.floor(point.getLongitude()));
//				convertedLine += fullLng;
//				double commaLng = point.getLongitude() - Math.floor(point.getLongitude());
//				int minutesLng = (int) (commaLng*60000);
//				convertedLine += minutesLng + "E";
//				
//				convertedLines.add(convertedLine);
//				}catch (Exception e) {
//					// TODO: handle exception
//				}
				
				int fullLat =(int) point.getLatitude();
				String fullLatString = String.format("%03d", (int) Math.floor(point.getLatitude()));
				
				double minlat =  (point.getLatitude()-(double)fullLat);
				minlat = minlat*0.6*100;
				
				String minlatString= "";
	
				if(minlat<10){
					minlatString+="0";
				}
				minlatString+=minlat;
				
				//System.out.println(String.format("%03f",minlat));
				minlatString = minlatString.replace(".", "").substring(0,5);
				
				
				int fulllng =(int) point.getLongitude();
				String fulllngString = String.format("%03d", (int) Math.floor(point.getLongitude()));
				
				double minlng =  (point.getLongitude()-(double)fulllng);
				minlng = minlng*0.6*100;
				
				String minlngString= "";
	
				if(minlng<10){
					minlngString+="0";
				}
				minlngString+=minlng;
				
				//System.out.println(String.format("%03f",minlng));
				minlngString = minlngString.replace(".", "").substring(0,5);
				
				System.out.println(point.toString()+""+fullLatString+""+minlatString+"N,"+fulllngString+""+minlngString+"E");
				convertedLines.add(point.toString()+""+fullLatString+""+minlatString+"N,"+fulllngString+""+minlngString+"E");
				
			});

			String time = Long.toString(System.currentTimeMillis());

			convertedLines.forEach(line -> {
				addToTxt(file.getName()+"_converted.txt", line);
			});

			JOptionPane.showMessageDialog(null, "completed conversion, converted "+convertedLines.size()+" lines, "+(lines.size()-convertedLines.size())+" were not converted");

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "error opening file \n" + path.toAbsolutePath().toString());
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "error \n" +e.getMessage());
		}
	}

	private static List<String> getLinesFromFile(Path path) throws IOException {
		return Files.readAllLines(path, Charset.forName("UTF-8"));
	}

	private static List<MyPointForConverter> getPointsFromLines(List<String> lines) {
		
		List<MyPointForConverter> points = new ArrayList<>();

		lines.stream().forEach(line -> {
			try{
			points.add(getPointFromline(line));
			}
			catch (Exception e) {
				// TODO: handle exception
			}
		});

		return points;
	}

	private static MyPointForConverter getPointFromline(String line) throws Exception {
	
		if (line.length() <60) {
			System.out.println("line length incorrect: " + line + "; "+line.length());
		}else{
		String date = line.substring(0, 18);

		String latDegree = line.split("  ")[1];
		String lngDegree = line.split("  ")[2];

		return new MyPointForConverter(new DegreeCoordinate(Double.parseDouble(latDegree)),
				new DegreeCoordinate(Double.parseDouble(lngDegree)), date);
		}
		throw new Exception("not valid");
	}

	public static void addToTxt(String filename, String text) {
		try (FileWriter fw = new FileWriter((file.getAbsoluteFile().getParent().replace(".TXT","") + "/" + filename ).replace(".TXT",""), true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(text);
			out.flush();
			
			//System.out.println((file.getAbsoluteFile().getParent().replace(".TXT","") + "/" + filename ).replace(".TXT","")); 
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
