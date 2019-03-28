package com.gzq.lib_resource.mvvm.base;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.databinding.ObservableField;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.SupportActivity;

import com.gzq.lib_resource.R;
import com.gzq.lib_resource.dialog.FDialog;

/**
 * Created by goldze on 2017/6/15.
 */
public abstract class BaseViewModel<V extends FragmentActivity> implements IBaseViewModel {
    protected V activity;
    private FDialog fd;

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        if (owner instanceof SupportActivity) {
            activity = (V) owner;
        } else if (owner instanceof Fragment) {
            activity = (V) ((Fragment) owner).getActivity();
        }
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
        hideLoadingDialog();
        owner.getLifecycle().removeObserver(this);
    }

    public void showLoadingDialog() {
        if (fd == null) {
            fd = FDialog.build()
                    .setSupportFM(activity.getSupportFragmentManager())
                    .setLayoutId(R.layout.dialog_layout_loading)
                    .setOutCancel(false)
                    .setDimAmount(0)
                    .show();
        } else {
            fd.show();
        }
    }

    public void hideLoadingDialog() {
        if (fd != null) {
            fd.dismiss();
        }
    }
}
