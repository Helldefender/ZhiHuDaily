package com.example.helldefender.rvfunction.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.helldefender.rvfunction.app.BaseFragment;
import com.example.helldefender.rvfunction.app.MyApplication;
import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.util.ActivityUtil;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import rx.Observer;
import rx.functions.Func2;

/**
 * Created by Helldefender on 2016/11/18.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener {
    public static final int FRAGMENT_REQUEST = 0X110;
    public static final String FRAGMENT_LOGIN = "FRAGMENT_LOGIN";
    private EditText mUserNameET;
    private EditText mPassWordET;
    private Button loginBtn;
    private Button registerBtn;

    private boolean isLogin;

    private Button logoutBtn;
    private ImageView avatar;
    private EditText logoutET;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isLogin = (boolean) getArguments().getSerializable(FRAGMENT_LOGIN);
        }
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        handleView(view);
        if (isLogin) {
            baseActivity.getSupportActionBar().setTitle("个人主页");
            logoutET.setText(MyApplication.sharedPreferences.getString("userName", ""));
            logoutBtn.setOnClickListener(this);
            ActivityUtil.handleImageToRound(getHoldingActivity(), "http://pic3.zhimg.com//0ecf2216c2612b04592126adc16affa2_im.jpg", avatar, 100, 100, 100);
        } else {
            baseActivity.getSupportActionBar().setTitle("登录");
            loginBtn.setOnClickListener(this);
            registerBtn.setOnClickListener(this);
            combineLatestEvent();
        }

    }

    @Override
    protected int getLayoutId() {
        return isLogin ? R.layout.fragment_logout : R.layout.fragment_login;
    }

    private void handleView(View view) {
        mUserNameET = (EditText) view.findViewById(R.id.login_username_edit);
        mPassWordET = (EditText) view.findViewById(R.id.login_password_edit);
        loginBtn = (Button) view.findViewById(R.id.login_btn);
        registerBtn = (Button) view.findViewById(R.id.login_register);

        logoutBtn = (Button) view.findViewById(R.id.logout_btn);
        avatar = (ImageView) view.findViewById(R.id.logout_avator);
        logoutET = (EditText) view.findViewById(R.id.logout_username);
    }

    private void combineLatestEvent() {
        //关于edittext获取焦点的问题
        Observable<CharSequence> usernameObservable = RxTextView.textChanges(mUserNameET);
        Observable<CharSequence> passwordObservable = RxTextView.textChanges(mPassWordET);
        Observable.combineLatest(usernameObservable, passwordObservable, new Func2<CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence userName, CharSequence password) {
                boolean isUserNameValid = judgeUserName(userName);
                if (!isUserNameValid) {
                    mUserNameET.setError("用户名未注册或输入错误");
                }
                boolean isPasswordValid = judgeUserPassword(userName, password);
                if (!isPasswordValid) {
                    mPassWordET.setError("密码输入错误");
                }
                return isUserNameValid && isPasswordValid;
            }
        }).subscribe(getObservable());
    }

    private Observer<Boolean> getObservable() {
        return new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                loginBtn.setEnabled(aBoolean);
            }
        };
    }

    private boolean judgeUserName(CharSequence userName) {
        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(userName).matches();
        if (isEmail) {
            return MyApplication.infoStorageManager.getUserEmail().contains(userName.toString());
        } else {
            return MyApplication.infoStorageManager.getUserName().contains(userName.toString());
        }
    }

    private boolean judgeUserPassword(CharSequence userName, CharSequence password) {
        boolean isEmail = Patterns.EMAIL_ADDRESS.matcher(userName).matches();
        if (isEmail) {
            return MyApplication.infoStorageManager.getPasswordByEmail(userName.toString()).equals(password.toString());
        } else {
            return MyApplication.infoStorageManager.getPasswordByUserName(userName.toString()).equals(password.toString());
        }
    }

    public static LoginFragment getInstance(boolean isLogin) {
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FRAGMENT_LOGIN, isLogin);
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                Intent intent = new Intent();
                intent.putExtra("userName", mUserNameET.getText().toString());
                getHoldingActivity().setResult(Activity.RESULT_OK, intent);
                getHoldingActivity().finish();
                break;
            case R.id.login_register:
                RegisterFragment registerFragment = RegisterFragment.getInstance();
                registerFragment.setTargetFragment(LoginFragment.this, FRAGMENT_REQUEST);
                addFragment(registerFragment);
                break;
            case R.id.logout_btn:
                MyApplication.editor.putBoolean("isLogin", false);
                MyApplication.editor.commit();
                getHoldingActivity().finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FRAGMENT_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    mUserNameET.setText(data.getStringExtra("userName"));
                    mPassWordET.setText(data.getStringExtra("password"));
                }
                break;
        }
    }
}
