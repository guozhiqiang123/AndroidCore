package com.gzq.androidcore;


import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.View;

import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvvm.base.BaseViewModel;
import com.gzq.lib_resource.mvvm.binding.command.BindingAction;
import com.gzq.lib_resource.mvvm.binding.command.BindingCommand;

public class MainActivityModel extends BaseViewModel {
    public ObservableField<String> userName = new ObservableField<>();

    public MainActivityModel(@NonNull Application application) {
        super(application);
    }

    public BindingCommand bindingCommand=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });
}
