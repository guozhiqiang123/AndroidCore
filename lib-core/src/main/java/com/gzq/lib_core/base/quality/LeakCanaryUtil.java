package com.gzq.lib_core.base.quality;

import android.app.Application;

import com.gzq.lib_core.utils.Preconditions;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public final class LeakCanaryUtil {
    private static volatile LeakCanaryUtil instance;
    private static RefWatcher watcher;

    private LeakCanaryUtil() {
    }

    public static LeakCanaryUtil getInstance() {
        if (instance == null) {
            synchronized (LeakCanaryUtil.class) {
                if (instance == null) {
                    instance = new LeakCanaryUtil();
                }
            }
        }
        return instance;
    }

    public void init(Application app) {
        watcher = LeakCanary.install(app);
    }

    public void watch(Object object) {
        Preconditions.checkNotNull(watcher, "Please init LeakCanary");
        watcher.watch(object);
    }
}
