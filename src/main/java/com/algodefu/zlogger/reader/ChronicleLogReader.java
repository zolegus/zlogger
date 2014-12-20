package com.algodefu.zlogger.reader;

import com.algodefu.zlogger.ZLogLevel;
import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptTailer;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author oleg.zherebkin
 */
public class ChronicleLogReader {
    public static final int BUFFER_SIZE = 1024 * 1024;
    public static final String NEWLINE = System.getProperty("line.separator");
    public static final String TMPDIR = System.getProperty("java.io.tmpdir");
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DLM = " | ";
    private final Chronicle chronicle;
    private String basePath = "./logs";

    public ChronicleLogReader(String basePath) throws IOException {
        this.basePath = basePath;
        chronicle = ChronicleQueueBuilder.VanillaChronicleQueueBuilder.vanilla(basePath).build();
    }

    public String printToString() throws IOException {
        ExcerptTailer reader = chronicle.createTailer();
        reader.toStart();

        StringBuilder sb = new StringBuilder(BUFFER_SIZE);
        while (reader.nextIndex()) {
            sb.append(new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(reader.readLong())).append(DLM)
                    .append(ZLogLevel.values()[reader.readInt()].toString()).append(DLM)
                    .append(reader.readUTF()).append(DLM)
                    .append(reader.readUTF()).append(DLM);
            while (reader.remaining() > 0)
                sb.append(reader.readLine());
            reader.finish();
            sb.append(NEWLINE);
            if (sb.length() >= BUFFER_SIZE)
                return "There is not possible out all data. There are too much.";
        }
        return sb.toString();
    }

    public void close() {
        try {
            chronicle.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
