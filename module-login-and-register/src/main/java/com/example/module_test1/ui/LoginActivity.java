package com.example.module_test1.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.example.module_test1.R;
import com.example.module_test1.databinding.ActivityLoginBinding;
import com.example.module_test1.utils.KeyboardWatcher;
import com.example.module_test1.vm.LoginViewModel;
import com.gzq.lib_resource.mvvm.base.BaseActivity;
import com.gzq.lib_resource.utils.ScreenUtils;
import com.sjtu.yifei.annotation.Route;
import com.sjtu.yifei.route.Routerfit;

@Route(path = "/login/register/login/activity")
public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements KeyboardWatcher.SoftKeyboardStateListener {


    private KeyboardWatcher keyboardWatcher;
    private int screenH;
    private float scale = 0.8f; //logo缩放比例
    private boolean flag = false;
    private boolean isStartForResult;

    @Override
    public void initParam(Intent intentArgument, Bundle bundleArgument) {
        isStartForResult = intentArgument.getBooleanExtra("isStartForResult", false);
    }

    @Override
    public int layoutId(Bundle savedInstanceState) {
        return R.layout.activity_login;
    }

    @Override
    public LoginViewModel setViewModel(ActivityLoginBinding binding) {
        LoginViewModel loginViewModel = new LoginViewModel(isStartForResult);
        binding.setVm(loginViewModel);
        return loginViewModel;
    }

    @Override
    public void setOtherModel(ActivityLoginBinding binding) {
        screenH = ScreenUtils.getScreenH();
        keyboardWatcher = new KeyboardWatcher(findViewById(Window.ID_ANDROID_CONTENT));
        keyboardWatcher.addSoftKeyboardStateListener(this);

        addEditTextChangeListener();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        keyboardWatcher.removeSoftKeyboardStateListener(this);
    }

    private void addEditTextChangeListener() {
        binding.etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && binding.ivCleanPhone.getVisibility() == View.GONE) {
                    binding.ivCleanPhone.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    binding.ivCleanPhone.setVisibility(View.GONE);
                }
            }
        });
        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && binding.cleanPassword.getVisibility() == View.GONE) {
                    binding.cleanPassword.setVisibility(View.VISIBLE);
                } else if (TextUtils.isEmpty(s)) {
                    binding.cleanPassword.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardSize) {
        int[] location = new int[2];
        binding.body.getLocationOnScreen(location); //获取body在屏幕中的坐标,控件左上角
        int x = location[0];
        int y = location[1];
        int bottom = screenH - (y + binding.body.getHeight());
        if (keyboardSize > bottom) {
            ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(binding.body, "translationY", 0.0f, -(keyboardSize - bottom));
            mAnimatorTranslateY.setDuration(300);
            mAnimatorTranslateY.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimatorTranslateY.start();
            zoomIn(binding.logo, keyboardSize - bottom);

        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(binding.body, "translationY", binding.body.getTranslationY(), 0);
        mAnimatorTranslateY.setDuration(300);
        mAnimatorTranslateY.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimatorTranslateY.start();
        zoomOut(binding.logo);
    }

    /**
     * 缩小
     *
     * @param view
     */
    public void zoomIn(final View view, float dist) {
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, scale);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, scale);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", 0.0f, -dist);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);

        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();

    }

    /**
     * f放大
     *
     * @param view
     */
    public void zoomOut(final View view) {
        if (view.getTranslationY() == 0) {
            return;
        }
        view.setPivotY(view.getHeight());
        view.setPivotX(view.getWidth() / 2);
        AnimatorSet mAnimatorSet = new AnimatorSet();

        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(view, "scaleX", scale, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(view, "scaleY", scale, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0);

        mAnimatorSet.play(mAnimatorTranslateY).with(mAnimatorScaleX).with(mAnimatorScaleY);
        mAnimatorSet.setDuration(300);
        mAnimatorSet.start();

    }

    /**
     * 密码：明文暗文
     *
     * @param view
     */
    public void showhidePassword(View view) {
        if (flag == true) {
            binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.ivShowPwd.setImageResource(R.drawable.ic_pass_gone);
            flag = false;
        } else {
            binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            binding.ivShowPwd.setImageResource(R.drawable.ic_pass_visuable);
            flag = true;
        }
        String pwd = binding.etPassword.getText().toString();
        if (!TextUtils.isEmpty(pwd))
            binding.etPassword.setSelection(pwd.length());
    }

}
