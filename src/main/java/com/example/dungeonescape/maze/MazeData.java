package com.example.dungeonescape.maze;

import com.example.dungeonescape.game.GameData;
import com.example.dungeonescape.game.collectable.Collectable;

import java.util.ArrayList;
import java.util.List;

/** Contains all data for the Maze Game. */
class MazeData extends GameData {

    /** A 2D Array of MazeCell cells. */
    private MazeCell[][] cells;

    /** A list of coins that can be collected for score. */
    private ArrayList<MazeCoin> coins;

    private List<Collectable> collectables;

    /** The number of columns and rows in this Maze. */
    private int numMazeCols;
    private int numMazeRows;

    /** The size of each MazeCell in pixels. */
    private float cellSize;

    /** The horizontal and vertical margin from the edge of the screen to the walls of the maze */
    private float horizontalPadding;
    private float verticalPadding;

    MazeData(){
        super();
    }

    /** Calculates the cellSize based on the screen's dimensions.
     *
     * @param screenWidth the width of the phone screen in pixels.
     * @param screenHeight the height of the phone screen in pixels.
     */
    void calculateCellSize(int screenWidth, int screenHeight) {
        float newCellSize;
        float screenWidthDivHeight = screenWidth / screenHeight;
        float mazeColsDivRows = numMazeCols / numMazeRows;

        if (screenWidthDivHeight < mazeColsDivRows) {
            newCellSize = screenWidth / (numMazeCols + 1);
        } else {
            newCellSize = screenHeight / (numMazeRows + 1);
        }

        setCellSize(newCellSize);
    }

    /**
     * Calculates the cell's horizontal padding based on the screen's width and calculated cell size.
     *
     * @param screenWidth the width of the phone screen in pixels.
     */
    void calculateCellHorizontalPadding(float screenWidth) {
         setHorizontalPadding((screenWidth - (numMazeCols * cellSize)) / 2);
    }

    /**
     * Calculates the cell's vertical padding based on the screen's height and calculated cell size.
     *
     * @param screenHeight the height of the phone screen in pixels.
     */
    void calculateCellVerticalPadding(float screenHeight) {
        setVerticalPadding((screenHeight - (numMazeRows * cellSize)) / 2);
    }

    int getNumMazeCols() {
        return this.numMazeCols;
    }

    /** Sets the number of columns in this Maze.
     *
     * @param numMazeCols the number of columns.
     */
    void setNumMazeCols(int numMazeCols) {
        this.numMazeCols = numMazeCols;
    }

    int getNumMazeRows() {
        return this.numMazeRows;
    }

    /** Sets the number of rows in this Maze.
     *
     * @param numMazeRows the number of rows.
     */
    void setNumMazeRows(int numMazeRows) {
        this.numMazeRows = numMazeRows;
    }

    MazeCell[][] getCells() {
        return this.cells;
    }

    /** Sets the cells array to the inputted array.
     *
     * @param cells the 2D array of MazeCell objects.
     */
    void setCells(MazeCell[][] cells) {
        this.cells = cells;
    }

    List<Collectable> getCollectables() {
        return this.collectables;
    }

    /** Sets the MazeCoins array to the inputted array.
     *
     * @param collectables the List of MazeCoins.
     */
    void setCollectables(List<Collectable> collectables) {
        this.collectables = collectables;
    }

    float getCellSize() {
        return this.cellSize;
    }

    /** Sets the cellSize in pixels.
     *
     * @param cellSize the float value of the cellSize.
     */
    private void setCellSize(float cellSize) {
        this.cellSize = cellSize;
    }

    /** Returns the Maze's horizontalPadding.
     *
     * @return the float value that is the horizontalPadding.
     */
    float getHorizontalPadding() {
        return this.horizontalPadding;
    }

    /** Sets the horizontalPadding of the Maze.
     *
     * @param horizontalPadding the float value.
     */
    private void setHorizontalPadding(float horizontalPadding) {
        this.horizontalPadding = horizontalPadding;
    }

    /** Returns the Maze's verticalPadding.
     *
     * @return the float value that is the verticalPadding.
     */
    float getVerticalPadding() {
        return this.verticalPadding;
    }

    /** Sets the verticalPadding of the Maze.
     *
     * @param verticalPadding the float value.
     */
    private void setVerticalPadding(float verticalPadding) {
        this.verticalPadding = verticalPadding;
    }
}
