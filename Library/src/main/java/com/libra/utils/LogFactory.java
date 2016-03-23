package com.libra.utils;

public class LogFactory {
    private static final String TAG = "LIBRA_CN";
    private static CommonLog log = null;


    public static CommonLog createLog() {
        if (log == null) {
            log = new CommonLog();
        }

        log.setTag(TAG);
        return log;
    }


    public static CommonLog createLog(String tag) {
        if (log == null) {
            log = new CommonLog();
        }

        if (tag == null || tag.length() < 1) {
            log.setTag(TAG);
        }
        else {
            log.setTag(tag);
        }
        return log;
    }


    public static CommonLog createLog(Class clazz) {
        return createLog(clazz.getName());
    }
}