package com.logger.logs;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

    public static final String LOGTAG = "MyLogger";
    public static final Queue<Logs> queue = new LinkedList<>();
    public static Logger logger;
    public static File DIRECTORY = null;
    public static File LOG_PATH = null;
    final static String FORMAT = "ddMMyy_HHmmss";
    private static final int MAX_LOGS_DAYS = -5;

    public static void init(Context context) {

        DIRECTORY = context.getFilesDir();
        LOG_PATH = new File(DIRECTORY, "applogs");

        if (!LOG_PATH.exists()) {
            LOG_PATH.mkdirs();
        }

        deleteLockFiles(LOG_PATH);
        deleteOlderFiles(LOG_PATH);

        logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        logger.setLevel(Level.ALL);

        String formatDate = new SimpleDateFormat(FORMAT).format(Calendar.getInstance().getTime());

        int fileSize = 1000 * 1000 * 5;

        try {
            String fileName = LOG_PATH.getAbsolutePath() + "/LogFile_" + formatDate + ".txt";

            Log.i(LOGTAG, fileName);

            FileHandler fileHandler = new FileHandler(fileName, fileSize, 10, true);

            fileHandler.setFormatter(new SimpleFormatter());

            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord rec) {

                    //Log.i(LOGTAG, "format(LogRecord)");

                    StringBuffer sb = new StringBuffer(1000);
                    Date currentTime = Calendar.getInstance().getTime();
                    sb.append(new SimpleDateFormat(FORMAT).format(currentTime));
                    sb.append(" ");
                    sb.append(formatMessage(rec));
                    sb.append("\n");
                    return sb.toString();
                }
            });

            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }


        startThread();

    }

    public static File getLogDirectory() {
        return LOG_PATH;
    }

    private static void startThread() {
        LoggerThread thread = new LoggerThread();
        thread.start();
    }

    public static void trace(Object... args) {

        try {
            //Log.i(LOGTAG, "trace");
            String className = new Exception().getStackTrace()[2].getClassName();
            String methodName = new Exception().getStackTrace()[2].getMethodName();
            Logs logs = new Logs(className, methodName, args, Thread.currentThread().getId());
            addToQueue(logs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addToQueue(Logs logs) {
        synchronized (queue) {
            //Log.i(LOGTAG, "addToQueue");
            queue.add(logs);
        }
    }

    public static Logs removeFromQueue() {

        synchronized (queue) {
            //Log.i(LOGTAG, "removeFromQueue");
            return queue.remove();
        }
    }


    private static void deleteLockFiles(File directory) {

        if (directory.exists()) {

            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {

                for (File file : listFiles) {

                    String name = file.getName();
                    String[] arr = name.split("\\.");
                    if (arr[arr.length - 1].equals("lck")) {
                        file.delete();
                    }
                }
            }
        }
    }


    private static void deleteOlderFiles(File directory) {

        if (directory.exists()) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, MAX_LOGS_DAYS);
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {

                Log.d(LOGTAG, "total logs files " + listFiles.length);
                for (File file : listFiles) {

                    Date lastModified = new Date(file.lastModified());
                    if (lastModified.before(calendar.getTime())) {
                        Log.i(LOGTAG, "older than " + MAX_LOGS_DAYS + " days. so deleting log file " + file.getName());
                        file.delete();
                    } else {
                        Log.i(LOGTAG, "Not older than " + MAX_LOGS_DAYS + " days.");
                    }

                }
            }
        }
    }
}