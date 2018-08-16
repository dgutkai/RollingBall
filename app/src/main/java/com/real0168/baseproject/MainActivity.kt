package com.real0168.baseproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.real0168.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
