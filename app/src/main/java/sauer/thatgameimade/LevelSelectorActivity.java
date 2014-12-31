package sauer.thatgameimade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class LevelSelectorActivity extends Activity {

    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selector);


        myApplication = (MyApplication) getApplication();

        LevelSelectorView levelSelectorView = (LevelSelectorView) findViewById(R.id.levelSelectorView);
        levelSelectorView.setOnLevelSelectedListener(new LevelSelectorView.OnLevelSelectedListener() {
            @Override
            public void levelSelected(int level) {
                Intent intent = new Intent(LevelSelectorActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
