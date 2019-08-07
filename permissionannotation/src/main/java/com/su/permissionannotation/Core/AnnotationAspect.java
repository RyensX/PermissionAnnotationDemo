package com.su.permissionannotation.Core;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.su.permissionannotation.Apis.APermissions;
import com.su.permissionannotation.Apis.Permissions;
import com.su.permissionannotation.Permission.PermissionActivity;
import com.su.permissionannotation.Permission.PermissionUtils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AnnotationAspect {

    /*
     *Around方式方式Advice，被注解方法不可返回值，否则无法编译，但可根据授权情况决定是否调用（即授权后才会调用）
     */
    @Pointcut("call(@com.su.permissionannotation.Apis.APermissions * *(..)) && @annotation(permissions)")
    public void APermissionsInject(APermissions permissions) {

    }

    @Around("APermissionsInject(permissions)")
    public void onAPermissionsInject(ProceedingJoinPoint joinPoint, APermissions permissions) {
        Log.d("切入点", joinPoint.getSignature().getName());

        Object object = joinPoint.getThis();
        Context context = getContext(object);


        if (context == null || permissions == null)
            return;

        for (String per : permissions.value())
            Log.d("注解值", per);

        PermissionActivity.setJoinPoint(joinPoint);
        PermissionUtils.requestPermission(context, permissions.requestCode(), permissions.value());
    }

    /*
     * Before方式Advice，被注解方法可返回值，但不可根据授权情况决定是否调用（是否授权都会调用）
     */
    @Pointcut("call(@com.su.permissionannotation.Apis.Permissions * *(..)) && @annotation(permissions)")
    public void PermissionsInject(Permissions permissions) {

    }

    @Before("PermissionsInject(permissions)")
    public void onPermissionsInject(JoinPoint joinPoint, Permissions permissions) {
        Log.d("切入点", joinPoint.getSignature().getName());

        Object object = joinPoint.getThis();
        Context context = getContext(object);

        if (context == null || permissions == null)
            return;

        for (String per : permissions.value())
            Log.d("注解值", per);

        //PermissionActivity.setJoinPoint(joinPoint);
        PermissionUtils.requestPermission(context, permissions.requestCode(), permissions.value());
    }

    private Context getContext(Object object) {
        Context context = null;
        if (object instanceof Activity)
            context = (Context) object;
        else if (object instanceof android.app.Fragment)
            context = ((android.app.Fragment) object).getActivity();
        else if (object instanceof android.support.v4.app.Fragment)
            context = ((android.support.v4.app.Fragment) object).getActivity();
        return context;
    }
}