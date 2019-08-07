package com.su.permissionannotationdemo;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.su.permissionannotation.Apis.Permissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testMethod(111);
    }


    @Permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private void testMethod(int t) {
        Log.d("注入测试,执行方法", "testMethod" + t);
    }
}