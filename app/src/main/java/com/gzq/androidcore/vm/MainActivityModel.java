package com.gzq.androidcore.vm;


import android.databinding.ObservableArrayList;

import com.gzq.androidcore.BR;
import com.gzq.androidcore.R;
import com.gzq.androidcore.bean.MainMenuBean;
import com.gzq.lib_resource.mvvm.base.BaseViewModel;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

public class MainActivityModel extends BaseViewModel {
    public ObservableArrayList<MainActivityItemModel> menus = new ObservableArrayList<>();

    public MainActivityModel() {
        menus.add(new MainActivityItemModel(this, new MainMenuBean("DataBinding RecycleView单布局", R.drawable.ic_buding)));
        menus.add(new MainActivityItemModel(this, new MainMenuBean("DataBinding RecycleView多布局", R.drawable.ic_baomihua)));
        menus.add(new MainActivityItemModel(this, new MainMenuBean("DataBinding XAOP的使用", R.drawable.ic_dangao)));
        menus.add(new MainActivityItemModel(this, new MainMenuBean("DataBinding NoDrawable的使用", R.drawable.ic_shengnvguo)));
    }

    //适配器
    public BindingRecyclerViewAdapter<MainActivityItemModel> adapter = new BindingRecyclerViewAdapter<>();
    public OnItemBind<MainActivityItemModel> itemBinding = new OnItemBind<MainActivityItemModel>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, MainActivityItemModel item) {
            itemBinding.set(BR.vm, R.layout.item_main_menu);
        }
    };


}
