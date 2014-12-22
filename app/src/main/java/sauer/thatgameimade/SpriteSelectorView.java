package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;

public class SpriteSelectorView extends ScrollView {

    public ArrayList<SpriteInfo> SPRITES;
    private OnSpriteSelectedListener onSpriteSelectedListener;

    public SpriteSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        if (isInEditMode()) {
//            return;
//        }
        init(context);
    }

    private void init(Context context) {
        SPRITES = new ArrayList<SpriteInfo>();
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
        final SpriteArrayAdapter adapter = new SpriteArrayAdapter(context, R.layout.sprite_list_item, SPRITES);
        final ListView spritePickerListView = (ListView) findViewById(R.id.spriteListView);
        spritePickerListView.setAdapter(adapter);

        spritePickerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onSpriteSelectedListener != null) {
                    onSpriteSelectedListener.spriteSelected(view, adapter.getItem(position).getBitmap());
                }
            }
        });
    }

    private SpriteInfo makeBitmap(@DrawableRes int resid) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resid).copy(Bitmap.Config.ARGB_8888, true);
        String name = getResources().getResourceEntryName(resid);
        return new SpriteInfo(name, bitmap);
    }

    public void setOnSpriteSelectedListener(OnSpriteSelectedListener onSpriteSelectedListener) {
        this.onSpriteSelectedListener = onSpriteSelectedListener;
    }

    public interface OnSpriteSelectedListener {
        void spriteSelected(View view, Bitmap bitmap);
    }
}
