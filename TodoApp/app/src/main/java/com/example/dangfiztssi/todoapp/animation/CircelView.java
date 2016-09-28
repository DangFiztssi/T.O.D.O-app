package com.example.dangfiztssi.todoapp.animation;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DangF on 09/26/16.
 */

public class CircelView extends View {

    private static final int START_COLOR = 0xFFFF5722;
    private static final int END_COLOR = 0xFFFFC107;

    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    private Paint circlePaint = new Paint();
    private Paint maskPaint = new Paint();

    private Bitmap tmpBitmap;
    private Canvas tmpCanvas;

    private int maxCircleSize;

    private float outerCircleRadius = 0;
    private float innerCircleRadius = 0;

    private void init(){
        circlePaint.setStyle(Paint.Style.FILL);
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public CircelView(Context context) {
        super(context);
        init();
    }

    public CircelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public CircelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //First clear whole canvas
        tmpCanvas.drawColor(0xffffff, PorterDuff.Mode.CLEAR);
        //Next draws inner and outer Circle
        tmpCanvas.drawCircle(getWidth()/2, getHeight()/2, outerCircleRadius * maxCircleSize, circlePaint);
        tmpCanvas.drawCircle(getWidth()/2, getHeight()/2, innerCircleRadius * maxCircleSize, maskPaint);

        canvas.drawBitmap(tmpBitmap,0,0,null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        maxCircleSize = w/2;
        tmpBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        tmpCanvas = new Canvas(tmpBitmap);
    }

    public void setInnerCircleRadius(float innerCircleRadius){
        this.innerCircleRadius = innerCircleRadius;
        postInvalidate();
    }

    public float getOuterCircleRadius() {
        return outerCircleRadius;
    }

    public void setOuterCircleRadius(float outerCircleRadius) {
        this.outerCircleRadius = outerCircleRadius;
        updateCicrleColor();
        postInvalidate();
    }

    public float getInnerCircleRadius() {
        return innerCircleRadius;
    }

    private void updateCicrleColor(){
        float colorProgress = (float) Utils.clamp(outerCircleRadius, 0.5, 1);
        colorProgress = (float) Utils.mapValueFromRangeToRange(colorProgress, 0.5f, 1f, 0f, 1f);
        this.circlePaint.setColor((Integer) argbEvaluator.evaluate(colorProgress, START_COLOR, END_COLOR));
    }
}
