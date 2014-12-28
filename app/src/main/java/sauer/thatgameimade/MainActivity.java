package sauer.thatgameimade;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity {

    public static final int VISIBILITY_FLAGS = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    @SuppressWarnings("unused")
    private static final String TAG = MainActivity.class.getSimpleName();
    private LevelEditorView levelEditorView;
    private SpriteEditorView spriteEditorView;
    private ColorPickerView colorPickerView;
    private LevelHolder levelHolder;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() != 2) {
            return false;
        }

        loadBitmap();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication myApplication = (MyApplication) getApplication();
        levelHolder = myApplication.getLevelHolder();

        levelEditorView = (LevelEditorView) findViewById(R.id.myView);
        levelEditorView.setLevelHolder(levelHolder);

        spriteEditorView = (SpriteEditorView) findViewById(R.id.pixelGridView);
        spriteEditorView.setOnBitmapChangedListener(new SpriteEditorView.OnBitmapChangedListener() {
            @Override
            public void bitMapChanged(View view, Bitmap bitmap) {
                invalidateBitmapViews();
            }
        });

        colorPickerView = (ColorPickerView) findViewById(R.id.colorChooserView);
        colorPickerView.setOnPaintChangeListener(new ColorPickerView.OnPaintChangeListener() {
            @Override
            public void onPaint(View v, Paint paint) {
                spriteEditorView.setBrushPaint(paint);
            }
        });

        SpriteSelectorView spriteSelectorView = (SpriteSelectorView) findViewById(R.id.spriteSelectorView);
        spriteSelectorView.setLevelHolder(levelHolder);
        spriteSelectorView.setOnSpriteSelectedListener(new SpriteSelectorView.OnSpriteSelectedListener() {
            @Override
            public void spriteSelected(Bitmap bitmap) {
                setBitmap(bitmap);
            }
        });

        loadBitmap();
    }

    private void invalidateBitmapViews() {
        spriteEditorView.invalidate();
        levelEditorView.invalidate();
    }

    private void loadBitmap() {
        Bitmap spriteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.my_face).copy(Bitmap.Config.ARGB_8888, true);
        setBitmap(spriteBitmap);
    }

    private void setBitmap(Bitmap spriteBitmap) {
        spriteEditorView.setSpriteBitmap(spriteBitmap);
        invalidateBitmapViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemControls();
    }

    private void hideSystemControls() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(VISIBILITY_FLAGS);
    }

}
