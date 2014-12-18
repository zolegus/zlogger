package com.algodefu.zlogger;

/**
 * @author oleg.zherebkin
 */
public interface ObjectFormatter<T> {

	/**
	 * <b>Note</b>: do not invoke <code>commit()</code> within this method.
	 *
	 * @param obj
	 * @param entry
	 */
	void append(T obj, ZLogEntry entry);

}
