package com.example.module_test1;

import android.content.Intent;
import android.os.Bundle;

import com.gzq.lib_resource.mvp.StateBaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;

public class TestActivity extends StateBaseActivity {
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return 0;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
    }

    @Override
    public void initView() {

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }
}
