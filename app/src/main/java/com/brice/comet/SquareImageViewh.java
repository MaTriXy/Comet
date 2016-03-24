package com.brice.comet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageViewh extends ImageView {

    public SquareImageViewh(Context context) {
        super(context);
    }

    public SquareImageViewh(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageViewh(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredHeight();
        setMeasuredDimension(width, width);
    }

}
