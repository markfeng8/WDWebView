package com.wdweblib.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liyaqing on 2017/8/30.
 */

public class ImageOperation {

    /**
     * provide get bitmap from uri method.
     *
     * @param ac  Activity object
     * @param uri file uri
     * @return bitmap
     * @throws IOException input & output exception
     */
    @Nullable
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws IOException {
        return getBitmapFormUri(ac, uri, 0);
    }

    /**
     * provide get bitmap from uri method.
     *
     * @param ac       Activity object
     * @param uri      file uri
     * @param maxBytes max bytes for single image
     * @return bitmap
     * @throws IOException input & output exception
     */
    @Nullable
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri, int maxBytes) throws IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        // optional
        onlyBoundsOptions.inDither = true;
        // optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        if (input != null) {
            input.close();
        }
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1)) {
            return null;
        }
        // 图片分辨率以480x800为标准
        // 这里设置高度为20f
        float hh = 800f;
        // 这里设置宽度为20f
        float ww = 480f;
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        // be=1表示不缩放
        int be = 4;
        // 如果宽度大的话根据宽度固定大小缩放
        if (originalWidth > originalHeight && originalWidth > ww) {
            be = (int) (originalWidth / ww);
            // 如果高度高的话根据宽度固定大小缩放
        } else if (originalWidth < originalHeight && originalHeight > hh) {
            be = (int) (originalHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        // 比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        // 设置缩放比例
        bitmapOptions.inSampleSize = be;
        // optional
        bitmapOptions.inDither = true;
        // optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        if (input != null) {
            input.close();
        }

        return (bitmap != null) ? compressImage(bitmap, maxBytes) : null;
    }

    /**
     * 质量压缩方法
     *
     * @param image image of bitmap
     * @return bitmap object
     */
    public static Bitmap compressImage(Bitmap image, int maxBytes) {
        if (maxBytes == 0) {
            // default 100kb
            maxBytes = 100 * 1024;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里 100 表示不压缩，把压缩后的数据存放到 baos 中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        // 循环判断如果压缩后图片是否大于 100kb,大于继续压缩
        while (baos.toByteArray().length > maxBytes) {
            // 重置 baos 即清空 baos
            baos.reset();
            // 第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            // 这里压缩 options%，把压缩后的数据存放到 baos 中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少 10
            options -= 10;
        }
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        // 把ByteArrayInputStream数据生成图片
        return BitmapFactory.decodeStream(isBm, null, null);
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap bitmap
     * @return base64 string of bitmap
     */
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}
