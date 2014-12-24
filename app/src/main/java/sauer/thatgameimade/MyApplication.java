package sauer.thatgameimade;

import android.app.Application;
import android.util.Log;

import java.util.Date;

public class MyApplication extends Application{
    @SuppressWarnings("unused")
    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "=========================== " + new Date());
    }

}
