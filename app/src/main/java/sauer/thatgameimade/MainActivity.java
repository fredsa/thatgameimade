package sauer.thatgameimade;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Date;

public class MainActivity extends Activity {

    public static final int VISIBILITY_FLAGS = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
    private static final String TAG = MainActivity.class.getSimpleName();
    private MyView myView;
    private PixelGridView pixelGridView;
    private Bitmap spriteBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(TAG, "-------------------------------------------" + new Date());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = (MyView) findViewById(R.id.myView);

        spriteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face).copy(Bitmap.Config.ARGB_8888, true);
        pixelGridView = (PixelGridView) findViewById(R.id.pixelGridView);

        myView.setSpriteBitmap(spriteBitmap);
        pixelGridView.setSpriteBitmap(spriteBitmap);
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
