package com.example.dungeonescape.platformer.entities;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.example.dungeonescape.game.collectable.Coin;
import com.example.dungeonescape.game.collectable.Collectable;
import com.example.dungeonescape.game.collectable.CollectableFactory;
import com.example.dungeonescape.game.collectable.Gem;
import com.example.dungeonescape.game.collectable.Potion;
import com.example.dungeonescape.player.Player;
import java.util.Random;
import java.util.*;
import java.util.ArrayList;

/**
 * Platform manager on a screen with characters and platforms.
 */
public class PlatformerManager{

    /**
     * The width of canvas.
     */
    private int gridWidth;
    /**
     * The height of canvas.
     */
    private int gridHeight;
    /**
     * The list of platforms.
     */
    private List<Platform> platforms;
    /**
     * The character for this game.
     */
    private Character character;
    /**
     * A list of coins.
     */
    private List<Coin> coins;
    /**
     * The player user.
     */
    private Player player;
    /**
     * The portal for entry to hidden level.
     */
    private Portal portal;
    /**
     * The portal image.
     */
    private Drawable portalImage;
    /**
     * Boolean representing if character has entered portal.
     */
    private boolean enterPortal;
    /**
     * The current game mode.
     */
    private String gameMode;

    private Gem gem;
    private Potion potion;
    private CollectableFactory factory;
    private List<Collectable> collectables = new ArrayList<>();

    /**
     * A list of all platformer objects except for character.
     */
    private List<PlatformerObject> entities = new ArrayList<>();

    /**
     * Platform manager on a screen with characters and platforms.
     * @param height height of the screen.
     * @param width the width of the screen.
     */
    public PlatformerManager(int height, int width) {

        init(height, width);
        createCoins(3);
        createPlatforms(7);
        gameMode = "Regular";

    }
    /**
     * Alternate Platform manager constructor for blitz level with specific number of coins.
     */
    public PlatformerManager(int h, int w, int coins) {
        init(h, w);
        createCoins(coins);
        createPlatforms(4);
        gameMode = "Blitz";
    }
    /**
     * Initializes constructor.
     */
    private void init(int h, int w) {
        gridHeight = h - 500;
        gridWidth = w;
        character = new Character(50,1000,100, this);
        player = null;

        factory = new CollectableFactory();
        Random random = new Random();

        this.gem = (Gem) factory.getCollectable("gem",
                random.nextInt(gridWidth - 150), random.nextInt(gridHeight - 150)
                , 70);

        this.potion = (Potion) factory.getCollectable("potion",
                random.nextInt(gridWidth - 150), random.nextInt(gridHeight - 150),
                70);
        collectables.add(this.potion);
        collectables.add(this.gem);

    }
    /**
     * Creates Coins.
     */
    private void createCoins(int number) {

        coins = new ArrayList<>(number);
        for (int i = 1; i <= number; i++) {
            Random random = new Random();
            int a = random.nextInt(gridWidth - 150);
            int b = random.nextInt(gridHeight - 150);
            Collectable newCoin = factory.getCollectable("coin", a, b, 30);
            coins.add((Coin) newCoin);
            collectables.add(newCoin);
        }
    }
    /**
     * @return gets the grid width.
     * */
    int getGridWidth() {
        return gridWidth;
    }


    /**
     * @return gets the grid height.
     * */
    int getGridHeight() {
        return gridHeight;
    }
    List<Coin> getCoins() {
        return coins;
    }

    /**
     * Sets the player and his attributes
     * */
    public void setPlayer(Player player){
        this.player = player;
        character.setColour(player.getColour());
    }

    /**
     * @return the player
     * */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the character scores
     * */
    public int getCharacterScore(){
        return character.getGameScore();
    }

    /**
     * Creates platforms.
     */
    private void createPlatforms(int number) {
        platforms = new ArrayList<>(number);
        for (int i = 1; i <= number; i++) {
            Random random = new Random();
            int a = random.nextInt(gridWidth - 150);
            Platform p = new Platform(a, gridHeight*i/number, 150, 30,
                    this);
            platforms.add(p);
            entities.add(p);
        }
    }
    /**
     * Draws the canvas screen.
     */
    public void draw(Canvas canvas) {

        for (int i = 0; i < platforms.size(); i++) {
            platforms.get(i).draw(canvas);
        }
        character.draw(canvas);
        gridHeight = canvas.getHeight();
        gridWidth = canvas.getWidth();
        for (int i = 0; i < coins.size(); i++) {
            coins.get(i).draw(canvas);
        }
        if (portal != null && !gameMode.equals("Blitz")) {
            portal.draw(canvas);
        }
        gem.draw(canvas);
        potion.draw(canvas);

    }
    /**
     * @return the platform locations in a List.
     * */
    public ArrayList<List> getPlatformPositions() {
        ArrayList<List> arr = new ArrayList<>(platforms.size());

        for (int i = 0; i < platforms.size(); i++) {
            ArrayList<Integer> coordinates = new ArrayList<>();
            coordinates.add(platforms.get(i).getX());
            coordinates.add(platforms.get(i).getY());
            arr.add(coordinates);
        }
        return arr;
    }
    /**
     * Sets platforms at given locations, used when returning to normal mode.
     */
    public void setPlatforms(ArrayList<List> arr) {
        platforms = new ArrayList<>(arr.size());
        for (int i = 0; i < arr.size() - 1; i++) {
            int x = (int) arr.get(i).get(0);
            int y = (int) arr.get(i).get(1);
            Platform p = new Platform(x,y,150, 30, this);
            platforms.add(p);
            entities.add(p);
        }
        // Creates a platform right under character, so character doesn't fall right away.
        Platform p = new Platform(character.getX() - 50, character.getY() + 400,150,
                30, this);
        platforms.add(p);
        entities.add(p);
    }
    /**
     * Sets Character score and location, used when returning to normal mode.
     */
    public void setCharacter(ArrayList<Integer> characterLocation, int score) {
        this.character.setX(characterLocation.get(0));
        this.character.setY(characterLocation.get(1));
        this.character.setGameScore(score);
    }
    /**
     * @return the character's x and y location
     * */
    public ArrayList<Integer> getCharacterLocation() {
        ArrayList<Integer> lst = new ArrayList<>();
        lst.add(character.getX());
        lst.add(character.getY());
        return lst;
    }
    /**
     * Left and right buttons to move character
     */
    public void left_button() {
        character.moveLeft();
    }
    public void right_button() {
        character.moveRight();
    }

    /**
     * Updates the characters, platforms and coins.
     */
    public void setImage(Drawable drawable) {
        portalImage = drawable;
    }
    /**
     * @return the portal
     * */
    public Portal getPortal() {
        return this.portal;
    }
    /**
     * Updates the game, and returns true if character has lost a life.
     * */
    public boolean update() {

        character.move();
        collisionDetection();
        updateAll();

        if (!character.isAlive()) {
            player.loseLife();
            return true;
        }

        if (character.getGameScore() == 2) {
            createPortal();
        }
        return false;
    }

    /**
     * Creates a portal.
     */
    private void createPortal() {
        Random random = new Random();
        int a = random.nextInt(gridWidth - 150);
        portal = new Portal(a, -100, this, portalImage);
        entities.add(portal);
    }
    /**
     * Returns a boolean that indicates if the player has passed this level.
     */
    public boolean finishedLevel() {
        return (character.getGameScore() > player.getGameDifficulty()*10);

    }
    /**
     * @return if the player has entered the portal.
     * */
    public boolean enterPortal() {
        return enterPortal;

    }

    /**
     * Updates all entities.
     */
    private void updateAll() {

        if (character.getY() < 550) {
            // how much the character moves up by
            int diff = Math.abs(550 - character.getY());

            character.update(1);
            // Update all collectables
            for (int i = 0; i < collectables.size(); i++) {
                collectables.get(i).update(diff, gridHeight);
            }
            // Update all entities
            for (int i = 0; i < entities.size(); i++) {
                entities.get(i).update(diff);
            }
        }
    }

    /** Checks for all collision detection. */
    private void collisionDetection() {
        collectableDetection();
        platformDetection();
        portalDetection();
    }

    /** Checks if there's a PlatformerCoin at the same coordinate as Character. */
    private void collectableDetection() {
        for (Collectable collectable: collectables) {
            if (character.getShape().intersect(collectable.getItemShape())) {
                collectable.gotCollectable();
                if (collectable instanceof Potion) {
                    player.addLife();
                }
                else {
                    collectable.collect(player);
                }
            }
        }
    }
    /** Checks if there's a portal at the same coordinate as Character. */
    private void portalDetection() {
        if (portal != null) {
            enterPortal = character.getShape().intersect(portal.getShape());
        }
    }

    /** Checks if the Character touches a platform. */
    private void platformDetection() {
        if (character.getSpeed() > 10) {
            for (Platform platform: platforms) {
                if (character.getShape().intersect(platform.getShape())) {
                    character.bounce(platform);
                }
            }
        }
    }

    /**
     *  Returns a boolean that indicates if the player has lost all their lives.
     */
    public boolean isDead(){ return (player.getNumLives() <= 0);}

}
