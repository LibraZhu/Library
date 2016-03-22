package com.libra.crash;

import com.libra.BaseApp;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CrashManager {
    private static final int MAX_LOG_FILES = 10;


    public static void registerHandler() {
        Thread.UncaughtExceptionHandler currentHandler
                = Thread.getDefaultUncaughtExceptionHandler();
        // Register if not already registered
        if (!(currentHandler instanceof com.libra.crash.ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(
                    new com.libra.crash.ExceptionHandler(currentHandler));
        }
        ScheduledExecutorService scheduledExecutorService
                = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(new ClearLogTask(), 5,
                TimeUnit.SECONDS);
    }


    private static class ClearLogTask implements Runnable {
        @Override public void run() {
            onlyLeftTenLogFilesInStorage();
        }
    }


    private static void onlyLeftTenLogFilesInStorage() {
        String[] files = searchForStackTraces();
        if (files == null) {
            return;
        }
        int length = files.length;
        if (length > MAX_LOG_FILES) {
            for (int i = MAX_LOG_FILES; i < length; i++) {
                new File(files[i]).delete();
            }
        }
    }


    private static String[] searchForStackTraces() {
        File logDir = BaseApp.getInstance().getCrashLogDir();
        if (!logDir.exists()) {
            return new String[0];
        }
        // Filter for ".stacktrace" files
        FilenameFilter filter = new FilenameFilter() {
            @Override public boolean accept(File dir, String name) {
                return name.endsWith(".stacktrace");
            }
        };
        String[] files = logDir.list(filter);
        for (int i = 0; i < files.length; i++) {
            files[i] = logDir.getAbsolutePath() + File.separator + files[i];
        }
        // desc sort arrays, then delete the longest file
        Arrays.sort(files, new Comparator<String>() {
            @Override public int compare(String aFilePath, String bFilePath) {
                long aDate = new File(aFilePath).lastModified();
                long bDate = new File(bFilePath).lastModified();
                return aDate > bDate ? -1 : 1;
            }
        });
        return files;
    }
}
