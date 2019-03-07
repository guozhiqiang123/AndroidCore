package com.gzq.androidcore;

import com.gzq.androidcore.controller.MainController;
import com.gzq.androidcore.databinding.ActivityMainBinding;
import com.gzq.androidcore.vm.MainActivityModel;
import com.gzq.lib_resource.mvvm.base.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainActivityModel, MainController> {

    @Override
    public void initParam() {

    }

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public MainActivityModel setViewModel(ActivityMainBinding binding) {
        MainActivityModel mainActivityModel = new MainActivityModel();
        binding.setVm(mainActivityModel);
        return mainActivityModel;
    }

    @Override
    public MainController setController(ActivityMainBinding binding) {
        MainController mainController = new MainController();
        binding.setController(mainController);
        return mainController;
    }

    @Override
    public void setOtherModel(ActivityMainBinding binding) {

    }


}
