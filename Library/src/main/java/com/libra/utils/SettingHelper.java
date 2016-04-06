package com.libra.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SettingHelper {
    private static Editor editor = null;
    private static SharedPreferences sharedPreferences = null;


    private SettingHelper() {
    }


    private static Editor getEditorObject(Context context) {
        if (editor == null) {
            editor = PreferenceManager.getDefaultSharedPreferences(context)
                                      .edit();
        }
        return editor;
    }


    public static int getSharedPreferences(Context context, String key, int defValue) {
        return getSharedPreferencesObject(context).getInt(key, defValue);
    }


    public static long getSharedPreferences(Context context, String key, long defValue) {
        return getSharedPreferencesObject(context).getLong(key, defValue);
    }


    public static Boolean getSharedPreferences(Context paramContext, String paramString, boolean paramBoolean) {
        return getSharedPreferencesObject(paramContext).getBoolean(paramString,
                paramBoolean);
    }


    public static String getSharedPreferences(Context paramContext, String paramString1, String paramString2) {
        return getSharedPreferencesObject(paramContext).getString(paramString1,
                paramString2);
    }


    private static SharedPreferences getSharedPreferencesObject(Context paramContext) {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
                    paramContext);
        }
        return sharedPreferences;
    }


    public static void setEditor(Context paramContext, String paramString, int paramInt) {
        getEditorObject(paramContext).putInt(paramString, paramInt).commit();
    }


    public static void setEditor(Context paramContext, String paramString, long paramLong) {
        getEditorObject(paramContext).putLong(paramString, paramLong).commit();
    }


    public static void setEditor(Context paramContext, String paramString, float paramFloat) {
        getEditorObject(paramContext).putFloat(paramString, paramFloat)
                                     .commit();
    }


    public static void setEditor(Context paramContext, String paramString, Boolean paramBoolean) {
        getEditorObject(paramContext).putBoolean(paramString, paramBoolean)
                                     .commit();
    }


    public static void setEditor(Context paramContext, String paramString1, String paramString2) {
        getEditorObject(paramContext).putString(paramString1, paramString2)
                                     .commit();
    }

    // -----------------------------------------华丽分割线-------------------------------------


    private static Editor getEditorObject(Context context, String dataBasesName) {
        return context.getSharedPreferences(dataBasesName, 0).edit();
    }


    private static SharedPreferences getSharedPreferencesObject(Context context, String dataBasesName) {
        return context.getSharedPreferences(dataBasesName, 0);
    }


    public static int getSharedPreferences(Context context, String dataBasesName, String key, int defValue) {
        return getSharedPreferencesObject(context, dataBasesName).getInt(key,
                defValue);
    }


    public static long getSharedPreferences(Context context, String dataBasesName, String key, long defValue) {
        return getSharedPreferencesObject(context, dataBasesName).getLong(key,
                defValue);
    }


    public static Boolean getSharedPreferences(Context context, String dataBasesName, String string, boolean defValue) {
        return getSharedPreferencesObject(context, dataBasesName).getBoolean(
                string, defValue);
    }


    public static String getSharedPreferences(Context context, String dataBasesName, String string, String defValue) {
        return getSharedPreferencesObject(context, dataBasesName).getString(
                string, defValue);
    }


    public static void setEditor(Context context, String dataBasesName, String name, Object value) {
        if (name == null || value == null) {
            return;
        }
        Editor editor = getEditorObject(context, dataBasesName);
        if (value instanceof Integer) {
            editor.putInt(name, Integer.parseInt(value.toString()));
        }
        else if (value instanceof Long) {
            editor.putLong(name, Long.parseLong(value.toString()));
        }
        else if (value instanceof Boolean) {
            editor.putBoolean(name, Boolean.parseBoolean(value.toString()));
        }
        else if (value instanceof String) {
            editor.putString(name, value.toString());
        }
        else if (value instanceof Float) {
            editor.putFloat(name, Float.parseFloat(value.toString()));
        }
        editor.commit();
    }
}
