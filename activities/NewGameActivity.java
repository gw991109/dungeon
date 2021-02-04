package com.example.dungeonescape.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dungeonescape.player.Player;
import com.example.dungeonescape.R;
import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

/**
 * The NewGameActivity for creating a new game and player name.
 *
 */
public class NewGameActivity extends GeneralGameActivity {

    private Player player;
    private EditText name;
    private String nameText;
    private Boolean isValid;

    /** For New Game choices. */
    private List<TextView> playerNameData = new ArrayList<>();
    private List<TextView> gameDifficulties = new ArrayList<>();
    private List<TextView> playerColourChoices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        load();
        isValid = false;
        name = findViewById(R.id.nameInput);
        configureActionButtons();
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
            Intent intent = new Intent(NewGameActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }

    /** Creates all Action Button events. */
    private void configureActionButtons() {
        createPlayerChoiceLists();
        configureEnterPlayerNameButton();
        configureEnterGameButton();
    }

    /** Creates and populates all Player Choice lists. */
    private void createPlayerChoiceLists() {
        setPlayerNameData(createPlayerNameData());
        setGameDifficulties(createDifficultyButtons());
        setPlayerColourChoices(createPlayerColourChoices());
    }

    /** Creates the Button event for Player Name Input.
     * Initializes Welcome Message with Colour prompt and Difficulty prompt.
     * */
    private void configureEnterPlayerNameButton() {
        Button enterName = findViewById(R.id.checkName);
        enterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkName();
                if (isValid){
                    player = new Player(nameText);
                    getPlayerManager().addPlayer(player);
                    setListVisibility(playerNameData, View.INVISIBLE);
                    createWelcomeMessage();
                    createColourPrompt();
                }
            }
        });
    }

    /** Creates the Button event for the Player to start the Game. */
    private void configureEnterGameButton() {
        Button enterGame = findViewById(R.id.enterGame);
        enterGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid) {
                    Intent intent = new Intent(NewGameActivity.this, HomeScreenActivity.class);
                    intent.putExtra("Player Name", player.getName());
                    startActivity(intent);
                }
            }
        });
    }

    /** Sets all Buttons in the given buttonList to the specified visibility.
     *
     * @param textViewList the list of Buttons to loop through & update Button visibility.
     * @param visibility the Visibility value.
     */
    private void setListVisibility(List<TextView> textViewList, int visibility) {
        for (TextView item: textViewList) {
            item.setVisibility(visibility);
        }
    }

    /** Sets the playerNameData list to the new inputted list.
     *
     * @param playerNameData the new list of Player Name Data (TextView) elements.
     */
    private void setPlayerNameData(List<TextView> playerNameData) {
        this.playerNameData = playerNameData;
    }

    /** Creates the Player Name Data for the Game. The TextView elements match up with the
     * corresponding ID from the activity_new_game.xml file.
     *
     * @return a List of TextView elements representing Player Name Data.
     */
    private List<TextView> createPlayerNameData() {
        List<TextView> nameData = new ArrayList<>();

        nameData.add((TextView) findViewById(R.id.newGameText));
        nameData.add((EditText) findViewById(R.id.nameInput));
        nameData.add((Button) findViewById(R.id.checkName));
        nameData.add((TextView) findViewById(R.id.enterNameText));

        return nameData;
    }

    /** Sets the gameDifficulties list to the new inputted list.
     *
     * @param gameDifficulties the new list of Game Difficulty (TextView) Buttons.
     */
    private void setGameDifficulties(List<TextView> gameDifficulties) {
        this.gameDifficulties = gameDifficulties;
    }

    /** Creates the difficulty buttons for the Game. The buttons match up with the corresponding ID
     * from the activity_new_game.xml file.
     *
     * @return a list of Game Difficulty Buttons.
     */
    private List<TextView> createDifficultyButtons() {
        final Button enter = findViewById(R.id.enterGame);
        List<TextView> difficultyButtons = new ArrayList<>();

        Button easyButton = findViewById(R.id.easy);
        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid) {
                    player.setGameDifficulty("Easy");
                    save(getPlayerManager());
                    enter.setVisibility(View.VISIBLE);
                }

            }
        });
        difficultyButtons.add(easyButton);

        Button hardButton = findViewById(R.id.hard);
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid) {
                    player.setGameDifficulty("Hard");
                    System.out.println(player.getNumLives());
                    save(getPlayerManager());
                    enter.setVisibility(View.VISIBLE);
                }

            }
        });
        difficultyButtons.add(hardButton);

        return difficultyButtons;
    }

    /** Sets the playerColourChoices list to the new inputted list.
     *
     * @param playerColourChoices the new list of Player Colour Choice (TextView) Buttons.
     */
    private void setPlayerColourChoices(List<TextView> playerColourChoices) {
        this.playerColourChoices = playerColourChoices;
    }

    /** Creates the Player Colour Choices. The Buttons match up with the corresponding ID from the
     * activity_new_game.xml file.
     *
     * @return a list of Player Colour Choice Buttons.
     */
    private List<TextView> createPlayerColourChoices() {
        final TextView diffPrompt = findViewById(R.id.diffPrompt);
        Button redPlayer = findViewById(R.id.colour1);
        Button greenPlayer = findViewById(R.id.colour2);
        Button yellowPlayer = findViewById(R.id.colour3);

        List<TextView> colourChoices = new ArrayList<>();

        redPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid) {
                    int color = Color.argb(255, 173, 0, 0);
                    player.setColour(color);
                    save(getPlayerManager());
                    diffPrompt.setText(("Choose Difficulty Level:"));
                    setListVisibility(gameDifficulties, View.VISIBLE);
                }
            }
        });
        colourChoices.add(redPlayer);

        greenPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid) {
                    int color = Color.argb(255, 76, 175, 80);
                    player.setColour(color);
                    save(getPlayerManager());
                    diffPrompt.setText(("Choose Difficulty Level:"));
                    setListVisibility(gameDifficulties, View.VISIBLE);
                }
            }
        });
        colourChoices.add(greenPlayer);


        yellowPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid) {
                    int color = Color.argb(255, 255, 193, 7);
                    player.setColour(color);
                    save(getPlayerManager());
                    diffPrompt.setText(("Choose Difficulty Level:"));
                    setListVisibility(gameDifficulties, View.VISIBLE);
                }
            }
        });
        colourChoices.add(yellowPlayer);

        return colourChoices;
    }

    /** Creates the Welcome Message for the Player. */
    private void createWelcomeMessage() {
        TextView welcomeDisplay = findViewById(R.id.welcomeDisplay);
        String welcomeMessage = "Welcome " + nameText;
        welcomeDisplay.setText(welcomeMessage);
        welcomeDisplay.setVisibility(View.VISIBLE);
    }

    /** Creates the Player Colour Choice prompt and buttons. */
    private void createColourPrompt() {
        TextView colorPrompt = findViewById(R.id.colorPromptText);
        colorPrompt.setText(("Select Character Colour:"));
        setListVisibility(playerColourChoices, View.VISIBLE);
    }

    private void checkName() {
        nameText = name.getText().toString();
        isValid = true;
        List<String> allPlayerNames = getPlayerManager().getPlayerNames();

        if (isEmpty(nameText)) {
            Toast t = Toast.makeText(this, "Please Enter a Name", Toast.LENGTH_SHORT);
            isValid = false;
            t.show();
        } else if (allPlayerNames.contains(nameText)) {
            Toast t = Toast.makeText(this, "There is already a player saved with " +
                    "this name", Toast.LENGTH_SHORT);
            isValid = false;
            t.show();
        }
    }

}
