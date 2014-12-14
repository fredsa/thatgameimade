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

public class MyView extends View {
    private static final int SPRITE_SIZE = 8;
    private static final String TAG = MyView.class.getSimpleName();
    public static final float SCALE = 1f;

    private final Paint paint;

    private float touchX;
    private float touchY;
    private float touchMajor;

    private Bitmap cakeBitmap;
    private AdvancedBitmap spriteBitmap;
    private Bitmap foursquareBitmap;

    {
        paint = new Paint();
        paint.setColor(Color.RED);

        cakeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cake_half_alt);

        foursquareBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.foursquare);
        foursquareBitmap = Bitmap.createScaledBitmap(foursquareBitmap, foursquareBitmap.getWidth() * 50, foursquareBitmap.getHeight() * 50, true);
    }

    private int canvasWidth;
    private int canvasHeight;
    private int halfway;
    private int blockSize;
    private Matrix drawMatrix = new Matrix();


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int weight, int height, int oldWidth, int oldHeight) {
        canvasWidth = weight;
        canvasHeight = height;
        halfway = Math.min(canvasWidth, canvasHeight) / 2;
        blockSize = halfway / SPRITE_SIZE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // paint background
        paint.setColor(Color.rgb(220, 220, 255));
        canvas.drawRect(0, 0, canvasWidth, canvasHeight, paint);

        // paint pretty pattern
        paint.setColor(Color.rgb(255, 0, 0));
        for (int i = 0; i <= halfway; i += halfway / 50) {
            canvas.drawLine(i, halfway, halfway, halfway - i, paint);
            canvas.drawLine(i, halfway, halfway, halfway + i, paint);
            canvas.drawLine(halfway * 2 - i, halfway, halfway, halfway - i, paint);
            canvas.drawLine(halfway * 2 - i, halfway, halfway, halfway + i, paint);
        }

        // paint sprite grid
        for (int x = 0; x < SPRITE_SIZE; x++) {
            for (int y = 0; y < SPRITE_SIZE; y++) {
                int x1 = x * blockSize;
                int y1 = y * blockSize;
                int x2 = x1 + blockSize - 3;
                int y2 = y1 + blockSize - 3;
                if (touchX >= x1 && touchX <= x2 && touchY >= y1 && touchY <= y2) {
                    paint.setColor(Color.rgb(220, 255, 220));
                } else {
                    paint.setColor(Color.rgb(255 * x / SPRITE_SIZE, 255 * y / SPRITE_SIZE, 255 - 255 * x / SPRITE_SIZE * y / SPRITE_SIZE));
                }
                canvas.drawRect(x1, y1, x2, y2, paint);
            }
        }

        // paint test foursquare
        {
            drawMatrix.reset();
            float ratio = halfway / foursquareBitmap.getWidth();
            drawMatrix.postScale(ratio, ratio);
            drawMatrix.postTranslate(0, halfway);
            canvas.drawBitmap(foursquareBitmap, drawMatrix, paint);
        }

        if (spriteBitmap == null) {
            return;
        }

        Bitmap bitmap = spriteBitmap.getCurrentBitmap();

        // paint bitmaps
        paint.setColor(Color.rgb(255, 255, 100));
        float ratio = SCALE * cakeBitmap.getWidth() / bitmap.getWidth();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                drawMatrix.reset();
                drawMatrix.postScale(SCALE, SCALE);
                drawMatrix.postTranslate(halfway + i * SCALE * cakeBitmap.getWidth(), j * SCALE * cakeBitmap.getHeight());
                canvas.drawBitmap(cakeBitmap, drawMatrix, paint);

                drawMatrix.reset();
                drawMatrix.postScale(ratio, ratio);
                drawMatrix.postTranslate(halfway + i * ratio * bitmap.getWidth(), halfway + j * ratio * bitmap.getHeight());
                canvas.drawBitmap(bitmap, drawMatrix, paint);
            }
        }

        // paint sprite when touched
        if (touchX >= SPRITE_SIZE * blockSize || touchY >= SPRITE_SIZE * blockSize) {
            drawMatrix.reset();
            drawMatrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
            drawMatrix.postScale(50, 50);
            drawMatrix.postTranslate(touchX, touchY);
            canvas.drawBitmap(bitmap, drawMatrix, paint);
        }

        // paint dot when touched
        if (touchMajor > 0) {
            paint.setColor(Color.rgb(200, 255, 0));
            canvas.drawCircle(touchX, touchY, touchMajor / 2, paint);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float touchMajor = event.getTouchMajor();
        placeDot(x, y, touchMajor);
        return true;
    }

    public void placeDot(float x, float y, float touchMajor) {
        this.touchX = x;
        this.touchY = y;
        this.touchMajor = touchMajor;
        invalidate();
    }

    public void setAdvancedBitmap(AdvancedBitmap spriteBitmap) {
        this.spriteBitmap = spriteBitmap;
    }

}
