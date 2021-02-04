package com.example.dungeonescape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.example.dungeonescape.player.Player;
import com.example.dungeonescape.player.PlayerManager;
import com.example.dungeonescape.R;
import com.example.dungeonescape.brickbreaker.BBMainActivity;


/**
 * The DeadActivity that shows up once the player has lost all of their lives
 *
 */
public class DeadActivity extends GeneralGameActivity {


    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dead);
        //* Gather saved data. */
        load();
        Intent i = getIntent();
        String name = (String) i.getSerializableExtra("Player Name");
        player = getPlayerManager().getPlayer(name);

        configureActionButtons();
        player.resetStats();
        save(getPlayerManager());
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
            Intent intent = new Intent(DeadActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void configureActionButtons() {
        configurePlayAgainButton();
        configureMenuButton();
    }

    private void configurePlayAgainButton() {
        Button playAgain = findViewById(R.id.playAgain);
        playAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                save(getPlayerManager());
                Intent intent = new Intent(DeadActivity.this, BBMainActivity.class);
                intent.putExtra("Player Name", player.getName());
                startActivity(intent);
            }
        });
    }

    private void configureMenuButton() {
        Button mainMenu = findViewById(R.id.mainMenu);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeadActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void save(PlayerManager playerManager) {
        super.save(playerManager);
    }

}
