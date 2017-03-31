package kmlGridCreator.model;

public abstract class AbstractLogger implements printToConsole {

	private LogLevel activeLogLevel;

	public AbstractLogger(LogLevel activeLogLevel) {
		this.activeLogLevel = activeLogLevel;
	}

	private final void log(String message, LogLevel loglevel) {
		if (loglevel.shouldBeLogged(this)) {
			// System.out.println("[" + loglevel.name() + "] " + message);
			message = "[" + loglevel.name() + "] " + message;
			printToConsole(message);
		}
	}

	public final void debug(String msg) {
		log(msg, LogLevel.DEBUG);
	}

	private LogLevel getActiveLogLevel() {
		return this.activeLogLevel;
	}

	public enum LogLevel {
		ERROR(0), WARNING(1), INFO(2), DEBUG(3);
		private int level;

		private LogLevel(int level) {
			this.level = level;
		}

		public int getLevel() {
			return this.level;
		}

		public boolean shouldBeLogged(AbstractLogger log) {
			if (this.level <= log.getActiveLogLevel().getLevel()) {
				return true;
			}
			return false;
		}
	}
}
