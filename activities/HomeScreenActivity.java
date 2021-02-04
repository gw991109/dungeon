package com.example.dungeonescape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.dungeonescape.R;
import com.example.dungeonescape.brickbreaker.BBInstructionsActivity;
import com.example.dungeonescape.maze.MazeInstructionsActivity;
import com.example.dungeonescape.platformer.PlatformerInstructionsActivity;
import com.example.dungeonescape.player.Player;
import java.util.Map;

/**
 * The HomeScreen for player high scores.
 *
 */
public class HomeScreenActivity extends GeneralGameActivity {
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        setTitle("Home Screen");
        /* Gather saved data. */
        Intent i = getIntent();
        load();
        String name = (String) i.getSerializableExtra("Player Name");
        player = getPlayerManager().getPlayer(name);

        TextView congratulatePlayer = findViewById(R.id.welcome);
        congratulatePlayer.setText(String.format("Welcome, %s!", player.getName()));

        Map<String, Integer> highScore = player.getHighScore();

        TextView playerLives = findViewById(R.id.numLives);
        playerLives.setText(String.format("%s", highScore.get("Lives")));

        TextView coins = findViewById(R.id.numCoins);
        coins.setText(String.format("%s", highScore.get("Coins")));


        TextView time = findViewById(R.id.numTime);
        String minutes = String.valueOf(player.totalMinutes());
        String seconds = String.valueOf(player.totalSeconds());
        String totalTime = minutes +":" + seconds;
        time.setText(totalTime);

        int level = player.getCurrentLevel();
        if (level > 1) {
            resumeButton();
        }
        newGameButton();
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
            Intent intent = new Intent(HomeScreenActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
    private void resumeButton() {

        Button enterGame = findViewById(R.id.resumeButton);
        enterGame.setVisibility(View.VISIBLE);
        enterGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress();
            }
        });
    }

    private void newGameButton() {
        Button enterGame = findViewById(R.id.newGameButton);

        enterGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.resetStats();
                save(getPlayerManager());
                Intent intent = new Intent(HomeScreenActivity.this, BBInstructionsActivity.class);
                intent.putExtra("Player Name", player.getName());
                startActivity(intent);
            }
        });
    }

    private void progress() {
        int level = player.getCurrentLevel();
        if (level == 1 || level == 0) {
            Intent intent = new Intent(HomeScreenActivity.this, BBInstructionsActivity.class);
            intent.putExtra("Player Name", player.getName());
            startActivity(intent);
        }
        else if(level == 2) {
            Intent intent = new Intent(HomeScreenActivity.this, MazeInstructionsActivity.class);
            intent.putExtra("Player Name", player.getName());
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(HomeScreenActivity.this, PlatformerInstructionsActivity.class);
            intent.putExtra("Player Name", player.getName());
            startActivity(intent);
        }
    }

}
