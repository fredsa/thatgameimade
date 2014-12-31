package sauer.thatgameimade;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LevelRecyclerViewAdapter extends RecyclerView.Adapter<LevelRecyclerViewAdapter.LevelViewHolder> {
    @SuppressWarnings("unused")
    private static final String TAG = LevelRecyclerViewAdapter.class.getSimpleName();
    private final OnItemSelectedListener onItemSelectedListener;

    LevelRecyclerViewAdapter(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.level_list_item, parent, false);
        return new LevelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LevelViewHolder levelViewHolder, final int position) {
        levelViewHolder.LevelNameTextView.setText("Level " + position);
        levelViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectedListener.itemSelected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public interface OnItemSelectedListener {
        void itemSelected(int blockIndex);
    }

    public static class LevelViewHolder extends RecyclerView.ViewHolder {
        TextView LevelNameTextView;
        private View itemView;

        public LevelViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            LevelNameTextView = (TextView) itemView.findViewById(R.id.levelNameTextView);
        }
    }
}
