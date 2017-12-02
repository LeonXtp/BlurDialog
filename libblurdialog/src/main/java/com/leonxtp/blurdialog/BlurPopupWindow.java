package com.leonxtp.blurdialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.leonxtp.blurdialog.utils.BlurUtils;

/**
 * @author leonxtp 2017-11-29
 *         PopupWindow弹出时,Gravity.CENTER,这时它的中心y是以屏幕(包括状态栏)为基准的。
 */

public class BlurPopupWindow extends PopupWindow {

    private final String TAG = this.getClass().getSimpleName();

    private Bitmap mBlurBgBmp;
    private Builder mBuilder;

    private ObjectAnimator animatorShow;
    private ObjectAnimator animatorDismiss;

    /**
     * 内容View
     */
    protected View mContentView;

    public BlurPopupWindow(Builder builder, View contentView,
                           int width, final int height) {

        super(contentView, width, height);

        this.mBuilder = builder;
        this.mContentView = mBuilder.contentView;

        if (mBlurBgBmp == null) {
            mBlurBgBmp = BlurUtils.getScreenShot(builder.activity,
                    builder.screenWidth, builder.screenHeight, builder.blurRadius);
        }

        mBuilder.backgroundView.setBackground(new BitmapDrawable(mBuilder.activity.getResources(),
                mBlurBgBmp));

        initAnimator();

        if (mBuilder.cancelable) {

            mBuilder.backgroundView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    BlurPopupWindow.this.dismiss();
                }
            });
        }

        // enable EditText focus
        setFocusable(true);
        setAnimationStyle(R.style.blur_pop_aim_style);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    private void initAnimator() {

        //背景透明度渐变动画
        animatorShow = ObjectAnimator.ofFloat(mBuilder.backgroundView,
                "alpha", 0f, 1f);
        animatorShow.setDuration(300);

        //内容“窗体”位移动画
        animatorDismiss = ObjectAnimator.ofFloat(mBuilder.backgroundView,
                "alpha", 1f, 0f);
        animatorDismiss.setDuration(300);
    }

    public void show() {
        if (animatorShow.isRunning() || animatorDismiss.isRunning()) {
            return;
        }

        mBuilder.activity.getWindow().addContentView(
                mBuilder.backgroundView,
                new FrameLayout.LayoutParams(mBuilder.screenWidth, mBuilder.screenHeight));

        animatorShow.start();

        showAtLocation(mBuilder.parentView, Gravity.BOTTOM, 0, 0);

    }

    @Override
    public void dismiss() {
        if (animatorShow.isRunning() || animatorDismiss.isRunning()) {
            return;
        }
        animatorDismiss.start();
        animatorDismiss.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                BlurPopupWindow.super.dismiss();

                ViewGroup parent = (ViewGroup) mBuilder.backgroundView.getParent();
                if (parent != null) {
                    parent.removeView(mBuilder.contentView);
                }

            }
        });
    }

    public static class Builder {

        private Activity activity;
        private int width;
        private int height;
        private View parentView;
        private View anchorView;
        private View contentView;
        private int gravity;
        private int blurRadius = 4;
        private boolean cancelable;
        private LinearLayout backgroundView;
        private int screenWidth, screenHeight, statusBarHeight;

        public Builder with(Activity context) {
            this.activity = context;
            return this;
        }

        public Builder contentWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder contentHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder layoutId(int layoutId) {
            this.contentView = LayoutInflater.from(activity).inflate(layoutId, null);
            return this;
        }

        public Builder parent(View parent) {
            this.parentView = parent;
            return this;
        }

        public Builder anchor(View anchor) {
            this.anchorView = anchor;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder blurRadius(int blurRadius) {
            this.blurRadius = blurRadius;
            return this;
        }

        public Builder gravity(int gravity) {

            if (gravity != Gravity.TOP
                    && gravity != Gravity.CENTER
                    && gravity != Gravity.BOTTOM) {
                throw new IllegalArgumentException("暂不支持该属性");
            }

            this.gravity = gravity;
            return this;
        }

        public BlurPopupWindow build() {

            screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
            screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
            statusBarHeight = BlurUtils.getStatusBarHeight(activity);

            backgroundView = new LinearLayout(activity);
            // 设置为CENTER，计算位移的时候，算的是View的中心点的坐标
            backgroundView.setGravity(Gravity.CENTER);
            // 背景高度为屏幕高度
            backgroundView.setLayoutParams(new ViewGroup.LayoutParams(screenWidth,
                    screenHeight - statusBarHeight));

            contentView.setLayoutParams(new LinearLayout.LayoutParams(width, height));

            // PopupWindow给它全屏高度
            return new BlurPopupWindow(this, contentView, width, height);
        }

    }

}
