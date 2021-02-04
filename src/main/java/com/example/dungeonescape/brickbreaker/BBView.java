package com.example.dungeonescape.brickbreaker;

import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Display;
import android.graphics.Point;
import android.app.Activity;
import com.example.dungeonescape.game.GameView;
import com.example.dungeonescape.game.collectable.Blitz;
/**
 * BBMainActivity and BBView were structured like the following game:
 * http://gamecodeschool.com/android/building-a-simple-game-engine/

 * Class that controls the logic of the game by handling collisions, updating the objects' stats and
 * drawing the new states onto the canvas.
 */
public class BBView extends GameView {
    /**
     * screenX - the width of the screen.
     * screenY - the height of the screen.
     * startGame - player has started playing game.
     * manager - the manager class that receives information from the view and relays it to the
     * game objects.
     * blitzStartTime - time when the player activated hidden level.
     */
    int screenX;
    int screenY;
    boolean startGame = false;
    BBGameManager manager;
    long blitzStartTime;
    public BBView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y - 350;    // 350 accounts for the buttons above the playing screen.
        manager = new BBGameManager(screenX, screenY);

    }

    /**
     * Initializes the surface in the context environment.
     * @param context the environment.
     */
    public BBView(Context context) {
        super(context);
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        this.screenX = size.x;
        this.screenY = size.y - 350;
        manager = new BBGameManager(screenX, screenY);
    }

    /** Updates the movement of the ball and paddle while the game is playing. */
    public void update() {
        if (startGame) {
            manager.moveBall();
            manager.movePaddle();
            if (manager.checkLifeCondition()) {
                startGame = false;
            }
            Blitz blitz = manager.getBlitz();
            /* Blitz item got collected and the hidden level got activated */
            if (blitz.getBlitzMode().equals("notStarted") && !blitz.getAvailableStatus()){
                blitz.setBlitzMode("started");
                manager.initializeBlitzCoins();
                blitzStartTime = SystemClock.elapsedRealtime();
            }
            long currTime = SystemClock.elapsedRealtime();
            /* 30 seconds have passed so blitz mode ends. */
            if ((int) ((currTime - blitzStartTime) / 1000)  == 30){
                blitz.setBlitzMode("done");
                manager.endBlitzGame();
            }

        }
    }

    /** Loads and draws objects on the canvas. */
    public void draw() {
        /* Make sure the drawing surface is valid or program crashes. */
        if (holder.getSurface().isValid()) {
            /* Lock the canvas ready to draw. */
            canvas = holder.lockCanvas();

            /* Draw the background color – black. */
            canvas.drawColor(Color.argb(255,  0, 0, 0));

            if (manager.getBlitz().getBlitzMode().equals("started")){
                manager.drawBlitzGame(canvas);
            }else{
                manager.drawGame(canvas);
            }

            /* Draws everything to the screen. */
            holder.unlockCanvasAndPost(canvas);
        }
    }

    /** Dictates the movement of the paddle based on touch. */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!startGame){
                startGame = true;
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            manager.setPaddleDirection(event, x);
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            manager.setPaddleDirection(event, x);
        }
        return super.onTouchEvent(event);
    }

    /**
     * Indicates whether the user has succeeded in passing the level.
     * @return true if user passed.
     */
    public boolean doneLevel() {
        return manager.hitAllBricks();
    }
}
