package main.java.kmlGridCreator.model;

import java.io.File;
import java.io.IOException;

import main.java.kmlGridCreator.utils.MyKmlFactory;
import main.java.kmlGridCreator.utils.TxtUtil;
import main.java.kmlGridCreator.view.View;

public class MapDataModel {

	private MyMap map;
	private File fileToReadFrom, fileToWriteTo;
	private View view;

	public void startCreation() throws IOException {
		view.printToViewConsole("Die Generierung des kml Files wurde gestartet.");

		map = new MyMap(TxtUtil.getPointsFromTxt(fileToReadFrom), 1000, view);
		view.printToViewConsole("--- PUNKTE WERDEN ZUGEORDNET");
		map.addPointsToTheAreas();
		view.printToViewConsole(
				"--- Die Zuordnung ist beendet ---");

		MyKmlFactory kml = new MyKmlFactory("generatedKmlDocument");

		view.printToViewConsole(" Datei " + fileToWriteTo.getName() + " wird erstellt.");
		for (MyBoundingArea area : map.getBoundingAreas()) {
			kml.addBoundingArea(area);
			if(view.addPointsToKml()){
				kml.addPointsToKml(area.getPoints()); 
			}
		}
		
		kml.saveKmlFile(fileToWriteTo);
		view.printToViewConsole(" Datei " + fileToWriteTo.getName() + " erstellt und gespeichert.");

	}

	public MyMap getMap() {
		return map;
	}

	public File getFileToReadFrom() {
		return fileToReadFrom;
	}

	public void setFileToReadFrom(File fileToReadFrom) {
		this.fileToReadFrom = fileToReadFrom;
	}

	public File getFileToWriteTo() {
		return fileToWriteTo;
	}

	public void setFileToWriteTo(File fileToWriteTo) {
		this.fileToWriteTo = fileToWriteTo;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

}
