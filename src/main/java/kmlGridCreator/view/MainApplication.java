package main.java.kmlGridCreator.view;

public class MainApplication {
	private String applicationName;

	public MainApplication(String name) {
		this.applicationName = name;

	}

	public String getTitle() {
		return this.applicationName;
	}

	public static void main(String[] args) {
		MainApplication app = new MainApplication("KML GENERATOR");
		new GuiView(app);
	}

}
