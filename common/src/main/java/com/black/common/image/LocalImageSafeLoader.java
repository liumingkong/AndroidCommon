package com.black.common.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.black.common.utils.Utils;

/**
 * Created by liumingkong on 15/6/5.
 */
public class LocalImageSafeLoader {

    // 安全加载图片
    public static Bitmap safeLoadImage(Context context, ImageView imageView, int drawableRes) {
        Bitmap newBitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false; // 设置成了true,不占用内存，只获取bitmap宽高
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inDither = false;
            options.inPurgeable = true;
            options.inSampleSize = 1;
            newBitmap = BitmapFactory.decodeResource(context.getResources(), drawableRes, options);
            if (!Utils.isNull(newBitmap) && !newBitmap.isRecycled()) {
                imageView.setImageBitmap(newBitmap);
            }
        } catch (Exception e) {
        } catch (OutOfMemoryError t) {
            System.gc();
        } finally {
            return newBitmap;
        }
    }

    // 安全释放图片
    public static void onReleaseImageView(Bitmap bitmap, ImageView view) {
        try {
            if (!Utils.isNull(bitmap) && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
            if (!Utils.isNull(view)) {
                view.setImageResource(0);
            }
        } catch (Throwable throwable) {
        }
    }
}
