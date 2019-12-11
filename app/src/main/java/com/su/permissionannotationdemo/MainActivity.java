package com.su.permissionannotationdemo;

import android.Manifest;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.su.permissionannotation.Apis.APermissions;
import com.su.permissionannotation.Apis.PermissionDenial;
import com.su.permissionannotation.Apis.Permissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testMethod(111);
        testMethod2(222);
    }


    @Permissions(value = Manifest.permission.WRITE_EXTERNAL_STORAGE, denialMsg = "测试msg")
    public int testMethod(int t) {
        Log.d("注入测试,执行方法", "testMethod" + t);
        return t;
    }

    @APermissions(value = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE}, requestCode = 111)
    public void testMethod2(int t) {
        Log.d("注入测试,执行方法", "testMethod2" + t);
    }

    @PermissionDenial(requestCode = 111)
    private void testMethod2Denial() {
        AlertDialog al = new AlertDialog.Builder(this)
                .setTitle("fdfd")
                .setMessage("sdufhusdfidjs")
                .setPositiveButton("测试", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        recreate();
                    }
                })
                .create();
        al.show();
    }
}