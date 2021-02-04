package com.example.dungeonescape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dungeonescape.player.Player;
import com.example.dungeonescape.R;

import java.util.List;

/**
 * The LoadGame screen that shows for loading users.
 *
 */
public class LoadGameActivity extends GeneralGameActivity {
    private Player player;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);
        /* Gather saved data. */
        load();
        backButton();
        if (getPlayerManager().getPlayerNames().size() == 0) {
            Toast t = Toast.makeText(this, "No players have been saved",
                    Toast.LENGTH_LONG);
            t.show();
        }
        else {
            setSpinner();
            configureActionButtons();
        }
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
            Intent intent = new Intent(LoadGameActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    private void setSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner);
        List<String> names = getPlayerManager().getPlayerNames();
        String[] arr = names.toArray(new String[names.size()]);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(LoadGameActivity.this,
                android.R.layout.simple_list_item_1, arr);
        myAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(myAdapter);
    }

    private void configureActionButtons() {
        configureEnterGameButton();
    }

    private void configureEnterGameButton() {
        Button enterGame = findViewById(R.id.enterGame);
        enterGame.setVisibility(View.VISIBLE);
        enterGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String playerName = spinner.getSelectedItem().toString();
                player = getPlayerManager().getPlayer(playerName);
                Intent intent = new Intent(LoadGameActivity.this, HomeScreenActivity.class);
                intent.putExtra("Player Name", player.getName());
                startActivity(intent);
            }
        });
    }

    private void backButton() {
        Button back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoadGameActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
