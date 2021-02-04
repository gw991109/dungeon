package com.example.dungeonescape.maze;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.dungeonescape.activities.GeneralGameActivity;
import com.example.dungeonescape.activities.MainActivity;
import com.example.dungeonescape.player.Player;
import com.example.dungeonescape.R;

/**
 * Creates an activity to display before a player enters the Maze level.
 *
 * This activity will give instructions to players on how to play the game, and what the objectives
 * of the level are in order to help them complete it.
 */
public class MazeInstructionsActivity extends GeneralGameActivity {

    /* The activity needs a Player and a PlayerManager attribute, but the level does not use them
     * for any specific purpose besides storing them and then sending them to the next activity,
     * which is MazeActivity, who then does use the two instances. */
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze_instructions);
        setTitle("Level2: Maze");

        /* Gather saved data. */
        load();
        Intent i = getIntent();
        String name = (String) i.getSerializableExtra("Player Name");
        player = getPlayerManager().getPlayer(name);

        /* Button to start the Maze. */
        configureNextButton();
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
            Intent intent = new Intent(MazeInstructionsActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    /** Creates and configures the Start button to play the Maze game following the instructions
     * page.
     */
    private void configureNextButton() {
        Button nextButton = findViewById(R.id.startGame);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        MazeInstructionsActivity.this, MazeActivity.class);
                intent.putExtra("Player Name", player.getName());
                startActivity(intent);
            }
        });
    }
}
