package com.algodefu.zlogger;

/**
 * @author oleg.zherebkin
 */
public interface LoggerService {

	LogLevel getLevel();

	ZLogEntry log(final LogLevel level, final String categoryName, final long appenderMask);

	void entryFlushed(final LocalLogEntry localEntry);

	void stop();

	ZLogger[] lookupLoggers(String name);
}
