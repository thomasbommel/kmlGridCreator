package old.application;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.peertopark.java.geocalc.Point;

public class MainGUI implements BaseUI {

	private static MainGUI gui;
	private JFrame frame;

	private MainGUI(String title) {
		this.frame = initializeJFrame(title);
	}

	public static void main(String[] args) {
		String s = "hallo";
		gui = new MainGUI("KMLGridCreator " + s.getClass().getPackage().getImplementationVersion());

	}

	private JFrame initializeJFrame(String title) {
		JFrame returnFrame = new JFrame(title);
		returnFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		returnFrame.setSize(640, 480);
		returnFrame.setLocationRelativeTo(null);
		returnFrame.setResizable(false);
		returnFrame.setVisible(true);
		return returnFrame;
	}

	@Override
	public void pointCreated(Point p) {
		// TODO
	}

}
