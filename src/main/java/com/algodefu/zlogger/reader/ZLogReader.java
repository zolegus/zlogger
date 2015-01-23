package com.algodefu.zlogger.reader;

import com.algodefu.zlogger.ZLogEntry;
import com.algodefu.zlogger.ZLogLevel;
import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptTailer;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author oleg.zherebkin
 */
public class ZLogReader {
    public static final String DLM = " | ";
    public static final String NEWLINE = System.getProperty("line.separator");
    public static final int BUFFER_LINE_SIZE = 1024 * 32;
    public static final int DEFAULT_PAGE_SIZE = 50;
    private StringBuilder sb = new StringBuilder(BUFFER_LINE_SIZE);
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    private String storagePath;
    private Chronicle chronicle;
    private ExcerptTailer reader;
    //SEARCH PARAMETERS
    private long beginTimestamp;
    private long endTimestamp;
    private int pageNumber;
    private int pageSize;
    private String threadName;
    private String className;
    private String textMessage;


    public ZLogReader(String storagePath) throws IOException {
        this.chronicle = ChronicleQueueBuilder.VanillaChronicleQueueBuilder.vanilla(storagePath).build();
        this.reader = chronicle.createTailer();
    }

    /**
     * Задать новый поиск
     */
    public void search() {
        // Выставляем маркер на начало
        reader.toStart();
        beginTimestamp = 0;
        endTimestamp = Long.MAX_VALUE;
        pageNumber = 1;
        pageSize = DEFAULT_PAGE_SIZE;
        threadName = null;
        className = null;
        textMessage = null;
    }

    /**
     * Продолжить последний поиск
     */
    public void next() {

    }


    public String[] search(int linesAmount) throws IOException {
        // Подключаемся к хранилищу


        // Читаем строки из логгера


        }
        try {
            for (int i = 0; i < linesAmount; i++) {
                readLine(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readLine(ExcerptTailer reader) throws Exception {
        sb.setLength(0);
        sb.append(new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(reader.readLong())).append(DLM)
                .append(ZLogLevel.values()[reader.readInt()].toString()).append(DLM)
                .append(reader.readUTF()).append(DLM)
                .append(reader.readUTF()).append(DLM);
        while (reader.remaining() > 0)
            sb.append(reader.readLine());
        return sb.toString();
    }
}
