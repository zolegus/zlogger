package com.algodefu.zlogger.alternative;

/**
 * @author oleg.zherebkin
 */
public interface ZLog {

	boolean isTraceEnabled();

	ZLogEntry trace();

//	FormattedZLogEntry trace(final String pattern);

	boolean isDebugEnabled();

	ZLogEntry debug();

//	FormattedZLogEntry debug(final String pattern);

	boolean isInfoEnabled();

	ZLogEntry info();

//	FormattedZLogEntry info(final String pattern);

	boolean isWarnEnabled();

	ZLogEntry warn();

//	FormattedZLogEntry warn(final String pattern);

	boolean isErrorEnabled();

	ZLogEntry error();

//	FormattedZLogEntry error(final String pattern);

	boolean isFatalEnabled();

	ZLogEntry fatal();

//	FormattedZLogEntry fatal(final String pattern);
}
