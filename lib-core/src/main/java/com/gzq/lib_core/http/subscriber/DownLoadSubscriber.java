package com.gzq.lib_core.http.subscriber;

import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.toast.T;

/**
 * Created by wzm on 2017/6/17.
 */

public abstract class DownLoadSubscriber extends BaseSubscriber {

    @Override
    public void onNext(Object o) {
        if (o instanceof Integer) {
            _onProgress((Integer) o);
        }

        if (o instanceof String) {
            _onNext((String) o);
        }
        mSubscription.request(1);
    }

    @Override
    protected void onError(ApiException ex) {
        T.show(ex.message+":"+ex.code);
    }

    protected abstract void _onNext(String result);

    protected abstract void _onProgress(Integer percent);

}
