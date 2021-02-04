package com.example.dungeonescape.platformer.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

/**
 * The view class that draws the hidden level in platformer.
 */
public class HiddenLevelView extends LevelView {

    public HiddenLevelView(Context context, AttributeSet attrs) {
        super(context, attrs, "Blitz");
    }
    /**
     * Updates the screen.
     */
    public void update() {
        if (getManager().update()) {
            exitHiddenLevel();
        }
    }
    /**
     * Draws the view.
     */
    @Override
    public void draw() {
        if (holder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.rgb(139,131,120));
            getManager().draw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }
    /**
     * A listener for exiting the hidden level.
     */
    public void exitHiddenLevel() {
        endGameListener.onEvent();
    }

}
