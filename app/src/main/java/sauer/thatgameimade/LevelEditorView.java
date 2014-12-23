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

    @DrawableRes
    private Bitmap[][] blocks;
    private ArrayList<Bitmap> bitmapList;
    private float scale = 1.3f;
    private int levelBlocksX;
    private int levelBlocksY;

    public LevelEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LevelEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_shroom);
        backgroundShader = new BitmapShader(backgroundBitmap, Shader.TileMode.REPEAT, Shader.TileMode.MIRROR);

        levelBlocksX = (int) Math.floor(backgroundBitmap.getWidth() / BLOCK_SIZE);
        levelBlocksY = (int) Math.floor(backgroundBitmap.getHeight() / BLOCK_SIZE);

        backgroundPaint = new Paint();
        backgroundPaint.setShader(backgroundShader);

        bitmapPaint = new Paint();

        touchPaint = new Paint();
        touchPaint.setColor(Color.argb(75, 255, 255, 255));

        if (isInEditMode()) {
            return;
        }
        initBitmapAssets();
        initLevelBlocks();
    }

    private void initLevelBlocks() {
        blocks = new Bitmap[levelBlocksX][levelBlocksY];
        for (int i = 0; i < levelBlocksX; i++) {
            for (int j = 0; j < levelBlocksY; j++) {
                int index = (i + j * levelBlocksX) % bitmapList.size();
                blocks[i][j] = bitmapList.get(index);
            }
        }
    }

    private void initBitmapAssets() {
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
    }

    @Override
    protected void onSizeChanged(int weight, int height, int oldWidth, int oldHeight) {
        canvasWidth = weight;
        canvasHeight = height;

        backgroundMatrix.setScale(scale, scale);
        backgroundShader.setLocalMatrix(backgroundMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, backgroundBitmap.getWidth() * scale, backgroundBitmap.getHeight() * scale, backgroundPaint);

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
        for (int i = 0; i < levelBlocksX; i++) {
            for (int j = 0; j < levelBlocksY; j++) {
                Bitmap bitmap = blocks[i][j];
                if (bitmap == null) {
                    continue;
                }
                drawMatrix.reset();
                drawMatrix.postTranslate(i * BLOCK_SIZE, j * BLOCK_SIZE);
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
            this.touchMajor = event.getTouchMajor();
            int x = (int) (touchX / scale / BLOCK_SIZE);
            int y = (int) (touchY / scale / BLOCK_SIZE);
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
