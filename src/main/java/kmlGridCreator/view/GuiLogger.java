package main.java.kmlGridCreator.view;

import main.java.kmlGridCreator.model.AbstractLogger;

public class GuiLogger extends AbstractLogger {

	private final GuiView view;

	public GuiLogger(LogLevel activeLogLevel, GuiView view) {
		super(activeLogLevel);
		this.view = view;
	}

	@Override
	public synchronized void printToConsole(String msg) {
		view.getConsoleTextArea().append(msg + "\n");
	}

	@Override
	public synchronized void setConsoleText(String text) {
		view.getConsoleTextArea().setText(text);
	}

}
