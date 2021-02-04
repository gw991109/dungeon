package com.example.dungeonescape.maze;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.example.dungeonescape.game.Drawable;
import com.example.dungeonescape.game.RetrieveData;
import com.example.dungeonescape.game.collectable.Collectable;

import java.util.List;

/** Draws all instances of GameObjects onto the screen. Collects and reads data from MazeData.
 *
 * The original code from MazeView was from the following videos:
 * https://www.youtube.com/watch?v=I9lTBTAk5MU
 * https://www.youtube.com/watch?v=iri0wZ3NvdQ
 *
 * It has been edited and adjusted to fit our own objectives and visions of the game.
 */
public class MazeView extends View {

    /** Player and exit objects, and their positions. */
    private Sprite playerSprite = new Sprite();
    private Sprite exitSprite = new Sprite();

    private MazeData mazeData;

    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    void setMazeData(MazeData mazeData) {
        this.mazeData = mazeData;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        calculateDimensions();
        updateMazeObjectsData();

        // translate the canvas by our padding values so the maze is always centered on our screen.
        canvas.translate(mazeData.getHorizontalPadding(), mazeData.getVerticalPadding());

        // draws walls, Coins, the Player and the exit square on the screen
        paintWalls(canvas);
        paintCollectables(canvas);
        playerSprite.draw(canvas);
        exitSprite.draw(canvas);


    }

    /** Performs dimensions calculations including cellSize and padding values. */
    private void calculateDimensions() {
        mazeData.calculateCellSize(getWidth(), getHeight());
        mazeData.calculateCellHorizontalPadding(getWidth());
        mazeData.calculateCellVerticalPadding(getHeight());
    }

    /** Draws walls (borders) for each mazeCell.
     *
     * @param canvas the Canvas to draw the walls on.
     */
    private void paintWalls(Canvas canvas) {
        for(int x = 0; x < mazeData.getNumMazeCols(); x++) {
            for(int y = 0; y < mazeData.getNumMazeRows(); y++) {
                mazeData.getCells()[x][y].draw(canvas);
            }
        }
    }

    /** Draws the Coin circles on the screen.
     *
     * @param canvas the Canvas to draw the Coins on.
     */
    private void paintCollectables(Canvas canvas) {
        List<Collectable> collectables = mazeData.getCollectables();

        for (Collectable obj : collectables) {
            if (obj instanceof Drawable) {
                ((Drawable) obj).draw(canvas);
            }
        }
    }

    /** Runs method setMazeData on all GameObjects that implement RetrieveData. */
    void updateMazeObjectsData() {
        playerSprite.setGameData(this.mazeData);
        exitSprite.setGameData(this.mazeData);
        updateMazeCellData();
        updateCollectableData();
    }

    /** Runs method setMazeData on all MazeCells. */
    void updateMazeCellData() {
        for (int x = 0; x < mazeData.getNumMazeRows(); x++) {
            for (int y = 0; y < mazeData.getNumMazeRows(); y++) {
                mazeData.getCells()[x][y].setGameData(mazeData);
            }
        }
    }

    void updateCollectableData() {
        List<Collectable> collectables = mazeData.getCollectables();

        for (Collectable obj : collectables) {
            if (obj instanceof RetrieveData) {
                ((RetrieveData)obj).setGameData(this.mazeData);
            }
        }
    }

    /** Returns the Maze's ExitSprite.
     *
     * @return the ExitSprite GameObject.
     */
    Sprite getExitSprite() {
        return this.exitSprite;
    }

    /** Sets the location of the ExitSprite. */
    void setExitSprite() {
        this.exitSprite.setX(mazeData.getNumMazeCols() - 1);
        this.exitSprite.setY(mazeData.getNumMazeRows() - 1);
    }

    /** Returns the PlayerSprite.
     *
     * @return the PlayerSprite GameObject.
     */
    Sprite getPlayerSprite() {
        return this.playerSprite;
    }
}
