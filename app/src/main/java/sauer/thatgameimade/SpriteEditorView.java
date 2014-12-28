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


public class SpriteEditorView extends View {

    @SuppressWarnings("unused")
    private static final String TAG = SpriteEditorView.class.getSimpleName();

    private Paint backgroundPaint;
    private Paint spritePaint;
    private BlockInfo blockInfo;
    private float scale;
    private OnBlockInfoChangedListener onBlockInfoChangedListener;
    private int canvasWidth;
    private int canvasHeight;
    private Matrix drawMatrix = new Matrix();
    private Paint brushPaint;
    private int shortSide;
    private Canvas canvas;
    private float lastX;
    private float lastY;

    public SpriteEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = makeBgPaint();
        spritePaint = new Paint();
    }

    private Paint makeBgPaint() {
        Paint bgPaint;
        Bitmap bgBitmap;
        BitmapShader bgShader;
        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my_checker_gray);
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

        if (blockInfo == null) {
            return;
        }
        recalculateScale();
    }

    private void recalculateScale() {
        scale = Math.min((float) canvasWidth / blockInfo.getBitmap().getWidth(),
                (float) canvasHeight / blockInfo.getBitmap().getHeight());
        drawMatrix.reset();
        drawMatrix.postScale(scale, scale);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (blockInfo == null) {
            return;
        }

        canvas.drawRect(0, 0, blockInfo.getBitmap().getWidth() * scale, blockInfo.getBitmap().getHeight() * scale, backgroundPaint);
        canvas.drawBitmap(blockInfo.getBitmap(), drawMatrix, spritePaint);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getPointerCount() > 1) {
            return false;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = event.getX() / scale;
            lastY = event.getY() / scale;
        }
        for (int i = 0; i < event.getHistorySize(); i++) {
            drawTo(event.getHistoricalX(i), event.getHistoricalY(i));
        }
        drawTo(event.getX(), event.getY());

        if (onBlockInfoChangedListener != null) {
            onBlockInfoChangedListener.blockInfoChanged(this, blockInfo);
        }

        return true;
    }

    private void drawTo(float x, float y) {
        float newX = x / scale;
        float newY = y / scale;
        canvas.drawLine(lastX, lastY, newX, newY, brushPaint);
        canvas.drawPoint(newX, newY, brushPaint); // when pixels are scaled up
        lastX = newX;
        lastY = newY;
    }

    public void setBlockInfo(BlockInfo blockInfo) {
        this.blockInfo = blockInfo;
        canvas = new Canvas(blockInfo.getBitmap());
        recalculateScale();
    }

    public void setBrushPaint(Paint brushPaint) {
        this.brushPaint = brushPaint;
    }

    public void setOnBlockInfoChangedListener(OnBlockInfoChangedListener onBlockInfoChangedListener) {
        this.onBlockInfoChangedListener = onBlockInfoChangedListener;
    }

    public interface OnBlockInfoChangedListener {
        void blockInfoChanged(View view, BlockInfo blockInfo);
    }
}
