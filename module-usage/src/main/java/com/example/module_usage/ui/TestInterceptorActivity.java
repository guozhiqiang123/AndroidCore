package com.example.module_usage.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.module_usage.R;
import com.example.module_usage.databinding.ActivityTestInterceptorBinding;
import com.example.module_usage.vm.TestInterceptorViewModel;
import com.gzq.lib_resource.mvvm.base.BaseActivity;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/usage/test/interceptor/activity")
public class TestInterceptorActivity extends BaseActivity<ActivityTestInterceptorBinding, TestInterceptorViewModel> {
    @Override
    public void initParam(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_test_interceptor;
    }

    @Override
    public TestInterceptorViewModel setViewModel(ActivityTestInterceptorBinding binding) {
        TestInterceptorViewModel viewModel = new TestInterceptorViewModel();
        binding.setVm(viewModel);
        return viewModel;
    }

    @Override
    public void setOtherModel(ActivityTestInterceptorBinding binding) {

    }


}
