package com.example.dungeonescape.game.collectable;

import com.example.dungeonescape.game.GameData;
import com.example.dungeonescape.game.GameObject;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Path;
import com.example.dungeonescape.game.Drawable;
import com.example.dungeonescape.game.RetrieveData;
import com.example.dungeonescape.player.Player;
import java.io.Serializable;
import java.util.Random;


/**
 * Creates a potion that the player can collect to gain an extra life.
 */
public class Potion extends GameObject implements Collectable, Drawable, RetrieveData, Serializable {
    /**
     * available - whether the potion is available for the user to collect.
     * potionShape - Rect representation of the potion.
     */

    private Boolean available;
    private Rect potionShape;
    /** The size of the potion. */
    private int size;
    private GameData gameData;

    /**
     * Creates a potion item.
     * @param x x coordinate of the top left corner of the potion.
     * @param y y coordinate of the top left corner of the potion.
     * @param size size of the potion.
     */
    public Potion(int x, int y, int size) {
        super(x, y);
        available = true;
        this.size = size;
        setPaintColour(Color.RED);
        potionShape = new Rect(x, y, x + size, y + size);
    }
    /** Draws the Potion onto canvas */
    @Override
    public void draw(Canvas canvas) {
        int width = potionShape.width();
        int x = getX();
        int y = getY();

        Path path = new Path();

        path.moveTo(x + width/3, y + width/3);
        path.lineTo(x + width/3, y);
        path.lineTo(x + (width * 2/3), y);
        path.lineTo(x + (width * 2/3), y + width/3);
        path.lineTo(x + width, y + width/3);
        path.lineTo(x + (width * 2/3), y + width);
        path.lineTo(x + width/3, y + width);
        path.lineTo(x, y + width/3);
        path.lineTo(x + width/3, y + width/3);
        path.close();

        canvas.drawPath(path, getPaint());
    }

    /** Moves the Gem down when the Character jumps up. */
    public void update(int down, int height) {
        if (getY() + down > height) {
            /* Moves coin up if the Character moves down without collection the PlatformerCoin. */
            setY(-700);
            Random r = new Random();
            int a = r.nextInt(height - 150);
            this.setX(a);
        } else {
            incY(down);
        }
        updatePotionLocation();
    }
    /** Adds the collectable to the player's satchel. */
    @Override
    public void collect(Player player) {
        player.addToSatchel(this);
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }
    /** Updates the potion location */
    private void updatePotionLocation() {
        this.potionShape.top = getY();
        this.potionShape.right = getX() + size;
        this.potionShape.bottom = getY() + size;
        this.potionShape.left = getX();
    }
    /** Updates the potion location when collected*/
    public void gotCollectable() {
        setY(-800);
        Random r = new Random();
        setX(r.nextInt(1080 - 150));
        updatePotionLocation();
    }
    @Override
    public Boolean getAvailableStatus() {
        return this.available;
    }

    @Override
    public void gotCollected() {
        this.available = false;
    }

    @Override
    public Rect getItemShape(){
        return potionShape;
    }
}
