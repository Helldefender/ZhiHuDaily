package com.example.helldefender.rvfunction.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

/**
 * Created by Helldefender on 2017/4/18.
 */

public class ImageManager {
    public static void handleImageByGlide(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).thumbnail(0.1f).into(imageView);
    }

    public static void handleImageToRound(Context context, String url, final ImageView imageView, final int width, final int height, final int radius) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable imageDrawable = new BitmapDrawable(resource);
                Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(output);
                RectF outerRect = new RectF(0, 0, width, height);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.RED);
                canvas.drawRoundRect(outerRect, radius, radius, paint);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                imageDrawable.setBounds(0, 0, width, height);
                canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
                imageDrawable.draw(canvas);
                canvas.restore();
                imageView.setImageBitmap(output);
            }
        });
    }
}
