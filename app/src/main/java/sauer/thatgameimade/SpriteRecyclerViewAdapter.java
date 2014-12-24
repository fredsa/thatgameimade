package sauer.thatgameimade;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SpriteRecyclerViewAdapter extends RecyclerView.Adapter<SpriteRecyclerViewAdapter.SpriteViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = SpriteRecyclerViewAdapter.class.getSimpleName();
    private final OnItemSelectedListener onItemSelectedListener;
    private ArrayList<SpriteInfo> sprites;

    SpriteRecyclerViewAdapter(ArrayList<SpriteInfo> sprites, OnItemSelectedListener onItemSelectedListener) {
        this.sprites = sprites;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public SpriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sprite_list_item, parent, false);
        SpriteViewHolder spriteViewHolder = new SpriteViewHolder(itemView);
        return spriteViewHolder;
    }

    @Override
    public void onBindViewHolder(SpriteViewHolder spriteViewHolder, int position) {
        final SpriteInfo spriteInfo = sprites.get(position);
        spriteViewHolder.spriteNameTextView.setText(spriteInfo.getName());
        spriteViewHolder.spriteImageView.setImageBitmap(spriteInfo.getBitmap());
        spriteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedListener.itemSelected(spriteInfo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sprites.size();
    }

    public interface OnItemSelectedListener {
        void itemSelected(SpriteInfo spriteInfo);
    }

    public static class SpriteViewHolder extends RecyclerView.ViewHolder {
        TextView spriteNameTextView;
        ImageView spriteImageView;
        private View itemView;

        public SpriteViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            spriteNameTextView = (TextView) itemView.findViewById(R.id.spriteNameTextView);
            spriteImageView = (ImageView) itemView.findViewById(R.id.spriteImageView);
        }
    }
}
