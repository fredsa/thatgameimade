package sauer.thatgameimade;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    public static final int VISIBILITY_FLAGS = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    private static final String TAG = MainActivity.class.getSimpleName();
    private MyView myView;
    private PixelGridView pixelGridView;
    private ColorChooserView colorChooserView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = (MyView) findViewById(R.id.myView);

        Bitmap spriteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face).copy(Bitmap.Config.ARGB_8888, true);
        pixelGridView = (PixelGridView) findViewById(R.id.pixelGridView);

        myView.setSpriteBitmap(spriteBitmap);
        pixelGridView.setSpriteBitmap(spriteBitmap);
        pixelGridView.setOnBitmapChangedListener(new PixelGridView.OnBitmapChangedListener() {
            @Override
            public void bitMapChanged(View view, Bitmap bitmap) {
                pixelGridView.invalidate();
                myView.invalidate();
            }
        });

        colorChooserView = (ColorChooserView) findViewById(R.id.colorChooserView);
        colorChooserView.setOnColorChangeListener(new ColorChooserView.OnColorChangeListener() {
            @Override
            public void onColor(View v, int color) {
                pixelGridView.setDrawingColor(color);
            }
        });
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
