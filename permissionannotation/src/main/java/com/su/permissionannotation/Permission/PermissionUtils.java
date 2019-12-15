package com.su.permissionannotation.Permission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;

import com.su.permissionannotation.Apis.PermissionDenial;
import com.su.permissionannotation.Interface.PermissionStatusListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PermissionUtils {

    static final String NAME_PERMISSIONS = "permissions";
    static final String NAME_REQUSETCODE = "requestCode";
    public static final String ROOT_PERMISSION = "android.permission.ROOT";//root权限值
    public static final int DEFAULT_REQUSETCODE = 100;//Before方式Advice默认requestCode
    public static final int DEFAULT_AREQUSETCODE = 200;//Around方式Advice默认requestCode
    public static final int ERROR_REQUESTCODE = -1;
    public static final String DEFAULT_MSG = "本应用需要一定权限才可正常运行，请授予权限";//默认权限说明

    //开始进行权限申请流程
    public static void requestPermission(PermissionStatusListener listener, Context context, int requestCode, String... permissions) {
        if (permissions.length == 0)
            return;
        PermissionActivity.listener = listener;
        //若发现有申请root，则优先向系统发起申请并过滤权限数组
        RequestRootPermission rrp = new RequestRootPermission(permissions);
        if (!rrp.isNoRequestRoot()) {
            //需要申请root权限
            if (rrp.request(context))
                //申请成功
                permissions = rrp.getPermissions();
            else {
                //申请失败
                if (requestCode == DEFAULT_REQUSETCODE || requestCode == DEFAULT_AREQUSETCODE)
                    //调用默认拒绝回调
                    listener.onDefaultDenial();
                else {
                    //调用自定义拒绝回调
                    try {
                        listener.onCustomDenial();
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                //root没有申请成功其他权限就洗洗睡吧
                return;
            }
        }
        //申请常规权限
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(NAME_REQUSETCODE, requestCode);
        intent.putExtra(NAME_PERMISSIONS, permissions);
        context.startActivity(intent);
        //屏蔽过渡动画
        if (context instanceof Activity)
            ((Activity) context).overridePendingTransition(0, 0);
    }

    //检查权限是否授予
    static boolean checkPermissions(Context context, String... permissions) {
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

    //调用被@PermissionDenial注解的对应requestCode方法
    public static void onCustomPermissionDenial(Object object, int requestCode) throws InvocationTargetException, IllegalAccessException {
        //TODO 利用Aspect注入拒绝回调
        long s = System.currentTimeMillis();
        Class<?> c = object.getClass();
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(PermissionDenial.class)) {
                PermissionDenial permissionDenial = method.getAnnotation(PermissionDenial.class);
                if (permissionDenial.requestCode() == requestCode) {
                    method.invoke(object);
                    Log.d("Denial回调耗时：", System.currentTimeMillis() - s + "ms");
                    return;
                }
            }
        }
    }

    //默认权限说明
    public static void showDefaultMsg(Context context, String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("权限说明")
                .setMessage(msg)
                .setNegativeButton("了解", null)
                .create();
        alertDialog.show();
    }
}