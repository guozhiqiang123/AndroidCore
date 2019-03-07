package com.gzq.androidcore.ui;

import com.gzq.androidcore.R;
import com.gzq.androidcore.controller.LoginController;
import com.gzq.androidcore.databinding.ActivityLoginBinding;
import com.gzq.androidcore.vm.LoginViewModel;
import com.gzq.lib_resource.mvvm.base.BaseActivity;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel, LoginController> {
    @Override
    public void initParam() {

    }

    @Override
    public int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel setViewModel(ActivityLoginBinding binding) {
        LoginViewModel loginViewModel = new LoginViewModel();
        binding.setVm(loginViewModel);
        return loginViewModel;
    }

    @Override
    public LoginController setController(ActivityLoginBinding binding) {
        LoginController loginController = new LoginController();
        binding.setController(loginController);
        return loginController;
    }

    @Override
    public void setOtherModel(ActivityLoginBinding binding) {

    }
}
