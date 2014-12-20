package com.algodefu.zlogger.alternative.log4j;

import com.algodefu.zlogger.alternative.*;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author oleg.zherebkin
 */
public class Log4jLoggerServiceImpl implements LoggerService {

	private final ThreadLocal<Map<String, Log4jEntry>> entries;
	private final LogLevel level;

	public Log4jLoggerServiceImpl() {
		this(LogLevel.DEBUG);
	}

	public Log4jLoggerServiceImpl(final LogLevel level) {
		this.level = level;
		this.entries = new ThreadLocal<Map<String, Log4jEntry>>(){
			@Override
			protected Map<String, Log4jEntry> initialValue() {
				return new HashMap<String, Log4jEntry>();
			}
		};
		BasicConfigurator.configure();
	}

	@Override
	public LogLevel getLevel() {
		return level;
	}

	@Override
	public ZLogEntry log(LogLevel level, String categoryName, final long appenderMask) {
		final Map<String, Log4jEntry> map = entries.get();
		Log4jEntry entry = map.get(categoryName);
		if (entry == null){
			entry = new Log4jEntry(LogFactory.getLog(categoryName));
			map.put(categoryName, entry);
		}
		entry.setLogLevel(level);
		entry.reset();
		return entry;
	}


	@Override
	public ZLogger[] lookupLoggers(String name) {
		return ZLogger.EMPTY;
	}

	@Override
	public void entryFlushed(LocalLogEntry localEntry) {
		// nothing
	}

	@Override
	public void stop() {
		LogManager.shutdown();
	}

}
