package com.example.dungeonescape.maze;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.dungeonescape.activities.MainActivity;
import com.example.dungeonescape.player.Player;
import com.example.dungeonescape.R;
import com.example.dungeonescape.activities.GeneralGameActivity;
import com.example.dungeonescape.platformer.PlatformerInstructionsActivity;
import com.example.dungeonescape.player.PlayerManager;

import java.util.Locale;
import java.util.Map;

/** Runs the Maze game. */
public class MazeActivity extends GeneralGameActivity {
    /** The MazeManager for this MazeActivity. */
    private MazeManager mazeManager;

    /** Initial time set in milliseconds. */
    private long counter = 120000; // 2 min

    /** The minutes and seconds left in the counter. */
    private long minutes;
    private long seconds;

    /** The Player of this MazeActivity. */
    private Player player;

    /** The time elapsed within this MazeActivity. */
    private long startTime;

    private boolean isGameRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        // gather Player data
        getPlayerSaveData(getIntent());

        initializeMazeManager();

        // start the countdown
        startCountDown();

        configureActionButtons();

        // starts the Time Elapsed clock
        startTime = SystemClock.elapsedRealtime();

        isGameRunning = true;
        startThread();
    }

    /** Creates the Thread that updates the display for the Player lives & coins. */
    private void startThread() {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {

                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isGameRunning) {
                                    updatePlayerScoreText();
                                    updatePlayerLivesText();
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

    /** Creates and runs the countdown to losing the game. */
    private void startCountDown() {
        final TextView countTime = findViewById(R.id.countTime);
        countTime.setTextColor(Color.BLACK);

        /* Countdown code from: https://www.tutorialspoint.com/how-to-make-a-countdown-timer-in-android
          Partially edited.
         */
        new CountDownTimer(counter, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                counter = millisUntilFinished;
                updateCountDown();
                countTime.setText(String.format(Locale.getDefault(),
                        "Time Left: %02d:%02d", minutes, seconds));
                counter--;
            }

            @Override
            public void onFinish() {
                isGameRunning = false;
                // Player loses a life when the countdown runs out
                player.loseLife();
                int playerLivesLeft = player.getNumLives();

                updatePlayerLivesText();

                // Change UI to Lose Life screen
                setContentView(R.layout.activity_maze_lose_life);
                TextView textView = findViewById(R.id.playerLives);
                textView.setText(String.format(Locale.getDefault(),
                        "You have %d lives left.", playerLivesLeft));
                configureStartOverButton();
            }
        }.start();
    }

    /** Updates the display of the Player's score. */
    private void updatePlayerScoreText() {
        int playerNumCoins = player.getNumCoins();
        String playerScoreStr = "Score: " + playerNumCoins;
        TextView playerScore = findViewById(R.id.score);
        playerScore.setText(playerScoreStr);
    }

    /** Updates the display of the number of lives the Player has. */
    private void updatePlayerLivesText() {
        int playerNumLives = player.getNumLives();
        String life = "Lives: " + playerNumLives;
        TextView playerLives = findViewById(R.id.lives);
        playerLives.setText(life);
    }

    /** Assigns variables player and playerManager to data from the specified Intent.
     *
     * @param intent the Intent to gather data from.
     */
    private void getPlayerSaveData(Intent intent) {
        load();
        String name = (String) intent.getSerializableExtra("Player Name");
        player = getPlayerManager().getPlayer(name);
    }

    /** Initializes the MazeManager for this Maze game. */
    private void initializeMazeManager() {
        MazeView mazeView = findViewById(R.id.view);
        mazeManager = new MazeManager(mazeView, player);
        mazeManager.relocatePlayerSprite();
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
            Intent intent = new Intent(MazeActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    /** Converts milliseconds to minutes and seconds.
     * Calculation from:
     * https://codinginflow.com/tutorials/android/countdowntimer/part-2-configuration-changes
     */
    private void updateCountDown() {
        minutes = (counter / 1000) / 60;
        seconds = (counter / 1000) % 60;
    }

    /** Initializes Buttons for this Maze. Note: does not include movement buttons. */
    private void configureActionButtons() {
        configureNextButton();
        configureSatchelButton();
    }

    /** Initializes the NextLevel Button. Note: testing & demo purposes. */
    private void configureNextButton() {
        Button nextButton = findViewById(R.id.nextlvl);
        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                save(getPlayerManager());
                Intent intent = new Intent(MazeActivity.this,
                        PlatformerInstructionsActivity.class);
                intent.putExtra("Player Name", player.getName());
                startActivity(intent);
            }
        });
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

    /** Creates the Button to play the Maze Game from the beginning. */
    private void configureStartOverButton() {
        Button startOver = findViewById(R.id.startOver);
        startOver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                save(getPlayerManager());
                Intent intent = new Intent(MazeActivity.this, MazeActivity.class);
                intent.putExtra("Player Name", player.getName());
                startActivity(intent);
            }
        });
    }

    /** Creates the AlertDialog that displays the contents of the Player's Satchel. */
    private void openSatchel() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage(getPlayerSatchelData());
        dialogBuilder.setCancelable(true);

        dialogBuilder.setNeutralButton(
                "Return to Game",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
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

    /** Moves the Player up by one MazeCell. */
    public void movePlayerUp(View view) {
        mazeManager.movePlayerSprite("UP");
        goToNextLevel(view);
    }

    /** Moves the Player down by one MazeCell. */
    public void movePlayerDown(View view) {
        mazeManager.movePlayerSprite("DOWN");
        goToNextLevel(view);
    }

    /** Moves the Player left by one MazeCell. */
    public void movePlayerLeft(View view) {
        mazeManager.movePlayerSprite("LEFT");
        goToNextLevel(view);
    }

    /** Moves the Player right by one MazeCell. */
    public void movePlayerRight(View view) {
        mazeManager.movePlayerSprite("RIGHT");
        goToNextLevel(view);
    }

    /** Checks if the Maze has been completed 3 times. Go to next level if true. */
    public void goToNextLevel(View view) {
        if (mazeManager.hasCompletedLevel())
            nextLevel();
    }

    /** Send the Player to the next level of the game. */
    protected void nextLevel() {
        // The time at which the user has finished the level
        long elapsedMilliSeconds = SystemClock.elapsedRealtime() - startTime;
        player.updateTotalTime(elapsedMilliSeconds);

        save(getPlayerManager());

        Intent intent = new Intent(this, PlatformerInstructionsActivity.class);
        intent.putExtra("Player Name", player.getName());
        startActivity(intent);
    }

    /**
     * set the level of the player to level 3 so it's recorded that the player has passed the maze
     * level.
     * @param playerManager the playerManager instance sotring the players.
     */
    @Override
    public void save(PlayerManager playerManager) {
        player.setCurrentLevel(3);
        super.save(playerManager);
    }
}
