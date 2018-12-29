package com.gzq.androidcore;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import com.gzq.lib_bluetooth.BluetoothConnectHelper;
import com.gzq.lib_bluetooth.BluetoothSearchHelper;
import com.gzq.lib_bluetooth.ConnectListener;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.gzq.lib_bluetooth.SearchListener;
import com.gzq.lib_resource.mvp.base.BaseActivity;
import com.gzq.lib_resource.mvp.base.BasePresenter;
import com.gzq.lib_resource.mvp.base.IPresenter;
import com.gzq.lib_resource.mvp.base.IView;

public class MainActivity extends BaseActivity implements IBluetoothView{
    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initParams(Intent intentArgument, Bundle bundleArgument) {

        BluetoothSearchHelper bluetoothSearchHelper = new BluetoothSearchHelper();
        //每次8秒，搜索3次
        bluetoothSearchHelper.searchBle(8000, 3, new SearchListener() {
            @Override
            public void onSearching(boolean isOn) {
                //isOn=true:正在搜索，isOn=false:停止搜索
            }

            @Override
            public void onNewDeviceFinded(BluetoothDevice newDevice) {
                //搜索到新设备
            }

            @Override
            public void obtainDevice(BluetoothDevice device) {
                //搜索到目标设备
            }

            @Override
            public void noneFind() {
                //没有搜索到目标设备
            }
        },"Ble1","Ble2");

        bluetoothSearchHelper.clear();

        BluetoothConnectHelper bluetoothConnectHelper=new BluetoothConnectHelper();
        bluetoothConnectHelper.connect("1B:F4:E2:8A:85:AB", new ConnectListener() {
            @Override
            public void success(BluetoothDevice device) {
                //连接成功
            }

            @Override
            public void failed() {
                //连接失败
            }

            @Override
            public void disConnect(String address) {
                //断开连接
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
