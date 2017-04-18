package com.example.helldefender.rvfunction.ui.activity;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.helldefender.rvfunction.R;
import com.example.helldefender.rvfunction.ui.fragment.base.BaseFragment;
import com.example.helldefender.rvfunction.ui.fragment.MainFragment;
import com.example.helldefender.rvfunction.ui.fragment.ThemeFragment;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppActivity {
    private boolean isModeChanged=false;

    @Override
    protected BaseFragment getFragment() {
        return MainFragment.getInstance();
    }

    @Override
    protected BaseFragment getReplacedFragment(int id) {
        return ThemeFragment.getInstance(id);
    }

    @Override
    protected void refreshPartViewMode(TypedValue background, int color,TypedValue toolbarBackground) {
        if (!isModeChanged) {
            RecyclerView mRecyclerView = (RecyclerView) getSupportFragmentManager().findFragmentByTag("MainFragment").getView().findViewById(R.id.recyclerview);
            int childCount = mRecyclerView.getChildCount();
            for (int childIndex = 1; childIndex < childCount; childCount++) {
                ViewGroup childView = (ViewGroup) mRecyclerView.getChildAt(childIndex);
                childView.setBackgroundResource(background.resourceId);
                View layout = childView.findViewById(R.id.item_container);
                layout.setBackgroundResource(background.resourceId);
                TextView textView = (TextView) childView.findViewById(R.id.item_title);
                textView.setBackgroundResource(background.resourceId);
                textView.setTextColor(color);
            }
            Class<RecyclerView> recyclerViewClass = RecyclerView.class;
            try {
                Field declaredField = recyclerViewClass.getDeclaredField("mRecycler");
                declaredField.setAccessible(true);
                Method declaredMethod = Class.forName(RecyclerView.Recycler.class.getName()).getDeclaredMethod("clear", (Class<?>[]) new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(declaredField.get(mRecyclerView), new Object[0]);
                RecyclerView.RecycledViewPool recycledViewPool = mRecyclerView.getRecycledViewPool();
                recycledViewPool.clear();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            //如果是使用settingactivity 在onCreate
            View layout = getSupportFragmentManager().findFragmentByTag("SettingFragment").getView().findViewById(R.id.setting_layout);
            TextView textView = (TextView) layout.findViewById(R.id.setting_username);
            textView.setTextColor(color);
            textView.setBackgroundResource(background.resourceId);
            SwitchCompat switchCompat = (SwitchCompat) layout.findViewById(R.id.switchCompat);
            switchCompat.setTextColor(color);
            switchCompat.setBackgroundResource(background.resourceId);
        }

    }

    public void modeChange() {
        isModeChanged = true;
        dayNightMode();
    }
}
