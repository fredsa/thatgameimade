package sauer.thatgameimade;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class LevelHolder {

    @SuppressWarnings("unused")
    private static final String TAG = LevelHolder.class.getSimpleName();

    private final int blockSize;
    private final AssetManager assetManager;
    private final Context context;
    private ArrayList<BlockInfo> blockList;
    private int[][] levelBlocks;
    private Bitmap backgroundBitmap;
    private int levelBlocksX;
    private int levelBlocksY;

    public LevelHolder(Context context, String assetDirectory, String backgroundFilename) {
        this.context = context;
        Resources resources = context.getResources();
        assetManager = resources.getAssets();
        blockSize = resources.getInteger(R.integer.block_size);

        backgroundBitmap = getBitmap(backgroundFilename);

        levelBlocksX = (int) Math.floor(backgroundBitmap.getWidth() / blockSize);
        levelBlocksY = (int) Math.floor(backgroundBitmap.getHeight() / blockSize);

        blockList = makeBlockList(assetDirectory);

        levelBlocks = makeLevelBlocks();
    }

    private Bitmap getBitmap(String assetFilename) {
        try {
            return BitmapFactory.decodeStream(assetManager.open(assetFilename));
        } catch (IOException ignore) {
            throw new RuntimeException("Unable to load game asset file '" + assetFilename + "'");
        }
    }

    private ArrayList<BlockInfo> makeBlockList(String assetDirectory) {
        ArrayList<BlockInfo> blockList = new ArrayList<>();
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
            blockList.add(new BlockInfo(fileName, bitmap));
        }
        return blockList;
    }

    private String[] getAssets(String assetDirectory) {
        try {
            return assetManager.list(assetDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Unable to access asset directory '" + assetDirectory + "'");
        }
    }

    private int[][] makeLevelBlocks() {
        int[][] blocks = new int[levelBlocksX][levelBlocksY];
        for (int i = 0; i < levelBlocksX; i++) {
            for (int j = 0; j < levelBlocksY; j++) {
                int index = (i + j * levelBlocksX) % blockList.size();
                blocks[i][j] = index;
            }
        }
        return blocks;
    }

    public ArrayList<BlockInfo> getBlockList() {
        return blockList;
    }

    public int[][] getLevelBlocks() {
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
