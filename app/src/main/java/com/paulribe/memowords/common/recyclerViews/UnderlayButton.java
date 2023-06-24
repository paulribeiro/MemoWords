package com.paulribe.memowords.common.recyclerViews;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class UnderlayButton {
    private final String text;
    private final Bitmap bitmap;
    private final int color;
    private int pos;
    private RectF clickRegion;
    private final SwipeHelper.UnderlayButtonClickListener clickListener;

    public UnderlayButton(String text, Bitmap bitmap, int color, SwipeHelper.UnderlayButtonClickListener clickListener) {
        this.text = text;
        this.bitmap = bitmap;
        this.color = color;
        this.clickListener = clickListener;
    }

    public boolean onClick(float x, float y) {
        if (clickRegion != null && clickRegion.contains(x, y)) {
            clickListener.onClick(pos);
            return true;
        }
        return false;
    }

    public void onDraw(Canvas canvas, RectF rect, int pos) {
        Paint paint = new Paint();
        drawBackground(paint, canvas, rect);

        float spaceHeight = 0;
        //float textWidth = paint.measureText(text);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        float combinedHeight = bitmap.getHeight() + spaceHeight + bounds.height();
        canvas.drawBitmap(bitmap, rect.centerX() - (bitmap.getWidth() / 2f), rect.centerY() - (combinedHeight / 2), null);
        //If you want text as well with bitmap
        //canvas.drawText(text, rect.centerX() - (textWidth / 2), rect.centerY() + (combinedHeight / 2), paint);

        clickRegion = rect;
        this.pos = pos;
    }

    private void drawBackground(Paint p, Canvas c, RectF rect) {
        p.setColor(color);
        c.drawRect(rect, p);
    }
}
