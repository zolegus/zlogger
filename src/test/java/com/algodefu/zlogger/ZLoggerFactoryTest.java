package com.algodefu.zlogger;

import com.algodefu.zlogger.reader.ChronicleLogReader;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.algodefu.zlogger.util.FileCommon.removePath;

public class ZLoggerFactoryTest extends TestCase {

    private final String BASE_PATH = "./testlogs";

    @Before
    public void setUp() throws Exception {
        LoggerConfiguration loggerConfiguration = new LoggerConfiguration();
        loggerConfiguration.setBasePath(BASE_PATH);
        loggerConfiguration.setLevel(ZLogLevel.TRACE);
        ZLoggerFactory.init(loggerConfiguration);
    }

    @After
    public void tearDown() throws Exception {
        ZLoggerFactory.stop();
        if (removePath(BASE_PATH) > 0)
            System.out.printf("Files %s removed\n", BASE_PATH);
    }

    @Test
    public void testSingleClass() throws Exception {
        final ZLogger logger = ZLoggerFactory.getLogger(ZLoggerFactoryTest.class);
        for (int i = 1; i < 3; i++)
            logger.info().append("Info message ").append(i).append(" message, pi=").append(3.14 * i).commit();
        logger.error().append("Error сообщение").commit();
        logger.warn().append("Warn сообщение").commit();
        logger.debug().append("Debug сообщение").commit();
        logger.trace().append("Trace сообщениe ").append("Trace сообщение").commit();
        ChronicleLogReader reader = new ChronicleLogReader(BASE_PATH);
        System.out.println(reader.printToString(true));
        reader.close();
    }
}