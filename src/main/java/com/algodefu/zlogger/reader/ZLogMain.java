package com.algodefu.zlogger.reader;

import com.algodefu.zlogger.ZLogLevel;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author oleg.zherebkin
 */
public class ZLogMain {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Try `zlog --help' or `zlog --usage' for more information.");
            return;
        }
        if (args[0].equals("--help") || args[0].equals("--usage") ) {
            //TODO Нужно перенести и выводить из ресурсов
            System.out.println("Синтаксис: zlog ПУТЬ {ПАРАМЕТРЫ}\n" +
                    "ПУТЬ\t\tОбязательный параметр, определяет расположение хранилище z-логов\n" +
                    "ПАРАМЕТРЫ\tУсловия фильтрациии лог-сообщений:\n\n" +
                    " -b\t\t\tВремя первого сообщения, в формате yyyyMMdd-HH:mm:ss.SSS\n" +
                    " -e\t\t\tВремя последнего сообщения, в формате yyyyMMdd-HH:mm:ss.SSS\n" +
                    " -l\t\t\tУровень логирования. Варианты: TRACE, DEBUG, INFO, WARN, ERROR. По умолчанию TRACE\n" +
                    " -t\t\t\tНазвание потока. Обязательно полное имя. Не фильтрует по части названия\n" +
                    " -c\t\t\tНазвание класса. Обязательно полное имя без пакетов. Не фильтрует по части названия\n" +
                    " -m\t\t\tСообщение. Можно указывать часть сообщения или даже один символ\n" +
                    " -n\t\t\tКоличество записей, которые будут выданы. По умолчанию 50. Для получения всех записей указать 0\n" +
                    " -onlycount\tВывести только количество найденных записей. Блокирует ключ -n\n" +
                    " -unicode\tЧитать лог-сообщения в unicode кодировке");
            return;
        }

        if (args[0].equals("--version")) {
            //TODO Нужно перенести и выводить из ресурсов
            System.out.println("zlogger 1.2.1-SNAPSHOT");
            return;
        }

        String BASE_PATH = args[0];
        File folder = new File(BASE_PATH);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Не верно указан путь к хранилищу лог-файлов");
            System.out.println("Try `zlog --help' or `zlog --usage' for more information.");
            return;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm:ss.SSS");
        long beginTimestamp = 0;
        long endTimestamp = Long.MAX_VALUE;
        String parameter = "";
        ZLogLevel logLevel = ZLogLevel.TRACE;
        String threadName = "";
        String className = "";
        String message = "";
        int numMessages = 0;
        boolean onlyCount = false;
        boolean unicode = false;

        try {
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    parameter = args[i];
                    if (parameter.equals("-b")) {
                        // Парсим время начала
                        LocalDateTime dateTime = LocalDateTime.parse(args[++i], dtf);
                        beginTimestamp = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                        continue;
                    }

                    if (parameter.equals("-e")) {
                        // Парсим время конца
                        LocalDateTime dateTime = LocalDateTime.parse(args[++i], dtf);
                        endTimestamp = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                        continue;
                    }

                    if (parameter.equals("-l")) {
                        // Лог уровень
                        logLevel = ZLogLevel.fromStringLevel(args[++i]);
                        continue;
                    }
                    if (parameter.equals("-t")) {
                        // Поток
                        threadName = args[++i];
                        continue;
                    }
                    if (parameter.equals("-c")) {
                        // Класс
                        className = args[++i];
                        continue;
                    }
                    if (parameter.equals("-m")) {
                        // Сообщение
                        message =  args[++i];
                        continue;
                    }
                    if (parameter.equals("-n")) {
                        // Количество записей
                        numMessages = Integer.parseInt(args[++i]);
                        continue;
                    }
                    if (parameter.equals("-onlycount")) {
                        // Количество записей
                        onlyCount = true;
                        continue;
                    }
                    if (parameter.equals("-unicode")) {
                        // Количество записей
                        unicode = true;
                        continue;
                    }
                }
            }
        } catch (Exception ex) {
            System.out.printf("Не верно указан параметр %s. %s.%n", parameter, ex.getMessage());
            System.out.println("Try `zlog --help' or `zlog --usage' for more information.");
            return;
        }

        try {
            ZLogReader reader = new ZLogReader(BASE_PATH);
            reader.setUnicode(unicode);
            String[] result = reader.search(beginTimestamp, endTimestamp, logLevel, threadName, className, message, 1);
            int n = 1;
            if (result.length == 0)
                System.out.println("Ничего не найдено!");
            else {
                while (result.length > 0) {
                    if (onlyCount)
                        n++;
                    else
                        System.out.println(result[0]);
                    if (!onlyCount && numMessages != 0 && n++ >= numMessages)
                        break;
                    result = reader.next();
                }
                if (onlyCount)
                    System.out.printf("Найдено %d записей%n", n-1);
            }
        } catch (Exception ex) {
            System.out.printf("Проблема с чтением данных из хранилища. %s.%n", ex.getMessage());
            System.out.println("Try `zlog --help' or `zlog --usage' for more information.");
            return;
        }
    }
}
