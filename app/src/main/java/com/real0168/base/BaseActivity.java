package com.real0168.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by lanmi on 2018/8/16.
 */

public abstract class BaseActivity extends PermissionsActivity {

    public abstract int getLayoutID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
    }
}
