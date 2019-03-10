package com.gzq.androidcore.vm;

import android.databinding.ObservableField;

import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvvm.base.RVItemBaseModel;
import com.gzq.lib_resource.mvvm.binding.command.BindingAction;
import com.gzq.lib_resource.mvvm.binding.command.BindingCommand;
import com.gzq.lib_resource.router.CommonRouterApi;
import com.sjtu.yifei.route.Routerfit;

public class MainActivityItemModel extends RVItemBaseModel<MainActivityModel> {
    public ObservableField<String> item = new ObservableField<>();

    public MainActivityItemModel(MainActivityModel viewModel, String string) {
        super(viewModel);
        this.item.set(string);
    }

    //item的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            int position = viewModel.menus.indexOf(MainActivityItemModel.this);
            if (position==0){
                Routerfit.register(CommonRouterApi.class).skipRecycleViewSingleStyleActivity();
            }
        }
    });
}
