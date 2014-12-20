package com.algodefu.zlogger;

/**
 * @author oleg.zherebkin
 */
public class NullLogEntry implements ZLogEntry {

    public static final NullLogEntry INSTANCE = new NullLogEntry();

    public NullLogEntry() {
    }

    @Override
    public ZLogEntry append(final char c) {
        return this;
    }

    @Override
    public ZLogEntry append(final CharSequence csq) {
        return this;
    }

    @Override
    public ZLogEntry append(final CharSequence csq, final int start, final int end) {
        return this;
    }

    @Override
    public ZLogEntry append(String string) {
        return this;
    }

    @Override
    public ZLogEntry append(final boolean b) {
        return this;
    }

    @Override
    public ZLogEntry append(final int i) {
        return this;
    }

    @Override
    public ZLogEntry append(final long i) {
        return this;
    }

    @Override
    public ZLogEntry append(final double i) {
        return this;
    }

    @Override
    public ZLogEntry append(final double i, final int precision) {
        return this;
    }

    @Override
    public <T> ZLogEntry append(T[] array, String separator) {
        return this;
    }

    @Override
    public <T> ZLogEntry append(Iterable<T> iterable, String separator) {
        return this;
    }

    @Override
    public ZLogEntry append(Throwable e) {
        return this;
    }

    @Override
    public ZLogEntry append(Object o) {
        return this;
    }

    @Override
    public void appendLast(char c) {
        // nothing
    }

    @Override
    public void appendLast(CharSequence csq) {
        // nothing
    }

    @Override
    public void appendLast(CharSequence csq, int start, int end) {
        // nothing
    }

    @Override
    public void appendLast(boolean b) {
        // nothing
    }

    @Override
    public void appendLast(int i) {
        // nothing
    }

    @Override
    public void appendLast(long i) {
        // nothing
    }

    @Override
    public void appendLast(double i) {
        // nothing
    }

    @Override
    public void appendLast(double i, int precision) {
        // nothing
    }

    @Override
    public <T> void appendLast(T[] array, String separator) {
        // nothing
    }

    @Override
    public <T> void appendLast(Iterable<T> iterable, String separator) {
        // nothing
    }

    @Override
    public void appendLast(Throwable e) {
        // nothing
    }

    @Override
    public void appendLast(Object o) {
        // nothing
    }

    @Override
    public void commit() {
        // nothing
    }
}
