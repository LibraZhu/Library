package com.libra.utils;

import android.app.Activity;
import java.util.Stack;

public class AppManager {
    private static Stack<Activity> activityStack;

    private static AppManager instance;

    private Activity visibleActivity;


    private AppManager() {
    }


    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }


    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }


    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            Activity activity = activityStack.lastElement();
            return activity;
        }
        return null;
    }


    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            Activity activity = activityStack.lastElement();
            finishActivity(activity);
        }
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }


    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        }
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            Stack<Activity> stack = (Stack<Activity>) activityStack.clone();
            activityStack.clear();
            for (int i = 0, size = stack.size(); i < size; i++) {
                if (null != stack.get(i)) {
                    stack.get(i).finish();
                }
            }
        }
    }


    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
        }
    }


    public Activity getVisibleActivity() {
        return visibleActivity;
    }


    public void setVisibleActivity(Activity visibleActivity) {
        this.visibleActivity = visibleActivity;
    }
}