package com.algodefu.zlogger;

import com.algodefu.zlogger.reader.ChronicleLogReader;
import junit.framework.TestCase;

import static com.algodefu.zlogger.util.FileCommon.removePath;

public class ZLoggerOneThreadMultiClassTest extends TestCase {

    public static final String BASE_PATH = "./testlogs";

    public void setUp() throws Exception {
        LoggerConfiguration loggerConfiguration = new LoggerConfiguration();
        loggerConfiguration.setBasePath(BASE_PATH);
        loggerConfiguration.setLevel(ZLogLevel.INFO);
        ZLoggerFactory.init(loggerConfiguration);
    }

    public void tearDown() throws Exception {
        ZLoggerFactory.stop();
        if (removePath(BASE_PATH) > 0)
            System.out.printf("Files %s removed",BASE_PATH);
    }

    public void testZLoggerFactorySingleMessage() throws Exception {
        ZLogger logger = ZLoggerFactory.getLogger(Class.class);
        logger.info().append("Test message!").commit();
        logger = ZLoggerFactory.getLogger(Object.class);
        logger.info().append("Test message!").commit();
        logger = ZLoggerFactory.getLogger(Integer.class);
        logger.info().append("Test message!").commit();
        logger = ZLoggerFactory.getLogger(Double.class);
        logger.info().append("Test message!").commit();
        logger = ZLoggerFactory.getLogger(Long.class);
        logger.info().append("Test message!").commit();
        logger = ZLoggerFactory.getLogger(Class.class);
        logger.info().append("Test message!").commit();
        ChronicleLogReader reader = new ChronicleLogReader(BASE_PATH);
        System.out.println(reader.printToString());
    }
}