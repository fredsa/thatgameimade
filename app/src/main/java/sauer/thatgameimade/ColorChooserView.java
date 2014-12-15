package sauer.thatgameimade;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;

import static android.graphics.Color.RED;

public class ColorChooserView extends SurfaceView {

    private int canvasWidth;
    private int canvasHeight;
    private int shortSide;
    private Paint colorPaint;
    private Paint bgPaint;

    public ColorChooserView(Context context) {
        super(context);
    }

    public ColorChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorChooserView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorChooserView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        canvasWidth = width;
        canvasHeight = height;
        shortSide = Math.min(width, height);
    }

    {
        bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);

        colorPaint = new Paint();
        colorPaint.setColor(RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, canvasWidth, canvasHeight, bgPaint);
        canvas.drawCircle(shortSide / 2, shortSide / 2, shortSide / 2 * .8f, colorPaint);
        canvas.drawCircle(shortSide / 2, shortSide / 2, shortSide / 2 * .7f, bgPaint);
    }
}
