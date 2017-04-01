package kmlGridCreator.model;

import java.io.File;

public class MapDataModel {

	private MyMap map;
	private File fileToReadFrom, fileToWriteTo;

	public void startCreation() {
		// TODO
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

}
