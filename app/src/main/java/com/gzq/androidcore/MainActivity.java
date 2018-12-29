package com.gzq.androidcore;

import android.content.Intent;
import android.os.Bundle;

import com.gzq.lib_resource.mvp.base.BaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.mvp.base.IView;

public class MainActivity extends BaseActivity{
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public void initView() {

    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }
}
