package com.example.dungeonescape.platformer.entities;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.dungeonescape.game.Drawable;
import com.example.dungeonescape.game.GameObject;

/**
 * An abstract class for all platformer objects.
 */
abstract public class PlatformerObject extends GameObject implements Drawable {
    /**
     * The size of the object.
     */
    private int size;
    /**
     * The rect shape of the object.
     */
    private Rect shape;
    /**
     * The gridHeight for the canvas.
     */
    private int gridHeight;
    /**
     * The gridWidth for the canvas.
     */
    private int gridWidth;


    PlatformerObject(int x, int y, int size, PlatformerManager manager) {
        super(x, y);
        this.size = size;
        gridHeight = manager.getGridHeight();
        gridWidth = manager.getGridWidth();
    }

    /**
     * @return size of the object
     */
    int getSize() {
        return this.size;
    }

    /**
     * Sets the rectangle for this object
     */
    void setShape() {
        this.shape = new Rect(getX() - size / 2, getY() - size / 2,
                getX() + size / 2, getY() + size / 2);
    }
    /**
     * @param length the length of the shape.
     * @param width the length of the shape.
     * Sets the rectangle for this object with defined length and width.
     */
    void setShape(int length, int width) {
        this.shape = new Rect(getX(), getY(), length + getX(),
                getY()+ width);
    }
    /**
     * @return shape of object.
     */
    Rect getShape() {
        return this.shape;
    }

    /**
     * Draws this item to canvas.
     */
    @Override
    public void draw(Canvas canvas) {
        if (this instanceof Platform) {
            canvas.drawRect(getShape(), getPaint());
        }
        else {
            canvas.drawOval(getX() - size / 2, getY() - size / 2, getX() + size / 2,
                    getY() + size / 2, getPaint());
        }
    }
    abstract void update(int down);


    int getGridHeight() {
        return gridHeight;
    }

    int getGridWidth() {
        return gridWidth;
    }
}

