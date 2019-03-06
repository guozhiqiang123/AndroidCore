package com.gzq.androidcore;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.gzq.androidcore.databinding.ActivityMainBinding;
import com.gzq.lib_resource.mvvm.base.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainActivityModel> {


    @Override
    public int layoutId(Bundle savedInstanceState) {

        return R.layout.activity_main;
    }

    @Override
    public int initVariableId() {
        return BR.ViewMode;
    }

    @Override
    public MainActivityModel getViewModelSubClass() {
        return ViewModelProviders.of(this).get(MainActivityModel.class);
    }
}
