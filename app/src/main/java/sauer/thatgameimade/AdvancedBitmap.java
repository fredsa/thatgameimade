package sauer.thatgameimade;

import android.graphics.Bitmap;

public class AdvancedBitmap {
    private final Bitmap originalBitmap;
    private Bitmap currentBitmap;

    public AdvancedBitmap(Bitmap bitmap) {
        originalBitmap = bitmap;
        currentBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    public void resetBitmap() {
        currentBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
    }
}
