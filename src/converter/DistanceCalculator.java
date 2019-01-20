package converter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.kmlGridCreator.utils.TxtUtil;

public class DistanceCalculator {

	private static File file;

	private static JFrame frame;
	private static JLabel label;

	public static void main(String[] args) throws IOException {
		file = selectInputFile();
		List<MyPointForConverterWithDateAndDistance> points = TxtUtil.getPointsFromTxt(file);

		List<String> convertedLines = new ArrayList<>();

		for (int i = 0; i < points.size(); i++) {
			MyPointForConverterWithDateAndDistance point = points.get(i);

			try {
				//
				double lat = point.getLatitude();
				double lng = point.getLongitude();

				DecimalFormat df = new DecimalFormat("00.00000");

				DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
				DateFormat dateFormatToSave = new SimpleDateFormat("dd.MM.yyyy");

				String dateToSave = dateFormatToSave.format(dateFormat.parse(point.getDate()));

				String dist = point.getDistance() == null ? "" : "" + Math.round(point.getDistance()) + " m";

				convertedLines.add("N" + df.format(lat).replace(",", ".") + " E" + df.format(lng).replace(",", ".")  + " " + dateToSave + " "
						+ point.getHourAndMinute() + "\t\t\t\t" + dist);
			} catch (Exception e) {
				System.out.println("exception "+e.getMessage());
				e.printStackTrace();
			}
		}

		frame = new JFrame("Converting...");

		JPanel contentPane = (JPanel) frame.getContentPane();
		contentPane.setLayout(new BorderLayout());

		label = new JLabel("", SwingConstants.CENTER);
		contentPane.add(label, BorderLayout.CENTER);

		frame.pack();
		frame.setSize(400, 100);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		
		
		String filename = file.getName() + "_distance";
		
		String fn = file.getAbsoluteFile().getParent().replace(".TXT", "") + "/" + filename;
		fn = fn.replace(".txt", "");
		fn = fn + ".txt";
		
		for (int i = 0; i < convertedLines.size(); i++) {
			String line = convertedLines.get(i);
			addToTxt(fn, line);

			if (i % 100 == 0) {
				label.setText("" + i + " / " + convertedLines.size());
			}

		}
		JOptionPane.showMessageDialog(frame, "Completed conversion of " + convertedLines.size() + " Points.");
		
		if(!TxtUtil.errorLines.isEmpty()){
			frame.setSize(400, 400);
			JTextArea area = new JTextArea();
			JScrollPane sp = new JScrollPane(area);
			sp.setBounds(10,10,360,340);
			sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			contentPane.removeAll();
			frame.setTitle("ERRORS");
			contentPane.add(sp, BorderLayout.CENTER);
			String errorLines = TxtUtil.errorLines.stream().collect(Collectors.joining("\n"));
			area.setText(errorLines);
		}
		
		
	}


	public static void addToTxt(String filename, String text) {


		try (FileWriter fw = new FileWriter(filename, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			out.println(text);
			out.flush();

			// System.out.println((file.getAbsoluteFile().getParent().replace(".TXT","")
			// + "/" + filename ).replace(".TXT",""));
			// LOGGER.info("added: '"+text+"' to: "+filename);
		} catch (Exception e) {
			System.out.println("exception " + e.getMessage());
			// LOGGER.severe("wasn't able to apped to txt file " +
			// e.getMessage());
			e.printStackTrace();
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
