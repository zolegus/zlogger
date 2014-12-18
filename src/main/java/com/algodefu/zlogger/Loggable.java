package com.algodefu.zlogger;

/**
 * @author oleg.zherebkin
 */
public interface Loggable {

	void appendTo(final ZLogEntry entry);
}
