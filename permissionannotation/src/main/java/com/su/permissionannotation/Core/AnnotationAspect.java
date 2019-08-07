package com.su.permissionannotation.Core;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.su.permissionannotation.Apis.Permissions;
import com.su.permissionannotation.Permission.PermissionActivity;
import com.su.permissionannotation.Permission.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class AnnotationAspect {

    @Pointcut("call(@com.su.permissionannotation.Apis.Permissions * *(..)) && @annotation(permissions)")
    public void PermissionsInject(Permissions permissions) {

    }

    @Around("PermissionsInject(permissions)")
    public void onPermissionsInject(ProceedingJoinPoint joinPoint, Permissions permissions) {
        Log.d("切入点", joinPoint.getSignature().getName());

        Object object = joinPoint.getThis();
        Context context = null;
        if (object instanceof Activity)
            context = (Context) object;
        else if (object instanceof android.app.Fragment)
            context = ((android.app.Fragment) object).getActivity();
        else if (object instanceof android.support.v4.app.Fragment)
            context = ((android.support.v4.app.Fragment) object).getActivity();

        if (context == null || permissions == null)
            return;

        for (String per : permissions.value())
            Log.d("注解值", per);

        PermissionActivity.setJoinPoint(joinPoint);
        PermissionUtils.requestPermission(context, permissions.requestCode(), permissions.value());
    }

}