package com.algodefu.zlogger.alternative;

/**
 * @author oleg.zherebkin
 */
public interface Loggable {

	void appendTo(final ZLogEntry entry);
}
