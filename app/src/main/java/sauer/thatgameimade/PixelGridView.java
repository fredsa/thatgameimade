package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class PixelGridView extends View {

    private static final String TAG = PixelGridView.class.getSimpleName();
    private AdvancedBitmap spriteBitmap;

    private final Paint bgPaint;
    private final Paint spritePaint;
    private float scale;

    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.checker_gray);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 16, bitmap.getHeight() * 16, false);
        BitmapShader bgShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bgPaint = new Paint();
        bgPaint.setShader(bgShader);

        spritePaint = new Paint();
    }

    private MainActivity mainActivity;
    private int canvasWidth;
    private int canvasHeight;
    private Matrix drawMatrix = new Matrix();
    private int drawingColor;
    private int shortSide;


    public PixelGridView(Context context) {
        super(context);
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PixelGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        canvasWidth = width;
        canvasHeight = height;
        if (spriteBitmap == null) {
            return;
        }
        shortSide = Math.min(canvasWidth, canvasHeight);
        scale = Math.min((float) canvasWidth / spriteBitmap.getCurrentBitmap().getWidth(),
                (float) canvasHeight / spriteBitmap.getCurrentBitmap().getHeight());
        drawMatrix.reset();
        drawMatrix.postScale(scale, scale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw background
        canvas.drawRect(0, 0, shortSide, shortSide, bgPaint);

        if (isInEditMode()) {
            return;
        }

        if (spriteBitmap == null) {
            Log.w(TAG, "bitmap not set");
            return;
        }

        Bitmap bitmap = spriteBitmap.getCurrentBitmap();
        canvas.drawBitmap(bitmap, drawMatrix, spritePaint);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getPointerCount() > 1) {
            spriteBitmap.resetBitmap();
        }

        Bitmap bitmap = spriteBitmap.getCurrentBitmap();
        int x = (int) (event.getX() / scale);
        int y = (int) (event.getY() / scale);
        if (x < 0 || y < 0 || x >= bitmap.getWidth() || y >= bitmap.getHeight()) {
            return false;
        }

        bitmap.setPixel(x, y, drawingColor);
        mainActivity.invalidateViews();
        return true;
    }

    public void setAdvancedBitmap(AdvancedBitmap spriteBitmap) {
        this.spriteBitmap = spriteBitmap;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setDrawingColor(int drawingColor) {
        this.drawingColor = drawingColor;
    }
}
