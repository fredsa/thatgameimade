package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ColorPickerView extends View {

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
    private final Paint sliderPaint;
    @SuppressWarnings("unused")
    private String TAG = ColorPickerView.class.getSimpleName();
    private float[] hsvFloats = new float[]{0f, 1f, 1f};
    private OnPaintChangeListener onPaintChangeListener;
    private int canvasWidth;
    private int canvasHeight;
    private int shortSide;
    private Paint wheelPaint;
    private Paint bgPaint;
    private Paint brushPaint;

    {
        bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);

        wheelPaint = new Paint();

        sliderPaint = new Paint();

        brushPaint = new Paint();
        brushPaint.setColor(Color.WHITE);
        brushPaint.setStrokeWidth(1f);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        canvasWidth = width;
        canvasHeight = height;
        shortSide = Math.min(width, height);

        wheelPaint.setShader(new SweepGradient(shortSide / 2, shortSide / 2, COLORS, POSITIONS));

        updateSliderPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        updateSliderPaint();

        canvas.drawRect(0, 0, canvasWidth, canvasHeight, bgPaint);

        canvas.drawRect(canvasWidth * .8f, 30, canvasWidth * .9f, canvasHeight - 30, sliderPaint);

        canvas.rotate(-90, shortSide / 2, shortSide / 2);
        canvas.drawCircle(shortSide / 2, shortSide / 2, shortSide / 2 * .9f, wheelPaint);
        canvas.drawCircle(shortSide / 2, shortSide / 2, shortSide / 2 * .8f, bgPaint);
        canvas.drawCircle(shortSide / 2, shortSide / 2, shortSide / 2 * .3f, brushPaint);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getPointerCount() > 1) {
            return false;
        }

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
        updateColor(color);
        return true;
    }

    private void updateColor(int color) {
        brushPaint.setColor(color);
        updateSliderPaint();
        if (onPaintChangeListener != null) {
            onPaintChangeListener.onPaint(this, brushPaint);
        }
        invalidate();
    }

    private void updateSliderPaint() {
        int fromColor = 0xFFFFFFFF;
        int toColor = brushPaint.getColor() | 0xFF000000;
        sliderPaint.setShader(new LinearGradient(0, 0, 0, canvasHeight, fromColor, toColor, Shader.TileMode.CLAMP));
    }

    private int toHSV(float degrees) {
        hsvFloats[0] = degrees;
        float[] hsv = hsvFloats;
        return Color.HSVToColor(hsv);
    }

    public void setOnPaintChangeListener(OnPaintChangeListener onPaintChangeListener) {
        this.onPaintChangeListener = onPaintChangeListener;
        onPaintChangeListener.onPaint(this, brushPaint);
    }

    public interface OnPaintChangeListener {
        void onPaint(View v, Paint paint);
    }
}
