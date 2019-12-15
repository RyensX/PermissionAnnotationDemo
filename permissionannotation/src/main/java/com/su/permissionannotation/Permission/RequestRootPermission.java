package com.su.permissionannotation.Permission;

import android.content.Context;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class RequestRootPermission {

    private static final String TAG = "Root权限申请";
    private boolean isNoRequestRoot = true;
    private String[] permissions;

    public RequestRootPermission(String[] permissions) {
        this.permissions = getNoRootPermissions(permissions);
    }

    public boolean request(Context context) {
        DataOutputStream dataOutputStream = null;
        try {
            Process process = Runtime.getRuntime().exec("su");
            OutputStream outputStream = process.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes("chmod 777 " + context.getPackageCodePath());
            dataOutputStream.flush();
            outputStream.close();
            int code = process.waitFor();
            PermissionUtils.LogUtils.d(TAG, "申请结果code=" + (code == 0));
            return code == 0;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null)
                    dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String[] getNoRootPermissions(String[] permissions) {
        List<String> ps = new ArrayList<>();
        for (String s : permissions)
            if (!s.equals(PermissionUtils.ROOT_PERMISSION))
                ps.add(s);
            else {
                isNoRequestRoot = false;
                PermissionUtils.LogUtils.d(TAG, "开始申请Root");
            }
        return ps.toArray(new String[ps.size()]);
    }

    public String[] getPermissions() {
        return permissions;
    }

    public boolean isNoRequestRoot() {
        return isNoRequestRoot;
    }
}
