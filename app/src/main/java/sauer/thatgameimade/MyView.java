package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {
    private static final int SPRITE_SIZE = 4;
    private static final String TAG = MyView.class.getSimpleName();

    private final Paint paint;

    {
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    private float touchX;
    private float touchY;
    private float touchR;

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = canvas.getHeight();
        int width = canvas.getWidth();
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
                paint.setColor(Color.rgb((int) (255 * x / SPRITE_SIZE), (int) (255 * y / SPRITE_SIZE), (int) (255 - 255 * x / SPRITE_SIZE * y / SPRITE_SIZE)));
                int left = x * blockSize;
                int top = y * blockSize;
                canvas.drawRect(left, top, left + blockSize - 3, top + blockSize - 3, paint);
            }
        }

        // paint touch dot
        if (touchX > 0 && touchY > 0) {
            paint.setColor(Color.rgb(200, 255, 0));
            canvas.drawCircle(touchX, touchY, touchR, paint);
        }
    }

    public void placeDot(float x, float y, float r) {
        this.touchX = x;
        this.touchY = y;
        this.touchR = r;
        invalidate();
    }
}
