package com.gzq.androidcore.controller;

import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvvm.base.BaseController;
import com.gzq.lib_resource.mvvm.binding.command.BindingAction;
import com.gzq.lib_resource.mvvm.binding.command.BindingCommand;

public class MainController extends BaseController {
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("登录成功");
        }
    });
}
