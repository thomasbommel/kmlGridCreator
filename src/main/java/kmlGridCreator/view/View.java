package main.java.kmlGridCreator.view;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.kmlGridCreator.model.MapDataModel;

public abstract class View {

	private MapDataModel model;

	public View() {
		this.model = new MapDataModel();
		model.setView(this);
	}

	protected abstract File selectInputFile();

	protected abstract File selectOutputFile(FileNameExtensionFilter extensionFilter);

	public abstract void printToViewConsole(String msg);

	public abstract void setViewConsoleText(String msg);

	public abstract String getCurrentText();
	
	public abstract boolean addPointsToKmlEnabled();

	public MapDataModel getModel() {
		return model;
	}

	public String formatForConsole(double number) {
		return getFormatter().format(number);
	}

	private DecimalFormat getFormatter() {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.GERMAN);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);
		return formatter;
	}



}
