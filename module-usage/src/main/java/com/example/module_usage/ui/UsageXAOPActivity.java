package com.example.module_usage.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.module_usage.R;
import com.example.module_usage.databinding.ActivityUsageXaopBinding;
import com.example.module_usage.vm.XAOPViewModel;
import com.gzq.lib_resource.mvvm.base.BaseActivity;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/usage/xaop/activity")
public class UsageXAOPActivity extends BaseActivity<ActivityUsageXaopBinding, XAOPViewModel> {
    @Override
    public void initParam(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_usage_xaop;
    }

    @Override
    public XAOPViewModel setViewModel(ActivityUsageXaopBinding binding) {
        XAOPViewModel xaopViewModel = new XAOPViewModel();
        binding.setVm(xaopViewModel);
        return xaopViewModel;
    }

    @Override
    public void setOtherModel(ActivityUsageXaopBinding binding) {

    }

}
