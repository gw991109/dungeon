package com.example.dungeonescape.maze;

import android.graphics.Canvas;

import com.example.dungeonescape.game.Drawable;
import com.example.dungeonescape.game.GameData;
import com.example.dungeonescape.game.GameObject;
import com.example.dungeonescape.game.RetrieveData;

class Sprite extends GameObject implements Drawable, RetrieveData {
    private MazeData mazeData;

    Sprite(){}

    /** Draws the PlayerSprite square on the screen.
     *
     * @param canvas the Canvas to draw the PlayerSprite on. */
    public void draw(Canvas canvas) {
        float margin = mazeData.getCellSize() / 10;
        canvas.drawRect(
                getX() * mazeData.getCellSize() + margin,
                getY() * mazeData.getCellSize() + margin,
                (getX() + 1) * mazeData.getCellSize() - margin,
                (getY() + 1) * mazeData.getCellSize() - margin,
                getPaint());
    }

    @Override
    public void setGameData(GameData mazeData) {
        this.mazeData = (MazeData) mazeData;
    }
}

