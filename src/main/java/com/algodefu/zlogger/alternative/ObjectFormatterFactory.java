package com.algodefu.zlogger.alternative;

/**
 * @author oleg.zherebkin
 */
public interface ObjectFormatterFactory {

	ObjectFormatter getObjectFormatter(final Object obj);
}
