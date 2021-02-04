package com.example.dungeonescape.game.collectable;

import com.example.dungeonescape.game.Drawable;
import com.example.dungeonescape.game.GameObject;
import com.example.dungeonescape.player.Player;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import java.io.Serializable;
import java.util.Random;

/**
 *  Class that creates a coin that a user can collect to increase score.
 */
public class Coin extends GameObject implements Collectable, Drawable, Serializable {


    /** The shape of the coin. */
    private Rect coinShape;

    /** The available status of the coin */
    private Boolean available;

    /** The radius of the coin. */
    private int coinRadius;

    public Coin(int x, int y, int coinRadius) {
        super(x, y);
        setPaintColour(Color.YELLOW);
        this.available = true;
        this.coinRadius = coinRadius;
        this.coinShape = new Rect(x, y, x + coinRadius, y + coinRadius);
    }

    private void updateCoinLocation() {
        this.coinShape.top = getY();
        this.coinShape.right = getX() + coinRadius;
        this.coinShape.bottom = getY() + coinRadius;
        this.coinShape.left = getX();
    }

    public void gotCollectable() {
        setY(0);
        Random r = new Random();
        setX(r.nextInt(1080 - 150));
        updateCoinLocation();
    }
    public Rect getItemShape() {
        return coinShape;
    }

    /** Moves the coin down when the Character jumps up. */
    public void update(int down, int height) {

        if (getY() + down > height) {
            /* Moves coin up if the Character moves down without collection the PlatformerCoin. */
            int diff = Math.abs(getY() + down - height);
            if (diff > 400) {
                setY(0);
            }
            else if (diff > 200) {
                setY(-200);
            }
            else {
                setY(-diff);
            }
            Random r = new Random();
            int a = r.nextInt(height - 150);
            this.setX(a);
        }
        else {
            incY(down);
        }
        updateCoinLocation();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(this.getX(), this.getY(), coinRadius, this.getPaint());
    }

    /**
     * add this coin to a player's backpack.
     * @param player the player who's backpack we're adding this coin to.
     */
    public void collect(Player player) {
        player.addToSatchel(this);
    }

    /**
     * return whether this coin is up for collection or not.
     */
    public Boolean getAvailableStatus() {
        return this.available;
    }

    /**
     * update the status of the coin to collected.
     */
    public void gotCollected() {
        this.available = false;
    }

}
