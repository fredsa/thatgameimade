package sauer.thatgameimade;

import android.graphics.Bitmap;

public class BlockInfo {
    private String name;
    private Bitmap bitmap;

    BlockInfo(String name, Bitmap bitmap) {
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
