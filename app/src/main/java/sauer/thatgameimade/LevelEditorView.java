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
import android.view.MotionEvent;
import android.view.View;

public class LevelEditorView extends View {

    public static final float SCALE = 1f;
    @SuppressWarnings("unused")
    private static final String TAG = LevelEditorView.class.getSimpleName();
    private Bitmap backgroundBitmap;
    private Bitmap iceBlockBitmap;
    private Bitmap spriteBitmap;

    private BitmapShader backgroundShader;

    private Paint backgroundPaint;
    private Paint bitmapPaint;
    private Paint touchPaint;

    private float touchX;
    private float touchY;
    private float touchMajor;

    private int canvasWidth;
    private int canvasHeight;

    private Matrix backgroundMatrix = new Matrix();
    private Matrix drawMatrix = new Matrix();

    public LevelEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LevelEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_desert);

        backgroundShader = new BitmapShader(backgroundBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        backgroundPaint = new Paint();
        backgroundPaint.setShader(backgroundShader);

        iceBlockBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icepack_ice_block);

        bitmapPaint = new Paint();

        touchPaint = new Paint();
        touchPaint.setColor(Color.argb(200, 200, 200, 200));
    }

    @Override
    protected void onSizeChanged(int weight, int height, int oldWidth, int oldHeight) {
        canvasWidth = weight;
        canvasHeight = height;

        backgroundMatrix.setScale(canvasWidth / backgroundBitmap.getWidth(), canvasHeight / backgroundBitmap.getHeight());
        backgroundShader.setLocalMatrix(backgroundMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, canvasWidth, canvasHeight, backgroundPaint);

        if (spriteBitmap != null) {
            paintBitmaps(canvas, spriteBitmap);
            paintSpriteWhenTouched(canvas, spriteBitmap);
        }
        paintDotWhenTouched(canvas);
    }

    private void paintSpriteWhenTouched(Canvas canvas, Bitmap bitmap) {
        if (touchMajor > 0) {
            drawMatrix.reset();
            drawMatrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
            float scale = 200f / bitmap.getWidth();
            drawMatrix.postScale(scale, scale);
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
        float ratio = SCALE * iceBlockBitmap.getWidth() / bitmap.getWidth();
        int rows = 2;
        int cols = 5;
        float y2 = rows * SCALE * iceBlockBitmap.getHeight();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                drawMatrix.reset();
                drawMatrix.postScale(SCALE, SCALE);
                drawMatrix.postTranslate(i * SCALE * iceBlockBitmap.getWidth(), j * SCALE * iceBlockBitmap.getHeight());
                canvas.drawBitmap(iceBlockBitmap, drawMatrix, bitmapPaint);

                drawMatrix.reset();
                drawMatrix.postScale(ratio, ratio);
                drawMatrix.postTranslate(i * ratio * bitmap.getWidth(), y2 + j * ratio * bitmap.getHeight());
                canvas.drawBitmap(bitmap, drawMatrix, bitmapPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getPointerCount() == 1) {
            this.touchX = event.getX();
            this.touchY = event.getY();
            this.touchMajor = event.getTouchMajor();
            invalidate();
            return true;
        }
        return false;
    }

    public void setSpriteBitmap(Bitmap spriteBitmap) {
        this.spriteBitmap = spriteBitmap;
    }

}
