package com.gzq.lib_resource.mvvm.base;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.ParameterizedType;
import me.yokeyword.fragmentation.SupportFragment;

public abstract class BaseFragment<V extends ViewDataBinding, VM extends BaseViewModel>
        extends SupportFragment implements IBaseActivity {
    protected V binding;
    protected VM viewModel;
    protected View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel = null;
        if (binding != null) {
            binding.unbind();
            binding = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false);
            binding.setVariable(initVariableId(), viewModel = getViewModelSubClass());
            mView = binding.getRoot();
            getLifecycle().addObserver(viewModel);
        }
        fillData();
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mView != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
            mView = null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewObservable();
    }

    @Override
    public void initParams() {

    }

    //刷新布局
    public void refreshLayout() {
        if (viewModel != null&&binding!=null) {
            binding.setVariable(initVariableId(), viewModel);
        }
    }

    /**
     * 初始化根布局
     *
     * @return 布局layout的id
     */
    public abstract int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

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
