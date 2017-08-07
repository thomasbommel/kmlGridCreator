package main.java.kmlGridCreator.view;

import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.kmlGridCreator.exceptions.OverlappingPolyStylesException;
import main.java.kmlGridCreator.model.MapDataModel;

/**
 * this View is only used for testing
 * @author Sallaberger
 *
 */
public class TestView extends View{

	public boolean addPointsToKml;
	
	public TestView(MapDataModel model) throws OverlappingPolyStylesException {
		super();
	}

	@Override
	protected File selectInputFile() {
		throw new IllegalAccessError();
	}

	@Override
	protected File selectOutputFile(FileNameExtensionFilter filter) {
		throw new IllegalAccessError();
	}

	@Override
	public void printToViewConsole(String msg) {
		
	}

	@Override
	public void setViewConsoleText(String msg) {
		System.out.println(msg);
	}

	@Override
	public String getCurrentText() {
		throw new IllegalAccessError();
	}

	@Override
	public boolean addPointsToKmlEnabled() {
		return addPointsToKml;
	}

}
