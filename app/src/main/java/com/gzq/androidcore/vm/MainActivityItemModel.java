package com.gzq.androidcore.vm;

import android.databinding.ObservableField;

import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvvm.base.RVItemBaseModel;
import com.gzq.lib_resource.mvvm.binding.command.BindingAction;
import com.gzq.lib_resource.mvvm.binding.command.BindingCommand;

public class MainActivityItemModel extends RVItemBaseModel<MainActivityModel> {
    public ObservableField<String> item = new ObservableField<>();

    public MainActivityItemModel(MainActivityModel viewModel, String string) {
        super(viewModel);
        this.item.set(string);
    }

    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtils.showShort("点击了第" + (viewModel.menus.indexOf(MainActivityItemModel.this) + 1) + "个");
        }
    });
}
