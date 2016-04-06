package com.libra.rxpermissions;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.libra.view.base.BaseActivity;

/**
 * Created by libra on 16/4/6 上午10:55.
 */
@TargetApi(Build.VERSION_CODES.M) public class PermissionActivity
        extends BaseActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }


    @Override protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        String[] permissions = intent.getStringArrayExtra("permissions");
        requestPermissions(permissions, 42);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        RxPermissions.getInstance(this)
                     .onRequestPermissionsResult(requestCode, permissions,
                             grantResults);
        finish();
    }
}
