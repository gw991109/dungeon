package com.example.dungeonescape.brickbreaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.dungeonescape.activities.MainActivity;
import com.example.dungeonescape.maze.MazeInstructionsActivity;
import com.example.dungeonescape.player.PlayerManager;
import com.example.dungeonescape.activities.GeneralGameActivity;
import com.example.dungeonescape.player.Player;
import com.example.dungeonescape.R;
import com.example.dungeonescape.activities.DeadActivity;

import android.os.SystemClock;

import java.util.Map;

/**
 * The main activity of the game (entry point).
 */
public class BBMainActivity extends GeneralGameActivity {
    /**
     * The game's view that updates and draws the objects within it.
     */
    BBView gameView;
    boolean running;
    Player player;
    /**
     * The time at which the brick breaker game has been started.
     */
    long startTime;


    /**
     *
     * @param savedInstanceState Bundle object that passes data between activities.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startTime = SystemClock.elapsedRealtime();  // starts the clock
        setContentView(R.layout.activity_brick_breaker_main);   // Set the View we are using
        gameView = findViewById(R.id.BBView2);

        setTitle("Level1: Brick Breaker");
        /* Gather saved data. */
        load();
        Intent i = getIntent();
        String name = (String) i.getSerializableExtra("Player Name");
        player = getPlayerManager().getPlayer(name);

        gameView.manager.addPlayer(player);

        Button nextButton = findViewById(R.id.nextlvl);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.setCurrentLevel(2);
                save(getPlayerManager());
                Intent intent = new Intent(BBMainActivity.this, MazeInstructionsActivity.class);
                intent.putExtra("Player Name", player.getName());
                startActivity(intent);
            }
        });

        configureSatchelButton();

        running = true;

        // Thread code is from the following Youtube Video, however, body of run() is original.
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
                                    // Update the score shown
                                    int score = player.getNumCoins();
                                    String scr = String.valueOf(score) ;
                                    String scre = "Coins: " + scr;

                                    TextView score1 = findViewById(R.id.score);

                                    score1.setText(scre);
                                    int lives = player.getNumLives();
                                    String life = "Lives: " + lives;
                                    TextView lifeText = findViewById(R.id.lives);
                                    lifeText.setText(life);
                                    boolean doneLevel = gameView.doneLevel();
                                    if (doneLevel) {
                                        nextLevel();
                                        running = false;
                                    }
                                    if (player.getNumLives() == 0){
                                        endGame();
                                        running = false;
                                    }
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
            save(getPlayerManager());
            Intent intent = new Intent(BBMainActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    /**
     * Method executes when the player starts the game.
     */
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    /**
     * Method executes when the player quits the game.
     */
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    /**
     * User has successfully finished Brick Breaker and will now move on to Maze.
     */
    protected void nextLevel(){
        player.setCurrentLevel(2);
        // time at which the user has finished the level.
        long endTime = SystemClock.elapsedRealtime();
        long elapsedMilliSeconds = endTime - startTime;
        player.updateTotalTime(elapsedMilliSeconds);
        save(getPlayerManager());
        //Intent intent = new Intent(BBMainActivity.this, MazeActivity.class);
        Intent intent = new Intent(BBMainActivity.this, MazeInstructionsActivity.class);
        intent.putExtra("Player Name", player.getName());
        startActivity(intent);
    }

    /**
     * User has lost the Game i.e. no more lives left.
     */
    protected void endGame(){
        // time at which the user has lost.
        long endTime = SystemClock.elapsedRealtime();
        long elapsedMilliSeconds = endTime - startTime;
        player.updateTotalTime(elapsedMilliSeconds);
        Intent intent = new Intent(BBMainActivity.this, DeadActivity.class);
        save(getPlayerManager());
        intent.putExtra("Player Name", player.getName());
        startActivity(intent);
    }

    @Override
    public void save(PlayerManager playerManager) {
        super.save(playerManager);
        player.setCurrentLevel(2);
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
