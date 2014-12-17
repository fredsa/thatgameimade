package sauer.thatgameimade;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ColorChooserView extends View {

    private int canvasWidth;
    private int canvasHeight;
    private int shortSide;
    private Paint wheelPaint;
    private Paint bgPaint;
    private Paint currentColorPaint;
    private String TAG = ColorChooserView.class.getSimpleName();
    public static final int[] COLORS = new int[]{
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.CYAN,
            Color.BLUE,
            Color.MAGENTA,
            Color.RED};
    public static final float[] POSITIONS = new float[]{
            0f / 360f,
            60f / 360f,
            120f / 360f,
            180f / 360f,
            240f / 360f,
            300f / 360f,
            360f / 360f,
    };
    private float[] hsvFloats = new float[]{0f, 1f, 1f};
    private MainActivity mainActivity;


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

        wheelPaint.setShader(new SweepGradient(shortSide / 2, shortSide / 2, COLORS, POSITIONS));
    }

    {
        bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);

        wheelPaint = new Paint();

        currentColorPaint = new Paint();
        currentColorPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, bgPaint);

        canvas.rotate(-90, shortSide / 2, shortSide / 2);

        canvas.drawCircle(shortSide / 2, shortSide / 2, shortSide / 2 * .9f, wheelPaint);
        canvas.drawCircle(shortSide / 2, shortSide / 2, shortSide / 2 * .8f, bgPaint);

        canvas.drawCircle(shortSide / 2, shortSide / 2, shortSide / 2 * .3f, currentColorPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float deltaX = event.getX() - shortSide / 2;
        float deltaY = -(event.getY() - shortSide / 2);
        double radians = Math.atan2(deltaY, deltaX); // range: [-Math.PI, Math.PI]
        float sweepNumber = (float)
                (
                        radians > 0 ?
                                1 - (radians / (2f * Math.PI))
                                : 0 - (radians / (2f * Math.PI))
                ); // range: [0, 1]
        float degrees = (sweepNumber * 360 + 90) % 360;
        int color = toHSV(degrees);
        currentColorPaint.setColor(color);
        mainActivity.setDrawingColor(color);
        invalidate();
        return true;
    }

    private int toHSV(float degrees) {
        hsvFloats[0] = degrees;
        float[] hsv = hsvFloats;
        return Color.HSVToColor(hsv);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
