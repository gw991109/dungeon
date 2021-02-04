package com.example.dungeonescape.maze;

import android.graphics.Canvas;

import com.example.dungeonescape.game.Drawable;
import com.example.dungeonescape.game.GameData;
import com.example.dungeonescape.game.GameObject;
import com.example.dungeonescape.game.RetrieveData;

/**
 * Represents a MazeCell GameObject which is a square cell in the Maze.
 */
public class MazeCell extends GameObject implements Drawable, RetrieveData {

    private MazeData mazeData;

    /** Assigns whether or not this cell as a wall on the specified side. */
    private boolean topWall = true;
    private boolean bottomWall = true;
    private boolean leftWall = true;
    private boolean rightWall = true;

    /** If this cell has been visited before. */
    private boolean visited = false;

    MazeCell(int col, int row) {
        super(col, row);
    }

    @Override
    public void draw(Canvas canvas){
        float cellSize = mazeData.getCellSize();
        if (isTopWall()) {
            canvas.drawLine(
                    getX() * cellSize,
                    getY() * cellSize,
                    (getX() + 1) * cellSize,
                    getY() * cellSize,
                    getPaint());
        }
        if (isLeftWall()) {
            canvas.drawLine(
                    getX() * cellSize,
                    getY() * cellSize,
                    getX() * cellSize,
                    (getY() + 1) * cellSize,
                    getPaint());
        }
        if (isBottomWall()) {
            canvas.drawLine(
                    getX() * cellSize,
                    (getY() + 1) * cellSize,
                    (getX() + 1) * cellSize,
                    (getY() + 1) * cellSize,
                    getPaint());
        }
        if (isRightWall()) {
            canvas.drawLine(
                    (getX() + 1) * cellSize,
                    getY() * cellSize,
                    (getX() + 1) * cellSize,
                    (getY() + 1) * cellSize,
                    getPaint());
        }
    }

    @Override
    public void setGameData(GameData mazeData) {
        this.mazeData = (MazeData) mazeData;
    }

    boolean isTopWall() {
        return topWall;
    }

    /**
     * Sets if the MazeCell has a topWall.
     *
     * @param topWall MazeCell's topWall.
     */
    void setTopWall(boolean topWall) {
        this.topWall = topWall;
    }

    boolean isBottomWall() {
        return bottomWall;
    }

    /**
     * Sets if the MazeCell has a bottomWall.
     *
     * @param bottomWall MazeCell's bottomWall.
     */
    void setBottomWall(boolean bottomWall) {
        this.bottomWall = bottomWall;
    }

    boolean isLeftWall() {
        return leftWall;
    }

    /**
     * Sets if the MazeCell has a leftWall.
     *
     * @param leftWall MazeCell's leftWall.
     */
    void setLeftWall(boolean leftWall) {
        this.leftWall = leftWall;
    }

    boolean isRightWall() {
        return rightWall;
    }

    /**
     * Sets if the MazeCell has a rightWall.
     *
     * @param rightWall MazeCell's rightWall.
     */
    void setRightWall(boolean rightWall) {
        this.rightWall = rightWall;
    }

    boolean isVisited() {
        return visited;
    }

    /**
     * Sets if the MazeCell has been visited.
     *
     * @param visited MazeCell's visit case.
     */
    void setVisited(boolean visited) {
        this.visited = visited;
    }
}
