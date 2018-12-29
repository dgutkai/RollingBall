package com.real0168.rollingball;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.real0168.base.BaseActivity;

public class MainActivity extends BaseActivity {
    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void buttonClick(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("你点击了按键" + view.getTag())
                .setNegativeButton("确定", null).create();
        alertDialog.show();
    }
}
