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
	private MyKmlFactory kml;
	
	public MapDataModel(MyKmlFactory kml){
		this.kml= kml;
	}
	

	public void startCreation() throws IOException, OverlappingPolyStylesException {
		kml.createPolyStylesInDocument();
		view.printToViewConsole("Die Generierung des kml Files wurde gestartet.");

		map = new MyMap(TxtUtil.getPointsFromTxt(fileToReadFrom), view.getSelectedGridSizeInMeter(), view);
		view.printToViewConsole("--- PUNKTE WERDEN ZUGEORDNET");
		map.addPointsToTheAreas();
		view.printToViewConsole(
				"--- Die Zuordnung ist beendet ---");

		view.printToViewConsole("Datei " + fileToWriteTo.getName() + " wird erstellt...");
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
				if(view.useIconsInsteadOfPinsForPointsInKML()){
					kml.addPointsToKmlWithIcon(area.getPoints()); 
				}else{
					kml.addPointsToKmlWithPin(area.getPoints()); 
				}
			}
		}
		kml.saveKmlFile(fileToWriteTo);
		view.printToViewConsole("Datei " + fileToWriteTo.getName() + " erstellt und gespeichert.");

		if(fileToWriteCSVTo != null){
			CSVCreatorUtils.savePointCountToBoundingAreaCountMapToCSV(map.getPointCountToBoundingAreaCountMap(), fileToWriteCSVTo);
			view.printToViewConsole("CSV Datei: "+fileToWriteCSVTo.getName()+" erstellt und gespeichert.");
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
	
	public File getFileTroWriteCSVTo(){
		return this.fileToWriteCSVTo;
	}
	
	public View getView() {
		return view;
	}
	
	public void setView(View view) {
		this.view = view;
	}

	public MyKmlFactory getKmlFactory(){
		return this.kml;
	}

	
	

}
