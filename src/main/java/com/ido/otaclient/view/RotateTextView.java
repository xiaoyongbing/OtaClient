package com.ido.otaclient.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Surface;

import androidx.appcompat.widget.AppCompatTextView;

import com.ido.otaclient.util.DipPixelUtil;


/**
 * Created by shangzheng on 2017/8/30
 * ☃☃☃ 19:45.
 * <p>
 * 文本可以朝着4个不同方向选择的TextView
 * 默认{@link Surface#ROTATION_270}为初始方向.
 * 主要用途:
 * 在CameraActivity2中,
 * 调用ToastUtils.show(int, int)方法,让Toast始终显示在当前方向的下面,
 * 以使弹出的Toast显示的方向和预期的一致
 */

public class RotateTextView extends AppCompatTextView {
    /**
     * 当前屏幕的方向
     */
    private int direction = Surface.ROTATION_270;
    private Bitmap leftBitmap;
    private int bitmapWidth;

    public RotateTextView(Context context) {
        super(context);
    }

    public RotateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (direction == 0 || direction == 2) {
            super.onMeasure(heightMeasureSpec, widthMeasureSpec);
            setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth() + bitmapWidth);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth() + bitmapWidth, getMeasuredHeight());
        }
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
        postInvalidate();
    }

    public void setLeftCompoundDrawables(int resId) {
        leftBitmap = ((BitmapDrawable) getResources().getDrawable(resId)).getBitmap();
        int padding = DipPixelUtil.dip2px(6);
        bitmapWidth = leftBitmap.getWidth() + padding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint textPaint = getPaint();
        textPaint.setColor(getCurrentTextColor());
        textPaint.drawableState = getDrawableState();

        canvas.save();

        if (direction == 0) {
            canvas.translate(0, getHeight());
            canvas.rotate(-90);
        } else if (direction == 1) {
            canvas.translate(getWidth(), getHeight());
            canvas.rotate(180);
        } else if (direction == 2) {
            canvas.translate(getWidth(), 0);
            canvas.rotate(90);
        }

        canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
        if (leftBitmap != null) {
            //文字左侧垂直居中绘制图片
            int top = 0;
            if (direction == 0 || direction == 2) {//竖屏
                top =getWidth()-getExtendedPaddingTop()*2-leftBitmap.getHeight()+getLayout().getTopPadding();
            } else {
                top =getHeight()-getExtendedPaddingTop()*2-leftBitmap.getHeight()+getLayout().getTopPadding();
            }
            canvas.drawBitmap(leftBitmap, 0, top, textPaint);
            canvas.translate(bitmapWidth, 0);
        }
        getLayout().draw(canvas);
        canvas.restore();
    }
}
