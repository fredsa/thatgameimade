package sauer.thatgameimade;

import android.graphics.Bitmap;

public class BlockInfo {
    private final String basename;
    private final String path;
    private final Bitmap bitmap;

    BlockInfo(String path, Bitmap bitmap) {
        this.path = path;
        this.basename = getBasename(path);
        this.bitmap = bitmap;
    }

    private static String getBasename(String path) {
        path = path.substring(path.lastIndexOf('/') + 1);
        int pos = path.lastIndexOf('.');
        if (pos != -1) {
            path = path.substring(0, pos);
        }
        return path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return basename;
    }
}
