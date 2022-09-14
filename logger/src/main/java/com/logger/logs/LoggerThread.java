package com.logger.logs;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;

public class LoggerThread extends Thread {

    @Override
    public void run() {
        super.run();

        while (true) {

            try {

                Thread.sleep(10);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Queue<Logs> localQueue = new LinkedList<>();
            synchronized (MyLogger.queue) {
                while (!MyLogger.queue.isEmpty()) {
                    localQueue.add(MyLogger.removeFromQueue());
                }
            }

            while (!localQueue.isEmpty()) {

                MyLogger.logger.setLevel(Level.INFO);
                MyLogger.logger.info(getMsg(localQueue.remove()));
            }
        }
    }

    private String getMsg(Logs logs) {

        StringBuilder msg = new StringBuilder();
        if (logs.getMessage() != null) {

            for (Object obj : logs.getMessage()) {

                if (obj != null) {
                    msg.append(obj + " ");
                }
            }
        }

        return "[" + logs.getClassName() + "] " +
                "[" + logs.getMethodName() + "] " +
                "[" + logs.getThreadId() + "] " +
                msg.toString();
    }
}
