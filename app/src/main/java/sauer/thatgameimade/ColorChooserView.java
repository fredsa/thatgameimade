package sauer.thatgameimade;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class ColorChooserView extends View {

    private int canvasWidth;
    private int canvasHeight;
    private int shortSide;
    private Paint colorPaint;
    private Paint bgPaint;
    private String TAG = ColorChooserView.class.getSimpleName();

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

        int[] colors = new int[]{Color.RED, Color.MAGENTA,Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED};
        colorPaint.setShader(new SweepGradient(shortSide / 2, shortSide / 2, colors, null));
    }

    {
        bgPaint = new Paint();
        bgPaint.setColor(Color.BLACK);

        colorPaint = new Paint();
//        colorPaint.setColor(RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, canvasWidth, canvasHeight, bgPaint);

        canvas.drawCircle(shortSide / 2, shortSide / 2, shortSide / 2 * .8f, colorPaint);
        canvas.drawCircle(shortSide / 2, shortSide / 2, shortSide / 2 * .6f, bgPaint);
    }
}
