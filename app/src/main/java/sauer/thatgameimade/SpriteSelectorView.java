package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
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

    public SpriteSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        SPRITES = new ArrayList<>();
        SPRITES.add(makeBitmap(R.drawable.my_face));
        SPRITES.add(makeBitmap(R.drawable.my_foursquare));
        SPRITES.add(makeBitmap(R.drawable.icepack_cane_green));
        SPRITES.add(makeBitmap(R.drawable.icepack_cane_green_top));
        SPRITES.add(makeBitmap(R.drawable.icepack_pine_sapling));
        SPRITES.add(makeBitmap(R.drawable.icepack_ice_water));
        SPRITES.add(makeBitmap(R.drawable.icepack_ice_water_mid));
        SPRITES.add(makeBitmap(R.drawable.icepack_ice_water_deep));
        SPRITES.add(makeBitmap(R.drawable.icepack_ice_block));
        SPRITES.add(makeBitmap(R.drawable.icepack_rock));
        SPRITES.add(makeBitmap(R.drawable.icepack_plant));
        SPRITES.add(makeBitmap(R.drawable.icepack_spikes_bottom));
        SPRITES.add(makeBitmap(R.drawable.icepack_tundra_left));
        SPRITES.add(makeBitmap(R.drawable.icepack_tundra_mid));
        SPRITES.add(makeBitmap(R.drawable.icepack_tundra_right));

        View.inflate(context, R.layout.spite_selector_view, this);
        final RecyclerView spriteSelectorRecyclerView = (RecyclerView) findViewById(R.id.spriteSelectorRecyclerView);
        spriteSelectorRecyclerView.setHasFixedSize(true);
        spriteSelectorRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        final SpriteRecyclerViewAdapter adapter = new SpriteRecyclerViewAdapter(SPRITES, new SpriteRecyclerViewAdapter.OnItemSelectedListener() {
            @Override
            public void itemSelected(SpriteInfo spriteInfo) {
                onSpriteSelectedListener.spriteSelected(spriteInfo.getBitmap());
            }
        });
        spriteSelectorRecyclerView.setAdapter(adapter);
    }

    private SpriteInfo makeBitmap(@DrawableRes int resourceId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId).copy(Bitmap.Config.ARGB_8888, true);
        String name = isInEditMode() ? "" + resourceId : getResources().getResourceEntryName(resourceId);
        return new SpriteInfo(name, bitmap);
    }

    public void setOnSpriteSelectedListener(OnSpriteSelectedListener onSpriteSelectedListener) {
        this.onSpriteSelectedListener = onSpriteSelectedListener;
    }

    public interface OnSpriteSelectedListener {
        void spriteSelected(Bitmap bitmap);
    }
}
