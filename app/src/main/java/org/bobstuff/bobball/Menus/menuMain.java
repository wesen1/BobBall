package org.bobstuff.bobball.Menus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.bobstuff.bobball.Preferences;
import org.bobstuff.bobball.R;
import org.bobstuff.bobball.Utilities;


public class menuMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utilities.hideTitleBar(this);
        setContentView(R.layout.menu_main);
    }

    public void onResume() {
        super.onResume();
        Preferences.setContext(getApplicationContext());
    }


    public void openSinglePlayerMenu (View view) {
        Intent intent = new Intent(this, menuSinglePlayer.class);
        startActivity(intent);
    }

    public void openSettingsMenu (View view) {
        Intent intent = new Intent(this, menuOptions.class);
        startActivity(intent);
    }

    public void openHighScoresMenu(View view) {
        Intent intent = new Intent(this, menuHighScores.class);
        startActivity(intent);
    }

    public void openStatsMenu (View view) {
        Intent intent = new Intent(this, menuStatistics.class);
        startActivity(intent);
    }

    public void openHelpMenu (View view) {
        Intent intent = new Intent(this, menuHelp.class);
        startActivity(intent);
    }

    public void exitApp (View view) {
        finish();
    }
}
