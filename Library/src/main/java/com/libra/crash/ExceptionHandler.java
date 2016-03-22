package com.libra.crash;

import com.libra.BaseApp;
import com.libra.utils.ImageUtil;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler previousHandler;


    public ExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        this.previousHandler = handler;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        final Date now = new Date();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        try {
            printWriter.flush();
            File logDir = BaseApp.getInstance().getCrashLogDir();
            if (!logDir.exists()) {
                return;
            }
            String filename = BaseApp.getInstance().getPackageName() + "_" +
                    ImageUtil.getTempFileName();
            String path = logDir + File.separator + filename + ".stacktrace";
            BufferedWriter write = new BufferedWriter(new FileWriter(path));
            write.write("Package: " +
                    com.libra.crash.CrashManagerConstants.APP_PACKAGE + "\n");
            write.write("Version: " +
                    com.libra.crash.CrashManagerConstants.APP_VERSION + "\n");
            write.write("Android: " +
                    com.libra.crash.CrashManagerConstants.ANDROID_VERSION +
                    "\n");
            write.write("Manufacturer: " +
                    com.libra.crash.CrashManagerConstants.PHONE_MANUFACTURER +
                    "\n");
            write.write("Model: " +
                    com.libra.crash.CrashManagerConstants.PHONE_MODEL + "\n");
            write.write("Date: " + now + "\n");
            write.write("\n");
            write.write(result.toString());
            write.flush();
            write.close();
            uploadExceptionToServer();
        } catch (Exception another) {
        } finally {
            previousHandler.uncaughtException(thread, exception);
        }
    }


    private void uploadExceptionToServer() {
    }
}