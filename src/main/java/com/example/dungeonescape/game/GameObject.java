package com.example.dungeonescape.game;

import android.graphics.Paint;

/**
 * Represents any Object in the Game that contains an (x, y) coordinate.
 */
public class GameObject {
    private int x;
    private int y;
    private Paint paint;

    public GameObject() {
        setX(0);
        setY(0);
        setPaint(new Paint());
    }

    public GameObject(int x, int y) {
        setX(x);
        setY(y);
        setPaint(new Paint());
    }

    /**
     * Sets the Game Object Paint's colour.
     *
     * @param colour the RBG value for the Game Object's Paint.
     */
    public void setPaintColour(int colour) {
        paint.setColor(colour);
    }

    public int getPaintColour() {
        return paint.getColor();
    }

    /**
     * Sets the Game Object Paint's stroke width.
     *
     * @param width the stroke width of the Game Object's Paint.
     */
    public void setPaintStrokeWidth(int width) {
        paint.setStrokeWidth(width);
    }

    /**
     * Sets the Game Object's Paint.
     *
     * @param paint the Paint of the Game Object.
     */
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getX() {
        return x;
    }

    /**
     * Sets the Game Object's horizontal position to the given value.
     *
     * @param x the x-coordinate of the game object's location.
     */
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    /**
     * Sets the Game Object's vertical position to the given value.
     *
     * @param y the y-coordinate of the game object's location.
     */
    public void setY(int y) {
        this.y = y;
    }

    protected void incY(int y)  {
        this.y += y;
    }
    protected void incX(int x)  {
        this.x += x;
    }
}
