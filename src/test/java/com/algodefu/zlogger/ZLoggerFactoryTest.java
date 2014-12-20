package com.algodefu.zlogger;

import junit.framework.TestCase;

public class ZLoggerFactoryTest extends TestCase {

    public void setUp() throws Exception {
        LoggerConfiguration loggerConfiguration = new LoggerConfiguration();
        loggerConfiguration.setBasePath("./logs");
        loggerConfiguration.setLevel(ZLogLevel.INFO);
        ZLoggerFactory.init(loggerConfiguration);
    }

    public void tearDown() throws Exception {
        ZLoggerFactory.stop();
    }

    public void testZLoggerFactorySingle() throws Exception {
        final ZLogger logger = ZLoggerFactory.getLogger(ZLoggerFactoryTest.class);
        logger.info().append("First test message!").commit();
    }
}