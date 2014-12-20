package com.algodefu.zlogger.alternative.chronicle;

import com.algodefu.zlogger.alternative.*;
import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author oleg.zherebkin
 */
public class ChronicleLoggerServiceImpl implements LoggerService {
    private final ThreadLocal<Map<String, ChronicleEntry>> entries;
    private final LogLevel level;
    private final Chronicle chronicle;

    public ChronicleLoggerServiceImpl() throws IOException {
        this(LogLevel.DEBUG);
    }

    public ChronicleLoggerServiceImpl(final LogLevel level) throws IOException {
        this.level = level;
        String basePath = "./logs/";
        this.chronicle = ChronicleQueueBuilder.VanillaChronicleQueueBuilder.vanilla(basePath).build();
        this.entries = new ThreadLocal<Map<String, ChronicleEntry>>(){
            @Override
            protected Map<String, ChronicleEntry> initialValue() {
                return new HashMap<String, ChronicleEntry>();
            }
        };
    }

    @Override
    public LogLevel getLevel() {
        return level;
    }

    @Override
    public ZLogEntry log(LogLevel level, String categoryName, long appenderMask) {
        final Map<String, ChronicleEntry> map = entries.get();
        ChronicleEntry entry = map.get(categoryName);
        if (entry == null){
            try {
                entry = new ChronicleEntry(this.chronicle.createAppender(), level);
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.put(categoryName, entry);
        }
        return entry;
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
