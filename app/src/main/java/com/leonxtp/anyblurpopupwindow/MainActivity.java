package com.leonxtp.anyblurpopupwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.leonxtp.blurdialog.BlurPopupWindow;
import com.leonxtp.blurdialog.SimpleBlurDialogFragment;

public class MainActivity extends AppCompatActivity {

    private BlurPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showPop(View view) {
//        showBlurPop();
        showBlurDialog();
    }

    private void showBlurPop() {
        if (popupWindow == null) {
            popupWindow =
                    new BlurPopupWindow.Builder()
                            .with(this)
                            .contentWidth(400)
                            .contentHeight(400)
                            .layoutId(R.layout.layout_my_pop_content)
                            .parent(findViewById(R.id.contentView))
                            .blurRadius(5)
                            .cancelable(true)
                            .gravity(Gravity.BOTTOM)
                            .build();
        }

        if (!popupWindow.isShowing()) {
            popupWindow.show();
        } else {
            popupWindow.dismiss();
        }
    }

    private SimpleBlurDialogFragment dialogFragment;

    private void showBlurDialog() {
        if (dialogFragment == null) {
            dialogFragment = SimpleBlurDialogFragment
                    .newInstance(R.layout.layout_my_pop_content, R.style.blur_pop_aim_style);
            dialogFragment.setCancelable(true);
        }
        if (!dialogFragment.isShowing()) {
            dialogFragment.show(this);
        }
    }

    public void actionDismiss(View view) {
        dialogFragment.dismiss();
//        startActivity(new Intent(this, SecondActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
