package com.brice.comet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable d;

    public DividerItemDecoration(Context context) {
        int[] attrs = new int[]{
                android.R.attr.listDivider
        };
        final TypedArray a = context.obtainStyledAttributes(attrs);
        d = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        drawVertical(c, parent);
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft(), right = parent.getWidth() - parent.getPaddingRight();

        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin, bottom = top + d.getIntrinsicHeight();
            d.setBounds(left, top, right, bottom);
            d.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        outRect.set(0, 0, 0, d.getIntrinsicHeight());
    }
}
