package sauer.thatgameimade;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;


public class LevelSelectorView extends ScrollView {

    private OnLevelSelectedListener onLevelSelectedListener;
    private LevelRecyclerViewAdapter levelRecyclerViewAdapter;

    public LevelSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View.inflate(context, R.layout.level_selector_view, this);
        final RecyclerView levelSelectorRecyclerView = (RecyclerView) findViewById(R.id.levelSelectorRecyclerView);
        levelSelectorRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        levelSelectorRecyclerView.setLayoutManager(layoutManager);

        levelRecyclerViewAdapter = new LevelRecyclerViewAdapter(new LevelRecyclerViewAdapter.OnItemSelectedListener() {
            @Override
            public void itemSelected(int level) {
                onLevelSelectedListener.levelSelected(level);
            }
        });
        levelSelectorRecyclerView.setAdapter(levelRecyclerViewAdapter);
    }

    public void setOnLevelSelectedListener(OnLevelSelectedListener onLevelSelectedListener) {
        this.onLevelSelectedListener = onLevelSelectedListener;
    }

    public interface OnLevelSelectedListener {
        void levelSelected(int level);
    }
}
