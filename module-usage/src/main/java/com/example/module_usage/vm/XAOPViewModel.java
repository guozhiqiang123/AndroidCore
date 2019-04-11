package com.example.module_usage.vm;

import android.Manifest;
import android.view.View;

import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.toast.T;
import com.gzq.lib_resource.mvvm.base.BaseViewModel;
import com.xuexiang.xaop.XAOP;
import com.xuexiang.xaop.annotation.IOThread;
import com.xuexiang.xaop.annotation.Intercept;
import com.xuexiang.xaop.annotation.MainThread;
import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xaop.annotation.SingleClick;
import com.xuexiang.xaop.checker.Interceptor;
import com.xuexiang.xaop.enums.ThreadType;
import com.xuexiang.xaop.util.PermissionUtils;

import org.aspectj.lang.JoinPoint;

import java.util.List;

public class XAOPViewModel extends BaseViewModel {

    public XAOPViewModel() {
        XAOP.setOnPermissionDeniedListener(new PermissionUtils.OnPermissionDeniedListener() {
            @Override
            public void onDenied(List<String> permissionsDenied) {
                T.show("被拒绝的权限：" + Box.getGson().toJson(permissionsDenied));
            }
        });

        XAOP.setInterceptor(new Interceptor() {
            @Override
            public boolean intercept(int type, JoinPoint joinPoint) throws Throwable {
                switch (type) {
                    case 3:
                        T.show("拦截器中的操作");
                        return false;
                    case 5:
                        return false;
                    case 7:
                        return true;
                }
                //返回true  被拦截的方法不再执行
                //返回false 被拦截的方法在处理完此处的逻辑后，可以正常执行
                return false;
            }
        });
    }

    public View.OnClickListener clickPermission = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            requestPermissions();
        }
    };

    public View.OnClickListener clickSingle = new View.OnClickListener() {
        @SingleClick(2000)
        @Override
        public void onClick(View v) {
            T.show("疯狂点击我");
        }
    };
    public View.OnClickListener clickInterceptor = new View.OnClickListener() {
        @Intercept({3, 5})
        @Override
        public void onClick(View v) {
            T.show("拦截器之后的事件");
        }
    };

    public View.OnClickListener clickMainThread = new View.OnClickListener() {
        @MainThread
        @Override
        public void onClick(View v) {
            T.show("当前的线程：" + Thread.currentThread().getName());
        }
    };

    public View.OnClickListener clickIOThread = new View.OnClickListener() {
        @IOThread(ThreadType.Network)
        @Override
        public void onClick(View v) {
            T.show("当前的线程：" + Thread.currentThread().getName());
        }
    };

    @Permission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
    private void requestPermissions() {
        T.show("我是请求权限成功之后需要做的事情");
    }

}
