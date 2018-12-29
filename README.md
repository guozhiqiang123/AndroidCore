# Android项目架构探索  

## lib-core  
   作用：提供Android开发架构的核心功能：Application生命周期的分发、Activity生命周期的分发、Activity生命周期
   的监控（LeakCanary）、主线程耗时监控（BlockCanary）、全局实例的统一Provider(Application、Gson、Retrfit、
   RoomDatabase)、崩溃日志的本地拦截、Retrofit简单实现文件的上传和下载、Timber日志库集成、用户信息的全局管理
   (SessionManager)、常用工具
## 使用场景
- 非组件化的MVC、MVP、MVVM模式开发
- 组件化开发核心功能（推荐）

## 优势
- 解决组件化中每一个module因业务需要都需要继承Application
- 解决全局Activity生命周期不易管理和监控的问题
- 解决Retrofit使用仍需轻度封装的繁琐
- 解决在多设备上UI适配难的问题（UiUtils）
- 解决部分全局变量多次创建实例造成内存浪费的问题
- 解决用户信息全局共享、更新、监听的问题
- 解决文件的上传和下载仍需额外封装的繁琐
- 解决真机调试出现崩溃抓不到崩溃信息的尴尬
- 各种功能高度内敛，统一管理，超高自由度

## 使用
建议将gradle升级到3.x，并将Android studio升级到3.x，因为使用implementation比使用compile最大的好处是可以实
现依赖隔离，这样不容易出现“重复引用第三方库”的bug

### 1.将lib-core导入自己的项目，每一个业务module都implementation该核心库;  
### 2.全局配置，如下操作：
```java
public class GlobalConfiguration implements GlobalModule {
    private static final String TAG = "GlobalConfiguration";

    @Override
    public void applyOptions(Context context, GlobalConfig.Builder builder) {
        Timber.i("GlobalConfiguration---->applyOptions");
        builder
                //全局BaseUrl
                .baseurl("http://www.gcmlrt.com/")
                //Room数据库的名字
                .roomName("ABC")
                //设计图的宽 单位：px
                .designWidth(720)
                //设计图的高 单位：px
                .designHeight(1280)
                //配置是否Room数据库进行网络请求的缓存
                .roomCache(true, CacheMode.FIRST_CACHE_THEN_REQUEST, 60)
                //OkHttpClient的拓展配置
                .okhttpConfiguration(new OkhttpConfig() {
                    @Override
                    public void okhttp(Context context, OkHttpClient.Builder builder) {
                        builder.addInterceptor(new RoomCacheInterceptor())
                                //添加统一请求头
                                .addInterceptor(new Interceptor() {
                                    @Override
                                    public Response intercept(Chain chain) throws IOException {
                                        Request request = chain.request()
                                                .newBuilder()
                                                .addHeader("equipmentId", DeviceUtils.getIMEI())
                                                .build();
                                        return chain.proceed(request);
                                    }
                                });
                        if (context.getPackageName().equals(ProcessUtils.getCurProcessName(context))) {
                            builder.addInterceptor(Pandora.get().getInterceptor());
                        }
                    }
                })
                //用户信息全局管理配置
                .sessionManagerConfiguration(new SessionManagerConfig() {
                    @Override
                    public void session(Context context, SessionConfig.Builder builder) {
                        builder.userClass(SessionUserInfo.class).tokenClass(SessionToken.class);
                    }
                })
                //Room数据库配置
                .roomDatabaseConfiguration(new RoomDatabaseConfig() {
                    @Override
                    public void room(Context context, RoomDatabase.Builder builder) {

                    }
                })
                //Retrofit拓展配置
                .retrofitConfiguration(new RetrofitConfig() {
                    @Override
                    public void retrofit(Context context, Retrofit.Builder builder) {
                        Timber.i("retrofitConfiguration");
                    }
                })
                //崩溃信息拦截器拓展配置
                .crashManagerConfiguration(new CrashManagerConfig() {
                    @Override
                    public void crash(Context context, CaocConfig.Builder builder) {
                        //关闭崩溃全局监听
                        builder.enabled(false);
                    }
                });

    }
}
```
并且在AndroidManifest.xml中进行注册
```java
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.gzq.lib_resource">

    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_LOGS" />

    <application>
        <!--name:实现类的全路径；value:GlobalConfig固定值-->                
        <meta-data
            android:name="com.gzq.lib_resource.GlobalConfiguration"
            android:value="GlobalConfig" />
    </application>
</manifest>
```
大家看到这里通过一个meta-data就可以初始化实现类，是不是很神奇？这个不是我想出来的，我是看大神
[JessYanCoding](https://github.com/JessYanCoding)的开源项目[MVPArms](https://github.com/JessYanCoding/MVPArms)
学到的。大神有很多很优秀的开源项目，欢迎大家关注。包括Application、Activity、Fragment生命周期的分发都是通过这种方式实现的。     
### 3.在需要关联Appcation、Activity、Fragment的地方如下操作（只贴Application的代码）：
```java
public class SubApp implements AppLifecycle {

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        //就像在Application中一样，进行一些全局初始化的工作
    }

    @Override
    public void onTerminate(@NonNull Application application) {
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }
}
```
并且在AndroidManifest.xml中进行注册，**注意：一定要注册！！！一定要注册！！！一定要注册！！！** 
```java
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.gzq.lib_resource">

    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_LOGS" />

    <application>
        <!--name:实现类的全路径；value:AppLifecycle固定值-->
        <meta-data
            android:name="com.gzq.lib_resource.app.SubApp"
            android:value="AppLifecycle"/>
    </application>
</manifest>
```
meta-data的name还有如下值：
```java
public interface  MetaValue {
    /**
     * App生命周期分发的标签
     */
    String APP_LIFECYCLE="AppLifecycle";
    /**
     * Activity生命周期分发的标签
     */
    String ACTIVITY_LIFECYCLE = "ActivityLifecycle";
    /**
     * Fragment生命周期分发的标签
     */
    String FRAGMENT_LIFECYCLE="FragmentLifecycle";
    /**
     * 全局配置的的标签
     */
    String GLOBAL_CONFIG="GlobalConfig";
}
```
**注意：** 在AndroidManifest中注册的时候，value的值一定是AppLifecycle、ActivityLifecycle、FragmentLifecycle、
GlobalConfig中的一个，**一个字符都不能错**。
### 4.UI适配    
- 在GlobalConfiguration中配置好设计师设计的UI图的宽高，单位px
- 在xml文件中用pt代替dp，数值就填写UI图上的px单位的值。比如UI图上的图标是宽10px,高10px，那么在xml文件中就写成
layout_width="10pt",layout_height="10pt"
- dp和pt在同一个xml文件中可以混用，但是不建议这么做
- 字体大小也可以使用pt作为单位，但是否有兼容性问题，还未测试

### 5.全局用户信息共享   
SessionManager内部默认使用腾讯开源库[MMKV](https://github.com/Tencent/MMKV)实现缓存，如果想使用SharedPreferences
作为缓存工具，可在GlobalConfiguration中配置。具体使用如下：
- 在GlobalConfiguration中配置保存用户信息和Token的Class
```java
//用户信息全局管理配置
.sessionManagerConfiguration(new SessionManagerConfig() {
    @Override
    public void session(Context context, SessionConfig.Builder builder) {
        builder.sessionManager(new PreferencesSessionManager(context));
        builder.userClass(SessionUserInfo.class).tokenClass(SessionToken.class);
    }
})
```
- 在登录成功获取到用户信息之后
```java
 //保存用户信息
 SessionUserInfo userInfo = new SessionUserInfo();
 userInfo.setUserId("10001");
 userInfo.setName("测试");
 Box.getSessionManager().setUser(userInfo);
 
 //使用用户信息
 SessionUserInfo user = Box.getSessionManager().getUser();
 user.getUserId();
 user.getName();
 
 //保存Token
 SessionToken sessionToken = new SessionToken();
 sessionToken.setAccessToken("");
 sessionToken.setRefreshToken("");
 sessionToken.setUserId("");
 Box.getSessionManager().setUserToken(sessionToken);
 
 //使用Token
 SessionToken userToken = Box.getSessionManager().getUserToken();
 userToken.getAccessToken();
 userToken.getRefreshToken();
 userToken.getUserId();
```
### 6.全局变量的使用
在项目中频繁使用的对象，我们就可以在APP启动的时候就实例化供全局使用，而不是每次使用都创建一个新的对象，比如
Application实例、Gson实例、Retrofit实例。为了统一和使用方便，我用Box.class管理全局使用的对象。
```java
public class Box implements AppLifecycle {
    private static final String TAG = "Box";
    private static Application mApplication;
    private static Gson gson;
    private static Retrofit retrofit;

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        //初始化全局变量
        mApplication = application;
        gson = ObjectFactory.getGson(mApplication, App.getGlobalConfig());
        retrofit = ObjectFactory.getRetrofit(mApplication, App.getGlobalConfig());
        //初始化Ui工具
        ObjectFactory.initUiUtils(App.getGlobalConfig());
        //初始化LeakCanary
        LeakCanaryUtil.getInstance().init(application);
        //初始化KVUtil
        KVUtils.init(application);
        //用户信息管理器
        ObjectFactory.initSessionManager(mApplication, App.getGlobalConfig());
        //崩溃拦截配置
        ObjectFactory.initCrashManager(mApplication, App.getGlobalConfig());
        Timber.tag(TAG).i("onCreate");
    }

    @Override
    public void onTerminate(@NonNull Application application) {
        ObjectFactory.clear();
        mApplication = null;
        gson = null;
        retrofit = null;
        Timber.tag(TAG).i("onTerminate");
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        UiUtils.compatWithOrientation(newConfig);
    }


    public static Application getApp() {
        return mApplication;
    }

    /**
     * Gson
     *
     * @return
     */
    public static Gson getGson() {
        return gson;
    }

    /**
     * @param serviceClazz
     * @param <T>
     * @return
     */
    public static <T> T getRetrofit(Class<T> serviceClazz) {
        return retrofit.create(serviceClazz);
    }

    /**
     * 数据库Room
     *
     * @param databaseClazz
     * @param <DB>
     * @return
     */
    public static <DB extends RoomDatabase> DB getRoomDataBase(Class<? extends RoomDatabase> databaseClazz) {
        return ObjectFactory.getRoomDatabase(mApplication, databaseClazz, App.getGlobalConfig());
    }

    /**
     * 获取缓存数据库
     *
     * @param databaseClazz
     * @param <DB>
     * @return
     */
    public static <DB extends RoomDatabase> DB getCacheRoomDataBase(Class<? extends RoomDatabase> databaseClazz) {
        return ObjectFactory.getCacheRoomDatabase(mApplication, databaseClazz);
    }

    /**
     * 用户信息管理器
     *
     * @return
     */
    public static SessionManager getSessionManager() {
        return SessionManager.get();
    }

    /**
     * 为了解决在fragment中使用{@link android.support.v4.app.Fragment#getString(int)}
     * 偶尔会报java.lang.IllegalStateException-->Fragment xxxxx{xxx} not attached to a context的错误
     *
     * @param id 字符串资源id
     * @return
     */
    public static String getString(@StringRes int id) {
        return getApp().getResources().getString(id);
    }

    /**
     * 为了解决在fragment中使用{@link Fragment#getContext()} getContext().getColor(int color)
     * 偶尔会报java.lang.IllegalStateException-->Fragment xxxxx{xxx} not attached to a context的错误
     *
     * @param id 颜色资源id
     * @return
     */
    public static int getColor(@ColorRes int id) {
        return ContextCompat.getColor(getApp(), id);
    }
}
```
如果使用者还有另外的全局变量需要提供，也建议放置在该类中，方便统一管理和使用。

### 7.Retrofit的使用  
直接上代码
```java
Box.getRetrofit(Api.class)
   .loginWith("","")
   .compose(RxUtils.httpResponseTransformer())
   .as(RxUtils.autoDisposeConverter(this))
   .subscribe(new CommonObserver<SessionBean>() {
       @Override
       public void onNext(SessionBean sessionBean) {
           
       }
   });
```
其中RxUtils中提供了异常处理转换器、线程调度操作、Uber的AutoDispose，让写代码尽可能的少。其中还默认提供了一个
网络请求结果处理的包装类HttpResult.class，如果有不满足需求，也可以实现BaseModel进行扩展。代码如下
```java
/**
 * 默认HttpResult,如果格式和此处有较大差异，可implements BaseModel进行扩展
 */
public class HttpResult<T> implements BaseModel<T> {

    @SerializedName("tag")
    private boolean tag;
    @SerializedName(value = "code")
    private int code;
    @SerializedName(value = "message", alternate = {"status", "msg"})
    private String message;

    @Override
    public boolean isSuccess() {
        return tag;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    //用来模仿Data
    @SerializedName(value = "data", alternate = {"subjects", "result", "error"})
    private T data;

    @Override
    public T getData() {
        return data;
    }

}
```
### 8.文件上传
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
**注意：1.第三个参数一定是FileUploadService.class中的方法名，因为内部是使用反射实现方法的调用；2.使用
UploadSubscriber，这样才可以监听进度**

### 9.文件下载
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
### 10.其他
- ActivityUtils：自己维护Stack管理所有Activity。建议所有Activity的跳转都使用该工具类
- KVUtils：腾讯MMKV开源库的封装
- SPUtils:SharedPreferences的封装
- NetworkUtils：网络类型判断、是否WiFi等功能
- RxBus
- ToastUtils
## [CHANGELOG](CHANGELOG.md)
## 参考和感谢
[MVPArms](https://github.com/JessYanCoding/MVPArms)  
[EasyRxRetrofit](https://github.com/whichname/EasyRxRetrofit)    
[SessionManager](https://www.jianshu.com/p/df1e1b38dd97)  
[AvoidOnResult](https://github.com/guofeng007/AvoidOnResult)    
[LoggingInterceptor](https://github.com/ihsanbal/LoggingInterceptor)   
[timber](https://github.com/JakeWharton/timber)
