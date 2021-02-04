package com.example.dungeonescape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.dungeonescape.player.PlayerManager;
import com.example.dungeonescape.R;

/**
 * The MainActivity for the first screen of the game.
 *
 */
public class MainActivity extends GeneralGameActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
        if (getPlayerManager() == null) {
            setPlayerManager(new PlayerManager());
        }
        configureActionButtons();
    }

    private void configureActionButtons() {
        configureNewGameButton();
        configureLoadGameButton();
    }

    private void configureNewGameButton() {
        Button newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void configureLoadGameButton() {
        Button loadGame = (Button) findViewById(R.id.loadGame);
        loadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoadGameActivity.class);
                startActivity(intent);
            }
        });
    }

}
