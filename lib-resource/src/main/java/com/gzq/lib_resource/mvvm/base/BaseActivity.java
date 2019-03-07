package com.gzq.lib_resource.mvvm.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import me.yokeyword.fragmentation.SupportActivity;


/**
 * Created by goldze on 2017/6/15.
 * 一个拥有DataBinding框架的基Activity
 * 这里根据项目业务可以换成你自己熟悉的BaseActivity, 但是需要继承RxAppCompatActivity,方便LifecycleProvider管理生命周期
 */
public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel, C extends IController> extends SupportActivity {
    protected V binding;
    protected VM viewModel;
    protected C controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面接受的参数方法
        initParam();
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
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

    private void initViewDataBinding() {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, layoutId());
        viewModel = setViewModel(binding);
        controller = setController(binding);
        if (viewModel != null) {
            getLifecycle().addObserver(viewModel);
        }
        if (controller != null) {
            getLifecycle().addObserver(controller);
        }
        setOtherModel(binding);
    }


    public abstract void initParam();

    public abstract int layoutId();

    public abstract VM setViewModel(V binding);

    public abstract C setController(V binding);

    /**
     * 如果不止这ViewModel和Controller这个两variable设置到binding中
     *
     * @param binding
     */
    public abstract void setOtherModel(V binding);
}
