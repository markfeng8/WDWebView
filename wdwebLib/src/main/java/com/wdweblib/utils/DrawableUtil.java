package com.wdweblib.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * code by markfeng
 * <p>
 * create on 2021-06-07 15:17
 */
public class DrawableUtil {

    /**
     * 对目标 Drawable 进行着色
     *
     * @param context    上下文
     * @param drawableId image drawable id
     * @param colorId    rgb color id
     * @return Drawable object
     */
    @Nullable
    public static Drawable tintDrawable(Context context, @NonNull int drawableId, int colorId) {
        if (context != null) {
            int color = context.getApplicationContext().getResources().getColor(colorId);
            Drawable originBitmapDrawable = ContextCompat.getDrawable(context.getApplicationContext(), drawableId);
            if (originBitmapDrawable != null) {
                Drawable wrappedDrawable = DrawableCompat.wrap(originBitmapDrawable);
                DrawableCompat.setTint(wrappedDrawable, color);
                return wrappedDrawable;
            }
        }

        return null;
    }

    public static void scaleImage(final Activity activity, final View view, int drawableResId) {
        // 获取屏幕的高宽
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);

        // 解析将要被处理的图片
        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);
        if (resourceBitmap == null) {
            return;
        }

        // 开始对图片进行拉伸或者缩放
        // 使用图片的缩放比例计算将要放大的图片的高度
        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());

        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);

        view.getViewTreeObserver().addOnPreDrawListener(() -> {
            //这里防止图像的重复创建，避免申请不必要的内存空间
            if (scaledBitmap.isRecycled()) {
                //必须返回true
                return true;
            }

            // 当UI绘制完毕，我们对图片进行处理
            int viewHeight = view.getMeasuredHeight();

            // 计算将要裁剪的图片的顶部以及底部的偏移量
            int offset = (scaledBitmap.getHeight() - viewHeight) / 2;
            if (offset <= 0) {
                offset = 0;
            }

            // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
            Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, scaledBitmap.getWidth(),
                    scaledBitmap.getHeight() - offset * 2);

            if (!finallyBitmap.equals(scaledBitmap)) {//如果返回的不是原图，则对原图进行回收
                scaledBitmap.recycle();
                System.gc();
            }

            // 设置图片显示
            view.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), finallyBitmap));
            return true;
        });
    }

    /**
     * 根据图片的名称获取图片的 ID（非反射方式）
     *
     * @param imageName 图片名称，不带扩展名
     * @return DrawableId
     */
    public int getDrawableIdByResourceName(Context context, String imageName) {
        if (context == null) {
            return -1;
        }
        Context appContext = context.getApplicationContext();
        String packageName = appContext.getPackageName();
        return appContext.getResources().getIdentifier(imageName, "drawable", packageName);
    }
}