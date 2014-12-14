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


/**
 * TODO: document your custom view class.
 */
public class PixelGridView extends View {

    private static final String TAG = PixelGridView.class.getSimpleName();
    private AdvancedBitmap spriteBitmap;

    private final Paint bgPaint;
    private final Paint spritePaint;
    private final BitmapShader bgShader;
    private int scale;

    {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.checker_gray);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 16, bitmap.getHeight() * 16, false);
        bgShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        bgPaint = new Paint();
        bgPaint.setShader(bgShader);

        spritePaint = new Paint();
    }

    private MainActivity mainActivity;


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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw background
        canvas.drawRect(canvas.getClipBounds(), bgPaint);

        // fallback image (for use in editor)
        if (spriteBitmap == null) {
            drawRainboxGrid(canvas);
            return;
        }

        Bitmap bitmap = spriteBitmap.getCurrentBitmap();
        scale = Math.min(canvas.getClipBounds().width() / bitmap.getWidth(),
                canvas.getClipBounds().height() / bitmap.getHeight());
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        canvas.drawBitmap(bitmap, matrix, spritePaint);
    }

    private void drawRainboxGrid(Canvas canvas) {
        int SPRITE_SIZE = 4;
        Paint paint = new Paint();
        int height = canvas.getClipBounds().height();
        int width = canvas.getClipBounds().width();
        int shortSideLength = Math.min(width, height);
        int blockSize = shortSideLength/ SPRITE_SIZE;
        // paint sprite grid
        for (int x = 0; x < SPRITE_SIZE; x++) {
            for (int y = 0; y < SPRITE_SIZE; y++) {
                int x1 = x * blockSize;
                int y1 = y * blockSize;
                int x2 = x1 + blockSize - 3;
                int y2 = y1 + blockSize - 3;
                paint.setColor(Color.rgb((int) (255 * x / SPRITE_SIZE), (int) (255 * y / SPRITE_SIZE), (int) (255 - 255 * x / SPRITE_SIZE * y / SPRITE_SIZE)));
                canvas.drawRect(x1, y1, x2, y2, paint);
            }
        }
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

        int color = bitmap.getPixel(x, y);
        color = color ^ 0xffffff;
        color = color | 0xff000000;
        bitmap.setPixel(x, y, color);
        mainActivity.invalidateViews();
        return true;
    }

    public void setAdvancedBitmap(AdvancedBitmap spriteBitmap) {
        this.spriteBitmap = spriteBitmap;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
