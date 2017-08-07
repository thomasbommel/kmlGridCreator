package main.java.kmlGridCreator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

import main.java.kmlGridCreator.exceptions.OverlappingPolyStylesException;
import main.java.kmlGridCreator.model.AbstractLogger.LogLevel;
import main.java.kmlGridCreator.model.MapDataModel;
import main.java.kmlGridCreator.model.MyBoundingArea;
import main.java.kmlGridCreator.model.Unused;
import main.java.kmlGridCreator.utils.styles.ColorUtils;
import main.java.kmlGridCreator.utils.styles.MyPolyStyle;

class GuiView extends View {

	private JTextArea console;
	private MyJButton selectInputFileBtn, selectOutputFileBtn, startCreationBtn, selectCSVOutputFileBtn;
	private JCheckBox addPointsToKMLChkBox;
	private JColorChooser chooser;
	private List<MyBoundingArea> grid;

	private MainApplication app;
	private JFrame frame, colorChooserFrame;
	private JPanel contentPane;
	private GuiLogger log;
	private Lock buttonLock = new ReentrantLock(); 	// this lock is needed
													// because otherwise you can
													// click a button during the
													// generation

	private boolean useIconsInsteadOfPinsForPointsInKML = false;

	private static final String CURRENTLY_NOT_POSSIBLE = "derzeit nicht möglich";

	GuiView(MainApplication app) throws OverlappingPolyStylesException {
		super();

		log = new GuiLogger(LogLevel.DEBUG, this);
		this.frame = new JFrame(app.getTitle());
		this.app = app;

		this.initialiseFrame();
		// setLookAndFeel();

		this.frame.setSize(840, 640);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}

	@Unused
	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void initialiseFrame() {
		this.contentPane = (JPanel) this.frame.getContentPane();
		this.contentPane.setLayout(new FlowLayout());

		this.selectInputFileBtn = new MyJButton(true, "Eing. auswählen", "Eing. auswählen",
				"Mit diesem Button kann eine Datei, aus der dann gelesen wird, ausgewählt werden.",
				"es kann derzeit kein Datei zum Einlesen ausgewählt werden");
		this.selectInputFileBtn.addActionListener(a -> {
			if (buttonLock.tryLock()) {
				try {
					File selectedFile = selectInputFile();
					if (selectedFile != null) {
						this.getModel().setFileToReadFrom(selectedFile);
						this.selectInputFileBtn.setEnabledText("Eing. ändern");
						this.selectInputFileBtn
								.setToolTipText("Mit diesem Button kann die Auswahl der Eingabedatei geändert werden.");
						this.log.info(selectedFile.getName() + " wurde als Eingabedatei ausgewählt.");
						this.selectOutputFileBtn.setEnabled(true);
					}
				} finally {
					buttonLock.unlock();
				}
			}
		});
		this.contentPane.add(selectInputFileBtn);

		this.selectOutputFileBtn = new MyJButton(false, "Ausg. auswählen", "Ausg. auswählen",
				"Mit diesem Button kann eine Datei, in die dann geschrieben wird, ausgewählt werden",
				CURRENTLY_NOT_POSSIBLE);
		this.selectOutputFileBtn.addActionListener(a -> {
			if (buttonLock.tryLock()) {
				try {
					File selectedFile = selectOutputFile(new FileNameExtensionFilter("kml files (*.kml)", "kml"));
					if (selectedFile != null) {
						this.getModel().setFileToWriteTo(selectedFile);
						this.selectOutputFileBtn.setEnabledText("Ausg. ändern");
						this.selectOutputFileBtn
								.setToolTipText("Mit diesem Button kann die Auswahl der Ausgabedatei geändert werden.");
						this.log.info(selectedFile.getName() + " wurde als Ausgabedatei ausgewählt.");
						this.getModel().setFileToWriteTo(selectedFile);

						if (this.getModel().getFileToReadFrom() != null && this.getModel().getFileToWriteTo() != null) {
							this.startCreationBtn.setEnabled(true);
							this.addPointsToKMLChkBox.setEnabled(true);
							this.selectCSVOutputFileBtn.setEnabled(true);
						}
					}
				} finally {
					buttonLock.unlock();
				}
			}
		});

		this.contentPane.add(selectOutputFileBtn);

		this.startCreationBtn = new MyJButton(false, "starte Generierung", "starte Generierung",
				"startet die Berechnung der Punkte und speichert dann die .kml Datei", CURRENTLY_NOT_POSSIBLE);
		this.startCreationBtn.addActionListener(a -> {
			MapDataModel model = this.getModel();
			Thread thread = new Thread() {
				@Override
				public void run() {
					if (buttonLock.tryLock()) {
						try {
							model.startCreation();
							log.info("---- ALLES FERTIG ----");
						} catch (Exception e) {
							e.printStackTrace();
							log.error("Es ist ein Fehler aufgetreten\n" + e.getMessage());
						} finally {
							buttonLock.unlock();
						}
					}
				}
			};
			thread.start();
		});

		this.contentPane.add(startCreationBtn);

		this.addPointsToKMLChkBox = new JCheckBox("Punkte erst.");
		this.addPointsToKMLChkBox
				.setToolTipText("legt fest ob die Punkte der '.txt' Datei zu der '.kml' Datei hinzugefügt werden");
		this.addPointsToKMLChkBox.setEnabled(false);

		this.addPointsToKMLChkBox.addActionListener(x -> {
			if(this.addPointsToKMLChkBox.isSelected()){
				int useIconsInsteadOfPins = JOptionPane.showConfirmDialog(frame, "Icons verwenden?", null,
						JOptionPane.YES_NO_OPTION);
				if (useIconsInsteadOfPins == JOptionPane.YES_OPTION) {
						this.useIconsInsteadOfPinsForPointsInKML = true;
				}
			}
		});

		this.contentPane.add(addPointsToKMLChkBox);

		JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
		this.contentPane.add(separator);

		selectCSVOutputFileBtn = new MyJButton(false, "CSV Ausg. wählen", "CSV Ausg. wählen",
				"legt den Pfad für die zu erstellende '.csv' Datei fest", CURRENTLY_NOT_POSSIBLE);
		selectCSVOutputFileBtn.addActionListener(a -> {
			if (buttonLock.tryLock()) {
				try {
					File selectedFile = selectOutputFile(new FileNameExtensionFilter("csv files (*.csv)", "csv"));
					if (selectedFile != null) {
						this.getModel().setFileToWriteCSVTo(selectedFile);
						this.selectCSVOutputFileBtn.setEnabledText("CSV Ausg. ändern");
						this.selectOutputFileBtn.setToolTipText(
								"Mit diesem Button kann die Auswahl der CSV Ausgabedatei geändert werden.");
						this.log.info(selectedFile.getName() + " wurde als CSV Ausgabedatei ausgewählt.");
						this.getModel().setFileToWriteCSVTo(selectedFile);
					}
				} finally {
					buttonLock.unlock();
				}
			}
		});
		this.contentPane.add(selectCSVOutputFileBtn);

		JButton showColorChooserBtn = new JButton("wähle Farben");
		showColorChooserBtn.addActionListener(x -> {
			if (buttonLock.tryLock()) {
				this.colorChooserFrame.setVisible(true);
			}
		});
		this.contentPane.add(showColorChooserBtn);

		this.console = new JTextArea(28, 70);
		this.console.setFont(this.console.getFont().deriveFont(14f));
		final DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		final JScrollPane scrollPane = new JScrollPane(console);

		this.console.setEditable(false);
		this.contentPane.add(scrollPane);

		this.chooser = new JColorChooser();
		for (AbstractColorChooserPanel panel : chooser.getChooserPanels()) {
			if (!panel.getDisplayName().equals("RGB")) {
				chooser.removeChooserPanel(panel);
			}
		}

		class Preview extends JComponent implements ChangeListener {
			private List<JPanel> panels;
			private JButton saveThisColorSchemeBtn;
			private List<JLabel> labels;
			private int selectedPanel = 0;

			Preview() {
				this.setLayout(new FlowLayout());
				this.panels = new ArrayList<>();
				this.labels = new ArrayList<>();
				this.saveThisColorSchemeBtn = new JButton("save");

				List<MyPolyStyle> polyStyles = getKmlFactory().getPolyStyleHandler().getPolyStyles();

				for (int i = 0; i < polyStyles.size(); i++) {
					MyPolyStyle polyStyle = polyStyles.get(i);
					JLabel l = new JLabel(polyStyle.getMinPointCount() + "-" + polyStyle.getMaxPointCount());

					JPanel p = new JPanel();
					p.setBackground(polyStyle.getColorForTxt());
					int count = polyStyles.size();
					p.setPreferredSize(new Dimension((int) Math.max(600 / count, 30), (int) Math.max(400 / count, 30)));

					this.labels.add(l);
					this.panels.add(p);
					this.add(l);
					this.add(p);

					final int index = i;
					p.addMouseListener(new MyMouseListener() {
						@Override
						public void mouseIsClicked(MouseEvent evt) {
							selectedPanel = index;
							chooser.setColor(polyStyles.get(selectedPanel).getColorForTxt());
						}
					});

					saveThisColorSchemeBtn.addActionListener(x -> saveColorSchemeFile());
					this.add(saveThisColorSchemeBtn);
				}
			}

			private void saveColorSchemeFile() {
				List<String> lines = new ArrayList<>();
				for (int i = 0; i < getKmlFactory().getPolyStyleHandler().getPolyStyles().size(); i++) {
					MyPolyStyle polyStyle = getKmlFactory().getPolyStyleHandler().getPolyStyles().get(i);
					lines.add(polyStyle.getPolyStyleStringForTxt());
				}

				try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File("colors.txt")))) {
					lines.forEach(line -> {
						appendLineTOBufferedWriter(line, bw);
					});
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			private void appendLineTOBufferedWriter(String line, BufferedWriter bw) {
				try {
					bw.write(line);
					bw.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void stateChanged(ChangeEvent e) {
				panels.get(selectedPanel).setBackground(chooser.getColor());
				getKmlFactory().getPolyStyleHandler().getPolyStyles().get(selectedPanel)
						.setColor(ColorUtils.convertColorToKMLColorString(chooser.getColor()));

				getKmlFactory().getPolyStyleHandler().getPolyStyles().get(selectedPanel)
						.setColorForTxt(chooser.getColor());
			}
		}
		Preview prev = new Preview();
		this.chooser.setPreviewPanel(prev);
		this.colorChooserFrame = new JFrame("Color chooser");
		this.colorChooserFrame.getContentPane().setLayout(new FlowLayout());
		this.colorChooserFrame.getContentPane().add(chooser);
		this.colorChooserFrame.pack();
		this.colorChooserFrame.setLocationRelativeTo(frame);
		this.colorChooserFrame.setAlwaysOnTop(true);
		this.chooser.getSelectionModel().addChangeListener(prev);
		this.colorChooserFrame.addComponentListener(new ComponentListener() {
			@Override
			public void componentShown(ComponentEvent e) {
				// do nothing
			}

			@Override
			public void componentResized(ComponentEvent e) {
				// do nothing
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// do nothing
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				buttonLock.unlock();
			}
		});
	}

	@Override
	protected File selectInputFile() {
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
				JOptionPane.showMessageDialog(frame, "Datei existiert nicht oder ist ein ungültiges Format");
			} else {
				return inputFileChooser.getSelectedFile();
			}
		}
		return null;
	}

	@Override
	protected File selectOutputFile(FileNameExtensionFilter extensionFilter) {
		String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()
				.getAbsolutePath();

		JFileChooser outputFileChooser = new JFileChooser();
		outputFileChooser.setPreferredSize(new Dimension(1000, 600));
		FileNameExtensionFilter fileExtensionFilter = extensionFilter;
		outputFileChooser.setFileFilter(fileExtensionFilter);
		outputFileChooser.setCurrentDirectory(new java.io.File(desktopPath));
		outputFileChooser.setDialogTitle("Ausgabedatei auswählen.");
		outputFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		outputFileChooser.setAcceptAllFileFilterUsed(false);

		outputFileChooser.setCurrentDirectory(new java.io.File(desktopPath));
		int retrival = outputFileChooser.showSaveDialog(null);
		if (retrival == JFileChooser.APPROVE_OPTION) {
			File selected = outputFileChooser.getSelectedFile();
			if (!selected.getAbsolutePath().contains("." + extensionFilter.getExtensions()[0])) {
				selected = new File(selected.getAbsolutePath() + "." + extensionFilter.getExtensions()[0]);
			}
			if (fileExtensionFilter.accept(selected)) {
				if (Paths.get(selected.getAbsolutePath()).toFile().exists()) {
					int overwrite = JOptionPane.showConfirmDialog(frame, "überschreiben?", null,
							JOptionPane.YES_NO_OPTION);
					if (overwrite == JOptionPane.NO_OPTION) {
						return null;
					}
				}
				return selected;
			}
		}
		return null;
	}

	@Override
	public void printToViewConsole(String msg) {
		log.info(msg);
	}

	@Override
	public String getCurrentText() {
		return this.getConsoleTextArea().getText();
	}

	@Override
	public void setViewConsoleText(String msg) {
		this.console.setText(msg + "\n");
	}

	@Override
	public boolean addPointsToKmlEnabled() {
		return this.addPointsToKMLChkBox.isSelected();
	}

	@Override
	public boolean useIconsInsteadOfPinsForPointsInKML() {
		return this.useIconsInsteadOfPinsForPointsInKML;
	}

	// =====================================================================
	public List<MyBoundingArea> getGrid() {
		return grid;
	}

	public void setGrid(List<MyBoundingArea> grid) {
		this.grid = grid;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public GuiLogger getLog() {
		return log;
	}

	public void setLog(GuiLogger log) {
		this.log = log;
	}

	public JTextArea getConsoleTextArea() {
		return console;
	}

	public JButton getSelectInputFileBtn() {
		return selectInputFileBtn;
	}

	public JButton getSelectOutputFileBtn() {
		return selectOutputFileBtn;
	}

	public JButton getStartCreationBtn() {
		return startCreationBtn;
	}

	public MainApplication getApp() {
		return app;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

}
