package com.example.dungeonescape.maze;

import android.graphics.Color;
import android.util.SparseIntArray;

import com.example.dungeonescape.game.collectable.Collectable;
import com.example.dungeonescape.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

/**
 * Managers all (visible) Maze GameObjects and calculates sizes for various objects. Mazes and coins
 * are created in this class and then passed to MazeView to be drawn out on screen.
 */
class MazeManager {

    /** A 2D Array of MazeCell cells. */
    private MazeCell[][] cells;

    /** The number of columns and rows in the Maze, passed in from MazeView. */
    private int numMazeCols;
    private int numMazeRows;

    /** a Random instance used to randomize Coin locations. */
    private Random rand = new Random();

    private Player player;

    private MazeData mazeData = new MazeData();
    private MazeView mazeView;
    private Sprite playerSprite;
    private MazeCell playerLocation;
    private Sprite exitSprite;

    /** Number of times the Player has gone through the maze. */
    private int mazeIterations = 0;

    MazeManager(MazeView mazeView, Player player) {
        this.mazeView = mazeView;

        this.player = player;
        initializeMazeSize();

        mazeView.setMazeData(this.mazeData);

        populateMaze();
    }

    /** Creates the size of the Maze. */
    private void initializeMazeSize() {
        setNumMazeRows(5 * player.getGameDifficulty());
        setNumMazeCols(5 * player.getGameDifficulty());

        mazeData.setNumMazeCols(getNumMazeCols());
        mazeData.setNumMazeRows(getNumMazeRows());
    }

    /** Creates and assigns the Maze 2D Array to this.cells. */
    private void initializeMazeArray() {
        this.cells = createMaze();
        mazeData.setCells(this.cells);
    }

    /** Populate Maze with GameObjects. */
    private void populateMaze() {
        initializeMazeArray();
        mazeData.setCollectables(createCollectables());
        this.playerSprite = mazeView.getPlayerSprite();
        createExitCell();
    }

    /** Create a MazeCell object in the bottom right hand corner of the Maze. */
    private void createExitCell() {
        mazeView.setExitSprite();
        mazeView.getExitSprite().setPaintColour(Color.BLUE);
        this.exitSprite = mazeView.getExitSprite();
    }

    /** Populates a 2D Array with MazeCell GameObjects to create the Maze.
     *
     * @return a 2D Array of MazeCell GameObjects.
     */
    private MazeCell[][] createMaze(){
        MazeCell[][] cellArray = new MazeCell[numMazeCols][numMazeRows];

        // Populates the 2D Array cellArray with MazeCell GameObjects
        for (int x = 0; x < numMazeCols; x++) {
            for (int y = 0; y < numMazeRows; y++) {
                cellArray[x][y] = createMazeCell(x, y);
            }
        }

        traverseMaze(cellArray);

        return cellArray;
    }

    /** Returns a MazeCell GameObject at the specified coordinates with a specific colour and line
     * width.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return a MazeCell GameObject.
     */
    private MazeCell createMazeCell(int x, int y) {
        MazeCell mazeCell = new MazeCell(x, y);
        mazeCell.setPaintColour(Color.WHITE);
        mazeCell.setPaintStrokeWidth(4);
        return mazeCell;
    }

    /** Traverses through the cellArray of MazeCell GameObjects and removes some walls to create
     * the Maze.
     *
     * @param cellArray the 2D of MazeCell GameObjects to traverse through.
     */
    private void traverseMaze(MazeCell[][] cellArray) {
        Stack<MazeCell> stack = new Stack<>();
        MazeCell current, next;

        current = cellArray[0][0];
        current.setVisited(true);

        // check for MazeCell neighbours and remove walls until all MazeCells are traversed
        do {
            next = getNeighbour(current, cellArray);
            if (next != null) {
                removeMazeCellWall(current, next);
                stack.push(current);
                current = next;
                current.setVisited(true);
            } else {
                current = stack.pop();
            }
        } while (!stack.isEmpty());
    }

    /** Returns a MazeCell that has not been visited by the createMaze() function.
     *
     * @param currCell the current cell we're at, used to determine possible neighbors.
     * @param cells all the possible cells in the maze.
     * @return an unvisited cell which is a neighbor of the passed in cell.
     */
    private MazeCell getNeighbour(MazeCell currCell, MazeCell[][] cells) {
        ArrayList<MazeCell> neighbours = new ArrayList<>();

        // check MazeCell on the left
        if (currCell.getX() - 1 >= 0 &&
                !cells[currCell.getX() - 1][currCell.getY()].isVisited()) {
            neighbours.add(cells[currCell.getX() - 1][currCell.getY()]);
        }

        // check MazeCell on the left
        if (currCell.getX() + 1 < numMazeCols &&
                !cells[currCell.getX() + 1][currCell.getY()].isVisited()) {
            neighbours.add(cells[currCell.getX() + 1][currCell.getY()]);
        }

        // check MazeCell below
        if (currCell.getY() + 1 < numMazeRows &&
                !cells[currCell.getX()][currCell.getY() + 1].isVisited()) {
            neighbours.add(cells[currCell.getX()][currCell.getY() + 1]);
        }

        // check MazeCell above
        if (currCell.getY() - 1 >= 0 &&
                !cells[currCell.getX()][currCell.getY() - 1].isVisited()) {
            neighbours.add(cells[currCell.getX()][currCell.getY() - 1]);
        }

        if (neighbours.isEmpty()) {
            return null;
        } else {
            return neighbours.get(rand.nextInt(neighbours.size()));
        }
    }

    /** Removes walls between this MazeCell and the next MazeCell, as long as they're not borders.
     *
     * @param currCell the current MazeCell.
     * @param nextCell the next MazeCell to check.
     */
    private void removeMazeCellWall(MazeCell currCell, MazeCell nextCell){
        if (currCell.getX() == nextCell.getX() - 1) {
            currCell.setRightWall(false);
            nextCell.setLeftWall(false);
        } else if (currCell.getX() == nextCell.getX() + 1) {
            currCell.setLeftWall(false);
            nextCell.setRightWall(false);
        } else if (currCell.getY() == nextCell.getY() - 1) {
            currCell.setBottomWall(false);
            nextCell.setTopWall(false);
        } else {
            currCell.setTopWall(false);
            nextCell.setBottomWall(false);
        }
    }

    /**
     * Create 2 coins at random locations in the maze and return this list.
     * @return The list of coins we just created.
     */
    private ArrayList<Collectable> createCollectables() {
        ArrayList<Collectable> collectables = new ArrayList<>();
        SparseIntArray coordinates = new SparseIntArray();
        coordinates.append(0,0);
        coordinates.append(numMazeCols, numMazeRows);
        while(coordinates.size()<4){
            int x = rand.nextInt(numMazeCols);
            if(coordinates.get(x, -1) == -1) {
                coordinates.append(x, rand.nextInt(numMazeRows));
            } else
                {
                int y = rand.nextInt(numMazeRows);
                while(coordinates.get(x)==y){
                    y = rand.nextInt(numMazeRows);
                }
                coordinates.append(x,y);
            }
        }
        coordinates.delete(0);
        coordinates.delete(numMazeCols);
        for (int i = 0; i < 2; i++) {
            int x = coordinates.keyAt(i);
            int y = coordinates.get(x);
            // controls which type of Collectable to add to the collectables Array
            MazeCoin coin = new MazeCoin(x, y, (int) Math.ceil(mazeData.getCellSize()));
            collectables.add(coin);
        }
        return collectables;
    }

    /** Returns if the Player has completed 3 iterations of the MazeCell.
     *
     * @return boolean value for Player level completion.
     */
    boolean hasCompletedLevel() {
        return mazeIterations >= 3;
    }

    void movePlayerSprite(String direction){
        // depending on the given direction, move the player to that cell if it's in the maze.
        switch (direction){
            case "UP":
                if(!playerLocation.isTopWall()) {
                    playerLocation = this.cells[playerSprite.getX()][playerSprite.getY() - 1];
                    playerSprite.setY(playerSprite.getY() - 1);
                }
                break;
            case "DOWN":
                if(!playerLocation.isBottomWall()) {
                    playerLocation = this.cells[playerSprite.getX()][playerSprite.getY() + 1];
                    playerSprite.setY(playerSprite.getY() + 1);
                }
                break;
            case "LEFT":
                if(!playerLocation.isLeftWall()) {
                    playerLocation = this.cells[playerLocation.getX() - 1][playerLocation.getY()];
                    playerSprite.setX(playerSprite.getX() - 1);
                }
                break;
            case "RIGHT":
                if(!playerLocation.isRightWall()) {
                    playerLocation = this.cells[playerLocation.getX() + 1][playerLocation.getY()];
                    playerSprite.setX(playerSprite.getX() + 1);
                }
                break;
        }
        playerAtExit();
        playerOnCollectable();
        mazeView.invalidate();
    }

    /** Checks if this Player has arrived at the exit, create a new Maze if true. */
    private void playerAtExit() {
        if (playerSprite.getX() == exitSprite.getX() && playerSprite.getY() == exitSprite.getY()) {
            mazeIterations++;
            this.cells = createMaze();
            mazeData.setCells(this.cells);
            mazeData.setCollectables(createCollectables());
            relocatePlayerSprite();
        }
    }

    /** Checks if this Player has the same coordinates as a Coin.
     * Removes Coin from game & adds it to Player if true.
     */
    private void playerOnCollectable() {
        Iterator<Collectable> collectableIterator = mazeData.getCollectables().iterator();
        while (collectableIterator.hasNext()) {
            Collectable toCollect = collectableIterator.next();
            if (toCollect instanceof MazeCoin) {
                if (playerSprite.getX() == ((MazeCoin) toCollect).getX()
                        && playerSprite.getY() == ((MazeCoin) toCollect).getY()) {
                    collectableIterator.remove();
                    toCollect.collect(player);
                }
            }
        }
    }

    /** Sets the PlayerSprite's position to the initial state. */
    void relocatePlayerSprite() {
        playerSprite.setPaintColour(player.getColour());
        playerLocation = this.cells[0][0];
        if (playerSprite != null) {
            playerSprite.setX(0);
            playerSprite.setY(0);
        }
    }

    /** Returns the number of columns in this Maze.
     *
     * @return the integer value for the number of columns in this Maze.
     */
    private int getNumMazeCols() {
        return numMazeCols;
    }

    /** Sets the number of columns in this Maze.
     *
     * @param numMazeCols the number of columns to create.
     */
    private void setNumMazeCols(int numMazeCols){ this.numMazeCols = numMazeCols; }

    /** Returns the number of rows in this Maze.
     *
     * @return the integer value for the number of rows in this Maze.
     */
    private int getNumMazeRows() {
        return numMazeRows;
    }


    /** Sets the number of rows in this Maze.
     *
     * @param numMazeRows the number of rows to create.
     */
    private void setNumMazeRows(int numMazeRows){ this.numMazeRows = numMazeRows; }
}
