package com.algodefu.zlogger.alternative;

/**
 * @author oleg.zherebkin
 */
public class ZLogView implements ZLog {

	private LoggerService loggerService;
	private LogLevel level;
	private volatile boolean valid;
	private final long[] appenderMask = new long[LogLevel.values.length];

	private final NullLogEntry mockLogEntry;

	private final String name;

	public ZLogView(final String name) {
		this.mockLogEntry = NullLogEntry.INSTANCE;
		this.name = name;
	}

	void invalidate(){
		this.loggerService = null;
		this.level = null;
		this.valid = false;
	}

	LoggerService setLoggerService(LoggerService loggerService) {
		this.loggerService = loggerService;

		final ZLogger[] loggers = loggerService != null ? loggerService.lookupLoggers(name) : ZLogger.EMPTY;
		for(int i = 0; i < LogLevel.values.length; i++){
			final LogLevel level = LogLevel.values[i];
			final int ordinal = level.ordinal();
			appenderMask[ordinal] = 0;

			for (final ZLogger zLogger : loggers) {
				final LogLevel loggerLevel = zLogger.getLogLevel();
				if (!loggerLevel.greaterThan(level)) {
					final long m = zLogger.getAppenderMask(level);
					appenderMask[ordinal] |= m;
					if (!zLogger.hasAdditivity()) break;
				}
			}
		}

		this.level = LogLevel.FATAL;
		for (final ZLogger zLogger : loggers) {
			final LogLevel loggerLevel = zLogger.getLogLevel();
			this.level = !this.level.greaterThan(loggerLevel) ? this.level : loggerLevel;
		}

		this.valid = loggerService != null;
		return this.loggerService;
	}
	private boolean hasNecessaryLevel(LogLevel level) {
		return loggerService() != null && !this.level.greaterThan(level);
	}

	private LoggerService loggerService() {
		if (valid) return loggerService;

		// lazy reinit
		return setLoggerService(ZLogFactory.lookupService(name));
 	}

	private ZLogEntry logEntry(final LogLevel logLevel) {
		return hasNecessaryLevel(logLevel) ?
			loggerService.log(logLevel, name, appenderMask[logLevel.ordinal()]) :
			mockLogEntry;
	}

	@Override
	public boolean isTraceEnabled() {
		return hasNecessaryLevel(LogLevel.TRACE);
	}

	@Override
	public ZLogEntry trace() {
		return logEntry(LogLevel.TRACE);
	}

	@Override
	public boolean isDebugEnabled() {
		return hasNecessaryLevel(LogLevel.DEBUG);
	}

	@Override
	public ZLogEntry debug() {
		return logEntry(LogLevel.DEBUG);
	}

	@Override
	public boolean isInfoEnabled() {
		return hasNecessaryLevel(LogLevel.INFO);
	}

	@Override
	public ZLogEntry info() {
		return logEntry(LogLevel.INFO);
	}

	@Override
	public boolean isWarnEnabled() {
		return hasNecessaryLevel(LogLevel.WARN);
	}

	@Override
	public ZLogEntry warn() {
		return logEntry(LogLevel.WARN);
	}

	@Override
	public boolean isErrorEnabled() {
		return hasNecessaryLevel(LogLevel.ERROR);
	}

	@Override
	public ZLogEntry error() {
		return logEntry(LogLevel.ERROR);
	}

	@Override
	public boolean isFatalEnabled() {
		return hasNecessaryLevel(LogLevel.ERROR);
	}

	@Override
	public ZLogEntry fatal() {
		return logEntry(LogLevel.FATAL);
	}
}
