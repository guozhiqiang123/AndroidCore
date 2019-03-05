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

