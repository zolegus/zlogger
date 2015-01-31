package com.algodefu.zlogger.reader;

import com.algodefu.zlogger.ZLogLevel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
                    "ПУТЬ - определяет расположение хранилище z-логов\n" +
                    "ПАРАМЕТРЫ: Условия фильтрациии лог-сообщений" +
                    "-b Время первого сообщения, в формате YYYYMMDD-HHMMSS.\n" +
                    "-e Время последнего сообщения, в формате YYYYMMDD-HHMMSS.\n" +
                    "-l  Уровень логирования. Варианты: TRACE, DEBUG, INFO, WARN, ERROR. По умолчанию TRACE.\n" +
                    "-t Название потока. Обязательно полное имя. Не фильтрует по части названия.\n" +
                    "-c Название класса. Обязательно полное имя без пакетов. Не фильтрует по части названия.\n" +
                    "-m Сообщение. Можно указывать часть сообщения или даже один символ.\n" +
                    "-n Количество записей, которые будут выданы. По умолчанию 50. Если указать 0, то полное содержание.");
            return;
        }

        if (args[0].equals("--version")) {
            //TODO Нужно перенести и выводить из ресурсов
            System.out.println("ZLogger 1.1-SNAPSHOT version");
            return;
        }

        String BASE_PATH = args[0];
        File folder = new File(BASE_PATH);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Не верно указан путь к хранилищу лог-файлов");
            System.out.println("Try `zlog --help' or `zlog --usage' for more information.");
            return;
        }

        String parameter = "";
        ZLogLevel logLevel = ZLogLevel.TRACE;
        String threadName = "";
        String className = "";
        String message = "";
        int numMessages = 0;

        try {
            if (args.length > 1) {
                for (int i = 1; i < args.length; i++) {
                    parameter = args[i];
                    if (parameter.equals("-b")) {
                        // Парсим время начала
                    }

                    if (parameter.equals("-e")) {
                        // Парсим время конца
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
                }
            }
        } catch (Exception ex) {
            System.out.printf("Не верно указан параметр %s. %s.%n", parameter, ex.getMessage());
            System.out.println("Try `zlog --help' or `zlog --usage' for more information.");
            return;
        }

        try {
            ZLogReader reader = new ZLogReader(BASE_PATH);
            String[] result = reader.search(0, Long.MAX_VALUE, logLevel, threadName, className, message, 1);
            int n = 1;
            if (result.length == 0)
                System.out.println("Ничего не найдено!");
            else {
                while (result.length > 0) {
                    System.out.println(result[0]);
                    if (numMessages != 0 && n++ >= numMessages)
                        break;
                    result = reader.next();
                }
            }
        } catch (IOException ex) {
            System.out.printf("Проблема с чтением данных из хранилища. %s.%n", ex.getMessage());
            System.out.println("Try `zlog --help' or `zlog --usage' for more information.");
            return;
        }
    }
}
