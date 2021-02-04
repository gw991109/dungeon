package com.example.dungeonescape.platformer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.dungeonescape.activities.DeadActivity;
import com.example.dungeonescape.activities.EndGameActivity;
import com.example.dungeonescape.activities.MainActivity;
import com.example.dungeonescape.platformer.views.PlatformerView;
import com.example.dungeonescape.player.PlayerManager;
import com.example.dungeonescape.activities.GeneralGameActivity;
import com.example.dungeonescape.player.Player;
import com.example.dungeonescape.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The activity for the main level 3 game.
 */
public class PlatformerMainActivity extends GeneralGameActivity{
    private PlatformerView game;
    private boolean running;
    private Player player;
    private long startTime;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startTime = SystemClock.elapsedRealtime();

        //* Gather saved data. */
        load();
        setContentView(R.layout.activity_level3_main);
        game = findViewById(R.id.level3);
        setTitle("Level3: Platformer");
        loadGameInfo();

        // Set Buttons
        buttons();
        running = true;

        // Set listeners
        game.setEnterPortalListener(new OnCustomEventListener() {
            public void onEvent() {enterHiddenLevel(savedInstanceState);

            }
        });
        game.setFinishLevelListener(new OnCustomEventListener() {
            public void onEvent() {endGame();
            }
        });
        game.setEndGameListener(new OnCustomEventListener() {
            public void onEvent() {deadPage();
            }
        });
        // Thread code is from the following Youtube Video, body of run() is written myself
        // https://www.youtube.com/watch?v=6sBqeoioCHE&t=193s
        Thread t = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {

                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (running) {
                                    updateScores();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    /**
     * Loads all the information from intent and initializes it into game.
     */
    private void loadGameInfo(){
        Intent i = getIntent();
        String name = (String) i.getSerializableExtra("Player Name");
        player = getPlayerManager().getPlayer(name);
        // Load Character and platform old locations if returning from hidden level.
        @SuppressWarnings("unchecked")
        ArrayList<Integer> character = (ArrayList<Integer>) i.getSerializableExtra("Character");
        @SuppressWarnings("unchecked")
        ArrayList<List> platformLocations = (ArrayList) i.getSerializableExtra("Platforms");

        // Setup Platform and Character Locations if returning from hidden level.
        if (platformLocations != null && character != null) {
            int score = (int) i.getSerializableExtra("Score");
            game.getManager().setCharacter(character, score);
            game.getManager().setPlatforms(platformLocations);
        }

        //pass player into manager
        game.getManager().setPlayer(player);
        // get Resource file for portal
        game.setPortalImage(this.getResources().getDrawable(R.drawable.portal, null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    /**
     * Updates Scores shown on screen
     */
    private void updateScores() {
        // Update the score shown
        int score = game.getManager().getCharacterScore();
        String scr = String.valueOf(score) ;
        String scre = "Score: " + scr;

        TextView score1 = findViewById(R.id.score);
        score1.setText(scre);
        // Update the lives shown
        int lives = game.getManager().getPlayer().getNumLives();
        String life = "Lives: " + (lives);

        TextView lifeText = findViewById(R.id.lives);
        lifeText.setText(life);
        // Update the coins shown
        int numCoins = game.getManager().getPlayer().getNumCoins();
        String strCoins = "Coins: " + (numCoins);

        TextView coinsText = findViewById(R.id.coins);
        coinsText.setText(strCoins);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_menu) {
            save(getPlayerManager());
            Intent intent = new Intent(PlatformerMainActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    /**
     * User has entered the portal to the hidden level.
     */
    private void enterHiddenLevel(Bundle savedInstanceState) {
        long endTime = SystemClock.elapsedRealtime();
        long elapsedMilliSeconds = endTime - startTime;
        player.updateTotalTime(elapsedMilliSeconds);
        save(getPlayerManager());
        Intent intent = new Intent(PlatformerMainActivity.this, PlatformerHiddenActivity.class);
        intent.putExtra("Player Name", player.getName());
        intent.putExtra("Character", game.getManager().getCharacterLocation());
        intent.putExtra("Platforms", game.getManager().getPlatformPositions());
        intent.putExtra("Score", game.getManager().getCharacterScore());

        startActivity(intent);
    }
    /**
     * User has successfully finished Platformer and will now move to the EndGamePage.
     */
    private void endGame() {
        long endTime = SystemClock.elapsedRealtime();
        long elapsedMilliSeconds = endTime - startTime;
        player.updateTotalTime(elapsedMilliSeconds);
        save(getPlayerManager());
        Intent intent = new Intent(PlatformerMainActivity.this, EndGameActivity.class);
        intent.putExtra("Player Name", player.getName());
        startActivity(intent);
    }
    /**
     * User has lost the Game i.e. no more lives left.
     */
    private void deadPage() {
        long endTime = SystemClock.elapsedRealtime();
        long elapsedMilliSeconds = endTime - startTime;
        player.updateTotalTime(elapsedMilliSeconds);
        save(getPlayerManager());
        Intent intent = new Intent(PlatformerMainActivity.this, DeadActivity.class);
        intent.putExtra("Player Name", player.getName());
        startActivity(intent);
    }

    /**
     * Method executes when the player starts the game.
     */
    @Override
    protected void onResume() {
        super.onResume();
        game.resume();
    }

    /**
     * Method for initializing left and right buttons.
     */
    private void buttons() {

        Button left = (Button) findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getManager().left_button();
            }
        });

        Button right = (Button) findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.getManager().right_button();
            }
        });
        configureSatchelButton();
    }

    @Override
    protected void onPause() {
        super.onPause();
        game.pause();
    }

    /**
     * Saves the  player data.
     */
    @Override
    public void save(PlayerManager playerManager) {
        super.save(playerManager);
        player.setCurrentLevel(3);
    }

    /** Creates the Satchel Button which opens up the Player's Satchel. */
    private void configureSatchelButton() {
        Button satchelButton = findViewById(R.id.satchelButton);
        satchelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSatchel();
            }
        });
    }

    /** Creates the AlertDialog that displays the contents of the Player's Satchel. */
    private void openSatchel() {
        onPause();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(getPlayerSatchelData());
        dialogBuilder.setCancelable(true);

        dialogBuilder.setNeutralButton(
                "Return to Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        onResume();
                    }
                });

        AlertDialog showSatchel = dialogBuilder.create();
        showSatchel.show();
    }

    /** Returns the contents of the Player's satchel.
     *
     * @return A string containing the contents of the Player's satchel.
     */
    private StringBuilder getPlayerSatchelData() {
        StringBuilder satchelContents = new StringBuilder();
        Map<String, Integer> satchel = player.getSatchel();
        for (Map.Entry<String, Integer> collectedItem : satchel.entrySet()) {
            satchelContents
                    .append(collectedItem.getKey())
                    .append(" ")
                    .append(collectedItem.getValue())
                    .append("\n");
        }
        return satchelContents;
    }

}

