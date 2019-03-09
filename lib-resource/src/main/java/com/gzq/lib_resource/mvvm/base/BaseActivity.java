package com.gzq.lib_resource.mvvm.base;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import me.yokeyword.fragmentation.SupportActivity;


/**
 * Created by gzq on 2017/6/15.
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends SupportActivity {
    protected V binding;
    protected VM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面接受的参数方法
        initParam(getIntent(), getIntent().getExtras());
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除ViewModel生命周期感应
        getLifecycle().removeObserver(viewModel);
        if (binding != null) {
            binding.unbind();
        }
    }

    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, layoutId(savedInstanceState));
        viewModel = setViewModel(binding);
        if (viewModel != null) {
            getLifecycle().addObserver(viewModel);
        }
        setOtherModel(binding);
    }


    public abstract void initParam(Intent intentArgument, Bundle bundleArgument);

    public abstract int layoutId(Bundle savedInstanceState);

    public abstract VM setViewModel(V binding);
    /**
     * 如果不止ViewModel和Controller这个两variable设置到binding中
     *
     * @param binding
     */
    public abstract void setOtherModel(V binding);
}
