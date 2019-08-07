package com.su.permissionannotation.Interface;

public interface PermissionStatusListener {
    void onSuccess();
    void onDefaultDenial();
    void onDenial();
}