package com.algodefu.zlogger;

/**
 * @author oleg.zherebkin
 */
public interface ZLogEntry extends Appendable {

	/**
	 * appends a single char
	 * @return a reference to this object.
	 */
	@Override
	ZLogEntry append(char c);

	@Override
	ZLogEntry append(CharSequence csq);

	@Override
	ZLogEntry append(CharSequence csq, int start, int end);

	ZLogEntry append(boolean b);

	ZLogEntry append(int i);

	ZLogEntry append(long i);

	ZLogEntry append(double i);

	ZLogEntry append(double i, int precision);

	/**
	 * append heterogeneous items (items of the same class) of an array
	 *
	 * @param array
	 * @param separator
	 * @return
	 */
	<T> ZLogEntry append(T[] array, String separator);

	/**
	 * append heterogeneous items (items of the same class) of an iterator
	 * <b>Note</b>: leads to garbage footprint
	 *
	 * @param iterable
	 * @param separator
	 * @return
	 */
	<T> ZLogEntry append(Iterable<T> iterable, String separator);

	ZLogEntry append(Throwable e);

	ZLogEntry append(Loggable loggable);

	/**
	 * appends an object using {@link ObjectFormatter} if it is available for object's class.
	 * Otherwise <code>toString()</code> method will be used. It leads to garbage footprint !
	 *
	 * @param o
	 * @return
	 */
	ZLogEntry append(Object o);

	/**
	 * appends last a single char
	 * @param c char to add
	 * @return a reference to this object.
	 */
	void appendLast(char c);

	/**
	 * appends last char sequence
	 * @param csq char sequence to add
	 * @return a reference to this object.
	 */
	void appendLast(CharSequence csq);

	void appendLast(CharSequence csq, int start, int end);

	void appendLast(boolean b);

	void appendLast(int i);

	void appendLast(long i);

	void appendLast(double i);

	void appendLast(double i, int precision);

	/**
	 * append last heterogeneous items (items of the same class) of an array
	 *
	 * @param array
	 * @param separator
	 */
	<T> void appendLast(T[] array, String separator);

	/**
	 * append heterogeneous items (items of the same class) of an iterator
	 * <b>Note</b>: leads to garbage footprint
	 *
	 * @param iterable
	 * @param separator
	 * @return
	 */
	<T> void appendLast(Iterable<T> iterable, String separator);

	void appendLast(Throwable e);

	void appendLast(Loggable loggable);

	/**
	 * appends last an object using {@link ObjectFormatter} if it is available for object's class.
	 * Otherwise <code>toString()</code> method will be used. It leads to garbage footprint !
	 *
	 * @param o
	 * @return
	 */
	void appendLast(Object o);

	/**
	 * commit an entry
	 */
	void commit();

}
