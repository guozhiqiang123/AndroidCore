package com.gzq.androidcore;

import android.content.Intent;
import android.os.Bundle;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.mvp.base.BaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;

public class MainActivity extends BaseActivity {
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        Box.getRetrofit(API.class)
                .test()
                //自定义的数据流转换器
                .compose(RxUtils.httpResponseTransformer())
                //Retrofit的生命周期管理
                .as(RxUtils.autoDisposeConverter(this))
                //CommonObserver中加入了网络监测
                .subscribe(new CommonObserver<Object>(900) {
                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    protected void onNetError() {

                    }

                    @Override
                    protected void onEmptyData() {

                    }
                });

    }

    @Override
    public void initView() {

    }

    @Override
    public IPresenter obtainPresenter() {
        return null;
    }


}
