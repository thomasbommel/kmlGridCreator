package main.java.kmlGridCreator.view;

import main.java.kmlGridCreator.exceptions.OverlappingPolyStylesException;

class MainApplication {
	private String applicationName;

	private MainApplication(String name) {
		this.applicationName = name;

	}

	public String getTitle() {
		return this.applicationName;
	}

	public static void main(String[] args) throws OverlappingPolyStylesException {
		MainApplication app = new MainApplication("KML GENERATOR");
		new GuiView(app);
	}

}
