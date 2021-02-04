package com.example.dungeonescape.platformer.entities;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

/**
 * A portal class that represents the portal that the character enters to go to the hidden level.
 */
class Portal extends PlatformerObject {

    /**
     * Represents drawable portal.
     */
    private final Drawable portal;

    Portal(int x, int y, PlatformerManager manager, Drawable drawable) {
        super(x,y,200, manager);
        portal = drawable;
        setShape();
    }

    public void draw(Canvas canvas) {
        portal.setBounds(getX() - 100, getY() - 100, getX() + 100, getY() + 100);
        portal.draw(canvas);
    }
    /**
     * Moves the portal when the character moves up.
     */
    void update(int down) {
        incY(down);
        this.setShape();
    }

}
