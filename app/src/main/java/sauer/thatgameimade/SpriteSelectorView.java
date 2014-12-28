package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;

public class SpriteSelectorView extends ScrollView {

    @SuppressWarnings("unused")
    public static final String TAG = SpriteSelectorView.class.getSimpleName();

    public ArrayList<SpriteInfo> SPRITES;
    private OnSpriteSelectedListener onSpriteSelectedListener;
    private LevelHolder levelHolder;
    private SpriteRecyclerViewAdapter spriteRecyclerViewAdapter;

    public SpriteSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.sprite_selector_view, this);
        final RecyclerView spriteSelectorRecyclerView = (RecyclerView) findViewById(R.id.spriteSelectorRecyclerView);
        spriteSelectorRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        spriteSelectorRecyclerView.setLayoutManager(layoutManager);

        spriteRecyclerViewAdapter = new SpriteRecyclerViewAdapter(new SpriteRecyclerViewAdapter.OnItemSelectedListener() {
            @Override
            public void itemSelected(Bitmap bitmap) {
                onSpriteSelectedListener.spriteSelected(bitmap);
            }
        });
        spriteSelectorRecyclerView.setAdapter(spriteRecyclerViewAdapter);
    }

    public void setOnSpriteSelectedListener(OnSpriteSelectedListener onSpriteSelectedListener) {
        this.onSpriteSelectedListener = onSpriteSelectedListener;
    }

    public void setLevelHolder(LevelHolder levelHolder) {
        this.levelHolder = levelHolder;
        spriteRecyclerViewAdapter.setLevelHolder(levelHolder);
    }

    public interface OnSpriteSelectedListener {
        void spriteSelected(Bitmap bitmap);
    }
}
