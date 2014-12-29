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
        level1 = new LevelHolder(getApplicationContext(), "platformerArt_v4/png", "Platformer Art Complete Pack_0/Mushroom expansion/Backgrounds/bg_castle.png");
    }

    public LevelHolder getLevelHolder() {
        return level1;
    }
}
