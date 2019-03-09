package com.gzq.androidcore.vm;


import android.databinding.ObservableField;

import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvvm.base.BaseViewModel;
import com.gzq.lib_resource.mvvm.binding.command.BindingAction;
import com.gzq.lib_resource.mvvm.binding.command.BindingCommand;

public class MainActivityModel extends BaseViewModel {
    private int position = 0;
    public ObservableField<String> userName = new ObservableField<>("按钮");

    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("登录成功");
        }
    });
}
