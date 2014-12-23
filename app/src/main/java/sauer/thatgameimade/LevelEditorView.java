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

    public static final float SCALE = 1f;
    public static final String PLATFORMER_ART_V_4_PNGS = "platformerArt_v4/png";
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

    private int canvasWidth;
    private int canvasHeight;

    private Matrix backgroundMatrix = new Matrix();
    private Matrix drawMatrix = new Matrix();

    private
    @DrawableRes
    Bitmap[][] blocks;
    private ArrayList<Bitmap> bitmaps;

    public LevelEditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LevelEditorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        AssetManager assetManager = getResources().getAssets();
        bitmaps = new ArrayList<Bitmap>();
        try {
            for (String fileName : assetManager.list(PLATFORMER_ART_V_4_PNGS)) {
                if (!fileName.endsWith(".png")) {
                    Log.w(TAG, fileName);
                    continue;
                }
                Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open(PLATFORMER_ART_V_4_PNGS + "/" + fileName));
                bitmaps.add(bitmap);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load my game assets");
        }
        int cols = Math.max(3, (int) Math.sqrt(bitmaps.size()));
        int rows = Math.max(3, bitmaps.size() / cols);
        blocks = new Bitmap[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int index = (i * cols + j) % bitmaps.size();
                blocks[i][j] = bitmaps.get(index);
            }
        }

        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_desert);

        backgroundShader = new BitmapShader(backgroundBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

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

        backgroundMatrix.setScale(canvasWidth / backgroundBitmap.getWidth(), canvasHeight / backgroundBitmap.getHeight());
        backgroundShader.setLocalMatrix(backgroundMatrix);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, canvasWidth, canvasHeight, backgroundPaint);

        if (spriteBitmap != null) {
            paintBitmaps(canvas);
            paintSpriteWhenTouched(canvas, spriteBitmap);
        }
        paintDotWhenTouched(canvas);
    }

    private void paintSpriteWhenTouched(Canvas canvas, Bitmap bitmap) {
        if (touchMajor > 0) {
            drawMatrix.reset();
            drawMatrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
            float scale = 200f / bitmap.getWidth();
            drawMatrix.postScale(scale, scale);
            drawMatrix.postTranslate(touchX, touchY);
            canvas.drawBitmap(bitmap, drawMatrix, bitmapPaint);
        }
    }

    private void paintDotWhenTouched(Canvas canvas) {
        if (touchMajor > 0) {
            canvas.drawCircle(touchX, touchY, touchMajor / 2, touchPaint);
        }
    }

    private void paintBitmaps(Canvas canvas) {
        // determine draw matrix based on first block
        float ratio = SCALE * blocks[0][0].getWidth() / blocks[0][0].getWidth();
        drawMatrix.reset();
        drawMatrix.setScale(SCALE, SCALE);

        bitmapPaint.setColor(Color.rgb(255, 255, 100));
        int rows = blocks.length;
        int cols = blocks[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                canvas.drawBitmap(blocks[i][j], drawMatrix, bitmapPaint);
                drawMatrix.setTranslate(i * SCALE * blocks[i][j].getWidth(), j * SCALE * blocks[i][j].getHeight());
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getPointerCount() == 1) {
            this.touchX = event.getX();
            this.touchY = event.getY();
            this.touchMajor = event.getTouchMajor();
            invalidate();
            return true;
        }
        return false;
    }

    public void setSpriteBitmap(Bitmap spriteBitmap) {
        this.spriteBitmap = spriteBitmap;
    }

}
