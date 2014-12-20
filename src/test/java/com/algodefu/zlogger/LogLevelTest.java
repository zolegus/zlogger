package com.algodefu.zlogger;

import junit.framework.TestCase;
import org.junit.Test;

public class LogLevelTest extends TestCase {

    @Test
    public void testIsHigherOrEqualTo() throws Exception {
        assertTrue(ZLogLevel.ERROR.isHigherOrEqualTo(ZLogLevel.TRACE));
        assertTrue(ZLogLevel.INFO.isHigherOrEqualTo(ZLogLevel.INFO));
        assertFalse(ZLogLevel.DEBUG.isHigherOrEqualTo(ZLogLevel.WARN));
    }
}