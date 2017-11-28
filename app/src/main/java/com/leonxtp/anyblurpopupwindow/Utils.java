package com.leonxtp.anyblurpopupwindow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by bianjp on 17-11-28.
 */

public class Utils {

    /**
     * 截取屏幕
     */
    @Nullable
    public static Bitmap getScreenShot(Activity activity, int screenWidth, int screenHeight, int blurRadius) {

        // View是截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Bitmap bitmap = Bitmap.createBitmap(b, 0, 0, screenWidth, screenHeight);
        view.destroyDrawingCache();
        bitmap = FastBlur.fastBlur(bitmap, blurRadius);
        if (bitmap != null) {
            return bitmap;
        } else {
            return null;
        }
    }

}
