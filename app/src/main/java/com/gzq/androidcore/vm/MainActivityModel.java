package com.gzq.androidcore.vm;


import android.databinding.ObservableField;

import com.gzq.lib_resource.mvvm.base.BaseViewModel;

public class MainActivityModel extends BaseViewModel {
    private int position = 0;
    public ObservableField<String> userName = new ObservableField<>("按钮");


}
