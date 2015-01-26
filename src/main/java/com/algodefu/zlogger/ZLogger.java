package com.algodefu.zlogger;

import net.openhft.chronicle.ExcerptAppender;

/**
 * @author oleg.zherebkin
 */
public class ZLogger {

    public static final String DEFAULT_DATE_FORMAT = "yyyy.MM.dd-HH:mm:ss.SSS";
    private ExcerptAppender appender;
    private ZLogLevel logLevel;
    private String className;
    private ZLogEntry logEntry;
    private ZLogEntry nullLogEntry = new NullLogEntry();
    private String threadName = Thread.currentThread().getName();

    public ZLogger(ExcerptAppender appender, ZLogLevel logLevel, String className) {
        this.appender = appender;
        this.logLevel = logLevel;
        this.className = className;
        this.logEntry = new ZLogEntryImpl(appender);
    }

    private ZLogEntry stage(ZLogLevel level) {
        if (!level.isHigherOrEqualTo(this.logLevel))
            return this.nullLogEntry;
        appender.startExcerpt();
        // MESSAGE FORMAT [timestamp] [type_id] [thread_id] [className] [textMessage]
        appender.writeLong(System.currentTimeMillis());
        appender.writeInt(level.ordinal());
        appender.writeUTF(threadName);
        appender.writeUTF(className);
        return this.logEntry;
    }

    public ZLogEntry error() {
        return stage(ZLogLevel.ERROR);
    }

    public ZLogEntry warn() {
        return stage(ZLogLevel.WARN);
    }

    public ZLogEntry info() {
        return stage(ZLogLevel.INFO);
    }

    public ZLogEntry debug() {
        return stage(ZLogLevel.DEBUG);
    }

    public ZLogEntry trace() {
        return stage(ZLogLevel.TRACE);
    }
}
