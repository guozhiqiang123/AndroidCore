package com.example.module_test1.vm;

import android.app.Activity;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.Gravity;

import com.example.module_test1.R;
import com.example.module_test1.api.LoginRegisterApi;
import com.example.module_test1.ui.LoginActivity;
import com.gzq.lib_core.base.Box;
import com.gzq.lib_core.http.observer.CommonObserver;
import com.gzq.lib_core.toast.T;
import com.gzq.lib_core.toast.style.ToastQQStyle;
import com.gzq.lib_core.utils.RxUtils;
import com.gzq.lib_resource.mvvm.base.BaseViewModel;
import com.gzq.lib_resource.mvvm.binding.command.BindingAction;
import com.gzq.lib_resource.mvvm.binding.command.BindingCommand;
import com.gzq.lib_resource.mvvm.binding.command.BindingConsumer;
import com.gzq.lib_resource.router.CommonRouterApi;
import com.sjtu.yifei.route.Routerfit;

public class LoginViewModel extends BaseViewModel<LoginActivity> {
    private boolean isStartForResult;
    public ObservableField<String> editPhone = new ObservableField<>("");
    public ObservableField<String> editPassword = new ObservableField<>("");

    public LoginViewModel(boolean isStartForResult) {
        this.isStartForResult = isStartForResult;
    }

    /**
     * 点击返回按钮
     */
    public BindingCommand clickClose = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            activity.finish();
        }
    });

    /**
     * 监听电话号码输入框内容的变化
     */
    public BindingCommand editTextPhoneChange = new BindingCommand(new BindingConsumer<String>() {
        @Override
        public void call(String phone) {
            editPhone.set(phone);
        }
    });
    /**
     * 监听密码输入的变化
     */
    public BindingCommand editTextPasswordChange = new BindingCommand(new BindingConsumer<String>() {
        @Override
        public void call(String password) {
            if (password.isEmpty())
                return;
            if (!password.matches("[A-Za-z0-9]+")) {
                T.show(R.string.please_input_limit_pwd);
                editPassword.set("");
                return;
            }
            editPassword.set(password);
        }
    });
    /**
     * 清除手机号码
     */
    public BindingCommand clearPhone = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            editPhone.set("");
        }
    });
    /**
     * 清除密码
     */
    public BindingCommand clearPassword = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            editPassword.set("");
        }
    });
    /**
     * 点击登录按钮
     */
    public BindingCommand clickLogin = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            String phone = editPhone.get();
            String password = editPassword.get();
            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
                T.show(R.string.please_input_phone_and_password);
                return;
            }
            login(phone, password);
        }
    });

    public BindingCommand clickRegister = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            T.show("点击了注册按钮");
        }
    });

    public BindingCommand clickForgetPassword = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            T.show("点击了忘记密码");
        }
    });

    private void login(String phone, String password) {
        Box.getRetrofit(LoginRegisterApi.class)
                .login(phone, password)
                .compose(RxUtils.httpResponseTransformer())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        showLoadingDialog();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        hideLoadingDialog();
                    }

                    @Override
                    public void onNext(Object o) {
                        T.show("登录成功");
                        Box.getSessionManager().setUser(o);
                        if (!isStartForResult) {
                            Routerfit.register(CommonRouterApi.class).skipMainActivity();
                        } else {
                            Routerfit.setResult(Activity.RESULT_OK, true);
                            activity.finish();
                        }
                    }
                });
    }
}
