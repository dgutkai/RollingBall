package com.real0168.baseproject;

import android.os.Bundle;
import android.support.annotation.Nullable;

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
}
