package com.libra.view.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.libra.BaseApp;
import com.libra.R;
import com.libra.utils.AppManager;

/**
 * Created by libra on 16/3/7 下午1:22.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected String mProgressMessage = "";
    protected ProgressDialog mProgressDialog;
    protected Toolbar toolbar;
    protected TextView toolbarTitle, toolbarLeft;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        ((BaseApp) getApplication()).dispatchActivityCreated(this);
    }


    @Override protected void onStart() {
        super.onStart();
        ((BaseApp) getApplication()).dispatchActivityStarted(this);
    }


    @Override protected void onResume() {
        super.onResume();
        AppManager.getAppManager().setVisibleActivity(this);
        ((BaseApp) getApplication()).dispatchActivityResumed(this);
    }


    @Override protected void onPause() {
        super.onPause();
        ((BaseApp) getApplication()).dispatchActivityPaused(this);
    }


    @Override protected void onStop() {
        super.onStop();
        ((BaseApp) getApplication()).dispatchActivitStoped(this);
    }


    @Override protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        ((BaseApp) getApplication()).dispatchActivityDistroyed(this);
    }


    @Override protected void onRestart() {
        super.onRestart();
        ((BaseApp) getApplication()).dispatchActivityRestarted(this);
    }


    @Override protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }


    @Override public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    public void startActivityNoAnim(Intent intent) {
        super.startActivity(intent);
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


    public void finishNoAnim() {
        super.finish();
    }


    protected void initTollBar() {
        toolbar = (Toolbar) $(R.id.toolbar);
        if (toolbar != null) {
            this.setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    onBackPressed();
                }
            });
            toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
            if (toolbarTitle != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
    }


    protected void showBackButton(boolean visible) {
        getSupportActionBar().setDisplayShowHomeEnabled(visible);
        getSupportActionBar().setDisplayHomeAsUpEnabled(visible);
    }


    protected <T extends View> T $(@IdRes int id) {
        return (T) findViewById(id);
    }


    protected <T extends View> T $(View view, @IdRes int id) {
        return (T) view.findViewById(id);
    }


    public void showProgressDialog() {
        showProgressDialog("");
    }


    public void showProgressDialog(int resId) {
        showProgressDialog(getResources().getString(resId));
    }


    /**
     * 描述：显示进度框.
     *
     * @param message the message
     */
    public void showProgressDialog(String message) {
        String msg = mProgressMessage;
        if (!TextUtils.isEmpty(message)) {
            msg = message;
        }
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            // 设置点击屏幕Dialog不消失
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        closeProgressDialog();
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }


    /**
     * 关闭进度框
     */
    public void closeProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() &&
                !isFinishing()) {
            mProgressDialog.dismiss();
        }
    }


    /**
     * 显示短时间Toast
     */
    public void showShortToast(int pResId) {
        showShortToast(getString(pResId));
    }


    /**
     * 显示长时间Toast
     */
    public void showLongToast(int pResId) {
        showLongToast(getString(pResId));
    }


    /**
     * 显示短时间Toast
     */
    public void showShortToast(String pMsg) {
        if (!TextUtils.isEmpty(pMsg)) {
            Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 显示长时间Toast
     */
    public void showLongToast(String pMsg) {
        if (!TextUtils.isEmpty(pMsg)) {
            Toast.makeText(this, pMsg, Toast.LENGTH_LONG).show();
        }
    }


    public int dip2px(float dipValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * px转dip
     *
     * @return int
     */
    public int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
