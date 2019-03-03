package com.gzq.androidcore;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import com.gzq.lib_bluetooth.BluetoothConnectHelper;
import com.gzq.lib_bluetooth.BluetoothSearchHelper;
import com.gzq.lib_bluetooth.ConnectListener;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.gzq.lib_bluetooth.SearchListener;
import com.gzq.lib_pay.OnSuccessAndErrorListener;
import com.gzq.lib_pay.alipay.AliPayUtils;
import com.gzq.lib_pay.wechat.WechatPayUtils;
import com.gzq.lib_resource.mvp.base.BaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.mvp.base.IView;
import com.sjtu.yifei.route.Routerfit;

public class MainActivity extends BaseActivity implements IBluetoothView{
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

        AliPayUtils.aliPay(this, "后台获取的订单信息", new OnSuccessAndErrorListener() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onError(String s) {

            }
        });
        WechatPayUtils.wechatPayApp(this, "AppId", "商户号", "私钥",
                "预支付交易会话ID", "发起支付的时间戳", "签名", new OnSuccessAndErrorListener() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onError(String s) {

                    }
                });
    }

    @Override
    public void initView() {

    }

    @Override
    public IPresenter obtainPresenter() {
        return new BasePresenter(this) {};
    }

    @Override
    public void updateData(String... datas) {

    }

    @Override
    public void updateState(String state) {

    }
}
