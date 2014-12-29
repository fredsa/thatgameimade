package sauer.thatgameimade;

import android.app.Activity;
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
    private MyApplication myApplication;
    private SpriteSelectorView spriteSelectorView;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getPointerCount() != 2) {
            return false;
        }

        loadBlockInfo();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myApplication = (MyApplication) getApplication();

        levelEditorView = (LevelEditorView) findViewById(R.id.myView);

        spriteEditorView = (SpriteEditorView) findViewById(R.id.pixelGridView);
        spriteEditorView.setOnBlockInfoChangedListener(new SpriteEditorView.OnBlockInfoChangedListener() {
            @Override
            public void blockInfoChanged(View view, int blockInfo) {
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

        spriteSelectorView = (SpriteSelectorView) findViewById(R.id.spriteSelectorView);
        spriteSelectorView.setOnSpriteSelectedListener(new SpriteSelectorView.OnSpriteSelectedListener() {
            @Override
            public void spriteSelected(int blockIndex) {
                setBlockIndex(blockIndex);
            }
        });
    }

    private void invalidateBitmapViews() {
        spriteEditorView.invalidate();
        levelEditorView.invalidate();
    }

    private void loadBlockInfo() {
        setBlockIndex(0);
    }

    private void setBlockIndex(int blockIndex) {
        spriteEditorView.setBlockIndex(blockIndex);
        levelEditorView.setBlockIndex(blockIndex);
        invalidateBitmapViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemControls();
    }

    @Override
    protected void onStop() {
        super.onStop();
        levelHolder.save();
    }

    @Override
    protected void onStart() {
        super.onStart();
        levelHolder = myApplication.getLevelHolder();
        levelEditorView.setLevelHolder(levelHolder);
        spriteEditorView.setLevelHolder(levelHolder);
        spriteSelectorView.setLevelHolder(levelHolder);

        loadBlockInfo();
    }

    private void hideSystemControls() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(VISIBILITY_FLAGS);
    }

}
