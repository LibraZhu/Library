package com.libra.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by libra on 16/4/12 下午4:15.
 */
public class ToastUtil {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
