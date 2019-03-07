package com.gzq.lib_resource.mvvm.base;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

public abstract class BaseController implements IController{
    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {

    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {

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
    }
}
