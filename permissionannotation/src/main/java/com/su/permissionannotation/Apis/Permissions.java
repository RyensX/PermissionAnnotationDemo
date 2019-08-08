package com.su.permissionannotation.Apis;

import com.su.permissionannotation.Permission.PermissionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permissions {
    String[] value();
    int requestCode() default PermissionUtils.DEFAULT_REQUSETCODE;
    String denialMsg() default PermissionUtils.DEFAULT_MSG;
}