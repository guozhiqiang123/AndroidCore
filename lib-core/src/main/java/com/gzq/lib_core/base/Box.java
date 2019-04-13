package com.gzq.lib_core.base;

import android.app.Application;
import android.arch.persistence.room.RoomDatabase;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.google.gson.Gson;
import com.gzq.lib_core.base.quality.LeakCanaryUtil;
import com.gzq.lib_core.session.SessionManager;
import com.gzq.lib_core.toast.T;
import com.gzq.lib_core.utils.KVUtils;
import com.sankuai.erp.component.appinit.api.SimpleAppInit;
import com.sankuai.erp.component.appinit.common.AppInit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import timber.log.Timber;

@AppInit(priority = 0, description = "Box被初始化")
public final class Box extends SimpleAppInit {
    private static final String TAG = "Box";
    private static Application mApp;
    private static Gson gson;
    private static Retrofit retrofit;
    private static Handler handler;
    private static OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.i("初始化---->Box--->onCreate");
        mApp = mApplication;
        //Toast初始化
        T.instance().init(mApp);
        //初始化屏幕适配器
        ObjectFactory.INSTANCE.initAutoSize(App.getGlobalConfig());
        //初始化KVUtil
        KVUtils.init(mApp);
        //用户信息管理器
        ObjectFactory.INSTANCE.initSessionManager(mApp, App.getGlobalConfig());
    }

    @Override
    public boolean needAsyncInit() {
        return true;
    }

    @Override
    public void asyncOnCreate() {
        Timber.i("初始化---->Box--->asyncOnCreate");
        //崩溃拦截配置
        ObjectFactory.INSTANCE.initCrashManager(mApp, App.getGlobalConfig());
        //初始化LeakCanary
        LeakCanaryUtil.getInstance().init(mApp);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mApp = null;
        gson = null;
        retrofit = null;
    }


    public static Application getApp() {
        return mApp;
    }

    /**
     * Gson
     *
     * @return
     */
    public static Gson getGson() {
        if (gson == null) {
            gson = ObjectFactory.INSTANCE.getGson(mApp, App.getGlobalConfig());
        }
        return gson;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = ObjectFactory.INSTANCE.getOkHttpClient(mApp, App.getGlobalConfig());
        }
        return okHttpClient;
    }

    /**
     * @param serviceClazz
     * @param <T>
     * @return
     */
    public static <T> T getRetrofit(Class<T> serviceClazz) {
        if (retrofit == null) {
            retrofit = ObjectFactory.INSTANCE.getRetrofit(mApp, App.getGlobalConfig());
        }
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
        return ObjectFactory.INSTANCE.getRoomDatabase(mApp, databaseClazz, App.getGlobalConfig());
    }

    /**
     * 获取缓存数据库
     *
     * @param databaseClazz
     * @param <DB>
     * @return
     */
    public static <DB extends RoomDatabase> DB getCacheRoomDataBase(Class<? extends RoomDatabase> databaseClazz) {
        return ObjectFactory.INSTANCE.getCacheRoomDatabase(mApp, databaseClazz);
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

    /**
     * 提供全局的handler
     *
     * @param looper
     * @return
     */
    public static Handler getHandler(Looper looper) {
        if (handler == null) {
            handler = new Handler(looper);
        }
        return handler;
    }
}
