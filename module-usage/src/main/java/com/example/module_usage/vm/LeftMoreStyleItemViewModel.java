package com.example.module_usage.vm;

import com.example.module_usage.bean.MoreStyleBean;
import com.gzq.lib_core.toast.T;
import com.gzq.lib_resource.mvvm.binding.command.BindingAction;
import com.gzq.lib_resource.mvvm.binding.command.BindingCommand;

public class LeftMoreStyleItemViewModel {
    private MoreStyleBean bean;
    private int pos;
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            T.show(""+pos);
        }
    });

    public LeftMoreStyleItemViewModel(MoreStyleBean item, int position) {
        bean=item;
        pos=position;
    }
}
