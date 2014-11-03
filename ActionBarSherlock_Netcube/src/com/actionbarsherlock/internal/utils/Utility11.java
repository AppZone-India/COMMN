package com.actionbarsherlock.internal.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

// 11 is HONEYCOMB
@TargetApi(11)
public class Utility11 extends Utility9 {

    @Override
    public void viewSetActivated(View view, boolean activated) {
        view.setActivated(activated);
    }
    

    @Override
    public boolean hasPermanentMenuKey(ViewConfiguration vcfg) {
        return true;
    }
    
    @Override
    public void jumpDrawablesToCurrentState(View v) {
        v.jumpDrawablesToCurrentState();
    }
    
    @Override
    public void jumpToCurrentState(Drawable indeterminateDrawable) {
        indeterminateDrawable.jumpToCurrentState();
    }
    
    @Override
    public Drawable getActivityLogo(Context context) {
        Drawable mLogo = null;
        ApplicationInfo appInfo = context.getApplicationInfo();
        PackageManager pm = context.getPackageManager();
        if (context instanceof Activity) {
            try {
                mLogo = pm.getActivityLogo(((Activity) context).getComponentName());
            } catch (NameNotFoundException e) {
                Log.e("Utility", "Activity component name not found!", e);
            }
        }
        if (mLogo == null) {
            mLogo = appInfo.loadLogo(pm);
        }
        return mLogo;
    }
    
    @Override
    public PopupWindow buildPopupWindow(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        return new PopupWindow(context, attrs, defStyleAttr, defStyleRes);
    }
    
    @Override
    public int resolveSizeAndState(int size, int measureSpec, int state) {
        return View.resolveSizeAndState(size, measureSpec, state);
    }

    @Override
    public int getMeasuredState(View child) {
        return child.getMeasuredState();
    }
    
    @Override
    public int combineMeasuredStates(int curState, int newState) {
        return View.combineMeasuredStates(curState, newState);
    }

    @Override
    public void setLinearLayoutDividerDrawable(LinearLayout l, Drawable d) {
        l.setDividerDrawable(d);
        super.setLinearLayoutDividerDrawable(l, d);
    }
}
