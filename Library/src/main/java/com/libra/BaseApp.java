package com.libra;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.libra.crash.CrashManager;
import com.libra.crash.CrashManagerConstants;
import com.libra.utils.CommonLog;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by libra on 16/3/21 上午9:58.
 */
public class BaseApp extends Application {

    private final String PREFS_FILE = "device_id.xml";

    private final String PREFS_DEVICE_ID = "device_id";

    protected static BaseApp baseApp;


    public static BaseApp getInstance() {
        return baseApp;
    }


    @Override public void onCreate() {
        super.onCreate();
        baseApp = this;
        CommonLog.isDebug = BuildConfig.DEBUG;
        CrashManagerConstants.loadFromContext(this);
        CrashManager.registerHandler();
    }


    /**
     * 创建文件夹
     */
    public File createFloder(String path, boolean nomedia) {
        File file;
        if (!Environment.getExternalStorageState()
                        .equals(Environment.MEDIA_MOUNTED)) {
            File rootFile = getFilesDir().getParentFile();
            file = new File(rootFile, path);
        }
        else {
            file = new File(Environment.getExternalStorageDirectory()
                                       .getAbsolutePath() + path);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        if (nomedia) {
            try {
                File nomediaFile = new File(file, ".nomedia");
                if (!nomediaFile.exists()) {
                    nomediaFile.createNewFile();
                }
            } catch (IOException e) {
                Log.i("baseApp",
                        "Can't create \".nomedia\" file in application directory");
            }
        }
        return file;
    }


    public File getCrashLogDir() {
        File root = getExternalCacheDir();
        if (root != null && root.exists()) {
            File file = new File(root, "/CrashLog/");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file;
        }
        else {
            return createFloder("/CrashLog/", true);
        }
    }


    /**
     * 为每个设备产生唯一的UUID，以ANDROID_ID为基础，在获
     * 取失败时以TelephonyManager.getDeviceId()为备选方法，如果再失败，使用UUID的生成策略
     */
    @SuppressLint("DefaultLocale") public String getDeviceUuid() {
        synchronized (this) {
            SharedPreferences prefs = getSharedPreferences(PREFS_FILE, 0);
            String id = prefs.getString(PREFS_DEVICE_ID, null);
            if (id != null) {
                return id;
            }
            else {
                UUID uuid;
                String androidId = Settings.Secure.getString(
                        getContentResolver(), Settings.Secure.ANDROID_ID);
                try {
                    if (!"9774d56d682e549c".equals(androidId)) {// Android
                        // 2.2中的缺陷，受影响的设备具有相同的ANDROID_ID,就是9774d56d682e549c
                        uuid = UUID.nameUUIDFromBytes(
                                androidId.getBytes("utf8"));
                    }
                    else {
                        String deviceId = ((TelephonyManager) getSystemService(
                                Context.TELEPHONY_SERVICE)).getDeviceId();
                        uuid = deviceId != null ? UUID.nameUUIDFromBytes(
                                deviceId.getBytes("utf8")) : UUID.randomUUID();
                    }
                } catch (UnsupportedEncodingException e) {
                    uuid = UUID.randomUUID();
                }
                id = uuid.toString().replaceAll("-", "").toUpperCase();
                prefs.edit().putString(PREFS_DEVICE_ID, id).commit();
                return id;
            }
        }
    }
}
