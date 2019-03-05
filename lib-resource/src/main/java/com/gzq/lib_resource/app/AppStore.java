package com.gzq.lib_resource.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.base.delegate.AppLifecycle;
import com.gzq.lib_resource.BuildConfig;
import com.gzq.lib_resource.state_page.EmptyPage;
import com.gzq.lib_resource.state_page.ErrorPage;
import com.gzq.lib_resource.state_page.LoadingPage;
import com.gzq.lib_resource.state_page.NetErrorPage;
import com.gzq.lib_resource.utils.AppUtils;
import com.kingja.loadsir.core.LoadSir;
import com.tencent.bugly.crashreport.CrashReport;

import me.yokeyword.fragmentation.Fragmentation;

/**
 * copyright：杭州国辰迈联机器人科技有限公司
 * version: V1.3.0
 * created on 2018/10/24 17:01
 * created by: gzq
 * description: TODO
 */
public class AppStore implements AppLifecycle {
    private static final String TAG = "AppStore";
    private static final String BUGGLY_APPID = "8931446044";

    @Override
    public void attachBaseContext(@NonNull Context base) {

    }

    @Override
    public void onCreate(@NonNull Application application) {
        //初始化Fragment框架Fragmentation
        Fragmentation.builder()
                // 显示悬浮球 ; 其他Mode:SHAKE: 摇一摇唤出   NONE：隐藏
                .stackViewMode(Fragmentation.BUBBLE)
                .debug(BuildConfig.DEBUG)
                .install();


        //初始化Buggly
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(application);
        userStrategy.setAppChannel(AppUtils.getAppInfo().getPackageName());
        userStrategy.setAppVersion(AppUtils.getAppInfo().getVersionName());
        CrashReport.initCrashReport(application, BUGGLY_APPID, BuildConfig.DEBUG, userStrategy);

        //初始化状态页面控件LoadSir
        LoadSir.beginBuilder()
                .addCallback(new ErrorPage())
                .addCallback(new LoadingPage())
                .addCallback(new EmptyPage())
                .addCallback(new NetErrorPage())
                .setDefaultCallback(LoadingPage.class)
                .commit();
    }

    @Override
    public void onTerminate(@NonNull Application application) {
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {

    }
}
