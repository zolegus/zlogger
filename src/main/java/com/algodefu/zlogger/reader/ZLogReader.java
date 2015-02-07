package com.algodefu.zlogger.reader;

import com.algodefu.zlogger.ZLogLevel;
import net.openhft.chronicle.Chronicle;
import net.openhft.chronicle.ChronicleQueueBuilder;
import net.openhft.chronicle.ExcerptTailer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
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
    private ByteBuffer unicodeByteBuffer = ByteBuffer.allocate(LOG_MESSAGE_SIZE);
    private String storagePath;
    private Chronicle chronicle;
    private ExcerptTailer reader;
    //SEARCH PARAMETERS
    private long beginTimestamp;
    private long endTimestamp;
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
    private boolean unicode = false;

    public ZLogReader(String storagePath) throws IOException {
        this.storagePath = storagePath;
        this.chronicle = ChronicleQueueBuilder.VanillaChronicleQueueBuilder.vanilla(storagePath).build();
        this.reader = chronicle.createTailer();
        for (int i = 0; i < MAX_PAGE_SIZE; i++)
            tempStringBuffer[i] = new StringBuilder(LOG_MESSAGE_SIZE);
    }

    public void setUnicode(boolean unicode) {
        this.unicode = unicode;
    }

    /**
     * Задать новый поиск
     */
    public String[] search() throws UnsupportedEncodingException {
        // Выставляем маркер на начало
        reader.toStart();
        // Инициализируем параметры поиска
        beginTimestamp = 0;
        endTimestamp = Long.MAX_VALUE;
        logLevel = ZLogLevel.TRACE;
        pageSize = MAX_PAGE_SIZE;
        threadName = "";
        className = "";
        textMessage = "";
        return search(pageSize);
    }

    /**
     * Продолжить последний поиск
     */
    public String[] next() throws UnsupportedEncodingException {
        return search(pageSize);
    }


    public String[] search(long searchBeginTimestamp, long searchEndTimestamp, ZLogLevel searchLogLevel,
                           String searchThreadName, String searchClassName, String searchTextMessage, int searchPageSize) throws UnsupportedEncodingException {
        // Выставляем маркер на начало
        reader.toStart();
        // Инициализируем параметры поиска
        beginTimestamp = searchBeginTimestamp;
        endTimestamp = searchEndTimestamp;
        logLevel = searchLogLevel;
        if (searchPageSize > MAX_PAGE_SIZE)
            pageSize = MAX_PAGE_SIZE;
        else
            pageSize = searchPageSize;
        threadName = searchThreadName;
        className = searchClassName;
        textMessage = searchTextMessage;
        return search(pageSize);
    }

    private String[] search(int linesAmount) throws UnsupportedEncodingException {
        int readLogLine= 0;
        // Читаем, пока есть что читать
        while (readLogLine < linesAmount && reader.nextIndex()) {
            // Читаем данные
            curTimestamp = reader.readLong();
            curLogLevel = ZLogLevel.values()[reader.readInt()];
            curThreadName = reader.readUTF();
            curClassName = reader.readUTF();
            tempSB.setLength(0);
            if (unicode) {
                unicodeByteBuffer.clear();
                while (reader.remaining() > 0)
                    reader.read(unicodeByteBuffer);
                unicodeByteBuffer.flip();
                byte[] bytes = new byte[unicodeByteBuffer.remaining()];
                unicodeByteBuffer.get(bytes);
                tempSB.append(new String(bytes, "UTF-8"));
            } else {
                while (reader.remaining() > 0)
                    tempSB.append(reader.readLine());
            }
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
