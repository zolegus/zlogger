package com.algodefu.zlogger;

/**
 * @author oleg.zherebkin
 */
public interface ZLogger {

	static final ZLogger[] EMPTY = new ZLogger[0];

	LogLevel getLogLevel();

	String getCategory();

	long getAppenderMask(final LogLevel level);

	boolean hasAdditivity();

}
