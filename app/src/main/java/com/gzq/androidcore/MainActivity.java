package com.gzq.androidcore;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.exception.ApiException;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvp.base.BaseActivity;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.utils.LoadingObserver;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.util.PermissionUtils;

import java.util.List;

public class MainActivity extends BaseActivity {
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {
        XAOP.init(getApplication());
        XAOP.debug(true);
        XAOP.setOnPermissionDeniedListener(new PermissionUtils.OnPermissionDeniedListener() {
            @Override
            public void onDenied(List<String> permissionsDenied) {
                ToastUtils.showShort(Box.getGson().toJson(permissionsDenied));
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

    @SingleClick(10000)
    @Permission({Manifest.permission_group.CAMERA, Manifest.permission_group.STORAGE})
    public void testclick(View view) {
        ToastUtils.showShort("哈哈哈啊哈");
    }
}
