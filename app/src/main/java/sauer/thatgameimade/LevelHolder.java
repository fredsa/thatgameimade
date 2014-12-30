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

    private final int blockSize;
    private final Context context;
    private final int level;
    private String assetRoot;
    private String backgroundFilename;
    private ArrayList<BlockInfo> blockList;
    private int[][] levelBlocks;
    private Bitmap backgroundBitmap;

    private LevelHolder(Context context, int level, String assetRoot, String backgroundFilename, Bitmap backgroundBitmap, int blockSize, ArrayList<BlockInfo> blockList, int[][] levelBlocks) {
        this.context = context;
        this.level = level;
        this.assetRoot = assetRoot;
        this.backgroundFilename = backgroundFilename;
        this.backgroundBitmap = backgroundBitmap;
        this.blockSize = blockSize;
        this.blockList = blockList;
        this.levelBlocks = levelBlocks;
    }

    public static LevelHolder load(Context context, int level) throws IOException {
        String filename = getFilename(level);
        int blockSize;
        String assetRoot;
        String backgroundFilename;
        ArrayList<BlockInfo> blockList;
        int[][] levelBlocks;
        switch (level) {
            case 1:
                blockSize = 70;
                backgroundFilename = "Platformer Art Complete Pack_0/Mushroom expansion/Backgrounds/bg_castle.png";
                assetRoot = "platformerArt_v4/png";
                blockList = makeBlockList(context.getResources().getAssets(), assetRoot);
                break;
            case 2:
                blockSize = 21;
                backgroundFilename = "Platformer Art Complete Pack_0/Mushroom expansion/Backgrounds/bg_shroom.png";
                assetRoot = "Platformer Art Deluxe (Pixel)/spritesheet.png";
                blockList = makeBlockList(context.getResources().getAssets(), assetRoot, blockSize, 2, 1);
                break;
            default:
                throw new RuntimeException("Don't know how to create level " + level);
        }
        Bitmap backgroundBitmap = getBitmap(context.getResources().getAssets(), backgroundFilename);

        try {
            Log.w(TAG, "LOADING LEVEL " + level + " FROM " + filename);
            FileInputStream fileInputStream = context.openFileInput(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            blockSize = objectInputStream.read();
            assetRoot = (String) objectInputStream.readObject();
            backgroundFilename = (String) objectInputStream.readObject();
            levelBlocks = (int[][]) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            Log.w(TAG, "LEVEL " + level + " LOADED FROM " + filename);
        } catch (Exception e) {
            Log.w("Deserialize failure '" + filename + "'", e);
            int levelBlocksX = (int) Math.floor(backgroundBitmap.getWidth() / blockSize);
            int levelBlocksY = (int) Math.floor(backgroundBitmap.getHeight() / blockSize);
            levelBlocks = makeLevelBlocks(blockList, levelBlocksX, levelBlocksY);
        }
        LevelHolder levelHolder = new LevelHolder(context, level, assetRoot, backgroundFilename, backgroundBitmap, blockSize, blockList, levelBlocks);
        Log.w(TAG, "NEW LEVEL " + level + " DATA GENERATED");
        return levelHolder;
    }

    private static String getFilename(int level) {
        return "level" + level + ".dat";
    }

    private static ArrayList<BlockInfo> makeBlockList(AssetManager assetManager, String path, int blockSize, int margin, int trim) throws IOException {
        ArrayList<BlockInfo> blockList = new ArrayList<>();
        Bitmap bitmap = getBitmap(assetManager, path);
        int stride = blockSize + margin;
        int blocksX = (bitmap.getWidth() - 2 * trim) / stride;
        int blocksY = (bitmap.getHeight() - 2 * trim) / stride;
        for (int i = 0; i < blocksX; i++) {
            for (int j = 0; j < blocksY; j++) {
                // TODO avoid wasting memory on bitmap copies
                Bitmap b = Bitmap.createBitmap(bitmap, trim + margin + i * stride, trim + margin + j * stride, blockSize, blockSize);
                blockList.add(new BlockInfo(path, b));
            }
        }
        return blockList;
    }

    private static Bitmap getBitmap(AssetManager assetManager, String assetFilename) throws IOException {
        return BitmapFactory.decodeStream(assetManager.open(assetFilename));
    }

    private static ArrayList<BlockInfo> makeBlockList(AssetManager assetManager, String assetRoot) throws IOException {
        ArrayList<BlockInfo> blockList = new ArrayList<>();
        String[] items = assetManager.list(assetRoot);
        for (String item : items) {
            String path = assetRoot + "/" + item;
            try {
                Bitmap bitmap = getBitmap(assetManager, path);
                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                blockList.add(new BlockInfo(path, bitmap));
            } catch (Exception ignore) {
                blockList.addAll(makeBlockList(assetManager, path));
            }
        }
        return blockList;
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
        String filename = getFilename(level);
        Log.w(TAG, "SAVING LEVEL TO " + filename);
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.write(blockSize);
            objectOutputStream.writeObject(assetRoot);
            objectOutputStream.writeObject(backgroundFilename);
            objectOutputStream.writeObject(levelBlocks);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to open file '" + filename + "'", e);
        }
        Log.w(TAG, "LEVEL SAVED TO " + filename);
    }
}
