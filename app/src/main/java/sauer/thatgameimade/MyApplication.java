package sauer.thatgameimade;

import android.app.Application;
import android.util.Log;

import java.util.Date;

public class MyApplication extends Application {
    @SuppressWarnings("unused")
    private static final String TAG = MyApplication.class.getSimpleName();

    private LevelHolder level1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "=========================== " + new Date());
        level1 = LevelHolder.load(getApplicationContext());
    }

    public LevelHolder getLevelHolder() {
        return level1;
    }
}
