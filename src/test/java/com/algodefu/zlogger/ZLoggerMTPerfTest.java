package com.algodefu.zlogger;

import com.algodefu.zlogger.reader.ChronicleLogReader;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.algodefu.zlogger.util.FileCommon.removePath;

/**
 * @author oleg.zherebkin
 */
public class ZLoggerMTPerfTest extends TestCase {
    private final String BASE_PATH = "./testlogs";

    @Before
    public void setUp() throws Exception {
        LoggerConfiguration loggerConfiguration = new LoggerConfiguration();
        loggerConfiguration.setBasePath(BASE_PATH);
        loggerConfiguration.setLevel(ZLogLevel.INFO);
        ZLoggerFactory.init(loggerConfiguration);
    }

    @After
    public void tearDown() throws Exception {
        ZLoggerFactory.stop();
        if (removePath(BASE_PATH) > 0)
            System.out.printf("Files %s removed\n", BASE_PATH);
    }

    @Test
    public void testMultiThreadLogging() throws IOException, InterruptedException {
        final int RUNS = 300000;
        final int THREADS = Runtime.getRuntime().availableProcessors();
//        final int THREADS = 1;
        double average = 0.0;
        double averageTotal = 0.0;
        int count = 0;
        for (int size : new int[]{64, 128, 256, 512, 1024, 2048, 4096}) {
            final long start = System.nanoTime();
            ExecutorService es = Executors.newFixedThreadPool(THREADS);
            for (int t = 0; t < THREADS; t++) {
                es.submit(new RunnableLogger(RUNS, size));
            }
            es.shutdown();
            es.awaitTermination(2, TimeUnit.MINUTES);
            final long time = System.nanoTime() - start;
            average = time / 1e3 / (RUNS * THREADS);
            averageTotal += average;
            System.out.printf("Perf test (runs=%d, min size=%03d, elapsed=%.3f ms) took an average of %.3f us per entry\n",
                    RUNS,
                    size,
                    time / 1e6,
                    average
            );
            Thread.sleep(500);
            count++;
        }
        System.out.printf("Total average time is %.3f us per entry\n", averageTotal / count);
        ChronicleLogReader reader = new ChronicleLogReader(BASE_PATH);
        System.out.println(reader.printToString(false));
        reader.close();
    }

    protected final class RunnableLogger implements Runnable {
        private final int runs;
        private final ThreadLocal<ZLogger> logger = new ThreadLocal<ZLogger>() {
            @Override
            protected ZLogger initialValue() {
                return ZLoggerFactory.getLogger(RunnableLogger.class);
            }
        };

        public RunnableLogger(int runs, int pad) throws IOException {
            this.runs = runs;
        }

        @Override
        public void run() {
            String message = "Test message";
            for (int i = 0; i < this.runs; i++) {
                logger.get().info().append(message).commit();
            }
            System.out.println("Thread RunnableLogger finished!");
        }
    }

}
