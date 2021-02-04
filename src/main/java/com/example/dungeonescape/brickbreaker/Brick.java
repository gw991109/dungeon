package com.example.dungeonescape.brickbreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.dungeonescape.game.Drawable;
import com.example.dungeonescape.game.GameObject;
import com.example.dungeonescape.game.collectable.Collectable;

class Brick extends GameObject implements Drawable {
    /**
     * w,h - width and height of the brick, respectively.
     * hit - whether the brick has been hit by the ball or not.
     * item - the Collectable item hidden behind the brick, null if brick doesn't have any item.
     */
    private int w, h;
    private boolean hit;
    private Collectable item;

    /**
     * Construct a brick in the game.
     * @param x x coordinate of the top left corner of the brick
     * @param y y coordinate of the top left corner of the brick
     * @param w width of the brick
     * @param h height of the brick
     */
    Brick(int x, int y, int w, int h) {
        super(x, y);
        this.w = w;
        this.h = h;
        hit = false;
    }

    /**
     * Returns the width of the brick.
     * @return integer value of the width.
     */
    int getWidth() {
        return this.w;
    }

    /**
     * Returns the height of the brick.
     * @return integer value of the height.
     */
    int getHeight() {
        return this.h;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = getPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(3);
        canvas.drawRect(getX(), getY(), getX() + w, getY() + h, paint);
    }

    /**
     * Sets the item behind the brick.
     * @param item Collectable object
     */
    void setItem(Collectable item) {
        this.item = item;
    }

    /**
     * Returns the item behind the brick.
     * @return Collectable object.
     */
    Collectable getItem() {
        return this.item;
    }

    /**
     * Method constructs a rectangle.
     * @return a rectangle representation of the brick.
     */
    Rect getRect(){
        int x = getX();
        int y = getY();
        return new Rect(x, y, x + w,  y + h);
    }

    /**
     * Method changes this.hit to true to indicate that the brick has been hit by the ball.
     */
    void changeHitStatus(){
        hit = true;
    }

    /**
     * Methods returns the brick's state, whether it has been hit or not
     * @return this.hit
     */
    boolean getHitStatus(){
        return this.hit;
    }
}