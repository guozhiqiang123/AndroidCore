package com.example.module_test1.interceptor;

import android.app.Activity;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.toast.T;
import com.gzq.lib_resource.router.CommonRouterApi;
import com.sjtu.yifei.annotation.Interceptor;
import com.sjtu.yifei.route.AInterceptor;
import com.sjtu.yifei.route.ActivityCallback;
import com.sjtu.yifei.route.Routerfit;

@Interceptor(priority = 10)
public class LoginInterceptor implements AInterceptor {
    @Override
    public void intercept(Chain chain) throws Exception {
        String path = chain.path();
        if (!path.equals("/login/register/login/activity")) {
            if (!Box.getSessionManager().isLogin()) {
                Routerfit.register(CommonRouterApi.class).skipLoginActivity(true, new ActivityCallback() {
                    @Override
                    public void onActivityResult(int result, Object data) {
                        if (result == Activity.RESULT_OK) {
                            Boolean isLogin = (Boolean) data;
                            if (isLogin) {
                                chain.proceed();
                            } else {
                                T.show("请先登录");
                            }
                        }
                    }
                });
            } else {
                chain.proceed();
            }
        } else {
            chain.proceed();
        }
    }
}
