package org.tan.mylife.tomato;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.tan.mylife.R;

/**
 * Created by a on 2017/11/6.
 */

public class MyRest extends View {

    private int mMeasureHeigth;
    private int mMeasureWidth;

    private Paint mCirclePaint;
    private float mCircleX, mCircleY;        //园的圆心
    private float mRadius;;       //圆的半径

    private Paint mTextPaint;
    private String mShowText = "休息";
    private float mShowTextSize = 40;

    private Paint mLinePaint;       //绘制直线
    private float startX, startY, endX, endY;

    public MyRest(Context context){
        super(context);
    }

    public MyRest(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    public MyRest(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec);
        mMeasureHeigth = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mMeasureWidth,mMeasureHeigth);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制圆
        canvas.drawCircle(mCircleX,mCircleY,mRadius, mCirclePaint);
        //绘制文字
        canvas.drawText(mShowText, 0, mShowText.length(),
                mCircleX, mCircleY + (mShowTextSize / 4), mTextPaint);
        //绘制直线
        canvas.drawLine(startX,startY,endX,endY,mLinePaint);
    }

    private void initView(){
        mCircleX = mMeasureWidth / 4;
        mCircleY = mMeasureHeigth / 2;
        mRadius = mCircleX;
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(getResources().getColor(R.color.chartreuse));

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mShowTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(getResources().getColor(R.color.colorAccent));

        mLinePaint = new Paint();
        mLinePaint.setColor(getResources().getColor(R.color.chartreuse));
        startX = mMeasureWidth / 2;
        startY = mMeasureHeigth / 2;
        endX = mMeasureWidth;
        endY = mMeasureHeigth / 2;
    }
}
