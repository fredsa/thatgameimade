package sauer.thatgameimade;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class LevelHolder {

    private static final String TAG = LevelHolder.class.getSimpleName();
    private final int blockSize;
    private final AssetManager assetManager;
    private ArrayList<Bitmap> bitmapList;
    private Bitmap[][] levelBlocks;
    private Bitmap backgroundBitmap;
    private int levelBlocksX;
    private int levelBlocksY;

    public LevelHolder(Resources resources, String assetDirectory, String backgroundFilename) {
        assetManager = resources.getAssets();
        blockSize = resources.getInteger(R.integer.block_size);

        backgroundBitmap = getBitmap(backgroundFilename);

        levelBlocksX = (int) Math.floor(backgroundBitmap.getWidth() / blockSize);
        levelBlocksY = (int) Math.floor(backgroundBitmap.getHeight() / blockSize);

        bitmapList = makeBitmapAssets(assetDirectory);

        levelBlocks = makeLevelBlocks();
    }

    private Bitmap getBitmap(String assetFilename) {
        try {
            return BitmapFactory.decodeStream(assetManager.open(assetFilename));
        } catch (IOException ignore) {
            throw new RuntimeException("Unable to load game asset file '" + assetFilename + "'");
        }
    }

    private ArrayList<Bitmap> makeBitmapAssets(String assetDirectory) {
        ArrayList<Bitmap> bitmapList = new ArrayList<>();
        String[] assets = getAssets(assetDirectory);
        for (String fileName : assets) {
            if (!fileName.endsWith(".png")) {
                Log.w(TAG, fileName);
                continue;
            }
            Bitmap bitmap = getBitmap(assetDirectory + "/" + fileName);
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            if (bitmap.getWidth() > blockSize || bitmap.getHeight() > blockSize) {
                Log.w(TAG, fileName + " " + bitmap.getWidth() + " x " + bitmap.getHeight());
                continue;
            }
            bitmapList.add(bitmap);
        }
        return bitmapList;
    }

    private String[] getAssets(String assetDirectory) {
        try {
            return assetManager.list(assetDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Unable to access asset directory '" + assetDirectory + "'");
        }
    }

    private Bitmap[][] makeLevelBlocks() {
        Bitmap[][] blocks = new Bitmap[levelBlocksX][levelBlocksY];
        for (int i = 0; i < levelBlocksX; i++) {
            for (int j = 0; j < levelBlocksY; j++) {
                int index = (i + j * levelBlocksX) % bitmapList.size();
                blocks[i][j] = bitmapList.get(index);
            }
        }
        return blocks;
    }

    public ArrayList<Bitmap> getBitmapList() {
        return bitmapList;
    }

    public Bitmap[][] getLevelBlocks() {
        return levelBlocks;
    }

    public Bitmap getBackgroundBitmap() {
        return backgroundBitmap;
    }

    public int getLevelBlocksX() {
        return levelBlocksX;
    }

    public int getLevelBlocksY() {
        return levelBlocksY;
    }

    public int getBlockSize() {
        return blockSize;
    }
}
