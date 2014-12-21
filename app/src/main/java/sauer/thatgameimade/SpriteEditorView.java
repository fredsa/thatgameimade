package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class SpriteEditorView extends View {

    @SuppressWarnings("unused")
    private static final String TAG = SpriteEditorView.class.getSimpleName();
    private final Paint bgPaint;
    private final Paint spritePaint;
    private Bitmap spriteBitmap;
    private float scale;

    {
        bgPaint = makeBgPaint();
        spritePaint = new Paint();
    }

    private OnBitmapChangedListener onBitmapChangedListener;
    private int canvasWidth;
    private int canvasHeight;
    private Matrix drawMatrix = new Matrix();
    private Paint brushPaint;
    private int shortSide;
    private Canvas canvas;

    public SpriteEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Paint makeBgPaint() {
        Paint bgPaint;
        Bitmap bgBitmap;
        BitmapShader bgShader;
        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.checker_gray);
        bgBitmap = Bitmap.createScaledBitmap(bgBitmap, bgBitmap.getWidth() * 16, bgBitmap.getHeight() * 16, false);
        bgShader = new BitmapShader(bgBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bgPaint = new Paint();
        bgPaint.setShader(bgShader);
        return bgPaint;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        canvasWidth = width;
        canvasHeight = height;
        shortSide = Math.min(canvasWidth, canvasHeight);

        if (spriteBitmap == null) {
            return;
        }
        recalculateScale();
    }

    private void recalculateScale() {
        scale = Math.min((float) canvasWidth / spriteBitmap.getWidth(),
                (float) canvasHeight / spriteBitmap.getHeight());
        drawMatrix.reset();
        drawMatrix.postScale(scale, scale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        if (shortSide == 0 || isInEditMode()) {
//            return;
//        }

        // draw background
        canvas.drawRect(0, 0, shortSide, shortSide, bgPaint);

        if (spriteBitmap == null) {
            Log.w(TAG, "bitmap not set");
            return;
        }

        canvas.drawBitmap(spriteBitmap, drawMatrix, spritePaint);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getPointerCount() > 1) {
            return false;
        }

        int x = (int) (event.getX() / scale);
        int y = (int) (event.getY() / scale);
        if (x < 0 || y < 0 || x >= spriteBitmap.getWidth() || y >= spriteBitmap.getHeight()) {
            return false;
        }

        canvas.drawLine(0f, 0f, (float) x, (float) y, brushPaint);

        if (onBitmapChangedListener != null) {
            onBitmapChangedListener.bitMapChanged(this, spriteBitmap);
        }

        return true;
    }

    public void setSpriteBitmap(Bitmap spriteBitmap) {
        this.spriteBitmap = spriteBitmap;
        canvas = new Canvas(spriteBitmap);
        recalculateScale();
    }

    public void setBrushPaint(Paint brushPaint) {
        this.brushPaint = brushPaint;
    }

    public void setOnBitmapChangedListener(OnBitmapChangedListener onBitmapChangedListener) {
        this.onBitmapChangedListener = onBitmapChangedListener;
    }

    public interface OnBitmapChangedListener {
        void bitMapChanged(View view, Bitmap bitmap);
    }
}
