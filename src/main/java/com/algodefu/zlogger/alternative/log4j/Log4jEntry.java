package com.algodefu.zlogger.alternative.log4j;

import com.algodefu.zlogger.alternative.*;
import com.algodefu.zlogger.alternative.formatter.BufferFormatter;
import org.apache.commons.logging.Log;

import java.util.Iterator;

import static com.algodefu.zlogger.alternative.util.StackTraceUtils.*;

/**
 * @author oleg.zherebkin
 */
public class Log4jEntry implements ZLogEntry {

	// 2k
	private static final int DEFAULT_BUFFER_SIZE = 1 << 11;

	private final Log log;
	private final StringBuilder builder;

	private LogLevel logLevel;

	private final ObjectFormatterFactory formatterFactory = new DefaultObjectFormatterFactory();

	private String pattern;
	private int pPos;

	public Log4jEntry(Log log) {
		this.log = log;
		this.builder = new StringBuilder(DEFAULT_BUFFER_SIZE);
	}

	public void setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
	}

	public void setPattern(String pattern) {
		if (pattern == null){
			throw new IllegalArgumentException("expected not null pattern.");
		}
		this.pattern = pattern;
		this.pPos = 0;
		appendNextPatternChank();
	}

	protected void appendNextPatternChank(){
		final int len = pattern.length();
		for(; pPos < len; pPos++){
			final char ch = pattern.charAt(pPos);
			if (ch == '%' && (pPos + 1) < len){
				if (pattern.charAt(pPos + 1) != '%') break;
				pPos++;
			}
			append(ch);
		}
		if (this.pPos == len){
			commit();
		}
	}

	protected void checkPlaceholder(){
		if (pattern == null){
			throw new IllegalStateException("Entry has been commited.");
		}
		if (pPos + 2 >= pattern.length()){
			throw new IllegalStateException("Illegal pattern '" + pattern + "' or position " + pPos);
		}
		final char ch1 = pattern.charAt(pPos);
		final char ch2 = pattern.charAt(pPos + 1);
		if (ch1 != '%' || ch2 != 's'){
			throw new IllegalArgumentException("Illegal pattern placeholder '" + ch1 + "" + ch2 + " at " + pPos);
		}
		pPos += 2;
	}

	protected void checkAndCommit(){
		if (pattern == null) return;
		if (pPos + 1 != pattern.length()){
			throw new IllegalStateException("The pattern has not been finished. More parameters are required.");
		}
		commit();
	}

	public void reset(){
		builder.setLength(0);
	}

	@Override
	public ZLogEntry append(char c) {
		this.builder.append(c);
		return this;
	}

	@Override
	public ZLogEntry append(CharSequence csq) {
		this.builder.append(csq);
		return this;
	}

	@Override
	public ZLogEntry append(CharSequence csq, int start, int end) {
		this.builder.append(csq, start, end);
		return this;
	}

	@Override
	public ZLogEntry append(boolean b) {
		this.builder.append(b);
		return this;
	}

	@Override
	public ZLogEntry append(int i) {
		this.builder.append(i);
		return this;
	}

	@Override
	public ZLogEntry append(long i) {
		this.builder.append(i);
		return this;
	}

	@Override
	public ZLogEntry append(double i) {
		this.builder.append(i);
		return this;
	}

	@Override
	public ZLogEntry append(double i, int precision) {
		long x = (long)i;
		this.builder.append(x);
		this.builder.append('.');
		x = (long)((i -x) * (precision > 0 ? BufferFormatter.LONG_SIZE_TABLE[precision - 1] : 1));
		this.builder.append(x < 0 ? -x : x);
		return this;
	}

	@Override
	public <T> ZLogEntry append(T[] array, String separator) {
		if (array == null){
			append('n').append('u').append('l').append('l');
		} else {
			try {
				append('[');
				ObjectFormatter formatter = null;
				for(int i = 0; i < array.length; i++){
					if (i > 0){
						append(separator);
					}
					final T obj = array[i];
					if (obj != null){
						if (formatter == null) {
							formatter = formatterFactory.getObjectFormatter(obj);
						}
						formatter.append(obj, this);
					} else {
						append('n').append('u').append('l').append('l');
					}
				}
				append(']');
			} catch (Throwable e){
				//error("append(Object o)", e);
			}
			return this;
		}
		return this;
	}

	@Override
	public <T> ZLogEntry append(Iterable<T> iterable, String separator) {
		if (iterable == null){
			append('n').append('u').append('l').append('l');
		} else {
			try {
				append('[');
				ObjectFormatter formatter = null;
				for(final Iterator<T> it = iterable.iterator();it.hasNext();){
					final T obj = it.next();
					if (obj != null){
						if (formatter == null) {
							formatter = formatterFactory.getObjectFormatter(obj);
						}
						formatter.append(obj, this);
					} else {
						append('n').append('u').append('l').append('l');
					}
					if (it.hasNext()){
						append(separator);
					}
				}
				append(']');
			} catch (Throwable e){
				//error("append(Object o)", e);
			}
		}
		return this;
	}

	@Override
	public ZLogEntry append(Throwable e) {
		if (e != null){
			try {
				append(e.getClass().getName());
				String message = e.getLocalizedMessage();
				if (message != null){
					append(": ").append(message);
				}
				append('\n');
				final StackTraceElement[] trace = e.getStackTrace();
				for (int i = 0; i < trace.length; i++) {
					append("\tat ").append(trace[i].getClassName()).append('.').
						append(trace[i].getMethodName());
					append('(');
					if (trace[i].isNativeMethod()){
						append("native");
					} else {
						final String fileName = trace[i].getFileName();
						final int lineNumber = trace[i].getLineNumber();
						if (fileName != null){
							append(fileName);
							if (lineNumber >= 0){
								append(':').append(lineNumber);
							}

							final Class clazz =
								loadClass(trace[i].getClassName());
							if (clazz != null){
								append('[').append(getCodeLocation(clazz));
								final String implVersion = getImplementationVersion(clazz);
								if (implVersion != null){
									append(':').append(implVersion);
								}
								append(']');
							}

						} else {
							append("unknown");
						}
					}
					append(')').append('\n');
				}
			} catch (Throwable t){
				//
				t.printStackTrace();
			}
		}
		return this;
	}

	@Override
	public ZLogEntry append(Loggable loggable) {
		if (loggable != null){
			loggable.appendTo(this);
		} else {
			append('n').append('u').append('l').append('l');
		}
		return this;
	}

	@Override
	public ZLogEntry append(Object o) {
		try {
			if (o != null){
				final ObjectFormatter formatter = formatterFactory.getObjectFormatter(o);
				formatter.append(o, this);
			} else {
				append('n').append('u').append('l').append('l');
			}
		} catch (Throwable e){
			//error("append(Object o)", e);
		}
		return this;
	}

	@Override
	public void appendLast(final char c) {
		append(c);
		commit();
	}

	@Override
	public void appendLast(final CharSequence csq) {
		append(csq);
		commit();
	}

	@Override
	public void appendLast(final CharSequence csq, final int start, final int end) {
		append(csq, start, end);
		commit();
	}

	@Override
	public void appendLast(final boolean b) {
		append(b);
		commit();
	}

	@Override
	public void appendLast(final int i) {
		append(i);
		commit();
	}

	@Override
	public void appendLast(final long i) {
		append(i);
		commit();
	}

	@Override
	public void appendLast(final double i) {
		append(i);
		commit();
	}

	@Override
	public void appendLast(final double i, final int precision) {
		append(i, precision);
		commit();
	}

	@Override
	public <T> void appendLast(T[] array, String separator) {
		append(array, separator);
		commit();
	}

	@Override
	public <T> void appendLast(Iterable<T> iterable, String separator) {
		append(iterable, separator);
		commit();
	}

	@Override
	public void appendLast(Throwable e) {
		append(e);
		commit();
	}

	@Override
	public void appendLast(Loggable loggable) {
		append(loggable);
		commit();
	}

	@Override
	public void appendLast(Object o) {
		append(o);
		commit();
	}

	@Override
	public void commit() {
		switch (logLevel) {
		case TRACE:
			if (log.isTraceEnabled()) {
				log.trace(builder.toString());
			}
			break;
		case DEBUG:
			if (log.isDebugEnabled()) {
				log.debug(builder.toString());
			}
			break;
		case INFO:
			if (log.isInfoEnabled()) {
				log.info(builder.toString());
			}
			break;
		case WARN:
			if (log.isWarnEnabled()) {
				log.warn(builder.toString());
			}
			break;
		case ERROR:
			if (log.isErrorEnabled()) {
				log.error(builder.toString());
			}
			break;
		case FATAL:
			if (log.isFatalEnabled()) {
				log.fatal(builder.toString());
			}
			break;
		}
		if (builder.length() > DEFAULT_BUFFER_SIZE){
			builder.setLength(DEFAULT_BUFFER_SIZE);
			builder.trimToSize();
		}
		builder.setLength(0);
		pattern = null;
	}
}
