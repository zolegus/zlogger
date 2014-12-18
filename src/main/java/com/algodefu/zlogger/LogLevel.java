package com.algodefu.zlogger;

public enum LogLevel {

	TRACE, DEBUG, INFO, WARN, ERROR, FATAL;

	public static LogLevel[] values = values();

	public boolean lessThan(final LogLevel level){
		return this.ordinal() < level.ordinal();
	}

	public boolean greaterThan(final LogLevel level){
		return this.ordinal() > level.ordinal();
	}
}
