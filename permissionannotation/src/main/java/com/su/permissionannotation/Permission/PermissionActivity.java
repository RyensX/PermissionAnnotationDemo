package com.su.permissionannotation.Permission;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.su.permissionannotation.Interface.PermissionStatusListener;
import com.su.permissionannotation.R;

import java.lang.reflect.InvocationTargetException;


public class PermissionActivity extends Activity {

    private String TAG = this.toString();
    private String[] permissions;
    private int requestCode;
    static PermissionStatusListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        setData();
    }

    //获取数据
    private void setData() {
        permissions = getIntent().getExtras().getStringArray(PermissionUtils.NAME_PERMISSIONS);
        requestCode = getIntent().getIntExtra(PermissionUtils.NAME_REQUSETCODE, PermissionUtils.ERROR_REQUESTCODE);
        if (permissions == null || requestCode == PermissionUtils.ERROR_REQUESTCODE) {
            Log.d(TAG, "数据传递出错，终止申请");
            finish();
        }

        for (String permission : permissions)
            Log.d("申请权限", permission);

        requestPermissions();

    }

    //开始申请
    private void requestPermissions() {
        //检查是否已授权
        if (!PermissionUtils.checkPermissions(this, permissions)) {
            Log.d(TAG, "开始申请权限");
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        } else {
            listener.onSuccess();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.checkPermissions(this, permissions)) {
            listener.onSuccess();
            Log.d(TAG, "权限申请流程执行完毕");
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(this, permissions))
                switch (requestCode) {
                    case PermissionUtils.DEFAULT_REQUSETCODE:
                    case PermissionUtils.DEFAULT_AREQUSETCODE:
                        Log.d("授权回调", "默认Denial");
                        listener.onDefaultDenial();
                        break;
                    default:
                        Log.d("授权回调", "自定义Denial");
                        try {
                            listener.onCustomDenial();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                }

        }
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        listener = null;
        //屏蔽动画
        overridePendingTransition(0, 0);
    }
}