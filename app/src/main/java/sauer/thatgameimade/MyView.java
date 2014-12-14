package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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
    private float touchR;

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

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (spriteBitmap == null) {
            return;
        }

        Bitmap bitmap = spriteBitmap.getCurrentBitmap();

        int height = canvas.getClipBounds().height();
        int width = canvas.getClipBounds().width();
        int shortSideLength = Math.min(width, height);
        int halfway = shortSideLength / 2;
        int blockSize = halfway / SPRITE_SIZE;

        // paint background
        paint.setColor(Color.rgb(220, 220, 255));
        canvas.drawRect(0, 0, width, height, paint);

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
                    paint.setColor(Color.rgb((int) (255 * x / SPRITE_SIZE), (int) (255 * y / SPRITE_SIZE), (int) (255 - 255 * x / SPRITE_SIZE * y / SPRITE_SIZE)));
                }
                canvas.drawRect(x1, y1, x2, y2, paint);
            }
        }

        // paint bitmaps
        paint.setColor(Color.rgb(255, 255, 100));
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 3; j++) {
                Matrix matrix = new Matrix();
                matrix.postScale(SCALE, SCALE);
                matrix.postTranslate(halfway + i * SCALE * cakeBitmap.getWidth(), j * SCALE * cakeBitmap.getHeight());
                canvas.drawBitmap(cakeBitmap, matrix, paint);

                matrix.preScale(20, 20);
                matrix.postTranslate(0, 3 * SCALE * cakeBitmap.getHeight());
                canvas.drawBitmap(bitmap, matrix, paint);
            }
        }

        // paint test face
        {
            Matrix matrix = new Matrix();
            matrix.postScale(5, 5);
            matrix.postTranslate(halfway, halfway);
            canvas.drawBitmap(bitmap, matrix, paint);
        }

        // paint test foursquare
        {
            Matrix matrix = new Matrix();
            matrix.postScale(5, 5);
            matrix.postTranslate(0, halfway);
            canvas.drawBitmap(foursquareBitmap, matrix, paint);
        }

        // paint sprite when touched
        if (touchX >= SPRITE_SIZE * blockSize || touchY >= SPRITE_SIZE * blockSize) {
            Matrix matrix = new Matrix();
            matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
            matrix.postScale(50, 50);
            matrix.postTranslate(touchX, touchY);
            canvas.drawBitmap(bitmap, matrix, paint);
        }

        // paint dot when touched
        if (touchR > 0) {
            paint.setColor(Color.rgb(200, 255, 0));
            canvas.drawCircle(touchX, touchY, touchR, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float r = event.getSize();
//        Log.w(TAG, "onTouchEvent x=" + x + " y=" + y + " r=" + r + " evt:" + event.getHistorySize());
        placeDot(x, y, r * 1000);
        return true;
//        return super.onTouchEvent(event);
    }

    public void placeDot(float x, float y, float r) {
        this.touchX = x;
        this.touchY = y;
        this.touchR = r;
        invalidate();
    }

    public void setAdvancedBitmap(AdvancedBitmap spriteBitmap) {
        this.spriteBitmap = spriteBitmap;
    }

}
