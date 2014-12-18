package com.algodefu.zlogger.helpers;

import com.algodefu.zlogger.LogLevel;

import static com.algodefu.zlogger.helpers.OptionConverter.getBooleanProperty;
import static com.algodefu.zlogger.helpers.OptionConverter.getStringProperty;

/**
 * This class used to output log statements from within the zlogger package.
 *
 * <p>
 * Log4j components cannot make zlogger logging calls. However, it is sometimes
 * useful for the user to learn about what zlogger is doing. You can enable zlogger
 * internal logging by defining the <b>zlogger.configDebug</b> variable.
 *
 * <p>
 * All zlogger internal debug calls go to <code>System.out</code> where as
 * internal error messages are sent to <code>System.err</code>. All internal
 * messages are prepended with the string "zlogger: ".
 *
 * @author oleg.zherebkin
 */
public class LogLog {

	protected static LogLevel internalLogLevel = LogLevel.INFO;

	static {
		final String value = getStringProperty("zlogger.internalLogLevel", "INFO");
		try {
			internalLogLevel = LogLevel.valueOf(value);
		} catch (final Throwable e) {
			System.err.println("Unable to parse internal log level of '" + value + "'");
		}
	}

	/**
	 * In quietMode not even errors generate any output.
	 */
	private static boolean	  quietMode		= getBooleanProperty("zlogger.internalQuietMode", false);

	private static final String PREFIX		   = "zlogger: ";
	private static final String ERR_PREFIX	   = "zlogger:ERROR ";
	private static final String WARN_PREFIX	   = "zlogger:WARN ";

	private static boolean isLoggable(final LogLevel level){
		return !quietMode && isLoggable0(level);
	}

	public static boolean isLoggable0(final LogLevel level) {
		return !internalLogLevel.greaterThan(level);
	}

	/**
	 * This method is used to output zlogger internal debug statements. Output
	 * goes to <code>System.out</code>.
	 */
	public static void debug(String msg) {
		if (isLoggable(LogLevel.DEBUG)) {
			System.out.println(PREFIX + msg);
		}
	}

	/**
	 * This method is used to output zlogger internal info statements. Output
	 * goes to <code>System.out</code>.
	 */
	public static void info(String msg) {
		if (isLoggable(LogLevel.INFO)) {
			System.out.println(PREFIX + msg);
		}
	}

	/**
	 * This method is used to output zlogger internal debug statements. Output
	 * goes to <code>System.out</code>.
	 */
	public static void debug(String msg, Throwable t) {
		if (isLoggable(LogLevel.DEBUG)) {
			System.out.println(PREFIX + msg);
			if (t != null)
				t.printStackTrace(System.out);
		}
	}

	/**
	 * This method is used to output zlogger internal error statements. There is
	 * no way to disable error statements. Output goes to
	 * <code>System.err</code>.
	 */
	public static void error(String msg) {
		if (isLoggable0(LogLevel.ERROR)) {
			System.err.println(ERR_PREFIX + msg);
		}
	}

	/**
	 * This method is used to output zlogger internal error statements. There is
	 * no way to disable error statements. Output goes to
	 * <code>System.err</code>.
	 */
	public static void error(String msg, Throwable t) {
		if (isLoggable0(LogLevel.ERROR)) {
			System.err.println(ERR_PREFIX + msg);
			if (t != null) {
				t.printStackTrace();
			}
		}
	}

	/**
	 * In quite mode no LogLog generates strictly no output, not even for
	 * errors.
	 *
	 * @param quietMode
	 *			A true for not
	 */
	public static void setQuietMode(boolean quietMode) {
		LogLog.quietMode = quietMode;
	}

	/**
	 * This method is used to output zlogger internal warning statements. There is
	 * no way to disable warning statements. Output goes to
	 * <code>System.err</code>.
	 */
	public static void warn(String msg) {
		if (isLoggable0(LogLevel.WARN)) {
			System.err.println(WARN_PREFIX + msg);
		}
	}

	/**
	 * This method is used to output zlogger internal warnings. There is no way to
	 * disable warning statements. Output goes to <code>System.err</code>.
	 */
	public static void warn(String msg, Throwable t) {
		if (isLoggable0(LogLevel.WARN)) {
			System.err.println(WARN_PREFIX + msg);
			if (t != null) {
				t.printStackTrace();
			}
		}
	}
}
