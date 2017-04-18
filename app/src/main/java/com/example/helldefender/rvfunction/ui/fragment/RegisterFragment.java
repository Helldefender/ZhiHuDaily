package com.example.helldefender.rvfunction.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.helldefender.rvfunction.ui.fragment.base.BaseFragment;
import com.example.helldefender.rvfunction.app.MyApplication;
import com.example.helldefender.rvfunction.R;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func3;

/**
 * Created by Helldefender on 2016/11/25.
 */

public class RegisterFragment extends BaseFragment implements View.OnClickListener{
    private EditText mUserNameET;
    private EditText mPasswordET;
    private EditText mEmailET;
    private Button mRegisterBtn;

    private String mUserName;
    private String mPassword;
    private String mEmail;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        handleView(view);
        baseActivity.getSupportActionBar().setTitle("注册");
        combineLatestEvent();
        mRegisterBtn.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register;
    }

    private void handleView(View view) {
        mUserNameET = (EditText) view.findViewById(R.id.register_username_edit);
        mPasswordET = (EditText) view.findViewById(R.id.register_password_edit);
        mEmailET = (EditText) view.findViewById(R.id.register_email_edit);
        mRegisterBtn = (Button) view.findViewById(R.id.register_btn);
    }

    private void combineLatestEvent() {
        //如果不加skip的话，会先执行一次onNext()
        //如果在textchanges()后面加上函数skip(),会造成在第一次输入的时候，只有在输入全部EditText，才会发射数据，而不是在对其中任意一个进行遍写的时候就会发射数据
        Observable<CharSequence> userNameObservable = RxTextView.textChanges(mUserNameET).skip(1);
        Observable<CharSequence> passwordObservable = RxTextView.textChanges(mPasswordET);
        Observable<CharSequence> emailObservable = RxTextView.textChanges(mEmailET);
        Subscription subscription = Observable.combineLatest(userNameObservable, passwordObservable, emailObservable, new Func3<CharSequence, CharSequence, CharSequence, Boolean>() {
            @Override
            public Boolean call(CharSequence userName, CharSequence password, CharSequence email) {
                boolean isUserNameValid = !TextUtils.isEmpty(userName) && (userName.toString().length() > 2 && userName.toString().length() < 8);
                mUserName=userName.toString();
                if (!isUserNameValid) {
                    mUserNameET.setError("用户名无效");
                }
                boolean isPasswordValid = !TextUtils.isEmpty(password) && (password.toString().length() > 4 && password.toString().length() <8);
                mPassword=password.toString();
                if (!isPasswordValid) {
                    mPasswordET.setError("密码无效");
                }
                boolean isEmailValid = !TextUtils.isEmpty(password) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
                mEmail=email.toString();
                if (!isEmailValid) {
                    mEmailET.setError("邮箱无效");
                }
                return isUserNameValid && isPasswordValid && isEmailValid;
            }
        }).subscribe(getObserver());
    }

    private Observer<Boolean> getObserver() {
        return new Observer<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    saveToDatabase();
                }
                mRegisterBtn.setEnabled(aBoolean);
            }
        };
    }

    private void saveToDatabase() {
        MyApplication.infoStorageManager.saveUserInfo(mUserName.toString(),mPassword.toString(),mEmail.toString());
    }

    private void setResult() {
        if (getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra("userName", mUserName);
        intent.putExtra("password",mPassword);
        getTargetFragment().onActivityResult(LoginFragment.FRAGMENT_REQUEST, Activity.RESULT_OK, intent);
    }

    public static RegisterFragment getInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onClick(View v) {
        setResult();
        //addFragment(LoginFragment.getInstance(false));
        removeFragment();
    }
}
