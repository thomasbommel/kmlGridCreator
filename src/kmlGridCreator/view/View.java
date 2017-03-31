package kmlGridCreator.view;

import java.io.File;

import kmlGridCreator.model.MapDataModel;

public abstract class View {

	private MapDataModel model;

	protected abstract File selectInputFile();

	protected abstract File selectOutputFile();

	protected final void startCreation() {
		this.model.startCreation();
	};

	public abstract void printToConsole(String msg);

}
