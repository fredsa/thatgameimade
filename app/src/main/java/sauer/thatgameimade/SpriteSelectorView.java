package sauer.thatgameimade;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;

public class SpriteSelectorView extends ScrollView {

    public ArrayList<SpriteInfo> SPRITES;

    public SpriteSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        init(context);
    }

    private void init(Context context) {
        SPRITES = new ArrayList<SpriteInfo>();
        for (int i = 0; i < 2; i++) {
            SPRITES.add(makeBitmap("Kenny Game", R.drawable.kenny_game));
            SPRITES.add(makeBitmap("Smiley face", R.drawable.face));
            SPRITES.add(makeBitmap("Cake", R.drawable.cake_half_alt));
            SPRITES.add(makeBitmap("Foursquare", R.drawable.foursquare));
        }

        View.inflate(context, R.layout.spite_selector_view, this);
        SpriteArrayAdapter adapter = new SpriteArrayAdapter(context, R.layout.spite_selector_view, SPRITES);
        ListView spritePickerListView = (ListView) findViewById(R.id.spriteListView);
        spritePickerListView.setAdapter(adapter);
    }

    private SpriteInfo makeBitmap(String name, @DrawableRes int drawable) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawable).copy(Bitmap.Config.ARGB_8888, true);
        return new SpriteInfo(name, bitmap);
    }

}
