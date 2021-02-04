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
import com.example.dungeonescape.activities.MainActivity;
import com.example.dungeonescape.platformer.views.LevelView;
import com.example.dungeonescape.activities.GeneralGameActivity;
import com.example.dungeonescape.player.Player;
import com.example.dungeonescape.R;
import java.util.Map;

/**
 * The activity for the Hidden Blitz Level in Platformer.
 */
public class PlatformerHiddenActivity extends GeneralGameActivity {
    private LevelView game;
    private boolean running;
    private Player player;
    long startTime;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startTime = SystemClock.elapsedRealtime();

        //* Gather saved data. */
        load();
        i = getIntent();
        String name = (String) i.getSerializableExtra("Player Name");
        player = getPlayerManager().getPlayer(name);


        setContentView(R.layout.activity_platformer_hidden_main);
        game = findViewById(R.id.level);

        // getting player instance from intent
        //pass player into manager
        game.getManager().setPlayer(player);

        setTitle("Level 3: PlatformerBonusLevel");
        game.setEndGameListener(new OnCustomEventListener() {
            public void onEvent() {
                endLevel();
            }
        });
        // Set Buttons
        buttons();
        running = true;

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

                                    // Update the coins shown
                                    int numCoins = game.getManager().getPlayer().getNumCoins();
                                    String strCoins = "Coins: " + (numCoins);

                                    TextView coinsText = findViewById(R.id.hiddenCoins);
                                    coinsText.setText(strCoins);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_menu) {
            Intent intent = new Intent(PlatformerHiddenActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    /**
     * User has died in the hidden level i.e. Fell once.
     */
    private void endLevel() {
        long endTime = SystemClock.elapsedRealtime();
        long elapsedMilliSeconds = endTime - startTime;
        player.updateTotalTime(elapsedMilliSeconds);
        save(getPlayerManager());
        Intent intent = new Intent(PlatformerHiddenActivity.this, PlatformerMainActivity.class);
        intent.putExtra("Player Name", player.getName());
        intent.putExtra("Character", i.getSerializableExtra("Character"));
        intent.putExtra("Platforms", i.getSerializableExtra("Platforms"));
        intent.putExtra("Score", i.getSerializableExtra("Score"));
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


