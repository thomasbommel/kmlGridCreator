package kmlGridCreator.view;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

import kmlGridCreator.model.MapDataModel;

public class MainApplication {
	private String applicationName;

	public static final DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.GERMAN);
	private static final DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

	private MainApplication(String name) {
		this.applicationName = name;
		symbols.setGroupingSeparator(' ');
		formatter.setDecimalFormatSymbols(symbols);
	}

	public String getTitle() {
		return this.applicationName;
	}

	public static String formatForConsole(double number) {
		return formatter.format(number);
	}

	public static void main(String[] args) {

		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Date d = dateformat.parse("2017.05.20");

			if (new Date(System.currentTimeMillis()).getTime() > d.getTime()) {
				JOptionPane.showMessageDialog(null, "version abgelaufen");
				System.exit(0);
			}

		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}

		MainApplication app = new MainApplication("KML GENERATOR");
		new GuiView(app, new MapDataModel());
	}

}
