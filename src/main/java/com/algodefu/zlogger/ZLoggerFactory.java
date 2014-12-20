package com.algodefu.zlogger;

import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptAppender;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author oleg.zherebkin
 */
public final class ZLoggerFactory {

    private final Object lock = new Object();
    private Chronicle chronicle;
    private final AtomicReference<LoggerConfiguration> loggerConfiguration;
    private final ThreadLocal<Map<String, ZLogger>> classedLoggers;
    private final ThreadLocal<ExcerptAppender> threadAppender;

    private ZLoggerFactory(){
        this.loggerConfiguration = new AtomicReference<LoggerConfiguration>();
        this.classedLoggers = new ThreadLocal<Map<String, ZLogger>>(){
            @Override
            protected Map<String, ZLogger> initialValue() {
                return new HashMap<String, ZLogger>();
            }
        };

        this.threadAppender = new ThreadLocal<ExcerptAppender>();
//        {
//            @Override
//            protected ExcerptAppender initialValue() {
//                try {
//                    return chronicle.createAppender();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        };
    }

    private ZLogger get(final Class clazz){
        return getLogger(clazz.getName());//, classedLoggers);
    }

    public static ZLogger getLogger(final Class clazz){
        return Helper.FACTORY.get(clazz);
    }

    public ZLogger getLogger(final String className) {
        final Map<String, ZLogger> map = classedLoggers.get();
        ZLogger logger = map.get(className);
        if (logger == null) {
            if (threadAppender.get() == null)
                try {
                    threadAppender.set(this.chronicle.createAppender());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            logger = new ZLogger(threadAppender.get(), loggerConfiguration.get().getLevel(), className);
            map.put(className, logger);
        }
        return logger;
    }

    public static void stop(){
        synchronized (Helper.FACTORY.lock) {
            try {
                if (Helper.FACTORY.chronicle != null)
                    Helper.FACTORY.chronicle.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO method stop implementation
        }
    }

    public static ZLoggerFactory init(LoggerConfiguration configuration) throws IOException {
        synchronized (Helper.FACTORY.lock) {
            stop();
            if (configuration == null)
                throw new IllegalArgumentException("Not a null logger configuration is expected");
            Helper.FACTORY.loggerConfiguration.set(configuration);
            Helper.FACTORY.chronicle = ChronicleQueueBuilder.VanillaChronicleQueueBuilder.vanilla(configuration.getBasePath()).build();
        }
        return Helper.FACTORY;
    }

    private final static class Helper {
        final static ZLoggerFactory FACTORY = new ZLoggerFactory();
    }
}
