# 项目组件化、快速开发实践

​	2018年Android最火的是什么？当然属于组件化。组件化是个老生常谈的问题了，但是2018年却异常火爆，各种思想，框架，工具铺天盖地。趁着这波热度，我司的项目也逐渐过渡到组件化的开发。     
​	尝到了组件化开发的甜头，以后新的项目肯定也是朝这个方向规划的，所以这个组件化、可快速开发的基础框应运而生。    
​	组件化的优点很明显：①项目结构层次更加清晰；②方便多人协同开发；③方便单模块调试、测试     
废话说这么多就够了，咱们直接进入主题。   

## 核心功能
- 使用meta-data+反射的方式实现Application、Activity、Fragment生命周期的分发
- 使用建造者模式实现全局工具的统一配置（比如：网络baseUrl、UI适配设置、retrofit配置、数据库Room配置等）
- 使用ARetrofit作为应用的路由框架，简单易用，可完全取代原生用法
- 集成了LeakCanary、BlockCanary,自动监测内存泄漏、耗时代码；
- Retrofit更加内敛、更加方便使用的封装（支持配置Room进行缓存、支持简单的上传下载）
- 用户信息从登陆到退出程序的全局管理（一键更新，全局使用，处处可监听变化）
- 崩溃信息的捕获，方便脱机调试

## 项目结构
![](https://github.com/guozhiqiang123/AndroidCore/blob/master/img/structure_chart.png)
lib-core:集成了上面的核心功能，除特殊情况外，开发者不应该修改；   
lib-resource:
- 主要是放置公用的实体bean、代理接口、xml资源、图片资源、自定义的view、公用工具类等;
- 引用的第三方库或者是集成的SDK都应该放在这里；   
- app与module、module与module的通信是通过ARetrofit进行的（建议舍弃原生的startActivity和startActivityForResult）；
- app模块依赖其他module的时候应该使用addComponent，而不是implementation或者api(该功能对于渐进式改造老项目有奇效)

## 全局配置
第一步，实现GlobalModule接口，全局唯一实现子类，多个会运行时会报错：
```java
public class GlobalConfiguration implements GlobalModule {
    @Override
    public void applyOptions(Context context, GlobalConfig.Builder builder) {
        
    }
}
```
第二步，在AndroidManifest.xml中进行注册：
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.gzq.lib_resource">
    <application>
        <!--name:实现类的全路径；value:GlobalConfig固定值-->
        <meta-data
            android:name="com.gzq.lib_resource.GlobalConfiguration"
            android:value="GlobalConfig" />
    </application>
</manifest>
```

## Application生命周期

​	组件化最麻烦的事情就是各个业务模块在单独开发和调试的时候，需要用到Applicaiton的生命周期进行全局变量的初始化，第三方库的初始化工作。最常见的做法是在公用模块中创建BaseApplication,然后在业务模块中继承。这种做法最不让人愉快的地方就是在将业务模块合到壳模块app上的时候，需要将子Application中的代码复制到壳app模块中Application中去，并且还需要将AndroidManifest.xml中的注册代码去除。这种方式不仅繁琐，而且容易忘记导致编译失败。

​	目前我看到解决这个问题的方式有两种：①使用Google官方的AutoService库实现；②使用meta-data+反射的方式（我目前在使用的）。其实上面的全局配置也是使用的这种方式来进行实例化的，Application、Activity、Fragment生命周期的分发都是同样的实现方式。下面只列举Application的情况，因为这个最常用：

第一步，实现AppLifecycle接口（Activity->ActivityLifecycle  Fragment->FragmentLifecycle）：

```java
public class AppStore implements AppLifecycle {
    private static final String TAG = "AppStore";

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        //进行初始化的相关工作
    }

    @Override
    public void onTerminate(@NonNull Application application) {
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }
}
```

第二步，在AndroidManifest.xml中进行注册：

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.gzq.lib_resource">
    <application>
        <meta-data
            android:name="com.gzq.lib_resource.app.AppStore"
            android:value="AppLifecycle"/>
    </application>
</manifest>
```

这种做法因为不需要在<application>节点进行注册，所以不会和其他Application的子类冲突。

## 屏幕适配

项目中采用 [JessYanCoding](https://github.com/JessYanCoding) 大神的AndroidAutoSize框架作为适配，所以用法上完全遵循AndroidAutoSize的使用规则。

如果是老项目改造，则在AndroidManifest.xml中配置dp的尺寸:

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.gzq.lib_resource">
    <application>
        <!--老项目设计图宽度 单位：dp-->
        <meta-data
            android:name="design_width_in_dp"
            android:value="375" />
        <!--老项目设计图高度 单位：dp-->
        <meta-data
            android:name="design_height_in_dp"
            android:value="667" />
    </application>
</manifest>
```

并且在GlobalConfiguration中设置副单位支持和设计图的尺寸：

```java
@Override
public void applyOptions(Context context, GlobalConfig.Builder builder) {
    builder
            //设计图的宽 单位：px
            .designWidth(750)
            //设计图的高 单位：px
            .designHeight(1334)
            //设置对副单位的支持
            .autoSize(false, false, Subunits.PT);
}
```



如果是新项目，则建议全部使用副单位进行界面的绘制：

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.gzq.lib_resource">
    <application>
        <!--设计图宽度 单位：px-->
        <meta-data
            android:name="design_width_in_dp"
            android:value="750" />
        <!--设计图高度 单位：px-->
        <meta-data
            android:name="design_height_in_dp"
            android:value="1334" />
    </application>
</manifest>
```

并且在GlobalConfiguration中只需配置一下对哪个副单位的支持就行了，无需再额外配置设计图的宽高，当然设置了也没有啥影响：

```java
@Override
public void applyOptions(Context context, GlobalConfig.Builder builder) {
    builder
            //设计图的宽 单位：px 设置了也没什么影响
            .designWidth(750)
            //设计图的高 单位：px 设置了也没什么影响
            .designHeight(1334)
            //设置对副单位的支持
            .autoSize(false, false, Subunits.PT)；
}
```

## Activity|Fragment

lib-resource中默认提供了MVP和MVVM两种模式的BaseActivity。如果开发者有自己的书写习惯，完全可以把已有代码替换成自己的。另外需要说明的是lib-resource中默认继承了Fragment管理框架[Fragmentation](https://github.com/YoKeyword/Fragmentation)。同时可以看见里面还有StateBaseActivity和StateBaseFragment，这两个基础类是集成了状态页管理框架[LoadSir](https://github.com/KingJA/LoadSir)。

总之一句话，lib-resource是完全属于开发者管理的一个lib，里面笔者默认提供了一些东西，如果符合开发需求和开发者的习惯，就直接使用；不符合就完全替换成自己的东西。



## Dialog

默认提供的Dialog是基于DialogFragment自定义的，支持链式调用，支持自定义布局和众多属性。默认提供了两种布局：①底部两个按钮，上面内容体；②底部一个按钮，上面内容体。在实际开发中如果还有更多通用样式，也可以参照这两种样式自定义。

```java
//一个按钮
FDialog.build()
    .setSupportFM(getFragmentManager())
    .setOutCancel(false)
    .showConfirm()//一个按钮
    .setContentText("我是一个按钮对话框的内容体")
    .setContentTextGravity(Gravity.LEFT)
    .setConfirmText("我知道了！！！")
    .setConfirmTextColor(R.color.colorAccent)
    .show();


//两个按钮
FDialog.build()
    .setSupportFM(getFragmentManager())//必须调用
    .setDimAmount(0.3f)
    .setOutCancel(false)
    .showConfirmCancel()//两个按钮的Dialog
    .setContentText("我是两个按钮的对话框的内容体")
    .setContentTextColor(R.color.colorAccent)
    .setContentTextSize(18)
    .setContentTextGravity(Gravity.CENTER)
    .setLeftButtonText("再来一次")
    .setLeftButtonClickListen(new DialogClickListener() {
        @Override
        public void onClick(View v, FDialog dialog) {
            dialog.dismiss();
        }
    })
    .show();    
        
        
//自定义布局
FDialog.build()
    .setSupportFM(getFragmentManager())
    .setLayoutId(R.layout.pd_layout_entrance)
    .setWidth(800)
    .setOutCancel(false)
    .setDimAmount(0.5f)
    .setConvertListener(new ViewConvertListener() {
        @Override
        protected void convertView(DialogViewHolder holder, FDialog dialog) {
            holder.setText(R.id.view_panel_id, "ddd");
            holder.setOnClickListener(R.id.view_panel_id, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    })
    .show();
```

## 超级盒子Box.class

这个类是全局变量的提供者，比如Application的实例，Retrofit的实例，Gson的实例，数据库Room的实例。还提供了Application上下文的getColor和getString。之所以提供这两个方法的原因是经常在Fragment中使用color或者string的时候出现Fragment  not attached to a context这个错误。这个错误产生的原因是Fragment已经没有依附在Activity上了，但是Fragment还在使用Activity的上下文，解决办法是判断Fragment是否还依附在Activity上（Fragment.isAdd()）。

## 网络请求

```java
Box.getRetrofit(API.class)
                .test()
                //自定义的数据流转换器
                .compose(RxUtils.httpResponseTransformer())
                //Retrofit的生命周期管理
                .as(RxUtils.autoDisposeConverter(this))
                //CommonObserver中加入了网络监测
                .subscribe(new CommonObserver<Object>(900) {
                    @Override
                    public void onNext(Object o) {
						//成功回调 默认：successCode=200；
                    }

                    @Override
                    protected void onNetError() {
						//没有网络回调这里
                    }

                    @Override
                    protected void onEmptyData() {
						//在构造器中传入默认的空数据错误码，该方法才会回调
                    }
                });
```

在一个成熟的项目中，肯定不止使用一套域名，因此项目中baseUrl的切换肯定也是刚需，因此笔者还在lib-core中引用了 JessYanCoding的[RetrofitUrlManager](https://github.com/JessYanCoding/RetrofitUrlManager)这个动态切换baseUrl的库，如果有这个需求，直接在业务模块中使用即可，无需另外引用其他库。

## 文件上传

```java
public interface FileUploadService {

    @Multipart
    @POST("/upload")
    Flowable<HttpResult<String>> upload(@Part MultipartBody.Part file);

}
```
```java
RxUploadUtil.uploadFile(new File(""), FileUploadService.class,"upload")
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new UploadSubscriber<String>() {
                    @Override
                    protected void _onNext(String result) {
                        //上传成功返回的结果
                    }

                    @Override
                    protected void _onProgress(Integer percent) {
                        //上产进度
                    }

                    @Override
                    protected void _onError(int errorCode, String msg) {
                        //错误信息
                    }
                });
```
**注意：1.第三个参数一定是FileUploadService.class中的方法名，因为内部是使用反射实现方法的调用；2.使用**
**UploadSubscriber，这样才可以监听进度**

## 文件下载
```java
 RxDownloadUtil.downLoadFile("http://12kdalca.apk")
                .as(RxUtils.autoDisposeConverter(this))
                .subscribe(new DownLoadSubscriber() {
                    @Override
                    protected void _onNext(String result) {
                        //下载结果信息
                    }

                    @Override
                    protected void _onProgress(Integer percent) {
                        //下载进度
                    }
                });
```

**注意:使用DownLoadSubscriber才可以监听下载进度**