package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LevelEditorView extends View {

    @SuppressWarnings("unused")
    private static final String TAG = LevelEditorView.class.getSimpleName();

    public static final float SCALE = 1f;

    private final Paint bgPaint;
    private final Paint touchPaint;
    private final Paint bitmapPaint;

    private float touchX;
    private float touchY;
    private float touchMajor;

    private Bitmap cakeBitmap;
    private Bitmap spriteBitmap;

    {
        bgPaint = new Paint();
        bgPaint.setColor(Color.rgb(200, 255, 200));

        bitmapPaint = new Paint();

        touchPaint = new Paint();
        touchPaint.setColor(Color.argb(200, 200, 200, 200));

        cakeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cake_half_alt);
    }

    private int canvasWidth;
    private int canvasHeight;
    private float halfway;
    private Matrix drawMatrix = new Matrix();


    public LevelEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LevelEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int weight, int height, int oldWidth, int oldHeight) {
        canvasWidth = weight;
        canvasHeight = height;
        halfway = Math.min(canvasWidth, canvasHeight) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintBackground(canvas);

        if (isInEditMode()) {
            return;
        }

        paintBitmaps(canvas, spriteBitmap);
        paintSpriteWhenTouched(canvas, spriteBitmap);
        paintDotWhenTouched(canvas);
    }

    private void paintBackground(Canvas canvas) {
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, bgPaint);
    }

    private void paintSpriteWhenTouched(Canvas canvas, Bitmap bitmap) {
        if (touchMajor > 0) {
            drawMatrix.reset();
            drawMatrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
            drawMatrix.postScale(50, 50);
            drawMatrix.postTranslate(touchX, touchY);
            canvas.drawBitmap(bitmap, drawMatrix, bitmapPaint);
        }
    }

    private void paintDotWhenTouched(Canvas canvas) {
        if (touchMajor > 0) {
            canvas.drawCircle(touchX, touchY, touchMajor / 2, touchPaint);
        }
    }

    private void paintBitmaps(Canvas canvas, Bitmap bitmap) {
        bitmapPaint.setColor(Color.rgb(255, 255, 100));
        float ratio = SCALE * cakeBitmap.getWidth() / bitmap.getWidth();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                drawMatrix.reset();
                drawMatrix.postScale(SCALE, SCALE);
                drawMatrix.postTranslate(halfway + i * SCALE * cakeBitmap.getWidth(), j * SCALE * cakeBitmap.getHeight());
                canvas.drawBitmap(cakeBitmap, drawMatrix, bitmapPaint);

                drawMatrix.reset();
                drawMatrix.postScale(ratio, ratio);
                drawMatrix.postTranslate(halfway + i * ratio * bitmap.getWidth(), halfway + j * ratio * bitmap.getHeight());
                canvas.drawBitmap(bitmap, drawMatrix, bitmapPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getPointerCount() == 1) {
            float x = event.getX();
            float y = event.getY();
            float touchMajor = event.getTouchMajor();
            placeDot(x, y, touchMajor);
            return true;
        }
        return false;
    }

    public void placeDot(float x, float y, float touchMajor) {
        this.touchX = x;
        this.touchY = y;
        this.touchMajor = touchMajor;
        invalidate();
    }

    public void setSpriteBitmap(Bitmap spriteBitmap) {
        this.spriteBitmap = spriteBitmap;
    }

}
