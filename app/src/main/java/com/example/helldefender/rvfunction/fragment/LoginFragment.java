package com.example.helldefender.rvfunction.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.helldefender.rvfunction.MyApplication;
import com.example.helldefender.rvfunction.R;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func2;

/**
 * Created by Helldefender on 2016/11/18.
 */

public class LoginFragment extends BaseFragment implements View.OnClickListener {
    public static final int FRAGMENT_REQUEST = 0X110;
    private EditText mUserNameET;
    private EditText mPassWordET;
    private Button loginBtn;
    private Button registerBtn;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        handleView(view);
        baseActivity.getSupportActionBar().setTitle("登录");
        loginBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);
        combineLatestEvent();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }

    private void handleView(View view) {
        mUserNameET = (EditText) view.findViewById(R.id.login_username_edit);
        mPassWordET = (EditText) view.findViewById(R.id.login_password_edit);
        loginBtn = (Button) view.findViewById(R.id.login_btn);
        registerBtn = (Button) view.findViewById(R.id.login_register);
    }

    private void combineLatestEvent() {
        Observable<CharSequence> usernameObservable = RxTextView.textChanges(mUserNameET).skip(1);
        Observable<CharSequence> passwordObservable = RxTextView.textChanges(mPassWordET).skip(1);
        Subscription subscription = Observable.combineLatest(usernameObservable, passwordObservable, new Func2<CharSequence, CharSequence, Boolean>() {
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

    public static LoginFragment getInstance() {
        LoginFragment loginFragment = new LoginFragment();
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
