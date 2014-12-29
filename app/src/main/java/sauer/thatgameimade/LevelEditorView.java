package sauer.thatgameimade;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
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

    private Matrix backgroundMatrix = new Matrix();
    private Matrix drawMatrix = new Matrix();

    private LevelHolder levelHolder;
    private float scale;
    private int blockIndex;

    public LevelEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        backgroundShader = new BitmapShader(levelHolder.getBackgroundBitmap(), Shader.TileMode.REPEAT, Shader.TileMode.MIRROR);

        backgroundPaint = new Paint();
        backgroundPaint.setShader(backgroundShader);

        bitmapPaint = new Paint();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        if (isInEditMode()) {
            return;
        }
        scale = Math.min((float) width / levelHolder.getBackgroundBitmap().getWidth(), (float) height / levelHolder.getBackgroundBitmap().getHeight());
        backgroundMatrix.setScale(scale, scale);
        backgroundShader.setLocalMatrix(backgroundMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            return;
        }

        canvas.drawRect(0, 0, levelHolder.getBackgroundBitmap().getWidth() * scale, levelHolder.getBackgroundBitmap().getHeight() * scale, backgroundPaint);

        // TODO avoid conditional
        if (levelHolder != null) {
            paintBitmaps(canvas);
        }
    }

    private void paintBitmaps(Canvas canvas) {
        for (int i = 0; i < levelHolder.getLevelBlocksX(); i++) {
            for (int j = 0; j < levelHolder.getLevelBlocksY(); j++) {
                int blockIndex = levelHolder.getLevelBlocks()[i][j];
                drawMatrix.reset();
                drawMatrix.postTranslate(i * levelHolder.getBlockSize(), j * levelHolder.getBlockSize());
                drawMatrix.postScale(scale, scale);
                canvas.drawBitmap(levelHolder.getBlockList().get(blockIndex).getBitmap(), drawMatrix, bitmapPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getPointerCount() == 1) {
            int x = (int) (event.getX() / scale / levelHolder.getBlockSize());
            int y = (int) (event.getY() / scale / levelHolder.getBlockSize());
            try {
                levelHolder.getLevelBlocks()[x][y] = blockIndex;
            } catch (IndexOutOfBoundsException ignore) {
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

    public void setBlockIndex(int blockIndex) {
        this.blockIndex = blockIndex;
    }
}
