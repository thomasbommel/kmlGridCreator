package kmlGridCreator.view;

import java.awt.FlowLayout;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.text.DefaultCaret;

import kmlGridCreator.model.AbstractLogger.LogLevel;
import kmlGridCreator.model.MyBoundingArea;

public class GuiView extends View {

	private JTextArea console;
	private JButton selectInputFile, selectOutputFile, startCreation;
	private List<MyBoundingArea> grid;

	private MainApplication app;
	private JFrame frame;
	private JPanel contentPane;
	private GuiLogger log;

	public GuiView(MainApplication app) {
		super();
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

		this.frame.setSize(640, 480);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}

	private void initialiseFrame() {
		this.contentPane = (JPanel) this.frame.getContentPane();
		this.contentPane.setLayout(new FlowLayout());

		this.selectInputFile = new JButton("select input file");
		this.selectInputFile.addActionListener(a -> {
			printToConsole(a.getActionCommand());
		});
		this.contentPane.add(selectInputFile);

		this.selectOutputFile = new JButton("select output file");
		this.selectOutputFile.setEnabled(false);
		this.contentPane.add(selectOutputFile);

		this.startCreation = new JButton("start creation");
		this.startCreation.setEnabled(false);
		this.contentPane.add(startCreation);

		this.console = new JTextArea(25, 50);
		final DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		final JScrollPane scrollPane = new JScrollPane(console);

		this.console.setEditable(false);
		this.contentPane.add(scrollPane);
	}

	@Override
	protected File selectInputFile() {
		return null;
	}

	@Override
	protected File selectOutputFile() {
		return null;
	}

	@Override
	public void printToConsole(String msg) {
		log.debug(msg);
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
		return selectInputFile;
	}

	public JButton getSelectOutputFile() {
		return selectOutputFile;
	}

	public JButton getStartCreation() {
		return startCreation;
	}

	public MainApplication getApp() {
		return app;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

}
