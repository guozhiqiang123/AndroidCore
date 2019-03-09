package com.gzq.lib_resource.mvvm.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.yokeyword.fragmentation.SupportFragment;

public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel> extends SupportFragment {
    protected V binding;
    protected VM viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParam(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, layoutId(savedInstanceState), container, false);
        //私有的初始化Databinding和ViewModel方法
        initViewDataBinding();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        if (binding != null) {
            binding.unbind();
        }
        if (viewModel != null) {
            getLifecycle().removeObserver(viewModel);
        }
        super.onDestroyView();
    }

    private void initViewDataBinding() {
        viewModel = setViewModel(binding);
        if (viewModel != null) {
            getLifecycle().addObserver(viewModel);
        }
        setOtherModel(binding);
    }

    public abstract void initParam(Bundle bundle);

    public abstract int layoutId(Bundle savedInstanceState);

    public abstract VM setViewModel(V binding);


    /**
     * 如果不止ViewModel和Controller这个两variable设置到binding中
     *
     * @param binding
     */
    public abstract void setOtherModel(V binding);
}
