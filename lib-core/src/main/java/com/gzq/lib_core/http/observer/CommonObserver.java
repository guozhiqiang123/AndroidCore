package com.gzq.lib_core.http.observer;

import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.utils.ToastUtils;


public abstract class CommonObserver<T> extends BaseObserver<T> {
    private int emptyCode;

    public CommonObserver() {
    }

    public CommonObserver(int emptyDataCode) {
        this.emptyCode = emptyDataCode;
    }

    @Override
    protected void onError(ApiException ex) {
        ToastUtils.showShort(ex.message + ":" + ex.code);
        if (emptyCode != 0 && ex.code == emptyCode) {
            onEmptyData();
        }
    }

    @Override
    protected void onNetError() {
        ToastUtils.showShort("当前无网络，请检查网络情况");
    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onComplete() {

    }

    protected void onEmptyData() {

    }
}
