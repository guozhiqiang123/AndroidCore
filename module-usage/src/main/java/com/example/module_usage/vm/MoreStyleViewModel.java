package com.example.module_usage.vm;

import android.databinding.ObservableArrayList;

import com.example.module_usage.BR;
import com.example.module_usage.R;
import com.example.module_usage.bean.MoreStyleBean;
import com.gzq.lib_resource.mvvm.base.BaseViewModel;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

public class MoreStyleViewModel extends BaseViewModel {
    public ObservableArrayList<MoreStyleBean> items = new ObservableArrayList<>();

    public MoreStyleViewModel() {
        initData();
    }

    private void initData() {
        items.add(new MoreStyleBean("测试一", R.drawable.ic_error, R.drawable.ic_error));
        items.add(new MoreStyleBean("测试二", R.drawable.ic_error, R.drawable.ic_error));
        items.add(new MoreStyleBean("测试三", R.drawable.ic_error, R.drawable.ic_error));
        items.add(new MoreStyleBean("测试四", R.drawable.ic_error, R.drawable.ic_error));
        items.add(new MoreStyleBean("测试五", R.drawable.ic_error, R.drawable.ic_error));
        items.add(new MoreStyleBean("测试六", R.drawable.ic_error, R.drawable.ic_error));
        items.add(new MoreStyleBean("测试七", R.drawable.ic_error, R.drawable.ic_error));
        items.add(new MoreStyleBean("测试八", R.drawable.ic_error, R.drawable.ic_error));
    }

    public BindingRecyclerViewAdapter<MoreStyleBean> adapter = new BindingRecyclerViewAdapter<>();
    public OnItemBind<MoreStyleBean> itemBind = new OnItemBind<MoreStyleBean>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, MoreStyleBean item) {
            //此处可以根据item 或者位置来进行多布局的区分
            if (position % 2 == 0) {
                itemBinding.set(BR.bean, R.layout.item_morestyle_left);
                itemBinding.bindExtra(BR.itemViewModel, new LeftMoreStyleItemViewModel(item, position));
            } else {
                itemBinding.set(BR.bean, R.layout.item_morestyle_right);
                itemBinding.bindExtra(BR.itemViewModel, new RightMoreStyleItemViewModel(item, position));
            }
        }
    };
}
