package com.example.helldefender.rvfunction.app;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.entity.DayNight;
import com.example.helldefender.rvfunction.util.DayNightHelper;

/**
 * Created by Helldefender on 2016/10/19.
 */

public abstract class BaseActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected DayNightHelper mDayNightHelper;

    protected abstract int getContentViewId();

    protected abstract int getFragmentContentId();

    protected abstract void refreshViewMode(TypedValue background,int color,TypedValue toolbarBackground);

    protected abstract BaseFragment getFragment();

    public void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    protected void handleStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View view = getWindow().getDecorView();
            int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            view.setSystemUiVisibility(options);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutparams = getWindow().getAttributes();
            layoutparams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | layoutparams.flags);
        }
    }

    protected void navigationToTop(DrawerLayout mDrawerLayout) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mDrawerLayout.setFitsSystemWindows(true);
            mDrawerLayout.setClipToPadding(false);
        }
    }

    protected void initTheme() {
        if (mDayNightHelper.isDay()) {
            setTheme(R.style.DayTheme);
        } else {
            setTheme(R.style.NightTheme);
        }
    }

    protected void dayNightMode(){
        toggleThemeSetting();
        refreshUI();
    }

    protected void toggleThemeSetting() {
        if (mDayNightHelper.isDay()) {
            mDayNightHelper.setMode(DayNight.NIGHT);
            setTheme(R.style.NightTheme);
        } else {
            mDayNightHelper.setMode(DayNight.DAY);
            setTheme(R.style.DayTheme);
        }
    }

    protected void refreshUI() {
        TypedValue background=new TypedValue();
        TypedValue textColor=new TypedValue();
        TypedValue toolbarBackground=new TypedValue();
        Resources.Theme theme=getTheme();
        theme.resolveAttribute(R.attr.mColorBackground, background, true);
        theme.resolveAttribute(R.attr.mColorText, textColor, true);
        theme.resolveAttribute(R.attr.mColorPrimary, toolbarBackground, true);
        Resources resources=getResources();
        int color = resources.getColor(textColor.resourceId);
        refreshViewMode(background,color,toolbarBackground);
    }
}
