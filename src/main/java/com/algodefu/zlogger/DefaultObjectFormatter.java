package com.algodefu.zlogger;

/**
 * @author oleg.zherebkin
 */
public final class DefaultObjectFormatter implements ObjectFormatter<Object> {

	public static final ObjectFormatter<Object> DEFAULT_OBJECT_FORMATTER =
		new DefaultObjectFormatter();

	private DefaultObjectFormatter() {
	}

	@Override
	public void append(Object obj, ZLogEntry entry) {
		if (obj != null){
			entry.append(obj.toString());
		} else {
			entry.append('n').append('u').append('l').append('l');
		}
	}

	@Override
	public String toString() {
		return "DefaultObjectFormatter";
	}
}
