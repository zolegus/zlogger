package com.algodefu.zlogger.reader;

import com.algodefu.zlogger.LoggerConfiguration;
import com.algodefu.zlogger.ZLogLevel;
import com.algodefu.zlogger.ZLogger;
import com.algodefu.zlogger.ZLoggerFactory;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Test;

import static com.algodefu.zlogger.util.FileCommon.removePath;

public class ZLogReaderTest extends TestCase {
    private final String BASE_PATH = "./testlogs";
    @Override
    public void setUp() throws Exception {
        LoggerConfiguration loggerConfiguration = new LoggerConfiguration();
        loggerConfiguration.setBasePath(BASE_PATH);
        loggerConfiguration.setLevel(ZLogLevel.TRACE);
        ZLoggerFactory.init(loggerConfiguration);
    }


    @After
    public void tearDown() throws Exception {
        ZLoggerFactory.stop();
        if (removePath(BASE_PATH) > 0);
//            System.out.printf("Files %s removed\n", BASE_PATH);
    }

    @Test
    public void testSearchDefault() throws Exception {
        ZLogger logger = ZLoggerFactory.getLogger(ZLogReaderTest.class);
        // Пишем в лог
        for (int i = 0; i < 10; i++)
            logger.info().append("Infotest#").append(i).commit();
        // Читаем из лога
        ZLogReader reader = new ZLogReader(BASE_PATH);
        String[] logMessages = reader.search();
        assertEquals(10, logMessages.length);
        for (int i = 0; i < logMessages.length; i++) {
//            System.out.println(logMessages[i]);
            assertEquals(("Infotest#" + i), logMessages[i].split(" \\| ")[4]);
        }
    }

    @Test
    public void testSearchByTime() throws Exception {
        ZLogger logger = ZLoggerFactory.getLogger(ZLogReaderTest.class);
        // Пишем в лог
        long startTime = 0;
        long stopTime = 0;
        for (int i = 0; i < 10; i++) {
            if (i == 2)
                startTime = System.currentTimeMillis();
            logger.info().append("Infotest#").append(i).commit();
            if (i < 7)
                stopTime = System.currentTimeMillis();
            Thread.sleep(1);
        }
        // Читаем из лога
        ZLogReader reader = new ZLogReader(BASE_PATH);
        String[] logMessages = reader.search(startTime, stopTime, ZLogLevel.TRACE, "", "" ,"", 50);
        assertEquals(5, logMessages.length);
        for (int i = 0; i < logMessages.length; i++) {
//            System.out.println(logMessages[i]);
            assertEquals(("Infotest#" + (i+2)), logMessages[i].split(" \\| ")[4]);
        }
    }

    @Test
    public void testSearchByLogLevel() throws Exception {
        ZLogger logger = ZLoggerFactory.getLogger(ZLogReaderTest.class);
        // Пишем в лог
        for (int i = 0; i < 10; i++) {
            logger.error().append("Errortest#").append(i).commit();
            logger.warn().append("Warntest#").append(i).commit();
            logger.info().append("Infotest#").append(i).commit();
            logger.debug().append("Debugtest#").append(i).commit();
            logger.trace().append("Tracetest#").append(i).commit();
        }
        // Читаем из лога
        ZLogReader reader = new ZLogReader(BASE_PATH);
        assertEquals(50, reader.search(0, Long.MAX_VALUE, ZLogLevel.TRACE, "", "", "", 50).length);
        assertEquals(40, reader.search(0, Long.MAX_VALUE, ZLogLevel.DEBUG, "", "", "", 50).length);
        assertEquals(30, reader.search(0, Long.MAX_VALUE, ZLogLevel.INFO, "", "", "", 50).length);
        assertEquals(20, reader.search(0, Long.MAX_VALUE, ZLogLevel.WARN, "", "", "", 50).length);
        assertEquals(10, reader.search(0, Long.MAX_VALUE, ZLogLevel.ERROR, "", "", "", 50).length);
    }

    @Test
    public void testSearchByMessage() throws Exception {
        ZLogger logger = ZLoggerFactory.getLogger(ZLogReaderTest.class);
        // Пишем в лог
        for (int i = 0; i < 10; i++) {
            logger.error().append("Errortest#").append(i).commit();
            logger.warn().append("Warntest#").append(i).commit();
            logger.info().append("Infotest#").append(i).commit();
            logger.debug().append("Debugtest#").append(i).commit();
            logger.trace().append("Tracetest#").append(i).commit();
        }
        // Читаем из лога
        ZLogReader reader = new ZLogReader(BASE_PATH);
        assertEquals(50, reader.search(0, Long.MAX_VALUE, ZLogLevel.TRACE, "", "", "test#", 50).length);
        assertEquals(10, reader.search(0, Long.MAX_VALUE, ZLogLevel.TRACE, "", "", "Error", 50).length);
        assertEquals(0, reader.search(0, Long.MAX_VALUE, ZLogLevel.TRACE, "", "", "Check", 50).length);
    }

    @Test
    public void testSearchByClassName() throws Exception {
        ZLogger logger = ZLoggerFactory.getLogger(ZLogReaderTest.class);
        // Пишем в лог
        for (int i = 0; i < 10; i++) {
            logger.info().append("Infotest#").append(i).commit();
        }
        // Читаем из лога
        ZLogReader reader = new ZLogReader(BASE_PATH);
        assertEquals(10, reader.search(0, Long.MAX_VALUE, ZLogLevel.TRACE, "", "ZLogReaderTest", "", 50).length);
        assertEquals(0, reader.search(0, Long.MAX_VALUE, ZLogLevel.TRACE, "", "Object", "", 50).length);
    }

    @Test
    public void testNext() throws Exception {
        ZLogger logger = ZLoggerFactory.getLogger(ZLogReaderTest.class);
        // Пишем в лог
        for (int i = 0; i < 110; i++) {
            logger.info().append("Infotest#").append(i).commit();
        }
        // Читаем из лога
        ZLogReader reader = new ZLogReader(BASE_PATH);
        assertEquals(50, reader.search(0, Long.MAX_VALUE, ZLogLevel.TRACE, "", "", "", 50).length);
        assertEquals(50, reader.next().length);
        assertEquals(10, reader.next().length);
        assertEquals(0, reader.next().length);
    }
}