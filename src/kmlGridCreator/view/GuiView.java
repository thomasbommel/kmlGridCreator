package kmlGridCreator.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

import kmlGridCreator.model.AbstractLogger.LogLevel;
import kmlGridCreator.model.MapDataModel;
import kmlGridCreator.model.MyBoundingArea;

public class GuiView extends View {

	private JTextArea console;
	private MyJButton selectInputFileBtn, selectOutputFileBtn, startCreationBtn;
	private List<MyBoundingArea> grid;

	private MainApplication app;
	private JFrame frame;
	private JPanel contentPane;
	private GuiLogger log;
	private Lock lock = new ReentrantLock();

	public GuiView(MainApplication app, MapDataModel model) {
		super(model);

		log = new GuiLogger(LogLevel.DEBUG, this);

		// try {
		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		// } catch (ClassNotFoundException | InstantiationException |
		// IllegalAccessException | UnsupportedLookAndFeelException e) {
		// e.printStackTrace();
		// }
		this.frame = new JFrame(app.getTitle());
		this.app = app;

		this.initialiseFrame();

		this.frame.setSize(840, 640);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}

	private void initialiseFrame() {
		this.contentPane = (JPanel) this.frame.getContentPane();
		this.contentPane.setLayout(new FlowLayout());

		this.selectInputFileBtn = new MyJButton(true, "Eing. auswählen", "Eing. auswählen",
				"Mit diesem Button kann eine Datei, aus der dann gelesen wird, ausgewählt werden.",
				"es kann derzeit kein Datei zum Einlesen ausgewählt werden");
		this.selectInputFileBtn.addActionListener(a -> {
			if (lock.tryLock()) {
				try {
					File selectedFile = selectInputFile();
					if (selectedFile != null) {
						this.getModel().setFileToReadFrom(selectedFile);
						this.selectInputFileBtn.setEnabledText("Eing. ändern");
						this.selectInputFileBtn.setToolTipText("Mit diesem Button kann die Auswahl der Eingabedatei geändert werden.");
						this.log.info(selectedFile.getName() + " wurde als Eingabedatei ausgewählt.");
						this.selectOutputFileBtn.setEnabled(true);
					}
				} finally {
					lock.unlock();
				}
			}
		});
		this.contentPane.add(selectInputFileBtn);

		this.selectOutputFileBtn = new MyJButton(false, "Ausg. auswählen", "Ausg. auswählen",
				"Mit diesem Button kann eine Datei, in die dann geschrieben wird, ausgewählt werden", "derzeit nicht möglich");
		this.selectOutputFileBtn.addActionListener(a -> {
			if (lock.tryLock()) {
				try {
					File selectedFile = selectOutputFile();
					if (selectedFile != null) {
						this.getModel().setFileToWriteTo(selectedFile);
						this.selectOutputFileBtn.setEnabledText("Ausg. ändern");
						this.selectOutputFileBtn.setToolTipText("Mit diesem Button kann die Auswahl der Ausgabedatei geändert werden.");
						this.log.info(selectedFile.getName() + " wurde als Ausgabedatei ausgewählt.");
						this.getModel().setFileToWriteTo(selectedFile);

						if (this.getModel().getFileToReadFrom() != null && this.getModel().getFileToWriteTo() != null) {
							this.startCreationBtn.setEnabled(true);
						}
					}
				} finally {
					lock.unlock();
				}
			}
		});

		this.contentPane.add(selectOutputFileBtn);

		this.startCreationBtn = new MyJButton(false, "starte Generierung", "starte Generierung",
				"startet die Berechnung der Punkte und speichert dann die .kml Datei", "derzeit nicht möglich");
		this.startCreationBtn.addActionListener(a -> {
			MapDataModel model = this.getModel();
			Thread thread = new Thread() {
				@Override
				public void run() {
					if (lock.tryLock()) {
						try {
							model.startCreation();
						} finally {
							lock.unlock();
						}
					}
				}
			};
			thread.start();
		});

		this.contentPane.add(startCreationBtn);

		this.console = new JTextArea(28, 70);
		this.console.setFont(this.console.getFont().deriveFont(14f));
		final DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		final JScrollPane scrollPane = new JScrollPane(console);

		this.console.setEditable(false);
		this.contentPane.add(scrollPane);
	}

	@Override
	protected File selectInputFile() {
		String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

		JFileChooser chooser = new JFileChooser();
		chooser.setPreferredSize(new Dimension(1000, 600));
		FileNameExtensionFilter fileExtensionFilter = new FileNameExtensionFilter("text files (*.txt)", "txt");
		chooser.setFileFilter(fileExtensionFilter);
		chooser.setCurrentDirectory(new java.io.File(desktopPath));
		chooser.setDialogTitle("Eingabedatei auswählen.");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			if (!chooser.getSelectedFile().getName().toLowerCase().endsWith(".txt") || !Files.exists(Paths.get(chooser.getSelectedFile().getAbsolutePath()))) {
				JOptionPane.showMessageDialog(frame, "Datei existiert nicht oder ist ein ungültiges Format");
			} else {
				return chooser.getSelectedFile();
			}
		}
		return null;
	}

	@Override
	protected File selectOutputFile() {
		String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

		JFileChooser chooser = new JFileChooser();
		chooser.setPreferredSize(new Dimension(1000, 600));
		FileNameExtensionFilter fileExtensionFilter = new FileNameExtensionFilter("kml files (*.kml)", "kml");
		chooser.setFileFilter(fileExtensionFilter);
		chooser.setCurrentDirectory(new java.io.File(desktopPath));
		chooser.setDialogTitle("Ausgabedatei auswählen.");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		chooser.setCurrentDirectory(new java.io.File(desktopPath));
		int retrival = chooser.showSaveDialog(null);
		if (retrival == JFileChooser.APPROVE_OPTION) {
			File selected = chooser.getSelectedFile();
			if (!selected.getAbsolutePath().contains(".kml")) {
				selected = new File(selected.getAbsolutePath() + ".kml");
			}
			if (fileExtensionFilter.accept(selected)) {
				if (Files.exists(Paths.get(selected.getAbsolutePath()))) {
					int overwrite = JOptionPane.showConfirmDialog(frame, "überschreiben?", null, JOptionPane.YES_NO_OPTION);
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
		return this.getConsole().getText();
	}

	@Override
	public void setViewConsoleText(String msg) {
		this.console.setText(msg + "\n");
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

	public JTextArea getConsole() {
		return console;
	}

	public JButton getSelectInputFile() {
		return selectInputFileBtn;
	}

	public JButton getSelectOutputFile() {
		return selectOutputFileBtn;
	}

	public JButton getStartCreation() {
		return startCreationBtn;
	}

	public MainApplication getApp() {
		return app;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

}
