package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Bitmap;
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

    @SuppressWarnings("unused")
    private static final String TAG = LevelEditorView.class.getSimpleName();
    private BitmapShader backgroundShader;

    private Paint backgroundPaint;
    private Paint bitmapPaint;
    private Paint touchPaint;

    private float touchX;
    private float touchY;

    private Matrix backgroundMatrix = new Matrix();
    private Matrix drawMatrix = new Matrix();

    private LevelHolder levelHolder;
    private float scale = 1.4f;

    public LevelEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        backgroundShader = new BitmapShader(levelHolder.getBackgroundBitmap(), Shader.TileMode.REPEAT, Shader.TileMode.MIRROR);

        backgroundPaint = new Paint();
        backgroundPaint.setShader(backgroundShader);

        bitmapPaint = new Paint();

        touchPaint = new Paint();
        touchPaint.setColor(Color.argb(75, 255, 255, 255));
    }

    @Override
    protected void onSizeChanged(int weight, int height, int oldWidth, int oldHeight) {
        backgroundMatrix.setScale(scale, scale);
        backgroundShader.setLocalMatrix(backgroundMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, levelHolder.getBackgroundBitmap().getWidth() * scale, levelHolder.getBackgroundBitmap().getHeight() * scale, backgroundPaint);

        if (levelHolder != null) {
            paintBitmaps(canvas);
        }
    }

    private void paintBitmaps(Canvas canvas) {
        for (int i = 0; i < levelHolder.getLevelBlocksX(); i++) {
            for (int j = 0; j < levelHolder.getLevelBlocksY(); j++) {
                Bitmap bitmap = levelHolder.getLevelBlocks()[i][j];
                if (bitmap == null) {
                    continue;
                }
                drawMatrix.reset();
                drawMatrix.postTranslate(i * levelHolder.getBlockSize(), j * levelHolder.getBlockSize());
                drawMatrix.postScale(scale, scale);
                canvas.drawBitmap(bitmap, drawMatrix, bitmapPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getPointerCount() == 1) {
            this.touchX = event.getX();
            this.touchY = event.getY();
            int x = (int) (touchX / scale / levelHolder.getBlockSize());
            int y = (int) (touchY / scale / levelHolder.getBlockSize());
            try {
                levelHolder.getLevelBlocks()[x][y] = levelHolder.getBitmapList().get((int) (Math.random() * levelHolder.getBitmapList().size()));
            } catch (Exception ignore) {
            }
            invalidate();
            return true;
        }
        return false;
    }

    public void setLevelHolder(LevelHolder levelHolder) {
        this.levelHolder = levelHolder;
        init();
    }
}
