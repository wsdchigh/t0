package com.wsdchigh.g_a_rely.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

public class XImageView extends ImageView {
    Paint paint;

    public XImageView(Context context) {
        this(context,null);
    }

    public XImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        RectF rf = new RectF(0, (float) (height*0.9)+4,width,height+5);
        canvas.drawArc(rf,-180,180,true,paint);
        */
    }
}
