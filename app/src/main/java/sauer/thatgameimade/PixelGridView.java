package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class PixelGridView extends View {

    private final Bitmap spriteBitmap;

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

        spriteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face);
        spritePaint = new Paint();
    }


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
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), bgPaint);

        scale = Math.min(canvas.getWidth() / spriteBitmap.getWidth(),
                canvas.getHeight() / spriteBitmap.getHeight());
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        canvas.drawBitmap(spriteBitmap, matrix, spritePaint);
    }

}
