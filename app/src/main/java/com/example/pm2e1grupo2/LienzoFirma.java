package com.example.pm2e1grupo2;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LienzoFirma extends View {
    private final Path path = new Path();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public LienzoFirma(Context context) {
        super(context);
        init();
    }

    public LienzoFirma(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LienzoFirma(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX(), y = e.getY();
        if (e.getAction() == MotionEvent.ACTION_DOWN) path.moveTo(x, y);
        else if (e.getAction() == MotionEvent.ACTION_MOVE) path.lineTo(x, y);
        invalidate();
        return true;
    }

    public void limpiar() { path.reset(); invalidate(); }

    public Bitmap getBitmap() {
        Bitmap bmp = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        draw(c);
        return bmp;
    }
}
