package com.gzq.lib_core.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.gzq.lib_core.BuildConfig;
import com.gzq.lib_core.base.delegate.GlobalModule;
import com.gzq.lib_core.base.delegate.MetaValue;
import com.gzq.lib_core.base.quality.LeakCanaryUtil;
import com.gzq.lib_core.log.CrashReportingTree;
import com.gzq.lib_core.toast.T;
import com.gzq.lib_core.utils.KVUtils;
import com.gzq.lib_core.utils.ManifestParser;
import com.gzq.lib_core.utils.Preconditions;
import com.sankuai.erp.component.appinit.api.AppInitApiUtils;
import com.sankuai.erp.component.appinit.api.AppInitManager;
import com.sankuai.erp.component.appinit.api.SimpleAppInitCallback;
import com.sankuai.erp.component.appinit.common.AppInitItem;
import com.sankuai.erp.component.appinit.common.ChildInitTable;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class App extends Application {
    private static final String TAG = "App";
    private static Application instance;
    private static GlobalConfig globalConfig;
    private static GlobalConfig.Builder globalBuilder;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化分包插件
        MultiDex.install(base);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
        Timber.tag(TAG).i("attachBaseContext: ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        AppInitManager.get().init(this, new SimpleAppInitCallback() {
            /**
             * 开始初始化
             *
             * @param isMainProcess 是否为主进程
             * @param processName   进程名称
             */
            @Override
            public void onInitStart(boolean isMainProcess, String processName) {
                // 在所有的初始化类之前初始化
                initGlobalConfig();
            }

            /*
             * 是否为 debug 模式
             */
            @Override
            public boolean isDebug() {
                return BuildConfig.DEBUG;
            }

            /**
             * 通过 coordinate 自定义依赖关系映射，键值都是 coordinate。「仅在需要发热补的情况下才自定义，否则返回 null」
             *
             * @return 如果返回的 map 不为空，则会在启动是检测依赖并重新排序
             */
            @Override
            public Map<String, String> getCoordinateAheadOfMap() {
                return null;
            }

            /**
             * 同步初始化完成
             *
             * @param isMainProcess      是否为主进程
             * @param processName        进程名称
             * @param childInitTableList 初始化模块列表
             * @param appInitItemList    初始化列表
             */
            @Override
            public void onInitFinished(boolean isMainProcess, String processName, List<ChildInitTable> childInitTableList, List<AppInitItem> appInitItemList) {
                // 获取运行期初始化日志信息
                String initLogInfo = AppInitApiUtils.getInitOrderAndTimeLog(childInitTableList, appInitItemList);
                Log.d("statisticInitInfo", initLogInfo);
            }
        });
        Timber.tag(TAG).i("onCreate");

    }

    private void initGlobalConfig() {
        //先初始化全局配置，然后进行生命周期的分发
        List<GlobalModule> globalModules = new ManifestParser<GlobalModule>(instance, MetaValue.GLOBAL_CONFIG).parse();
        Preconditions.checkArgument(globalModules != null && globalModules.size() == 1,
                "Please configure your global configuration,and only one  is allowed");
        GlobalModule globalModule = globalModules.get(0);
        globalBuilder = GlobalConfig.builder();
        globalModule.applyOptions(instance, globalBuilder);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppInitManager.get().onTerminate();
        ObjectFactory.INSTANCE.clear();
        instance = null;
        globalConfig = null;
        globalBuilder = null;
        Log.i(TAG, "onTerminate: ");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        AppInitManager.get().onConfigurationChanged(newConfig);
        super.onConfigurationChanged(newConfig);
        Timber.tag(TAG).i("onConfigurationChanged:");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppInitManager.get().onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        AppInitManager.get().onTrimMemory(level);
    }

    /**
     * 获取Application的实例，一般在工具类或者生命周期较早的初始化中使用
     *
     * @return
     */
    public static Application getApp() {
        return instance;
    }

    public static GlobalConfig getGlobalConfig() {
        if (globalConfig == null) {
            globalConfig = globalBuilder.build();
        }
        return globalConfig;
    }
}
