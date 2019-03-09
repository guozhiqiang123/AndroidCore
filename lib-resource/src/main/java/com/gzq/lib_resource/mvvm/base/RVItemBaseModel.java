package com.gzq.lib_resource.mvvm.base;

public class RVItemBaseModel<T extends BaseViewModel> extends BaseViewModel {
    protected T viewModel;

    public RVItemBaseModel(T viewModel) {
        this.viewModel = viewModel;
    }
}
