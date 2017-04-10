package kmlGridCreator.view;

import java.io.File;

import kmlGridCreator.model.MapDataModel;

public abstract class View {

	private MapDataModel model;

	public View(MapDataModel model) {
		this.model = model;
		model.setView(this);
	}

	protected abstract File selectInputFile();

	protected abstract File selectOutputFile();

	public abstract void printToViewConsole(String msg);

	public abstract void setViewConsoleText(String msg);

	public abstract String getCurrentText();

	public MapDataModel getModel() {
		return model;
	}

}
