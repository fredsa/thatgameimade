package sauer.thatgameimade;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SpriteRecyclerViewAdapter extends RecyclerView.Adapter<SpriteRecyclerViewAdapter.SpriteViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = SpriteRecyclerViewAdapter.class.getSimpleName();
    private final OnItemSelectedListener onItemSelectedListener;
    private LevelHolder levelHolder;

    SpriteRecyclerViewAdapter(OnItemSelectedListener onItemSelectedListener) {
        this.levelHolder = levelHolder;
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
        final Bitmap bitmap = levelHolder.getBitmapList().get(position);
        spriteViewHolder.spriteNameTextView.setText("foo");
        spriteViewHolder.spriteImageView.setImageBitmap(bitmap);
        spriteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedListener.itemSelected(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        // TODO avoid conditional
        return levelHolder == null ? 0 : levelHolder.getBitmapList().size();
    }

    public void setLevelHolder(LevelHolder levelHolder) {
        this.levelHolder = levelHolder;
    }

    public interface OnItemSelectedListener {
        void itemSelected(Bitmap bitmap);
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
