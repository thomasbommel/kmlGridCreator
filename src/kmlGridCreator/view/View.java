package kmlGridCreator.view;

import java.io.File;

import kmlGridCreator.model.MapDataModel;

public abstract class View {

	protected MapDataModel model;

	public View(MapDataModel model) {
		this.model = model;
	}

	protected abstract File selectInputFile();

	protected abstract File selectOutputFile();

	public abstract void printToConsole(String msg);

	public MapDataModel getModel() {
		return model;
	}

}
