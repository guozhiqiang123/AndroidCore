package com.gzq.androidcore.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.githang.statusbar.StatusBarCompat;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_resource.router.CommonRouterApi;
import com.sjtu.yifei.route.Routerfit;

public class LauncherActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.setTranslucent(getWindow(), true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Box.getSessionManager().isLogin()) {
            Routerfit.register(CommonRouterApi.class).skipMainActivity();
        } else {
            Routerfit.register(CommonRouterApi.class).skipLoginActivity();
        }
        finish();
    }
}
