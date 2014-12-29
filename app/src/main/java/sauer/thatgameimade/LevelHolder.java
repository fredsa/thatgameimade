package sauer.thatgameimade;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class LevelHolder {

    @SuppressWarnings("unused")
    private static final String TAG = LevelHolder.class.getSimpleName();

    private static final String FILENAME = "level1.dat";

    private final int blockSize;
    private final Context context;
    private String assetDirectory;
    private String backgroundFilename;
    private ArrayList<BlockInfo> blockList;
    private int[][] levelBlocks;
    private Bitmap backgroundBitmap;

    private LevelHolder(Context context, String assetDirectory, String backgroundFilename, Bitmap backgroundBitmap, int blockSize, ArrayList<BlockInfo> blockList, int[][] levelBlocks) {
        this.context = context;
        this.assetDirectory = assetDirectory;
        this.backgroundFilename = backgroundFilename;
        this.backgroundBitmap = backgroundBitmap;
        this.blockSize = blockSize;
        this.blockList = blockList;
        this.levelBlocks = levelBlocks;
    }

    public static LevelHolder load(Context context) {
        int blockSize = context.getResources().getInteger(R.integer.block_size);
        try {
            Log.w(TAG, "LOADING LEVEL FROM " + FILENAME);
            FileInputStream fileInputStream = context.openFileInput(FILENAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            String assetDirectory = (String) objectInputStream.readObject();
            String backgroundFilename = (String) objectInputStream.readObject();
            Bitmap backgroundBitmap = getBitmap(context.getResources().getAssets(), backgroundFilename);
            int[][] levelBlocks = (int[][]) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            ArrayList<BlockInfo> blockList = makeBlockList(context.getResources().getAssets(), assetDirectory);
            LevelHolder levelHolder = new LevelHolder(context, assetDirectory, backgroundFilename, backgroundBitmap, blockSize, blockList, levelBlocks);
            Log.w(TAG, "LEVEL LOADED FROM " + FILENAME);
            return levelHolder;
        } catch (Exception e) {
            Log.w("Unable to deserialize file '" + FILENAME + "'", e);
        }
        String assetDirectory = "platformerArt_v4/png";
        String backgroundFilename = "Platformer Art Complete Pack_0/Mushroom expansion/Backgrounds/bg_castle.png";
        Bitmap backgroundBitmap = getBitmap(context.getResources().getAssets(), backgroundFilename);
        int levelBlocksX = (int) Math.floor(backgroundBitmap.getWidth() / blockSize);
        int levelBlocksY = (int) Math.floor(backgroundBitmap.getHeight() / blockSize);
        ArrayList<BlockInfo> blockList = makeBlockList(context.getResources().getAssets(), assetDirectory);
        int[][] levelBlocks = makeLevelBlocks(blockList, levelBlocksX, levelBlocksY);
        LevelHolder levelHolder = new LevelHolder(context, assetDirectory, backgroundFilename, backgroundBitmap, blockSize, blockList, levelBlocks);
        Log.w(TAG, "NEW LEVEL DATA GENERATED");
        return levelHolder;
    }

    private static Bitmap getBitmap(AssetManager assetManager, String assetFilename) {
        try {
            return BitmapFactory.decodeStream(assetManager.open(assetFilename));
        } catch (IOException ignore) {
            throw new RuntimeException("Unable to load game asset file '" + assetFilename + "'");
        }
    }

    private static ArrayList<BlockInfo> makeBlockList(AssetManager assetManager, String assetDirectory) {
        ArrayList<BlockInfo> blockList = new ArrayList<>();
        ArrayList<String> assets = getAssets(assetManager, assetDirectory);
        for (String path : assets) {
            Bitmap bitmap = getBitmap(assetManager, path);
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            blockList.add(new BlockInfo(path, bitmap));
        }
        return blockList;
    }

    private static ArrayList<String> getAssets(AssetManager assetManager, String assetDirectory) {
        ArrayList<String> list = new ArrayList<>();
        try {
            String[] items = assetManager.list(assetDirectory);
            for (String item : items) {
                String path = assetDirectory + "/" + item;
                if (path.endsWith(".png")) {
                    list.add(path);
                } else {
                    list.addAll(getAssets(assetManager, path));
                }
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException("Unable to access asset directory '" + assetDirectory + "'");
        }
    }

    private static int[][] makeLevelBlocks(ArrayList<BlockInfo> blockList, int levelBlocksX, int levelBlocksY) {
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
        return levelBlocks.length;
    }

    public int getLevelBlocksY() {
        return levelBlocks[0].length;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void save() {
        Log.w(TAG, "SAVING LEVEL TO " + FILENAME);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(assetDirectory);
            objectOutputStream.writeObject(backgroundFilename);
            objectOutputStream.writeObject(levelBlocks);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to open file '" + FILENAME + "'", e);
        }
        Log.w(TAG, "LEVEL SAVED TO " + FILENAME);
    }
}
