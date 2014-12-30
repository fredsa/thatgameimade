package sauer.thatgameimade;

import android.app.Application;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

public class MyApplication extends Application {
    @SuppressWarnings("unused")
    private static final String TAG = MyApplication.class.getSimpleName();

    private LevelHolder level1;
    private LevelHolder level2;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "=========================== " + new Date());
        try {
            level1 = LevelHolder.load(getApplicationContext(), 1);
            level2 = LevelHolder.load(getApplicationContext(), 2);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load level data", e);
        }
    }

    public LevelHolder getLevelHolder() {
        return level2;
    }
}
