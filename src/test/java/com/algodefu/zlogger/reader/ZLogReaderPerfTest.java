package com.algodefu.zlogger.reader;

import com.algodefu.zlogger.LoggerConfiguration;
import com.algodefu.zlogger.ZLogLevel;
import com.algodefu.zlogger.ZLogger;
import com.algodefu.zlogger.ZLoggerFactory;

import static com.algodefu.zlogger.util.FileCommon.removePath;

/**
 * @author oleg.zherebkin
 */
public class ZLogReaderPerfTest {
    private static String BASE_PATH = "./testlogs";

    public static void main(String[] args) throws Exception {
//        LoggerConfiguration loggerConfiguration = new LoggerConfiguration();
//        loggerConfiguration.setBasePath(BASE_PATH);
//        loggerConfiguration.setLevel(ZLogLevel.TRACE);
//        ZLoggerFactory.init(loggerConfiguration);
//
//        ZLogger logger = ZLoggerFactory.getLogger(ZLogReaderPerfTest.class);
//        // Пишем в лог
//        int msgAmount = 1000000;
////        System.in.read();
//        long start = System.currentTimeMillis();
//        for (int i = 0; i < msgAmount; i++)
//            logger.info().append("Среднестатистическое сообщение в лог-файл под номером #").append(i).commit();
//        long time = System.currentTimeMillis() - start;
//        System.out.printf("Заполнено %d за %.3f s\n", msgAmount, time / 1e3);
//
////        System.in.read();
//        int n = 0;
//        int msgFound = 0;
//        ZLogReader reader = new ZLogReader(BASE_PATH);
//        start = System.currentTimeMillis();
//        n = reader.search(0, Long.MAX_VALUE, ZLogLevel.TRACE, "", "", "").length;
//        while (n > 0) {
//            msgFound += n;
//            n = reader.next().length;
//        }
//        time = System.currentTimeMillis() - start;
//        System.out.printf("Найдено %d за %.3f s\n", msgFound , time / 1e3);
//
//
//        ZLoggerFactory.stop();
//        if (removePath(BASE_PATH) > 0);
//            System.out.printf("Files %s removed\n", BASE_PATH);

        for (int i = 0; i < 4 * 1024; i++) {
            if (i%100==0)
                System.out.printf("\n");
            System.out.printf("x");
        }

    }
}
