package sauer.thatgameimade;

import android.graphics.Bitmap;

public class SpriteInfo {
    private String name;
    private Bitmap bitmap;

    SpriteInfo(String name, Bitmap bitmap) {
        this.name = name;
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getName() {
        return name;
    }
}
