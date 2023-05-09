package com.my.xhcustomviews.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {

    private Paint mPaint;
    private Path mPath;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mOffset;
    private LinearGradient mLinearGradient;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5f);
        mPaint.setShadowLayer(20, 10, 20, 0x66000000); // 设置阴影效果

        mPath = new Path();
        mOffset = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth = w;
        mScreenHeight = h;
        mLinearGradient = new LinearGradient(0, 0, mScreenWidth, 0,
                new int[]{0xFFE91E63, 0xFFF06292, 0xFFE91E63},
                new float[]{0, 0.5f, 1},
                Shader.TileMode.CLAMP);
        mPaint.setShader(mLinearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int segmentCount = 10; // 将波浪线分割为10段

        for (int i = 0; i < 3; i++) {
            mPath.reset();
            mPath.moveTo(0, mScreenHeight * i / 2);

            for (int seg = 0; seg < segmentCount; seg++) {
                int startX = mScreenWidth * seg / segmentCount;
                int endX = mScreenWidth * (seg + 1) / segmentCount;

                mPath.reset();
                mPath.moveTo(startX, mScreenHeight * i / 2);

                for (int x = startX; x <= endX; x++) {
                    float y = (float) (mScreenHeight / 2 + 50 * Math.sin(4 * Math.PI * x / mScreenWidth + mOffset * Math.PI / (90*(i+1))));
                    mPath.lineTo(x, y);
                }

                // 根据当前段的起始和结束X坐标计算Hue值
                float hueOffset = (mOffset % 360 + 360) % 360; // 将mOffset映射到0-360的范围内
                float startHue = ((360.0f * startX) / mScreenWidth + hueOffset) % 360;
                float endHue = ((360.0f * endX) / mScreenWidth + hueOffset) % 360;

                // 使用线性渐变近似HSB颜色模型的渐变效果
                mLinearGradient = new LinearGradient(
                        startX, 0, endX, 0,
                        Color.HSVToColor(new float[]{startHue, 1f, 1f}),
                        Color.HSVToColor(new float[]{endHue, 1f, 1f}),
                        Shader.TileMode.CLAMP
                );
                mPaint.setShader(mLinearGradient);

                canvas.drawPath(mPath, mPaint);
            }

            mOffset -= 10;
        }

        mHandler.sendEmptyMessageDelayed(0, 50);
    }


//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        int segmentCount = 10; // 将波浪线分割为10段
//
//        for (int i = 0; i < 3; i++) {
//            mPath.reset();
//            mPath.moveTo(0, mScreenHeight * i / 2);
//
//            for (int seg = 0; seg < segmentCount; seg++) {
//                int startX = mScreenWidth * seg / segmentCount;
//                int endX = mScreenWidth * (seg + 1) / segmentCount;
//
//                mPath.reset();
//                mPath.moveTo(startX, mScreenHeight * i / 2);
//
//                for (int x = startX; x <= endX; x++) {
//                    float y = (float) (mScreenHeight / 2 + 50 * Math.sin(4 * Math.PI * x / mScreenWidth + mOffset * Math.PI / 180));
//                    mPath.lineTo(x, y);
//                }
//
//                // 根据当前段的起始和结束X坐标计算Hue值
//                float startHue = (360.0f * startX) / mScreenWidth;
//                float endHue = (360.0f * endX) / mScreenWidth;
//
//                // 使用线性渐变近似HSB颜色模型的渐变效果
//                mLinearGradient = new LinearGradient(
//                        startX, 0, endX, 0,
//                        Color.HSVToColor(new float[]{startHue, 1f, 1f}),
//                        Color.HSVToColor(new float[]{endHue, 1f, 1f}),
//                        Shader.TileMode.CLAMP
//                );
//                mPaint.setShader(mLinearGradient);
//
//                canvas.drawPath(mPath, mPaint);
//            }
//
//            mOffset -= 15;
//        }
//
//        mHandler.sendEmptyMessageDelayed(0, 50);
//    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };
}
