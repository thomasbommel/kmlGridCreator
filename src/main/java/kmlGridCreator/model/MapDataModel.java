package main.java.kmlGridCreator.model;

import java.io.File;
import java.io.IOException;

import main.java.kmlGridCreator.exceptions.NoPolyStyleCoveringThisPointCountException;
import main.java.kmlGridCreator.exceptions.OverlappingPolyStylesException;
import main.java.kmlGridCreator.utils.CSVCreatorUtils;
import main.java.kmlGridCreator.utils.MyKmlFactory;
import main.java.kmlGridCreator.utils.TxtUtil;
import main.java.kmlGridCreator.view.View;

public class MapDataModel {

	private MyMap map;
	private File fileToReadFrom, fileToWriteTo, fileToWriteCSVTo;
	private View view;

	public void startCreation() throws IOException {
		view.printToViewConsole("Die Generierung des kml Files wurde gestartet.");

		map = new MyMap(TxtUtil.getPointsFromTxt(fileToReadFrom), 1000, view);
		view.printToViewConsole("--- PUNKTE WERDEN ZUGEORDNET");
		map.addPointsToTheAreas();
		view.printToViewConsole(
				"--- Die Zuordnung ist beendet ---");

		MyKmlFactory kml = null;
		try {
			kml = new MyKmlFactory("generatedKmlDocument");
		} catch (OverlappingPolyStylesException e) {
			e.printStackTrace();
			view.printToViewConsole("FEHLER:"+e.getMessage());///FIXME
		}

		view.printToViewConsole(" Datei " + fileToWriteTo.getName() + " wird erstellt.");
		for (MyBoundingArea area : map.getBoundingAreas()) {
			try {
				kml.addBoundingArea(area);
			} catch (OverlappingPolyStylesException e) {
				e.printStackTrace();
			} catch (NoPolyStyleCoveringThisPointCountException e) {
				e.printStackTrace();
				view.printToViewConsole(e.getMessage());
			}
			if(view.addPointsToKmlEnabled()){
				kml.addPointsToKml(area.getPoints()); 
			}
		}
		kml.saveKmlFile(fileToWriteTo);
		view.printToViewConsole(" Datei " + fileToWriteTo.getName() + " erstellt und gespeichert.");

		if(fileToWriteCSVTo != null){
			CSVCreatorUtils.savePointCountToBoundingAreaCountMapToCSV(map.getPointCountToBoundingAreaCountMap(), fileToWriteCSVTo);
			//view.printToViewConsole("csv Datei: '"+fileToWriteCSVTo.getName()+"' erstellt.");
		}
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
	
	public void setFileToWriteCSVTo(File fileToWriteCSVTo) {
		this.fileToWriteCSVTo = fileToWriteCSVTo;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

}
