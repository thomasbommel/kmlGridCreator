package kmlGridCreator.view;

public class MainApplication {
	private String applicationName;

	private static View view;

	private MainApplication(String name) {
		this.applicationName = name;
	}

	public String getTitle() {
		return this.applicationName;
	}

	public static void main(String[] args) {
		MainApplication app = new MainApplication("KML GENERATOR");
		view = new GuiView(app);

	
	}

}
