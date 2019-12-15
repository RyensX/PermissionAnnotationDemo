package com.su.permissionannotation.Permission;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class RequestRootPermission {

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
            Log.d("权限解析", "申请结果code=" + code);
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
                Log.d("权限解析", "申请root");
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
