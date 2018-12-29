package com.gzq.androidcore;

import android.bluetooth.BluetoothDevice;

import com.gzq.lib_bluetooth.BaseBluetooth;
import com.gzq.lib_bluetooth.BluetoothStore;
import com.gzq.lib_bluetooth.BluetoothType;
import com.gzq.lib_bluetooth.IBluetoothView;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;

import java.util.UUID;

public class BluetoothPresenter extends BaseBluetooth{

    public BluetoothPresenter(IBluetoothView owner) {
        super(owner);

        //直接连接，跳过搜索过程
        start(BluetoothType.BLUETOOTH_TYPE_BLE,"1B:F4:E2:8A:85:AB");
        //或者
        start(BluetoothType.BLUETOOTH_TYPE_BLE,null,"BLE1","BLE2");
        //或者
        start(BluetoothType.BLUETOOTH_TYPE_CLASSIC,"1B:F4:E2:8A:85:AB");
        //或者
        start(BluetoothType.BLUETOOTH_TYPE_CLASSIC,null,"BLE1","BLE2");
    }

    /**
     * 当搜索到设备都会回调
     * @param device
     */
    @Override
    protected void newDeviceFinded(BluetoothDevice device) {

    }

    /**
     * 指定时间内没有发现目标设备
     */
    @Override
    protected void noneFind() {

    }

    /**
     * 连接成功
     * @param name 连接设备的蓝牙名字
     * @param address 连接设备的mac地址
     */
    @Override
    protected void connectSuccessed(String name, String address) {
        BluetoothStore.getClient().notify(address, UUID.fromString(""), UUID.fromString(""), new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {

            }

            @Override
            public void onResponse(int code) {

            }
        });

        BluetoothStore.getClient().write(address, UUID.fromString(""), UUID.fromString(""), new byte[]{0x00,0x00}, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

            }
        });
    }

    /**
     * 连接失败
     */
    @Override
    protected void connectFailed() {

    }

    /**
     * 连接断开
     * @param address
     */
    @Override
    protected void disConnected(String address) {

    }
}
