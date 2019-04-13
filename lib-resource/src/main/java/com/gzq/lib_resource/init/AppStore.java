package com.gzq.lib_resource.init;

import com.gzq.lib_resource.BuildConfig;
import com.gzq.lib_resource.state_page.EmptyPage;
import com.gzq.lib_resource.state_page.ErrorPage;
import com.gzq.lib_resource.state_page.LoadingPage;
import com.gzq.lib_resource.state_page.NetErrorPage;
import com.gzq.lib_resource.utils.AppUtils;
import com.kingja.loadsir.core.LoadSir;
import com.sankuai.erp.component.appinit.api.SimpleAppInit;
import com.sankuai.erp.component.appinit.common.AppInit;
import com.sjtu.yifei.route.Routerfit;
import com.tencent.bugly.crashreport.CrashReport;
import com.xuexiang.xaop.XAOP;

import me.yokeyword.fragmentation.Fragmentation;
import timber.log.Timber;

@AppInit(priority = 1, description = "AppStore被初始化")
public class AppStore extends SimpleAppInit {
    private static final String TAG = "AppStore";
    private static final String BUGGLY_APPID = "8931446044";


    @Override
    public void onCreate() {
        Timber.i("初始化：--->AppStore->onCreate");
        //初始化Fragment框架Fragmentation
        Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.NONE)
                .debug(BuildConfig.DEBUG)
                .install();
        //初始化状态页面控件LoadSir
        LoadSir.beginBuilder()
                .addCallback(new ErrorPage())
                .addCallback(new LoadingPage())
                .addCallback(new EmptyPage())
                .addCallback(new NetErrorPage())
                .setDefaultCallback(LoadingPage.class)
                .commit();
        //初始化路由框架
        Routerfit.init(mApplication);
    }

    @Override
    public boolean needAsyncInit() {
        return true;
    }

    @Override
    public void asyncOnCreate() {
        Timber.i("初始化：--->AppStore->asyncOnCreate");
        //初始化Buggly
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(mApplication);
        userStrategy.setAppChannel(AppUtils.getAppInfo().getPackageName());
        userStrategy.setAppVersion(AppUtils.getAppInfo().getVersionName());
        CrashReport.initCrashReport(mApplication, BUGGLY_APPID, BuildConfig.DEBUG, userStrategy);

        //初始化XAOP
        XAOP.init(mApplication);
        XAOP.debug(BuildConfig.DEBUG);
    }
}
