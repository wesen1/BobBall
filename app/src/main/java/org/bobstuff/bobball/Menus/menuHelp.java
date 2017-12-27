package org.bobstuff.bobball.Menus;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import org.bobstuff.bobball.Utilities;

import org.bobstuff.bobball.R;

public class menuHelp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utilities.hideTitleBar(this);
        setContentView(R.layout.menu_help);
    }

    public void goBack (View view) {
        finish();
    }
}
