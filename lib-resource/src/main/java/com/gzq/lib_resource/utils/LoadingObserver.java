package com.gzq.lib_resource.utils;

import android.arch.lifecycle.LifecycleOwner;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.BaseObserver;
import com.gzq.lib_core.utils.NetworkUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.R;
import com.gzq.lib_resource.dialog.FDialog;


public abstract class LoadingObserver<T> extends BaseObserver<T> {

    private FragmentManager fragmentManager = null;
    private FDialog fd;

    public LoadingObserver(LifecycleOwner owner) {
        if (owner instanceof FragmentActivity) {
            fragmentManager = ((FragmentActivity) owner).getSupportFragmentManager();
        } else if (owner instanceof Fragment) {
            fragmentManager = ((Fragment) owner).getFragmentManager();
        }
    }

    @Override
    protected void onNetError() {
        ToastUtils.showShort("当前无网络，请检查网络情况");
    }

    @Override
    protected void onError(ApiException ex) {
        hideLoading();
        ToastUtils.showShort(ex.message + ":" + ex.code);
    }

    @Override
    public abstract void onNext(T t);


    @Override
    public void onComplete() {
        hideLoading();
    }

    private void showLoading() {
        if (fragmentManager != null) {
            fd = FDialog.build()
                    .setSupportFM(fragmentManager)
                    .setLayoutId(R.layout.dialog_layout_loading)
                    .setOutCancel(false)
                    .show();
        }
    }

    private void hideLoading() {
        if (fd != null) {
            fd.dismiss();
        }
    }
}
