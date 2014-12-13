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
        Bitmap bitmap = spriteBitmap.getCurrentBitmap();
        super.onDraw(canvas);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), bgPaint);

        if (spriteBitmap == null) {
            return;
        }
        scale = Math.min(canvas.getWidth() / bitmap.getWidth(),
                canvas.getHeight() / bitmap.getHeight());
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        canvas.drawBitmap(bitmap, matrix, spritePaint);
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
