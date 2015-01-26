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
public class ZLogReader {
    public static final String DLM = " | ";
    public static final String NEWLINE = System.getProperty("line.separator");
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final int MAX_PAGE_SIZE = 50;
    public static final int LOG_MESSAGE_SIZE = 4 * 1024;
    private final StringBuilder[] tempStringBuffer = new StringBuilder[MAX_PAGE_SIZE];
    private StringBuilder tempSB = new StringBuilder(LOG_MESSAGE_SIZE);
    private String storagePath;
    private Chronicle chronicle;
    private ExcerptTailer reader;
    //SEARCH PARAMETERS
    private long beginTimestamp;
    private long endTimestamp;
    private int pageNumber;
    private int pageSize;
    private ZLogLevel logLevel;
    private String threadName;
    private String className;
    private String textMessage;
    private long curTimestamp;
    private ZLogLevel curLogLevel;
    private String curThreadName;
    private String curClassName;
    private String curTextMessage;

    public ZLogReader(String storagePath) throws IOException {
        this.storagePath = storagePath;
        this.chronicle = ChronicleQueueBuilder.VanillaChronicleQueueBuilder.vanilla(storagePath).build();
        this.reader = chronicle.createTailer();
        for (int i = 0; i < MAX_PAGE_SIZE; i++)
            tempStringBuffer[i] = new StringBuilder(LOG_MESSAGE_SIZE);
    }

    /**
     * Задать новый поиск
     */
    public String[] search() {
        // Выставляем маркер на начало
        reader.toStart();
        // Инициализируем параметры поиска
        beginTimestamp = 0;
        endTimestamp = Long.MAX_VALUE;
        logLevel = ZLogLevel.TRACE;
        pageNumber = 1;
        pageSize = MAX_PAGE_SIZE;
        threadName = "";
        className = "";
        textMessage = "";
        return search(pageSize);
    }

    public String[] search(long searchBeginTimestamp, long searchEndTimestamp, ZLogLevel searchLogLevel,
                           String searchThreadName, String searchClassName, String searchTextMessage) {
        // Выставляем маркер на начало
        reader.toStart();
        // Инициализируем параметры поиска
        beginTimestamp = searchBeginTimestamp;
        endTimestamp = searchEndTimestamp;
        logLevel = searchLogLevel;
        pageNumber = 1;
        pageSize = MAX_PAGE_SIZE;
        threadName = searchThreadName;
        className = searchClassName;
        textMessage = searchTextMessage;
        return search(pageSize);
    }

    /**
     * Продолжить последний поиск
     */
    public String[] next() {
        return search(pageSize);
    }

    private String[] search(int linesAmount) {
        int readLogLine= 0;
        // Читаем, пока есть что читать
        while (readLogLine < linesAmount && reader.nextIndex()) {
            // Читаем данные
            curTimestamp = reader.readLong();
            curLogLevel = ZLogLevel.values()[reader.readInt()];
            curThreadName = reader.readUTF();
            curClassName = reader.readUTF();
            tempSB.setLength(0);
            while (reader.remaining() > 0)
                tempSB.append(reader.readLine());
            curTextMessage = tempSB.toString();
            reader.finish();
            // Проверяем на удовлетворение условиям поиска
            if ((curTimestamp >= beginTimestamp && curTimestamp <= endTimestamp) &&
                (curLogLevel.isHigherOrEqualTo(logLevel)) &&
                (threadName.equals("") || curThreadName.equals(threadName)) &&
                (className.equals("") || curClassName.equals(className)) &&
                (textMessage.equals("") || curTextMessage.contains(textMessage)))
            {
                // Добавляем строку
                tempStringBuffer[readLogLine].setLength(0);
                tempStringBuffer[readLogLine].append(new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(curTimestamp)).append(DLM)
                        .append(curLogLevel.toString()).append(DLM)
                        .append(curThreadName).append(DLM)
                        .append(curClassName).append(DLM)
                        .append(curTextMessage);
                // Следующая строка
                readLogLine++;
            }
        }
        // Формируем масссив прочитанных строк
        String[] logsStringArray = new String[readLogLine];
        if (readLogLine > 0)
            for (int i = 0; i < readLogLine; i++)
                logsStringArray[i] = tempStringBuffer[i].toString();
        return logsStringArray;
    }

}
