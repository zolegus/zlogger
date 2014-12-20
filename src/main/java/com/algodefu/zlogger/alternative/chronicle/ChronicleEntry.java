package com.algodefu.zlogger.alternative.chronicle;

import com.algodefu.zlogger.alternative.LogLevel;
import com.algodefu.zlogger.alternative.Loggable;
import com.algodefu.zlogger.alternative.ZLogEntry;
import net.openhft.chronicle.ExcerptAppender;

/**
 * @author oleg.zherebkin
 */
public class ChronicleEntry implements ZLogEntry {

    private final ExcerptAppender appender;

    public ChronicleEntry(ExcerptAppender appender, LogLevel level) {
        this.appender = appender;
        this.appender.startExcerpt(); // an upper limit to how much space in bytes this message should need.
        this.appender.writeLong(System.currentTimeMillis());
        this.appender.writeChar('M');
    }

    @Override
    public ZLogEntry append(char c) {
        return null;
    }

    @Override
    public ZLogEntry append(CharSequence csq) {
        return null;
    }

    @Override
    public ZLogEntry append(CharSequence csq, int start, int end) {
        return null;
    }

    @Override
    public ZLogEntry append(boolean b) {
        return null;
    }

    @Override
    public ZLogEntry append(int i) {
        return null;
    }

    @Override
    public ZLogEntry append(long i) {
        return null;
    }

    @Override
    public ZLogEntry append(double i) {
        return null;
    }

    @Override
    public ZLogEntry append(double i, int precision) {
        return null;
    }

    @Override
    public <T> ZLogEntry append(T[] array, String separator) {
        return null;
    }

    @Override
    public <T> ZLogEntry append(Iterable<T> iterable, String separator) {
        return null;
    }

    @Override
    public ZLogEntry append(Throwable e) {
        return null;
    }

    @Override
    public ZLogEntry append(Loggable loggable) {
        return null;
    }

    @Override
    public ZLogEntry append(Object o) {
        return null;
    }

    @Override
    public void appendLast(char c) {

    }

    @Override
    public void appendLast(CharSequence csq) {

    }

    @Override
    public void appendLast(CharSequence csq, int start, int end) {

    }

    @Override
    public void appendLast(boolean b) {

    }

    @Override
    public void appendLast(int i) {

    }

    @Override
    public void appendLast(long i) {

    }

    @Override
    public void appendLast(double i) {

    }

    @Override
    public void appendLast(double i, int precision) {

    }

    @Override
    public <T> void appendLast(T[] array, String separator) {

    }

    @Override
    public <T> void appendLast(Iterable<T> iterable, String separator) {

    }

    @Override
    public void appendLast(Throwable e) {

    }

    @Override
    public void appendLast(Loggable loggable) {

    }

    @Override
    public void appendLast(Object o) {

    }

    @Override
    public void commit() {

    }
}
