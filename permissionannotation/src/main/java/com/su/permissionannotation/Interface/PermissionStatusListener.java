package com.su.permissionannotation.Interface;

import java.lang.reflect.InvocationTargetException;

public interface PermissionStatusListener {
    void onSuccess();
    void onDefaultDenial();
    void onCustomDenial() throws InvocationTargetException, IllegalAccessException;
}