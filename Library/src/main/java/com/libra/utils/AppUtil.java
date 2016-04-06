package com.libra.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.ResultReceiver;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import com.libra.BaseApp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class AppUtil {

    /**
     * 描述：打开并安装文件.
     *
     * @param context the context
     * @param file apk文件路径
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }


    /**
     * 描述：卸载程序.
     *
     * @param context the context
     * @param packageName 包名
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }


    /**
     * 用来判断服务是否运行.
     *
     * @param ctx the ctx
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context ctx, String className) {
        boolean isRunning = false;
        ActivityManager activityManager
                = (ActivityManager) ctx.getSystemService(
                Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> servicesList
                = activityManager.getRunningServices(Integer.MAX_VALUE);
        Iterator<RunningServiceInfo> l = servicesList.iterator();
        while (l.hasNext()) {
            RunningServiceInfo si = l.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }


    /**
     * 停止服务.
     *
     * @param ctx the ctx
     * @param className the class name
     * @return true, if successful
     */
    public static boolean stopRunningService(Context ctx, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(ctx, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = ctx.stopService(intent_service);
        }
        return ret;
    }


    /**
     * 描述：判断网络是否有效.
     *
     * @param context the context
     * @return true, if is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity
                    = (ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    /**
     * Gps是否打开 需要<uses-permission
     * android:name="android.permission.ACCESS_FINE_LOCATION" />权限
     *
     * @param context the context
     * @return true, if is gps enabled
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    /**
     * wifi是否打开.
     *
     * @param context the context
     * @return true, if is wifi enabled
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn
                = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        return mgrConn.getActiveNetworkInfo() != null &&
                mgrConn.getActiveNetworkInfo().getState() ==
                        NetworkInfo.State.CONNECTED ||
                mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS;
    }


    /**
     * 判断当前网络是否是wifi网络.
     *
     * @param context the context
     * @return boolean
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null &&
                activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }


    /**
     * 判断当前网络是否是3G网络.
     *
     * @param context the context
     * @return boolean
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null &&
                activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }


    /**
     * 导入数据库
     */
    public static boolean importDatabase(Context context, String dbName, int rawRes) {
        int buffer_size = 1024;
        InputStream is = null;
        FileOutputStream fos = null;
        boolean flag = false;
        try {
            String dbPath = "/data/data/" + context.getPackageName() +
                    "/databases/" + dbName;
            File dbfile = new File(dbPath);
            // 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
            if (!dbfile.exists()) {
                // 欲导入的数据库
                if (!dbfile.getParentFile().exists()) {
                    dbfile.getParentFile().mkdirs();
                }
                dbfile.createNewFile();
                is = context.getResources().openRawResource(rawRes);
                fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[buffer_size];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
            }
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                }
            }
        }
        return flag;
    }


    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(View view) {
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext()
                                                          .getSystemService(
                                                                  Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /**
     * 显示软键盘
     */
    public static void showSoftkeyboard(View view) {
        showSoftkeyboard(view, null);
    }


    /**
     * 显示软键盘
     */
    public static void showSoftkeyboard(View view, ResultReceiver resultReceiver) {
        Configuration config = view.getContext()
                                   .getResources()
                                   .getConfiguration();
        if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            InputMethodManager imm = (InputMethodManager) view.getContext()
                                                              .getSystemService(
                                                                      Context.INPUT_METHOD_SERVICE);
            if (resultReceiver != null) {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT,
                        resultReceiver);
            }
            else {
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }


    /**
     * 截屏
     */
    public static Bitmap shot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        Display display = activity.getWindowManager().getDefaultDisplay();
        view.layout(0, 0, display.getWidth(), display.getHeight());
        // 允许当前窗口保存缓存信息，这样getDrawingCache()方法才会返回一个Bitmap
        view.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return bmp;
    }


    /**
     * 检测sdcard是否可用
     *
     * @return true为可用，否则为不可用
     */
    public static boolean sdCardIsAvailable() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 检查SDCard是否有足够空间
     *
     * @param updateSize Size to Check
     * @return True if the Update will fit on SDCard, false if not enough space
     * on SDCard Will also return false, if the SDCard is not mounted as
     * read/write
     */
    public static boolean enoughSpaceOnSdCard(long updateSize) {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return updateSize < getRealSizeOnSdcard();
    }


    /**
     * 获取sdcard剩余空间
     */
    public static long getRealSizeOnSdcard() {
        File path = new File(
                Environment.getExternalStorageDirectory().getAbsolutePath());
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }


    /**
     * 检查手机内置是否有足够的空间
     *
     * @param updateSize Size to Check
     * @return True if the Update will fit on Phone, false if not enough space
     * on Phone
     */
    public static boolean enoughSpaceOnPhone(long updateSize) {
        return getRealSizeOnPhone() > updateSize;
    }


    /**
     * 获取手机内置剩余空间
     */
    public static long getRealSizeOnPhone() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long realSize = blockSize * availableBlocks;
        return realSize;
    }


    /**
     * 获取edittext值
     */
    public static String getEditTextValue(EditText et) {
        if (et == null) {
            return null;
        }
        return et.getText().toString().trim();
    }


    public static void setEditTextValue(EditText et, String value) {
        if (!TextUtils.isEmpty(value)) {
            et.setText(value);
            et.setSelection(value.length());
        }
    }


    /**
     * 获取spinner的显示值
     */
    public static String getSpinnerText(Spinner spinner) {
        if (spinner == null) {
            return null;
        }
        return spinner.getSelectedItem().toString();
    }


    /**
     * 获取uid
     */
    public static String getUid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-", "").toUpperCase();
    }


    /**
     * 获取电话号码
     */
    public static String getPhoneNumber(Context context) {
        TelephonyManager telephonyManager
                = (TelephonyManager) context.getSystemService(
                Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }


    // 毫秒转时分秒
    public static String formatLongToTimeStr(Long l) {
        // int hour = 0;
        int minute = 0;
        int second = 0;
        second = l.intValue() / 1000;
        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            // hour = minute / 60;
            minute = minute % 60;
        }
        // strtime = hour + "小时" + minute + "分钟" + second + "秒";
        String strtime;
        if (minute <= 0) {
            strtime = second + "秒";
        }
        else {
            strtime = minute + "分钟" + second + "秒";
        }
        return strtime;
    }


    /**
     * 获取控件的高度，如果获取的高度为0，则重新计算尺寸后再返回高度
     */
    public static int getViewMeasuredHeight(View view) {
        // int height = view.getMeasuredHeight();
        // if(0 < height){
        // return height;
        // }
        calcViewMeasure(view);
        return view.getMeasuredHeight();
    }


    /**
     * 获取控件的宽度，如果获取的宽度为0，则重新计算尺寸后再返回宽度
     */
    public static int getViewMeasuredWidth(View view) {
        // int width = view.getMeasuredWidth();
        // if(0 < width){
        // return width;
        // }
        calcViewMeasure(view);
        return view.getMeasuredWidth();
    }


    /**
     * 测量控件的尺寸
     */
    public static void calcViewMeasure(View view) {
        // int width =
        // View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        // int height =
        // View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        // view.measure(width,height);
        int width = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int expandSpec = View.MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        view.measure(width, expandSpec);
    }


    /**
     * 拨打电话，跳转拨号界面
     */
    public static void call(Activity activity, String mobileNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + mobileNumber);
        intent.setData(data);
        activity.startActivity(intent);
    }


    /**
     * 描述：判断是否安装了指定应用.
     *
     * @param context the context
     * @param packageName 应用包名
     */
    public static boolean isInstallApp(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * dp转px
     *
     * @param dipValue dp
     * @return int
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = BaseApp.getInstance()
                                   .getResources()
                                   .getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转dip
     *
     * @param pxValue px
     * @return int
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = BaseApp.getInstance()
                                   .getResources()
                                   .getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 获取版本名称
     *
     * @return String
     */
    public static String getAppVersionNumber() {
        String versionName;
        try {
            PackageManager packageManager = BaseApp.getInstance()
                                                   .getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    BaseApp.getInstance().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {
            versionName = "";
        }
        return versionName;
    }


    /**
     * 获取指定应用程序的版本号
     *
     * @return String
     */
    public static String getAppVersionCode() {
        String versionCode;
        try {
            PackageManager packageManager = BaseApp.getInstance()
                                                   .getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    BaseApp.getInstance().getPackageName(), 0);
            versionCode = Integer.toString(packageInfo.versionCode);
        } catch (Exception e) {
            versionCode = "";
        }
        return versionCode;
    }
    
}
