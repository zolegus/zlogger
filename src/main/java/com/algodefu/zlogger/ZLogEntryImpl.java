package com.algodefu.zlogger;

import net.openhft.chronicle.ExcerptAppender;

/**
 * @author oleg.zherebkin
 */
public class ZLogEntryImpl implements ZLogEntry {
    private ExcerptAppender excerptAppender;

    public ZLogEntryImpl(ExcerptAppender excerptAppender) {
        this.excerptAppender = excerptAppender;
    }

    @Override
    public ZLogEntryImpl append(String value) {
        excerptAppender.append(value);
        return this;
    }

    @Override
    public ZLogEntry append(char c) {
        excerptAppender.append(c);
        return this;
    }

    @Override
    public ZLogEntry append(CharSequence csq) {
        excerptAppender.append(csq);
        return this;
    }

    @Override
    public ZLogEntry append(CharSequence csq, int start, int end) {
        return null;
    }

    @Override
    public ZLogEntry append(boolean b) {
        excerptAppender.append(b);
        return this;
    }

    @Override
    public ZLogEntry append(int i) {
        excerptAppender.append(i);
        return this;
    }

    @Override
    public ZLogEntry append(long i) {
        excerptAppender.append(i);
        return this;
    }

    @Override
    public ZLogEntry append(double i) {
        excerptAppender.append(i);
        return this;
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
    public void appendLast(Object o) {

    }

    public void commit() {
        excerptAppender.finish();
    }
}
