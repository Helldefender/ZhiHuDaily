package com.example.helldefender.rvfunction.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.helldefender.rvfunction.app.BaseActivity;
import com.example.helldefender.rvfunction.app.MyApplication;
import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.app.BaseFragment;
import com.example.helldefender.rvfunction.fragment.ButtonFragment;
import com.example.helldefender.rvfunction.fragment.CollectionFragment;
import com.example.helldefender.rvfunction.fragment.SettingFragment;
import com.example.helldefender.rvfunction.util.ActivityUtil;
import com.example.helldefender.rvfunction.util.DayNightHelper;

/**
 * Created by Helldefender on 2016/10/28.
 */

public abstract class AppActivity extends BaseActivity implements View.OnClickListener {
    public final int REQUEST_CODE = 0X110;
    public boolean isLogin;

    public Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    public ActionMenuView actionMenuView;

    private View view;
    private TextView userName;
    private ImageView userAvator;
    private LinearLayout drawHeaderLayout;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_base;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.fragment_container;
    }

    @Override
    protected void refreshViewMode(TypedValue background, int color, TypedValue toolbarBackground) {
        toolbar.setBackgroundResource(toolbarBackground.resourceId);
        navigationView.setBackgroundResource(toolbarBackground.resourceId);
        TypedValue colorAccent = new TypedValue();
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.mColorAccent, colorAccent, true);
        View childView = navigationView.getHeaderView(0);
        childView.setBackgroundResource(colorAccent.resourceId);
        refreshPartViewMode(background, color, toolbarBackground);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDayNightHelper = DayNightHelper.getInstance(this);
        initTheme();
        setContentView(getContentViewId());

        initView();

        handleStatusBar();
        navigationToTop(drawerLayout);

        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //显示返回箭头 使用系统原生的返回键  通过进行监听，资源id为android.R.home
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置默认的标题 显示
            actionBar.setDisplayShowTitleEnabled(true);
        }

        //作用:更改图标，监听打开与关闭，
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        handleFragmentChange(1);
                        break;
                    case R.id.daily_psy:
                        handleFragmentChange(13);
                        break;
                    case R.id.user_recommend:
                        handleFragmentChange(12);
                        break;
                    case R.id.movie:
                        handleFragmentChange(3);
                        break;
                    case R.id.interesting:
                        handleFragmentChange(11);
                        break;
                    case R.id.design:
                        handleFragmentChange(4);
                        break;
                    case R.id.business:
                        handleFragmentChange(5);
                        break;
                    case R.id.economic:
                        handleFragmentChange(6);
                        break;
                    case R.id.internet_security:
                        handleFragmentChange(10);
                        break;
                    case R.id.games:
                        handleFragmentChange(2);
                        break;
                    case R.id.music:
                        handleFragmentChange(7);
                        break;
                    case R.id.comic:
                        handleFragmentChange(9);
                        break;
                    case R.id.physical:
                        handleFragmentChange(8);
                        break;
                    case R.id.setting:
                        handleFragmentChange(20);
                        break;
                    case R.id.collect:
                        handleFragmentChange(21);
                        break;
                }
                toolbar.setTitle(item.getTitle());
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return false;
            }
        });

        if (null != getIntent()) {
            handlerIntent(getIntent());
        }

        //避免重复添加fragment
        if (getSupportFragmentManager().getFragments() == null) {
            BaseFragment baseFragment = getFragment();
            if (baseFragment != null) {
                addFragment(baseFragment);
            }
        }

        userName.setOnClickListener(this);

        if (isLogin) {
            userName.setText(MyApplication.sharedPreferences.getString("userName", ""));
            ActivityUtil.handleImageToRound(this, "http://pic3.zhimg.com//0ecf2216c2612b04592126adc16affa2_im.jpg", userAvator, 200, 200, 200);
        } else {
            userName.setText("请登录");
            ActivityUtil.handleImageToRound(this, "http://pic3.zhimg.com//0ecf2216c2612b04592126adc16affa2_im.jpg", userAvator, 200, 200, 200);
        }

        initDownloadPart(ButtonFragment.getInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MyApplication.sharedPreferences.getBoolean("isLogin", false)) {
            userName.setText(MyApplication.sharedPreferences.getString("userName", ""));
            ActivityUtil.handleImageToRound(this, "http://pic3.zhimg.com//0ecf2216c2612b04592126adc16affa2_im.jpg", userAvator, 200, 200, 200);
        } else {
            userName.setText("请登录");
            ActivityUtil.handleImageToRound(this, "http://pic3.zhimg.com//0ecf2216c2612b04592126adc16affa2_im.jpg", userAvator, 200, 200, 200);
        }
    }

    private void handleFragmentChange(int id) {
        if (id == 1) {
            addFragment(getFragment());
        } else if (id == 20) {
            addFragment(SettingFragment.getInstance());
        } else if (id == 21) {
            addFragment(CollectionFragment.getInstance());
        } else {
            addFragment(getReplacedFragment(id));
        }
    }

    protected abstract BaseFragment getReplacedFragment(int id);

    protected void handlerIntent(Intent intent) {
    }

    protected abstract void refreshPartViewMode(TypedValue background, int color, TypedValue toolbarBackground);

    protected void initView() {
        isLogin = MyApplication.sharedPreferences.getBoolean("isLogin", false);

        navigationView = (NavigationView) findViewById(R.id.navigationView);
        toolbar = (Toolbar) findViewById(R.id.base_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.base_drawerlayout);
        actionMenuView = (ActionMenuView) findViewById(R.id.actionMenuView);

        view = navigationView.getHeaderView(0);
        userName = (TextView) view.findViewById(R.id.drawer_header_text);
        userAvator = (ImageView) view.findViewById(R.id.drawer_header_image);
        drawHeaderLayout = (LinearLayout) view.findViewById(R.id.drawer_header_layout);
    }

    private void initDownloadPart(BaseFragment baseFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.download_frameLayout, baseFragment, baseFragment.getClass().getSimpleName())
                .addToBackStack(baseFragment.getClass().getSimpleName())
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击 toolbar 返回键 finish 当前activity 系统原生返回键
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //点击返回键back时 抽屉打开 关闭抽屉
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
//            case R.id.drawer_header_layout: 为什么这种情况下无效，设置为drawerlayout_header_text和设置为drawer_heaeer_layout效果相同
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("isLogin", MyApplication.sharedPreferences.getBoolean("isLogin", false));
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //必须在前一个acitivity 调用finish 之后才会执行 只要跳转到的activity调用finish 比如从registfragmetn跳转过来此方法依旧会调用
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    isLogin = true;
                    String loginUserName = data.getStringExtra("userName");
                    MyApplication.editor.putString("userName", loginUserName);
                    MyApplication.editor.putBoolean("isLogin", true);
                    //请记住要提交  还有什么需要提交commit() fragment开启事物添加到返回栈 需要 commit
                    MyApplication.editor.commit();
                    userName.setText(loginUserName);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
