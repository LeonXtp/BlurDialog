package com.leonxtp.blurdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * @author leonxtp on 17-12-1.
 */

public class SimpleBlurDialogFragment extends BlurDialogFragment {

    private final String TAG = this.getClass().getSimpleName();

    private int mLayoutId;
    private int mAnimResId;

    public static SimpleBlurDialogFragment newInstance(int layoutId) {
        return newInstance(layoutId, 0);
    }

    public static SimpleBlurDialogFragment newInstance(int layoutId, int animResId) {
        SimpleBlurDialogFragment dialogFragment = new SimpleBlurDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("layoutId", layoutId);
        bundle.putInt("animResId", animResId);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.i(TAG, "onCreateDialog...");

        Bundle bundle = getArguments();
        if (bundle != null) {
            mLayoutId = bundle.getInt("layoutId");
            mAnimResId = bundle.getInt("animResId");
            if (mAnimResId == 0) {
                mAnimResId = R.style.blur_pop_aim_style;
            }
        }

        final Dialog dialog = super.onCreateDialog(savedInstanceState);

        initCustomFeature(dialog);

        return dialog;
    }

    private void initCustomFeature(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {

            if (mAnimResId != 0) {
                window.getAttributes().windowAnimations = mAnimResId;
            }
            if (mLayoutId != 0) {
                window.requestFeature(Window.FEATURE_NO_TITLE);
            }
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView...");

        if (mLayoutId == 0) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        return inflater.inflate(mLayoutId, null);
    }

}
