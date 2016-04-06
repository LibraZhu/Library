package com.libra.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import com.libra.R;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by libra on 15/7/31 下午4:31.
 */
public class UIUtils {

    /**
     * 确认框
     */
    public static AlertDialog showAlertDialog(Context context, String title, String message, String yes, DialogInterface.OnClickListener yesListener, String no, DialogInterface.OnClickListener noListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }
        if (yesListener != null) {
            builder.setPositiveButton(yes, yesListener);
        }
        if (noListener != null) {
            builder.setNegativeButton(no, noListener);
        }
        return builder.show();
    }


    /**
     * 输入框
     */
    public static AlertDialog.Builder showEditTextDialog(Context context, String title, final TextView view, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请输入");
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        final EditText editText = new EditText(context);
        editText.setSingleLine();
        builder.setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (view != null) {
                    view.setText(editText.getText().toString().trim());
                }
                if (listener != null) {
                    listener.onClick(dialog, 1);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) {
                    listener.onClick(dialog, 0);
                }
            }
        });
        builder.show();
        return builder;
    }


    /**
     * 单选框
     */
    public static void showChoiceDialog(Context context, String title, final String[] items, String selectValue, final TextView view, final DialogInterface.OnClickListener listener) {
        int checkedItem = -1;
        for (int i = 0; i < items.length; i++) {
            if (selectValue.equals(items[i])) {
                checkedItem = i;
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setSingleChoiceItems(items, checkedItem,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (view != null) {
                            view.setText(items[which]);
                        }
                        if (listener != null) {
                            listener.onClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                });
        builder.show();
    }


    /**
     * 日期选择弹出框
     */
    public static void showDateDialog(final Activity context, final String timeValue, final TextView view, final DialogInterface.OnClickListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View convertView = context.getLayoutInflater()
                                  .inflate(R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) convertView.findViewById(
                R.id.datePicker);
        //设置日期简略显示
        Calendar localCalendar = Calendar.getInstance();
        if (TextUtils.isEmpty(timeValue)) {
            localCalendar.setTime(new Date());
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                localCalendar.setTime(sdf.parse(timeValue));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        datePicker.init(localCalendar.get(Calendar.YEAR),
                localCalendar.get(Calendar.MONTH),
                localCalendar.get(Calendar.DAY_OF_MONTH), null);
        builder.setView(convertView);
        builder.setTitle("请选择");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                StringBuffer sb = new StringBuffer();
                sb.append(String.format("%d-%02d-%02d", datePicker.getYear(),
                        datePicker.getMonth() + 1, datePicker.getDayOfMonth()));
                view.setText(sb);
                if (listener != null) {
                    listener.onClick(dialog, which);
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /**
     * 选择头像弹出框
     */
    public static void showChoiceImage(final Activity context, String title, final DialogInterface.OnClickListener listener) {
        final String[] str = new String[] { "拍照", "图库" };
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            dialog.setTitle(title);
        }
        dialog.setItems(str, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onClick(dialogInterface, i);
            }
        });
        dialog.show();
    }
}
