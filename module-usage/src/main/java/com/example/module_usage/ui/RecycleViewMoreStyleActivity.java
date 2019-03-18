package com.example.module_usage.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.module_usage.R;
import com.example.module_usage.databinding.ActivityRecycleviewMorestyleBinding;
import com.example.module_usage.vm.MoreStyleViewModel;
import com.gzq.lib_resource.mvvm.base.BaseActivity;
import com.sjtu.yifei.annotation.Route;

@Route(path = "/usage/recycleview/morestyle")
public class RecycleViewMoreStyleActivity extends BaseActivity<ActivityRecycleviewMorestyleBinding, MoreStyleViewModel> {
    @Override
    public void initParam(Intent intentArgument, Bundle bundleArgument) {

    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_recycleview_morestyle;
    }

    @Override
    public MoreStyleViewModel setViewModel(ActivityRecycleviewMorestyleBinding binding) {
        MoreStyleViewModel viewModel = new MoreStyleViewModel();
        binding.setVm(viewModel);
        return viewModel;
    }

    @Override
    public void setOtherModel(ActivityRecycleviewMorestyleBinding binding) {

    }
}
