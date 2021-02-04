package com.example.dungeonescape.platformer.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;

import com.example.dungeonescape.game.GameView;
import com.example.dungeonescape.platformer.OnCustomEventListener;
import com.example.dungeonescape.platformer.entities.PlatformerManager;
import com.example.dungeonescape.player.Player;

/**
 * This LevelView abstract class is responsible for drawing out the game objects of the level,
 * as well as updating player state.
 */
abstract public class LevelView extends GameView implements Runnable {
    /**
     * The platformer manager.
     */
    private PlatformerManager manager;
    /**
     * The size of the screen
     */
    private Point size;
    /**
     * The listeners for when the player ends the level or loses the level.
     */
    OnCustomEventListener endGameListener;
    OnCustomEventListener finishLevelListener;

    public LevelView(Context context, AttributeSet attrs, String mode) {

        super(context, attrs);
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        if (mode.equals("Regular")) {
            manager = new PlatformerManager(size.y, size.x);
        }
        else {
            manager = new PlatformerManager(size.y, size.x, 20);
        }
        setFocusable(true);
        setZOrderOnTop(true);
    }

    public void draw() {
        if (holder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.WHITE);
            manager.draw(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }
    /**
     * @return the manager.
     */
    public PlatformerManager getManager() {
        return manager;
    }
    /**
     * Sets the manager.
     */
    void setManager(PlatformerManager manager) {
        this.manager = manager;
    }
    /**
     * @return the size of the screen.
     */
    Point getSize() {
        return size;
    }
    /**
     * An abstract method update.
     */
    public abstract void update();

    /**
     * Executes when the player has lost a life, but has not lost all lives.
     */
    public void gameOver(Player player) {
        manager = new PlatformerManager(size.y, size.x);
        manager.setPlayer(player);
        holder = getHolder();
        setFocusable(true);
        setZOrderOnTop(true);

    }
    /**
     * Set the listener for when the player loses all lives.
     */
    public void setEndGameListener(OnCustomEventListener eventListener) {
        this.endGameListener = eventListener;
    }




}
