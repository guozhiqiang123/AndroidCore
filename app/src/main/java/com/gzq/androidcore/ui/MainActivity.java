package com.gzq.androidcore.ui;

import android.content.Intent;
import android.os.Bundle;

import com.gzq.androidcore.R;
import com.gzq.androidcore.databinding.ActivityMainBinding;
import com.gzq.androidcore.vm.MainActivityModel;
import com.gzq.lib_resource.mvvm.base.BaseActivity;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/app/main/activity")
public class MainActivity extends BaseActivity<ActivityMainBinding, MainActivityModel> {


    @Override
    public void initParam(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public MainActivityModel setViewModel(ActivityMainBinding binding) {
        MainActivityModel mainActivityModel = new MainActivityModel();
        binding.setVm(mainActivityModel);
        return mainActivityModel;
    }

    @Override
    public void setOtherModel(ActivityMainBinding binding) {

    }


}
