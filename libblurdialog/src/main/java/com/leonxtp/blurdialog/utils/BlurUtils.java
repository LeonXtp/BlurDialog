package com.leonxtp.blurdialog.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by bianjp on 17-11-28.
 */

public class BlurUtils {

    /**
     * 截取屏幕
     */
    @Nullable
    public static Bitmap getScreenShot(Activity activity, int screenWidth, int screenHeight,
                                       int blurRadius) {

        // View是屏幕截图的View
        View view = activity.getWindow().getDecorView();

        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b = view.getDrawingCache();

        // 如果只截取app页面高度的图片，则显示模糊背景时有明显的“上移”感觉，体验差，直接取全屏高度，但是有个小问题：
        // 截图顶部会有一点白色，影响不大
        int statusBarHeight = getStatusBarHeight(activity);
        Bitmap bitmap = Bitmap.createBitmap(b, 0, statusBarHeight, screenWidth,
                screenHeight - statusBarHeight);

        view.destroyDrawingCache();
        bitmap = FastBlur.fastBlur(bitmap, blurRadius);
        if (bitmap != null) {
            return bitmap;
        } else {
            return null;
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }
}