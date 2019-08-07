package com.su.permissionannotation.Permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    static final String NAME_PERMISSIONS = "permissions";
    static final String NAME_REQUSETCODE = "requestCode";
    public final static int DEFALUT_REQUSETCODE = 100;
    public final static int ERROR_REQUESTCODE = -1;

    //开始进行权限申请流程
    public static void requestPermission(Context context, int requsetCode, String... permissions) {
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(NAME_REQUSETCODE, requsetCode);
        intent.putExtra(NAME_PERMISSIONS, permissions);
        context.startActivity(intent);
        //屏蔽过渡动画
        if (context instanceof Activity)
            ((Activity) context).overridePendingTransition(0, 0);
    }

    //检查权限是否授予
    public static boolean checkPermissions(Context context, String... permissions) {
        boolean isHas = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (permissions != null)
                for (String permission : permissions)
                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
                        isHas = true;
                    else
                        return false;
            else
                return false;
        else
            return true;
        return isHas;
    }

    //检查是否需要弹出提示（仅在用户直接拒绝且不勾选不再提示时返回true）
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions)
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                return true;
        return false;
    }
}