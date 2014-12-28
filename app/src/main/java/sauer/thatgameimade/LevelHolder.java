package sauer.thatgameimade;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class LevelHolder {

    private static final String TAG = LevelHolder.class.getSimpleName();
    private final int blockSize;
    private ArrayList<Bitmap> bitmapList;
    private Bitmap[][] levelBlocks;
    private Bitmap backgroundBitmap;
    private int levelBlocksX;
    private int levelBlocksY;

    public LevelHolder(Resources resources, String assetDirectory, @DrawableRes int bg_drawable) {
        blockSize = resources.getInteger(R.integer.block_size);

        backgroundBitmap = BitmapFactory.decodeResource(resources, bg_drawable);
        levelBlocksX = (int) Math.floor(backgroundBitmap.getWidth() / blockSize);
        levelBlocksY = (int) Math.floor(backgroundBitmap.getHeight() / blockSize);

        bitmapList = makeBitmapAssets(resources, assetDirectory);

        levelBlocks = makeLevelBlocks();
    }

    private ArrayList<Bitmap> makeBitmapAssets(Resources resources, String assetDirectory) {
        AssetManager assetManager = resources.getAssets();
        ArrayList<Bitmap> bitmapList = new ArrayList<>();
        try {
            for (String fileName : assetManager.list(assetDirectory)) {
                if (!fileName.endsWith(".png")) {
                    Log.w(TAG, fileName);
                    continue;
                }
                Bitmap bitmap = BitmapFactory.decodeStream(assetManager.open(assetDirectory + "/" + fileName));
                if (bitmap.getWidth() > blockSize || bitmap.getHeight() > blockSize) {
                    Log.w(TAG, fileName + " " + bitmap.getWidth() + " x " + bitmap.getHeight());
                    continue;
                }
                bitmapList.add(bitmap);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to load my game assets");
        }
        return bitmapList;
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
