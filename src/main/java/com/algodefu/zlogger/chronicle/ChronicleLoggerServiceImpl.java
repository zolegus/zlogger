package com.algodefu.zlogger.chronicle;

import com.algodefu.zlogger.*;

/**
 * @author oleg.zherebkin
 */
public class ChronicleLoggerServiceImpl implements LoggerService {
    @Override
    public LogLevel getLevel() {
        return null;
    }

    @Override
    public ZLogEntry log(LogLevel level, String categoryName, long appenderMask) {
        return null;
    }

    @Override
    public void entryFlushed(LocalLogEntry localEntry) {

    }

    @Override
    public void stop() {

    }

    @Override
    public ZLogger[] lookupLoggers(String name) {
        return new ZLogger[0];
    }
}
