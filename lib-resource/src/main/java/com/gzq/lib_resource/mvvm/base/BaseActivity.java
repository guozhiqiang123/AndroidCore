package com.gzq.lib_resource.mvvm.base;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;

import java.lang.reflect.ParameterizedType;

import me.yokeyword.fragmentation.SupportActivity;


/**
 * Created by goldze on 2017/6/15.
 * 一个拥有DataBinding框架的基Activity
 */

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel>
        extends SupportActivity implements IBaseActivity {
    protected V binding;
    protected VM viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParams();

        initViewDataBinding(savedInstanceState);

        initViewObservable();

        getLifecycle().addObserver(viewModel);

        fillData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel = null;
        if (binding != null) {
            binding.unbind();
            binding = null;
        }
    }

    /**
     * 注入绑定
     */
    private void initViewDataBinding(Bundle savedInstanceState) {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState));
        binding.setVariable(initVariableId(), viewModel = getViewModelSubClass());

    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null && binding != null) {
            binding.setVariable(initVariableId(), viewModel);
        }
    }

    @Override
    public void initParams() {

    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(Bundle savedInstanceState);

    /**
     * 初始化ViewModel的id
     *
     * @return BR的id
     */
    public abstract int initVariableId();


    public VM getViewModelSubClass() {
        Class<VM> vmClass = (Class<VM>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[1];
        return ViewModelProviders.of(this).get(vmClass);
    }

    @Override
    public void initViewObservable() {

    }

    public void fillData() {
    }
}
