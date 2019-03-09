# DataBinding RecycleView单布局

使用的第三方库：

[binding-collection-adapter]: https://github.com/guozhiqiang123/binding-collection-adapter

在xml中：

```xml
<android.support.v7.widget.RecyclerView
            adapter="@{vm.adapter}"
            itemBinding="@{vm.itemBinding}"
            items="@{vm.menus}"
            lineManager="@{LineManagers.horizontal()}"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layoutManager="android.support.v7.widget.LinearLayoutManager" />
```

相关引用：

```xml
<data>
    <variable
         name="vm"
         type="com.gzq.androidcore.vm.MainActivityModel" />
    <import                 type="com.gzq.lib_resource.mvvm.binding.viewadapter.recyclerview.LineManagers" />
</data>
```

在ViewModel中：

```java
//RecycleView的数据集
public ObservableArrayList<String> menus = new ObservableArrayList<>();
menus.add("DataBinding RecycleView单布局");
menus.add("DataBinding RecycleView多布局");
menus.add("DataBinding XAOP的使用");
//适配器
public BindingRecyclerViewAdapter<String> adapter = new BindingRecyclerViewAdapter<>();
//绑定item
public ItemBinding<String> itemBinding = ItemBinding.of(BR.item,R.layout.item_main_menu);
```

