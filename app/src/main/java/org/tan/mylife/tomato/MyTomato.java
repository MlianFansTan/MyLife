package org.tan.mylife.tomato;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.tan.mylife.R;

/**
 * Created by a on 2017/11/5.
 */

public class MyTomato extends View {

    private int TomatoOrRest = 0;   //默认0为tomato

    private int mMeasureHeigth;
    private int mMeasureWidth;


    private Paint mCirclePaint;
    private float mCircleXY;        //园的圆心
    private float mRadius;;       //圆的半径

    private Paint mArcPaint;
    private RectF mArcRectF;
    private float mSweepAngle;
    private float mSweepValue = 100;

    private Paint mTextPaint;
    private String mShowText = "开始番茄";
    private float mShowTextSize;

    public MyTomato(Context context){
        super(context);
    }

    public MyTomato(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    public MyTomato(Context context, AttributeSet attrs) {
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
        // 绘制圆
        canvas.drawCircle(mCircleXY, mCircleXY, mRadius, mCirclePaint);
        // 绘制弧线
        canvas.drawArc(mArcRectF, 270, mSweepAngle, false, mArcPaint);
        // 绘制文字
        canvas.drawText(mShowText, 0, mShowText.length(),
                mCircleXY, mCircleXY + (mShowTextSize / 4), mTextPaint);
    }

    private void initView(){
        float length = 0;
        if (mMeasureHeigth >= mMeasureWidth) {
            length = mMeasureWidth;
        } else {
            length = mMeasureHeigth;
        }

        mCircleXY = length / 2;
        mRadius = (float)(length * 0.4 / 2);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        if (TomatoOrRest == 0)
            mCirclePaint.setColor(getResources().getColor(android.R.color.holo_orange_dark));
        else
            mCirclePaint.setColor(getResources().getColor(R.color.chartreuse));

        mArcRectF = new RectF(
                (float) (length * 0.15),
                (float) (length * 0.15),
                (float) (length * 0.85),
                (float) (length * 0.85));
        mSweepAngle = (mSweepValue / 100f) * 360f;
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        if (TomatoOrRest == 0)
            mArcPaint.setColor(getResources().getColor(android.R.color.holo_orange_dark));
        else
            mArcPaint.setColor(getResources().getColor(R.color.chartreuse));
        mArcPaint.setStrokeWidth((float) (length * 0.1));
        mArcPaint.setStyle(Paint.Style.STROKE);

        mShowTextSize = setShowTextSize();
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mShowTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        if(TomatoOrRest == 0)
            mTextPaint.setColor(getResources().getColor(android.R.color.white));
        else
            mTextPaint.setColor(getResources().getColor(R.color.colorAccent));
    }

    private float setShowTextSize() {
        this.invalidate();
        return 65;
    }

    public void setShowTextAndSweepValue(String showText, float SweepValue){
        if (showText.equals("")){
            mShowText = "开始番茄";
        }else{
            mShowText = showText;
        }
        mSweepValue = SweepValue;
        mSweepAngle = (mSweepValue / 100f) * 360f;
        this.invalidate();
    }

    public void setPainColorToRest(){
        mTextPaint.setColor(getResources().getColor(R.color.colorAccent));
        mArcPaint.setColor(getResources().getColor(R.color.chartreuse));
        mCirclePaint.setColor(getResources().getColor(R.color.chartreuse));
        this.invalidate();
    }

    public void setPainColorToDefault(){
        mCirclePaint.setColor(getResources().getColor(android.R.color.holo_orange_dark));
        mArcPaint.setColor(getResources().getColor(android.R.color.holo_orange_dark));
        mTextPaint.setColor(getResources().getColor(android.R.color.white));
        this.invalidate();
    }

    public void changeColor(int changeNum){
        if (changeNum == 0)
            TomatoOrRest = 0;
        else if(changeNum == 1)
            TomatoOrRest = 1;
        initView();
    }

    /*public void setShowText(String showText){
        if (showText.equals("")){
            mShowText = "开始番茄";
        }else{
            mShowText = showText;
        }
        this.invalidate();
    }
    public void setSweepValue(float sweepValue) {
        if (sweepValue != 0) {
            mSweepValue = sweepValue;
            Log.d("Yes","setSweepValue："+mSweepValue);
        } else {
            mSweepValue = 25;
        }
        this.invalidate();
    }*/
}
