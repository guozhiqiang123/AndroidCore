package com.gzq.androidcore.vm;

import android.databinding.ObservableField;

import com.gzq.androidcore.bean.MainMenuBean;
import com.gzq.androidcore.router.AppRouterApi;
import com.gzq.lib_core.utils.ToastUtils;
import com.gzq.lib_resource.mvvm.base.RVItemBaseModel;
import com.gzq.lib_resource.mvvm.binding.command.BindingAction;
import com.gzq.lib_resource.mvvm.binding.command.BindingCommand;
import com.gzq.lib_resource.router.CommonRouterApi;
import com.sjtu.yifei.route.Routerfit;

public class MainActivityItemModel extends RVItemBaseModel<MainActivityModel> {
    public ObservableField<MainMenuBean> item = new ObservableField<>();

    public MainActivityItemModel(MainActivityModel viewModel, MainMenuBean menuBean) {
        super(viewModel);
        this.item.set(menuBean);
    }

    //item的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            int position = viewModel.menus.indexOf(MainActivityItemModel.this);
            ToastUtils.showShort("点击了" + position);
            if (position == 0) {
                ToastUtils.showShort("该页面就是");
            } else if (position == 1) {
                Routerfit.register(AppRouterApi.class).skipRecycleViewMoreStyleActivity();
            }
        }
    });
}
