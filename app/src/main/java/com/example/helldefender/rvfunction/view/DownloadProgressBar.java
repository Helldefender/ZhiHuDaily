package com.example.helldefender.rvfunction.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.helldefender.rvfunction.R;

/**
 * Created by Helldefender on 2016/11/17.
 */

public class DownloadProgressBar extends View implements Runnable {
    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

    private int DEFAULT_HEIGHT_DP = 35;

    private int borderWidth;

    private float maxProgress = 100f;

    private Paint textPaint;

    private Paint bgPaint;

    private Paint pgPaint;

    private String progressText;

    private Rect textRect;

    private RectF bgRectf;

    /**
     * 左右来回移动的滑块
     */
    private Bitmap flikerBitmap;

    /**
     * 滑块移动最左边位置，作用是控制移动
     */
    private float flickerLeft;

    /**
     * 进度条 bitmap ，包含滑块
     */
    private Bitmap pgBitmap;

    private Canvas pgCanvas;

    /**
     * 当前进度
     */
    private float progress;

    private boolean isFinish;

    private boolean isStop;

    /**
     * 下载中颜色
     */
    private int loadingColor;

    /**
     * 暂停时颜色
     */
    private int stopColor;

    /**
     * 进度文本、边框、进度条颜色
     */
    private int progressColor;

    private int textSize;

    private int radius;

    private Thread thread;

    BitmapShader bitmapShader;

    //自定义view需要三个构造函数，前两个均需要调用第三个构造函数（同时也可以是第一个调用第二个，第二个调用第三个）
    public DownloadProgressBar(Context context) {
        this(context, null, 0);
    }

    public DownloadProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    //自定义view在构造函数中初始化数据attrs styleable
    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.FlikerProgressBar);
        try {
            textSize = (int) ta.getDimension(R.styleable.FlikerProgressBar_textSize, 12);
            loadingColor = ta.getColor(R.styleable.FlikerProgressBar_loadingColor, Color.parseColor("#40c4ff"));
            stopColor = ta.getColor(R.styleable.FlikerProgressBar_stopColor, Color.parseColor("#ff9800"));
            radius = (int) ta.getDimension(R.styleable.FlikerProgressBar_radius, 0);
            borderWidth = (int) ta.getDimension(R.styleable.FlikerProgressBar_borderWidth, 1);
        } finally {
            ta.recycle();
        }
    }

    private void init() {
        //对于画笔的粗细的意义是什么
        //形参的意义是什么
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(borderWidth);

        pgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pgPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);

        textRect = new Rect();
        bgRectf = new RectF(borderWidth, borderWidth, getMeasuredWidth() - borderWidth, getMeasuredHeight() - borderWidth);

        if (isStop) {
            progressColor = stopColor;
        } else {
            progressColor = loadingColor;
        }

        flikerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mprogressviewbackground);
        flickerLeft = -flikerBitmap.getWidth();

        initPgBimap();
    }

    private void initPgBimap() {
        //生成一块新的画布，用于显示已完成进度
        //
        pgBitmap = Bitmap.createBitmap(getMeasuredWidth() - borderWidth, getMeasuredHeight() - borderWidth, Bitmap.Config.ARGB_8888);
        pgCanvas = new Canvas(pgBitmap);
        thread = new Thread(this);
        thread.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;
        switch (heightSpecMode) {
            case MeasureSpec.AT_MOST:
                height = dp2px(DEFAULT_HEIGHT_DP);
                break;
            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                height = heightSpecSize;
                break;
        }
        setMeasuredDimension(widthSpecSize, height);

        if (pgBitmap == null) {
            //进行绘图函数变量的初始化，包括画笔和矩形
            init();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //背景
        drawBackGround(canvas);

        //进度
        drawProgress(canvas);

        //进度text
        drawProgressText(canvas);

        //变色处理
        drawColorProgressText(canvas);
    }

    /**
     * 边框
     *
     * @param canvas
     */
    private void drawBackGround(Canvas canvas) {
        bgPaint.setColor(progressColor);
        //left、top、right、bottom不要贴着控件边，否则border只有一半绘制在控件内,导致圆角处线条显粗
        canvas.drawRoundRect(bgRectf, radius, radius, bgPaint);
    }

    /**
     * 进度
     */
    private void drawProgress(Canvas canvas) {
        pgPaint.setColor(progressColor);
        float right = (progress / maxProgress) * getMeasuredWidth();
        //这是在保存什么
        pgCanvas.save(Canvas.CLIP_SAVE_FLAG);
        //切割部分画笔。，天从颜色
        pgCanvas.clipRect(0, 0, right, getMeasuredHeight());
        pgCanvas.drawColor(progressColor);
        pgCanvas.restore();

        if (!isStop) {
            //添加的图片放在前面
            pgPaint.setXfermode(xfermode);
            pgCanvas.drawBitmap(flikerBitmap, flickerLeft, 0, pgPaint);
            pgPaint.setXfermode(null);
        }

        //控制显示区域 实现圆形和圆角图片，处理进度条的弧度问题
        bitmapShader = new BitmapShader(pgBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        pgPaint.setShader(bitmapShader);
        //对于原始画布进行完全的填充
        canvas.drawRoundRect(bgRectf, radius, radius, pgPaint);
    }

    /**
     * 进度提示文本
     *
     * @param canvas
     */
    private void drawProgressText(Canvas canvas) {
        textPaint.setColor(progressColor);
        progressText = getProgressText();
        //关于绘制各种图形的坐标的问题
        textPaint.getTextBounds(progressText, 0, progressText.length(), textRect);
        int tWidth = textRect.width();
        int tHeight = textRect.height();
        float xCoordinate = (getMeasuredWidth() - tWidth) / 2;
        float yCoordinate = (getMeasuredHeight() + tHeight) / 2;
        //绘制文本的时候为 文字开始的起始位置的坐标
        canvas.drawText(progressText, xCoordinate, yCoordinate, textPaint);
    }

    /**
     * 变色处理
     *
     * @param canvas
     */
    private void drawColorProgressText(Canvas canvas) {
        textPaint.setColor(Color.WHITE);
        int tWidth = textRect.width();
        int tHeight = textRect.height();
        //文字开始坐标
        float xCoordinate = (getMeasuredWidth() - tWidth) / 2;
        float yCoordinate = (getMeasuredHeight() + tHeight) / 2;
        float progressWidth = (progress / maxProgress) * getMeasuredWidth();
        if (progressWidth > xCoordinate) {
            canvas.save(Canvas.CLIP_SAVE_FLAG);
            float right = Math.min(progressWidth, xCoordinate + tWidth * 1.1f);
            //进行裁剪画布,只会显示被裁剪的区域  最后一个参数 六种类型
            canvas.clipRect(xCoordinate, 0, right, getMeasuredHeight());
            //切掉割掉一部分文字，保留一部分文字，问题在于 1.1

            canvas.drawText(progressText, xCoordinate, yCoordinate, textPaint);
            canvas.restore();
        }
    }

    public void setProgress(float progress) {
        if (!isStop) {
            if (progress < maxProgress) {
                this.progress = progress;
            } else {
                this.progress = maxProgress;
                finishLoad();
            }
            invalidate();
        }
    }

    public void setStop(boolean stop) {
        isStop = stop;
        if (isStop) {
            progressColor = stopColor;
            thread.interrupt();
        } else {
            progressColor = loadingColor;
            thread = new Thread(this);
            thread.start();
        }
        invalidate();
    }

    public void finishLoad() {
        isFinish = true;
        setStop(true);
    }

    public void toggle() {
        if (!isFinish) {
            if (isStop) {
                setStop(false);
            } else {
                setStop(true);
            }
        }
    }

    @Override
    public void run() {
        int width = flikerBitmap.getWidth();
        try {
            while (!isStop && !thread.isInterrupted()) {
                flickerLeft += dp2px(5);
                float progressWidth = (progress / maxProgress) * getMeasuredWidth();
                if (flickerLeft >= progressWidth) {
                    flickerLeft = -width;
                }
                postInvalidate();
                Thread.sleep(20);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置
     */
    public void reset() {
        setStop(true);
        progress = 0;
        isFinish = false;
        isStop = false;
        progressColor = loadingColor;
        progressText = "";
        flickerLeft = -flikerBitmap.getWidth();
        initPgBimap();
    }

    public float getProgress() {
        return progress;
    }

    public boolean isStop() {
        return isStop;
    }

    public boolean isFinish() {
        return isFinish;
    }

    private String getProgressText() {
        String text = "";
        if (!isFinish) {
            if (!isStop) {
                text = "下载中" + progress + "%";
            } else {
                text = "继续";
            }
        } else {
            text = "下载完成";
        }

        return text;
    }

    private int dp2px(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }
}
