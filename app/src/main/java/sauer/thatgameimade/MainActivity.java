package sauer.thatgameimade;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Date;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private MyView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(TAG, "-------------------------------------------" + new Date());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = (MyView) findViewById(R.id.myView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemControls();
    }

    private void hideSystemControls() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float r = event.getSize();
//        Log.w(TAG, "onTouchEvent x=" + x + " y=" + y + " r=" + r + " evt:" + event.getHistorySize());
        myView.placeDot(x, y, r * 1000);
        return true;
//        return super.onTouchEvent(event);
    }

}
