package com.leonxtp.anyblurpopupwindow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.leonxtp.blurdialog.SimpleBlurDialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showPop(View view) {
        showBlurDialog();
    }

    private SimpleBlurDialogFragment dialogFragment;

    private void showBlurDialog() {
        if (dialogFragment == null) {
            dialogFragment = SimpleBlurDialogFragment
                    .newInstance(R.layout.layout_my_pop_content);
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

}
