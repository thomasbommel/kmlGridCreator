package kmlGridCreator.view;

import kmlGridCreator.model.AbstractLogger;

public class GuiLogger extends AbstractLogger {

	private final GuiView view;

	public GuiLogger(LogLevel activeLogLevel, GuiView view) {
		super(activeLogLevel);
		this.view = view;
	}

	@Override
	public void printToConsole(String msg) {
		view.getConsole().setText(view.getConsole().getText() + msg + "\n");
	}

}
