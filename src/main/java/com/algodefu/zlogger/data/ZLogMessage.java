package com.algodefu.zlogger.data;

import com.algodefu.zlogger.ZLogLevel;

import java.text.SimpleDateFormat;

/**
 * @author oleg.zherebkin
 */
public class ZLogMessage {
    private final String DLM = " | ";
    private final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private long timestamp;
    private ZLogLevel logLevel;
    private String threadName;
    private String className;
    private String textMessage;

    public ZLogMessage(long timestamp, ZLogLevel logLevel, String threadName, String className, String textMessage) {
        this.timestamp = timestamp;
        this.logLevel = logLevel;
        this.threadName = threadName;
        this.className = className;
        this.textMessage = textMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ZLogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(ZLogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(timestamp)).append(DLM)
                .append(logLevel.toString()).append(DLM)
                .append(threadName).append(DLM)
                .append(className).append(DLM)
                .append(textMessage);
        return stringBuilder.toString();
    }
}
