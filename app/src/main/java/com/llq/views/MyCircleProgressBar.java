package com.llq.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.liliqing.R;

/**
 * Created by Administrator on 2015/8/30.
 */
public class MyCircleProgressBar extends View {

    public int progress = 0;

    private Context context;
    private Paint paint;

    //属性
    private int max;
    private int radius;
    private int bgColor;
    private int fgColor;
    private int drawStyle;
    private int strokeWidth;

    public MyCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);//消除锯齿
        paint.setStyle(Paint.Style.STROKE);
        initProperty(attrs);
    }

    private void initProperty(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyCircleProgressBar);
        max = typedArray.getInteger(R.styleable.MyCircleProgressBar_max, 100);
        radius = typedArray.getInteger(R.styleable.MyCircleProgressBar_radius, 10);
        bgColor = typedArray.getColor(R.styleable.MyCircleProgressBar_bgColor, Color.GRAY);
        fgColor = typedArray.getColor(R.styleable.MyCircleProgressBar_fgColor, Color.RED);
        drawStyle = typedArray.getInt(R.styleable.MyCircleProgressBar_drawStyle, 0);
        strokeWidth = typedArray.getInteger(R.styleable.MyCircleProgressBar_strokeWidth, 10);
    }


    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > max) {
            progress = max;
        } else {
            this.progress = progress;
        }
    }

    public int getMax() {
        return max;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        paint.setColor(bgColor);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(center, center, radius, paint);

        boolean opt;
        paint.setColor(fgColor);
        if (drawStyle == 0) {
            paint.setStyle(Paint.Style.STROKE);
            opt = false;
        } else {
            paint.setStyle(Paint.Style.FILL);
            opt = true;
        }
        int top = center - radius;
        int bottom = center + radius;
        RectF oval = new RectF(top, top, bottom, bottom);
        canvas.drawArc(oval, 270, 360*progress/max, opt, paint);
    }
}
