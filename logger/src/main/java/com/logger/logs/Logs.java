package com.logger.logs;

public class Logs {

    String className;
    String methodName;
    Object[] message;
    long threadId;

    public Logs(String className, String methodName, Object[] message, long threadId) {
        this.className = className;
        this.methodName = methodName;
        this.message = message;
        this.threadId = threadId;
    }

    public String getClassName() {
        return className;
    }


    public String getMethodName() {
        return methodName;
    }


    public Object[] getMessage() {
        return message;
    }


    public long getThreadId() {
        return threadId;
    }

}
