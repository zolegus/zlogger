package com.algodefu.zlogger;

import com.algodefu.zlogger.reader.ChronicleLogReader;
import junit.framework.TestCase;

import static com.algodefu.zlogger.util.FileCommon.removePath;

public class ZLoggerSingleTest extends TestCase {

    public static final String BASE_PATH = "./testlogs";

    public void setUp() throws Exception {
        LoggerConfiguration loggerConfiguration = new LoggerConfiguration();
        loggerConfiguration.setBasePath(BASE_PATH);
        loggerConfiguration.setLevel(ZLogLevel.TRACE);
        ZLoggerFactory.init(loggerConfiguration);
    }

    public void tearDown() throws Exception {
        ZLoggerFactory.stop();
        if (removePath(BASE_PATH) > 0)
            System.out.printf("Files %s removed",BASE_PATH);
    }

    public void testZLoggerFactorySingleMessage() throws Exception {
        final ZLogger logger = ZLoggerFactory.getLogger(ZLoggerSingleTest.class);
        for (int i = 1; i < 3; i++)
            logger.info().append("Info test ").append(i).append(" message, pi=").append(3.14*i).commit();
        logger.error().append("Error message").commit();
        logger.warn().append("Warn message").commit();
        logger.debug().append("Debug message").commit();
        logger.trace().append("Trace message").commit();
        ChronicleLogReader reader = new ChronicleLogReader(BASE_PATH);
        System.out.println(reader.printToString());
    }
}