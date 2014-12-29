package sauer.thatgameimade;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LevelHolder {

    @SuppressWarnings("unused")
    private static final String TAG = LevelHolder.class.getSimpleName();

    private static final String FILENAME = "level1.dat";

    private final int blockSize;
    private final AssetManager assetManager;
    private final Context context;
    private ArrayList<BlockInfo> blockList;
    private int[][] levelBlocks;
    private Bitmap backgroundBitmap;
    private int levelBlocksX;
    private int levelBlocksY;

    private LevelHolder(Context context, String assetDirectory, String backgroundFilename) {
        this.context = context;
        Resources resources = context.getResources();
        assetManager = resources.getAssets();
        blockSize = resources.getInteger(R.integer.block_size);

        backgroundBitmap = getBitmap(backgroundFilename);

        blockList = makeBlockList(assetDirectory);
    }

    public static LevelHolder load(Context context) {
        LevelHolder levelHolder = new LevelHolder(context, "platformerArt_v4/png", "Platformer Art Complete Pack_0/Mushroom expansion/Backgrounds/bg_castle.png");
        try {
            Log.w(TAG, "LOADING LEVEL FROM " + FILENAME);
            FileInputStream fileInputStream = context.openFileInput(FILENAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            int levelBlocksX = objectInputStream.read();
            int levelBlocksY = objectInputStream.read();
            int[][] levelBlocks = (int[][]) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();

            levelHolder.levelBlocksX = levelBlocksX;
            levelHolder.levelBlocksY = levelBlocksY;
            levelHolder.levelBlocks = levelBlocks;
            Log.w(TAG, "LEVEL LOADED FROM " + FILENAME);
            return levelHolder;
        } catch (InvalidClassException e) {
            Log.w("Unable to deserialize file '" + FILENAME + "'", e);
        } catch (IOException e) {
            Log.w("Unable to open file '" + FILENAME + "'", e);
        } catch (ClassNotFoundException e) {
            Log.w("Unable to locate class while opening file '" + FILENAME + "'", e);
        }
        levelHolder.levelBlocksX = (int) Math.floor(levelHolder.backgroundBitmap.getWidth() / levelHolder.blockSize);
        levelHolder.levelBlocksY = (int) Math.floor(levelHolder.backgroundBitmap.getHeight() / levelHolder.blockSize);
        levelHolder.makeLevelBlocks();
        Log.w(TAG, "NEW LEVEL DATA GENERATED");
        return levelHolder;
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

    private void makeLevelBlocks() {
        levelBlocks = new int[levelBlocksX][levelBlocksY];
        for (int i = 0; i < levelBlocksX; i++) {
            for (int j = 0; j < levelBlocksY; j++) {
                int index = (i + j * levelBlocksX) % blockList.size();
                levelBlocks[i][j] = index;
            }
        }
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

    public void save() {
        Log.w(TAG, "SAVING LEVEL TO " + FILENAME);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.write(levelBlocksX);
            objectOutputStream.write(levelBlocksY);
            objectOutputStream.writeObject(levelBlocks);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to open file '" + FILENAME + "'", e);
        }
        Log.w(TAG, "LEVEL SAVED TO " + FILENAME);
    }
}
