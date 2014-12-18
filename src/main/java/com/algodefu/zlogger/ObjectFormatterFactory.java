package com.algodefu.zlogger;

/**
 * @author oleg.zherebkin
 */
public interface ObjectFormatterFactory {

	ObjectFormatter getObjectFormatter(final Object obj);
}
