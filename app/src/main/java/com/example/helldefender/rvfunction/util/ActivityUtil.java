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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.helldefender.rvfunction.entity.TEntity;

/**
 * Created by Helldefender on 2016/10/19.
 */

public class ActivityUtil {
    public static void handleImageByGlide(Context context, String url, ImageView imageView) {
        Glide
                .with(context)
                .load(url)
                .thumbnail(0.1f)
                .into(imageView);
    }

    public static String structHtml(TEntity tEntity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<div class=\"img-wrap\">")
                .append("<h1 class=\"headline-title\">")
                .append(tEntity.getTitle()).append("</h1>")
                .append("<span class=\"img-source\">")
                .append(tEntity.getImage_source()).append("</span>")
                .append("<img src=\"").append(tEntity.getImage())
                .append("\" alt=\"\">")
                .append("<div class=\"img-mask\"></div>");
        String mNewsContent = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_content_style.css\"/>"
                + "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_header_style.css\"/>"
                + tEntity.getBody().replace("<div class=\"img-place-holder\">", stringBuilder.toString());
        return mNewsContent;
    }

    public static void handleImageToRound(Context context, String url, final ImageView imageView, final int width, final int height, final int radius) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Drawable imageDrawable = new BitmapDrawable(resource);
                Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                //画布
                Canvas canvas = new Canvas(output);
                //正方形
                RectF outerRect = new RectF(0, 0, width, height);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.RED);
                //圆角矩形
                canvas.drawRoundRect(outerRect, radius, radius, paint);
                //画笔特效处理，两个图层间的混合显示模式，叠加模式，遮罩
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                imageDrawable.setBounds(0, 0, width, height);
                //使用一个图层入栈，后面所有的操作都发生在这个图层上
                canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
                imageDrawable.draw(canvas);
                canvas.restore();
                imageView.setImageBitmap(output);
            }
        });
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


}
