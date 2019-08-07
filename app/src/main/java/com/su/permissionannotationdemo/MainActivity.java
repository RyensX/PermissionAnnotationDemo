package com.su.permissionannotationdemo;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.su.permissionannotation.Apis.APermissions;
import com.su.permissionannotation.Apis.Permissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testMethod(111);
        testMethod2(222);
    }


    @Permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public int testMethod(int t) {
        Log.d("注入测试,执行方法", "testMethod" + t);
        return t;
    }

    @APermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    public void testMethod2(int t) {
        Log.d("注入测试,执行方法", "testMethod2" + t);
    }
}