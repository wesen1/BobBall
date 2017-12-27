package org.bobstuff.bobball.Menus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.bobstuff.bobball.BobBallActivity;
import org.bobstuff.bobball.Paints;
import org.bobstuff.bobball.R;
import org.bobstuff.bobball.Scores;
import org.bobstuff.bobball.Settings;
import org.bobstuff.bobball.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class menuHighScores extends Activity {

    private int bestScore = 100;
    private String bestScoreName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utilities.hideTitleBar(this);
        setContentView(R.layout.menu_highscores);

        Spinner numPlayersSelector = (Spinner) findViewById(R.id.num_players);
        numPlayersSelector.setAdapter(Utilities.createDropdown(this, 3));

        int rank = 0;
        int numPlayers = 0;
        Scores scores = new Scores (1);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            if (extras.containsKey("rank") && extras.containsKey("numPlayers")) {
                rank = extras.getInt("rank");
                numPlayers = extras.getInt("numPlayers");

                Button retryButton = (Button) findViewById(R.id.retryButton);
                retryButton.setVisibility(View.VISIBLE);
                numPlayersSelector.setSelection(numPlayers-1);

                scores = new Scores (numPlayers);
            }
        }
        else {
            Button backToMainButton = (Button) findViewById(R.id.backToMainButton);
            backToMainButton.setVisibility(View.VISIBLE);
        }

        scores.loadScores();
        displayScores(scores, rank);

        final int listenerNumPlayers = numPlayers;
        final int listenerRank = rank;

        numPlayersSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Scores scores = new Scores(position + 1);
                scores.loadScores();

                if (position + 1 == listenerNumPlayers) {
                    displayScores(scores, listenerRank);
                }

                else {
                    displayScores(scores, 0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // nop
            }

        });

    }

    private void displayScores (Scores scores, int rank) {
        final TableLayout tableLayout = (TableLayout) findViewById(R.id.highScores);
        final TextView noRecords = (TextView) findViewById(R.id.noRecords);

        tableLayout.removeAllViews();

        if (scores.asCharSequence().length == 0) {
            noRecords.setVisibility(View.VISIBLE);
            tableLayout.setVisibility(View.GONE);
        }

        else {
            tableLayout.setVisibility(View.VISIBLE);
            noRecords.setVisibility(View.GONE);

            final CharSequence[] highScoreArray = scores.asCharSequence();

            for (int i = 1; i <= highScoreArray.length; i++) {

                TableRow highScoreEntry = new TableRow (this);

                TextView scoreText = new TextView(this);
                scoreText.setTextSize(20);
                scoreText.setGravity(Gravity.START);
                scoreText.setText("" + highScoreArray[i - 1]);

                TextView rankText = new TextView(this);
                rankText.setTextSize(20);
                rankText.setGravity(Gravity.END);

                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams (FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
                //rankText.setLayoutParams(params);
                params.setMargins(20, 0, 0, 0);
                //scoreText.setLayoutParams(params);

                if (i < 4) {
                    Drawable rankImage = ContextCompat.getDrawable(this, R.drawable.rank1);
                    if (i == 2) {
                        rankImage = ContextCompat.getDrawable(this, R.drawable.rank2);
                    } else if (i == 3) {
                        rankImage = ContextCompat.getDrawable(this, R.drawable.rank3);
                    }else if (i == 1){

                    }

                    scoreText.measure(0,0);
                    int scoreHeight = scoreText.getMeasuredHeight();
                    rankImage.setBounds(0, 0, scoreHeight, scoreHeight);
                    rankText.setCompoundDrawablesWithIntrinsicBounds(rankImage, null, null, null);
                } else {
                    rankText.setText(i + ".");
                }

                if (i == rank){
                    scoreText.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                    scoreText.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
                    rankText.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                    rankText.setBackgroundColor(ContextCompat.getColor(this, R.color.colorWhite));
                }

                highScoreEntry.addView(rankText);
                highScoreEntry.addView(scoreText);

                tableLayout.addView(highScoreEntry);
            }
        }
    }

    /**
     * This method creates a jpg file that shows the best players score and nickname
     * The trophy picture is a combination of those two pictures :
     *
     * https://pixabay.com/de/lorbeerkranz-kranz-auszeichnung-150577/
     * https://pixabay.com/de/troph%C3%A4e-leistung-auszeichnung-pokal-153395/
     */

    public void shareHighScore (View view) {

        final int width = 720;
        final int height = 1280;

        final int textSize = 80;    // 120
        final int marginBetweenElements = 40;
        final int maxTrophyHeight = height - 4 * marginBetweenElements - 2 * textSize;

        Bitmap backgroundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        backgroundBitmap.eraseColor(Color.WHITE);   // make Bitmap white
        Canvas bitmapCanvas = new Canvas(backgroundBitmap);

        // draw horizontal lines
        for (int y = 0; y < height; y += 40) {
            bitmapCanvas.drawLine (0, y, width, y, Paints.blackPaint);
        }

        // draw vertical lines
        for (int x = 0; x < width; x += 40) {
            bitmapCanvas.drawLine (x, 0, x, height, Paints.blackPaint);
        }


        // write title
        Paint title = new Paint();
        title.setTextSize(textSize);
        title.setTextAlign(Paint.Align.CENTER);
        bitmapCanvas.drawText(getString(R.string.appName), width/2, marginBetweenElements + textSize, title);

        // draw trophy (x : y; 1018 : 1280)
        Drawable trophy = ContextCompat.getDrawable(this, R.drawable.trophy);

        int boundsTop = 2 * marginBetweenElements + textSize;
        int trophyHeight = maxTrophyHeight - Math.round((1 - width/1264) * maxTrophyHeight);
        int paddingBottomTop = (maxTrophyHeight - trophyHeight) / 2;

        trophy.setBounds(0, boundsTop, width, height - (2 * marginBetweenElements + textSize));
        trophy.draw(bitmapCanvas);

        // write highest score
        Paint score = new Paint();
        score.setTextSize(textSize);
        score.setTextAlign(Paint.Align.CENTER);
        bitmapCanvas.drawText("" + bestScore, width/2, height - marginBetweenElements, score);

        // write highest score name
        // name field dimensions = 270 x 68
        //

        Paint name = new Paint();
        name.setTextSize(20);
        name.setTextAlign(Paint.Align.CENTER);
        name.setColor(Color.parseColor("#b88100"));

        // save the canvas as jpg

            try {
                File file = new File (Environment.getExternalStorageDirectory() + "/BobBall/BobBallHighScore.jpg");
                boolean fileCreated = file.createNewFile();
                FileOutputStream stream = new FileOutputStream(file);
                backgroundBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.close();
            }
            catch (IOException ioException){
                Toast.makeText(this, "Das hat nicht geklappt!" + ioException.getMessage(), Toast.LENGTH_SHORT).show();
            }
            finally  {
                Toast.makeText(this, "Es hat funktioniert!", Toast.LENGTH_SHORT).show();
            }
    }

    public void goBack (View view) {
        finish();
    }

    public void retry (View view){
        int retryAction = Settings.getRetryAction();
        Intent intent = new Intent(this, BobBallActivity.class);
        int numPlayers = Settings.getNumPlayers();
        finish();   // finish in any case, finish only for retryAction 0 (Go back to level select)

        if (retryAction == 1){  // restart last level lost, same numPlayers
            intent.putExtra("numPlayers", numPlayers);
            intent.putExtra("level", Settings.getLastLevelFailed());
            startActivity(intent);
        }
        if (retryAction == 2){  // restart from last selected level
            intent.putExtra("numPlayers", numPlayers);
            intent.putExtra("level", Settings.getSelectLevel() + 1);
            startActivity(intent);
        }
        if (retryAction == 3){  // restart from level 1
            intent.putExtra("numPlayers", numPlayers);
            intent.putExtra("level", 1);
            startActivity(intent);
        }
    }}
