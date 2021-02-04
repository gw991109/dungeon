package com.example.dungeonescape.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/*
GameView was structured like the following game:
http://gamecodeschool.com/android/building-a-simple-game-engine/
 */

/**
 * Class that controls the logic of the game by handling collisions, updating the objects' stats and
 * drawing the new states onto the canvas.
 */
public abstract class GameView extends SurfaceView implements Runnable{
    /**
     * gameThread - the main game thread that gets executed by the program.
     * holder - contains the canvas on which objects are drawn.
     * playing - indicates whether the game is running i.e. user is playing
     * paused - indicates if the game is paused; starts off as true since the user hasn't started
     *          playing yet.
     * canvas - the Canvas object onto which objects are drawn.
     * paint - the Paint object which determines the drawing style.
     */
    public Thread gameThread = null;
    public SurfaceHolder holder;
    public boolean playing;
    public boolean paused = true;
    public Canvas canvas;
    public Paint paint;

    /**
     * Initializes the surface in the context environment.
     * @param context the environment
     */
    public GameView(Context context){
        super(context);
        holder = getHolder();
        paint = new Paint();

    }

    public GameView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        holder = getHolder();
        paint = new Paint();
    }

    /**
     * The method that runs the program's while loop.
     */
    @Override
    public void run(){
        while (playing) {
            try {   // Capture the current time in milliseconds in startFrameTime
                Thread.sleep(1);    //set time in milli

            } catch (Exception e){
                e.printStackTrace();
            }

            if(!paused){    // Updating the frame
                update();
            }

            // Draw the frame
            draw();
        }
    }

    /**
     * Method updates the state of the objects within the game based on user events.
     */
    public abstract void update();

    /**
     * Method draws all the objects onto the screen.
     */
    public abstract void draw();

    /**
     * If Activity is paused/stopped, the thread must stop as well.
     */
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }

    /**
     * If Activity is started then thread must start as well.
     */
    public void resume() {
        paused = false;
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}

