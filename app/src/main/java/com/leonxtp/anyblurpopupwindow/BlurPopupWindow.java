package com.leonxtp.anyblurpopupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * Created by bianjp on 17-11-28.
 */

public class BlurPopupWindow extends PopupWindow {

    private Bitmap mBlurBmp;
    private Builder mBuilder;

    public BlurPopupWindow(Context context, Builder builder, View contentView,
                           int width, int height) {

        super(contentView, width, height);

        this.mBuilder = builder;

        if (mBlurBmp == null) {
            mBlurBmp = Utils.getScreenShot((Activity) builder.mContext,
                    builder.mScreenWidth, builder.mScreenHeight, builder.blurRadius);
        }

        mBuilder.mRootLayout.setBackground(new BitmapDrawable(mBuilder.mContext.getResources(), mBlurBmp));

    }

    public static class Builder {

        enum POSITION {
            CENTER,
            BOTTOM
        }

        private Context mContext;
        private int width;
        private int height;
        private int layoutId;
        private int blurRadius;
        private POSITION position;
        private LinearLayout mRootLayout;
        private int mScreenWidth, mScreenHeight;

        public Builder with(Context context) {
            this.mContext = context;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder layoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder blurRadius(int blurRadius) {
            this.blurRadius = blurRadius;
            return this;
        }

        public Builder POSITION(POSITION position) {
            this.position = position;
            return this;
        }

        public BlurPopupWindow build() {

            mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
            mScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;

            mRootLayout =
                    (LinearLayout) LayoutInflater.from(mContext)
                            .inflate(R.layout.layout_blur_popup, null);
            mRootLayout.setLayoutParams(new ViewGroup.LayoutParams(mScreenWidth, mScreenHeight));

            View contentView = LayoutInflater.from(mContext).inflate(layoutId, null);
            contentView.setLayoutParams(new LinearLayout.LayoutParams(width, height));

            mRootLayout.addView(contentView);

            BlurPopupWindow window = new BlurPopupWindow(mContext, this,
                    mRootLayout, mScreenWidth, mScreenHeight);

            return window;
        }

    }

}
