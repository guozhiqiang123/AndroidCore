package com.gzq.lib_resource.mvp.base;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.CallSuper;

/**
 * Created by afirez on 2017/7/12.
 */

public abstract class BasePresenter<V extends IView>
        implements IPresenter {
    private V mView;

    private LifecycleOwner mLifecycleOwner;

    public V getV() {
        return mView;
    }

    public LifecycleOwner getLife() {
        return mLifecycleOwner;
    }

    public BasePresenter(V view) {
        this.mView = view;
    }

    @CallSuper
    @Override
    public void onCreate(LifecycleOwner owner) {
        this.mLifecycleOwner=owner;
    }

    @Override
    public void onStart(LifecycleOwner owner) {

    }

    @Override
    public void onResume(LifecycleOwner owner) {

    }

    @Override
    public void onPause(LifecycleOwner owner) {

    }

    @Override
    public void onStop(LifecycleOwner owner) {

    }

    @CallSuper
    @Override
    public void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
        mView=null;
        mLifecycleOwner=null;
    }

}