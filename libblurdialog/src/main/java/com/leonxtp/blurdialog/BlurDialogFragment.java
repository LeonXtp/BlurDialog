package com.leonxtp.blurdialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.widget.FrameLayout;

import com.leonxtp.blurdialog.utils.BlurUtils;


/**
 * @author leonxtp on 17-12-1.
 */

public class BlurDialogFragment extends DialogFragment {

    private final String TAG = this.getClass().getSimpleName();

    private Activity mActivity;
    private View mBackgroundView;
    private ObjectAnimator animator;

    private int mScreenWidth;
    private int mScreenHeight;

    private static boolean isDetached = false;
    private boolean isShowing;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = super.onCreateDialog(savedInstanceState);

        this.mActivity = getActivity();

        mScreenWidth = mActivity.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = mActivity.getResources().getDisplayMetrics().heightPixels;

        isDetached = false;

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e(TAG, "onCreateDialog, activity = ");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new BlurAsyncTask().execute();

        Log.e(TAG, "onViewCreated:");
    }

    private void onBlurBackgroundReady(Bitmap mBlurBmp) {
        mBackgroundView = new View(mActivity);
        mBackgroundView.setLayoutParams(new FrameLayout.LayoutParams(mScreenWidth, mScreenHeight));

        Drawable drawable;
        if (mBlurBmp == null) {
            drawable = new ColorDrawable(Color.BLACK);
        } else {
            drawable = new BitmapDrawable(mActivity.getResources(), mBlurBmp);
        }
        mBackgroundView.setBackground(drawable);

        initDismissAnimator();

        startBlurAnimation(mBlurBmp != null);
    }

    private void startBlurAnimation(boolean isBlurBackground) {
        Window window = mActivity.getWindow();
        if (window != null) {

            ObjectAnimator animator = ObjectAnimator.ofFloat(mBackgroundView,
                    "alpha", 0f, isBlurBackground ? 1f : 0.7f);
            animator.setDuration(500);
            animator.start();

            window.addContentView(
                    mBackgroundView, new FrameLayout.LayoutParams(mScreenWidth, mScreenHeight));
        }
    }

    private void setDialogWindowTransparent() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                //使xml中的圆角背景生效
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.e(TAG, "onStart");

        setDialogWindowTransparent();

    }

    public boolean isShowing() {
        return this.isShowing;
    }

    public void show(Activity activity) {

        super.show(activity.getFragmentManager(), TAG);
        this.isShowing = true;

    }

    private void initDismissAnimator() {
        animator = ObjectAnimator.ofFloat(mBackgroundView,
                "alpha", 1f, 0f);
        animator.setDuration(400);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);

                Log.e(TAG, "onAnimationEnd:" + System.currentTimeMillis());

                ViewGroup parent = (ViewGroup) mBackgroundView.getParent();
                if (parent != null) {
                    parent.removeView(mBackgroundView);
                }

                isShowing = false;

            }
        });
    }

    @Override
    public void dismiss() {

        Log.e(TAG, "dismiss:" + System.currentTimeMillis());

        animator.start();
        super.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        Log.e(TAG, "onDismiss..." + System.currentTimeMillis());

        if (!animator.isStarted()) {
            animator.start();
        }
    }

    private class BlurAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {

            return BlurUtils.getScreenShot(mActivity, mScreenWidth, mScreenHeight, 4);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (!isDetached) {
                onBlurBackgroundReady(bitmap);
            } else {
                bitmap.recycle();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach:" + context);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume:");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy:");
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        Log.e(TAG, "onCreateAnimator:");
        return super.onCreateAnimator(transit, enter, nextAnim);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, "onActivityCreated:");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "onDestroyView:");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isDetached = true;
        Log.e(TAG, "onDetach:");
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        Log.e(TAG, "onInflate:");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, "onPause:");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop:");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e(TAG, "onViewStateRestored:");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate:");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e(TAG, "onSaveInstanceState:");
    }
}
