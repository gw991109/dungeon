package com.example.dungeonescape.platformer.entities;

import android.graphics.Color;
import java.lang.Math;
import java.util.Random;

/**
 * A Platform class that represents the platforms the character jumps on.
 */
class Platform extends PlatformerObject {

    /** length of the platforms. */
    private int length;

    /** width of the platforms. */
    private int width;


    Platform(int x, int y, int length, int width, PlatformerManager manager) {
        super(x,y,0, manager);
        this.length = length;
        this.width = width;
        getPaint().setColor(Color.rgb(130,199,169));
        setShape(this.length, this.width);
    }
    /** Updates the platforms, moving the platforms down when the ball surpasses them. */
    void update(int down) {
        // Moves platforms down
        platformDown(down);
        setShape(this.length, this.width);
    }


    /** Moves the platforms down, i.e when the ball surpasses the platforms they get moved to their
     * new location above the character. */
    private void platformDown(int down) {
        if (getY() + down >= getGridHeight()) {
            // Move it to the top
            int diff = Math.abs((int)getY() + down - getGridHeight());
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
            int a = r.nextInt(getGridWidth() - 150);
            this.setX(a);
        }
        else {
            incY(down);
        }
    }

}


