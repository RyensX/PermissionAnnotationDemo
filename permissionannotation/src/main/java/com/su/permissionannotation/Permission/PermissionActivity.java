package com.su.permissionannotation.Permission;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.su.permissionannotation.R;

import org.aspectj.lang.ProceedingJoinPoint;

public class PermissionActivity extends Activity {

    private String TAG = this.toString();
    private String[] permissions;
    private int requestCode;

    private static ProceedingJoinPoint joinPoint = null;

    public static void setJoinPoint(ProceedingJoinPoint point) {
        joinPoint = point;
    }

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

        try {
            requestPermissions();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    //开始申请
    private void requestPermissions() throws Throwable {
        //检查是否已授权
        if (!PermissionUtils.checkPermissions(this, permissions)) {
            Log.d(TAG, "开始申请权限");
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        } else {
            Log.d(TAG, permissions.length + "个权限" + "已全部授予");
            callMethob();
            finish();
        }
    }

    private void callMethob() throws Throwable {
        if (joinPoint != null)
            joinPoint.proceed();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.checkPermissions(this, permissions)) {
            Log.d(TAG, "权限申请流程执行完毕");
            try {
                callMethob();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(this, permissions))
                Toast.makeText(this, "用户拒绝授予权限", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        //屏蔽动画
        overridePendingTransition(0, 0);
    }
}