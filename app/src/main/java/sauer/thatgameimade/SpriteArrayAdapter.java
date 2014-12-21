package sauer.thatgameimade;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SpriteArrayAdapter extends ArrayAdapter<SpriteInfo> {
    @SuppressWarnings("unused")
    private static final String TAG = SpriteArrayAdapter.class.getSimpleName();
    private ArrayList<SpriteInfo> sprites;

    public SpriteArrayAdapter(Context context, @LayoutRes int resource, ArrayList<SpriteInfo> sprites) {
        super(context, resource, sprites);
        this.sprites = sprites;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SpriteInfo sprite = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sprite_list_item, parent, false);
        }

        TextView spriteNameTextView = (TextView) convertView.findViewById(R.id.spriteNameTextView);
        ImageView spriteImageView = (ImageView) convertView.findViewById(R.id.spriteImageView);

        spriteNameTextView.setText(sprite.getName());
        spriteImageView.setImageBitmap(sprite.getBitmap());
        return convertView;
    }
}
