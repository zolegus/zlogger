package com.algodefu.zlogger.alternative;

import java.nio.Buffer;

/**
 * @author oleg.zherebkin
 */
public interface LocalLogEntry extends ZLogEntry {

	LogLevel getLogLevel();

	void setLogLevel(final LogLevel logLevel);

	void setCategoryName(String categoryName);

	void setPattern(final String pattern);

	void setAppenderMask(final long mask);

	String getCategoryName();

	String getThreadName();

	long getAppenderMask();

	<T extends Buffer> void copyTo(T buffer);

	void clear();

	boolean isCommited();

	void setCommited(boolean commited);

	Throwable getError();

	String stringValue();

}
