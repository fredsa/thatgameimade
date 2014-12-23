package sauer.thatgameimade;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;

public class LevelEditorView extends View {

    public static final String PLATFORMER_ART_V_4_PNGS = "platformerArt_v4/png";
    private static final float BLOCK_SIZE = 70;
    @SuppressWarnings("unused")
    private static final String TAG = LevelEditorView.class.getSimpleName();
    private Bitmap backgroundBitmap;
    private Bitmap spriteBitmap;

    private BitmapShader backgroundShader;

    private Paint backgroundPaint;
    private Paint bitmapPaint;
    private Paint touchPaint;

    private float touchX;
    private float touchY;
    private float touchMajor;

    private float canvasWidth;
    private float canvasHeight;

    private Matrix backgroundMatrix = new Matrix();
    private Matrix drawMatrix = new Matrix();

    private
    @DrawableRes
    Bitmap[][] blocks;
    private ArrayList<Bitmap> bitmapList;
    private float scaleX;
    private float scaleY;

    public LevelEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LevelEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        AssetManager assetManager = getResources().getAssets();
        bitmapList = new ArrayList<>();
        try {
            for (String fileName : assetManager.list(PLATFORMER_ART_V_4_PNGS)) {
                if (!fileName.endsWith(".png")) {
                    Log.w(TAG, fileName);
                    continue;
                }
                Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open(PLATFORMER_ART_V_4_PNGS + "/" + fileName));
                if (bitmap.getWidth() > BLOCK_SIZE || bitmap.getHeight() > BLOCK_SIZE) {
                    Log.w(TAG, fileName + " " + bitmap.getWidth() + " x " + bitmap.getHeight());
                    continue;
                }
                bitmapList.add(bitmap);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load my game assets");
        }

        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_shroom);

        backgroundShader = new BitmapShader(backgroundBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        backgroundPaint = new Paint();
        backgroundPaint.setShader(backgroundShader);

        bitmapPaint = new Paint();

        touchPaint = new Paint();
        touchPaint.setColor(Color.argb(75, 255, 255, 255));
    }

    @Override
    protected void onSizeChanged(int weight, int height, int oldWidth, int oldHeight) {
        canvasWidth = weight;
        canvasHeight = height;

//        scaleX = canvasWidth / backgroundBitmap.getWidth();
//        scaleY = canvasHeight / backgroundBitmap.getHeight();
        scaleX = 1.5f;
        scaleY = 1.5f;
        backgroundMatrix.setScale(scaleX, scaleY);
        backgroundShader.setLocalMatrix(backgroundMatrix);

        int cols = (int) (canvasHeight / BLOCK_SIZE);
        int rows = (int) (canvasHeight / BLOCK_SIZE);
        blocks = new Bitmap[cols][rows];
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int index = (i + j * cols) % bitmapList.size();
                blocks[i][j] = bitmapList.get(index);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, canvasWidth, canvasHeight, backgroundPaint);

        if (spriteBitmap != null) {
            paintBitmaps(canvas);
        }
        paintDotWhenTouched(canvas);
    }

    private void paintDotWhenTouched(Canvas canvas) {
        if (touchMajor > 0) {
            canvas.drawCircle(touchX, touchY, touchMajor / 2, touchPaint);
        }
    }

    private void paintBitmaps(Canvas canvas) {
        int cols = blocks.length;
        int rows = blocks[0].length;
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                Bitmap bitmap = blocks[i][j];
                drawMatrix.reset();
//                drawMatrix.postScale(scaleX * BLOCK_SIZE / bitmap.getWidth(), scaleY * BLOCK_SIZE / bitmap.getHeight());
                drawMatrix.postScale(scaleX, scaleY);
                drawMatrix.postTranslate(i * BLOCK_SIZE * scaleX, j * BLOCK_SIZE * scaleY);
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
            int x = (int) (touchX / scaleX / BLOCK_SIZE);
            int y = (int) (touchY / scaleY / BLOCK_SIZE);
            try {
                blocks[x][y] = bitmapList.get((int) (Math.random() * bitmapList.size()));
            } catch (Exception ignore) {
            }
            invalidate();
            return true;
        }
        return false;
    }

    public void setSpriteBitmap(Bitmap spriteBitmap) {
        this.spriteBitmap = spriteBitmap;
    }

}
