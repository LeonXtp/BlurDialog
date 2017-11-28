package com.leonxtp.anyblurpopupwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

public class MainActivity extends AppCompatActivity {

    private BlurPopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showPopup(View view) {

        if (popupWindow == null) {
            popupWindow =
                    new BlurPopupWindow.Builder()
                            .with(this)
                            .width(400)
                            .height(400)
                            .layoutId(R.layout.layout_blur_popup)
                            .blurRadius(5)
                            .build();
        }
        if (!popupWindow.isShowing()) {
            popupWindow.showAtLocation(findViewById(R.id.contentView), Gravity.CENTER, 0, 0);
        } else {
            popupWindow.dismiss();
        }

    }
}
