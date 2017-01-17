package com.example.helldefender.rvfunction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;

import com.example.helldefender.rvfunction.app.BaseFragment;
import com.example.helldefender.rvfunction.fragment.LoginFragment;

/**
 * Created by Helldefender on 2016/11/18.
 */

public class LoginActivity extends SimpleActivity {
    private boolean isLogin;

    @Override
    protected BaseFragment getFragment() {
        return LoginFragment.getInstance(isLogin);
    }

    @Override
    protected void refreshPartViewMode(TypedValue background, int color) {

    }

    @Override
    protected void handlerIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            isLogin = bundle.getBoolean("isLogin", false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("DDD", "点击了Toolbar系统返回键");
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    finish();
                    Log.d("DDD", "马上就要调用Finish()");
                    return true;
                }else{
                    Log.d("DDD", "Remove Fragment");
                    removeFragment();
                }
                break;
        }
       return false;
    }
}
