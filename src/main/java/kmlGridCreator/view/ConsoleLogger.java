package main.java.kmlGridCreator.view;

import main.java.kmlGridCreator.model.AbstractLogger;

public class ConsoleLogger extends AbstractLogger {

	public ConsoleLogger(LogLevel activeLogLevel) {
		super(activeLogLevel);
	}

	@Override
	public void printToConsole(String text) {
		System.out.println(text);
	}

	@Override
	public void setConsoleText(String text) {
		System.out.println(text);
	}

}
