package com.example.dungeonescape.game.collectable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import java.util.List;
import java.util.ArrayList;

import com.example.dungeonescape.game.GameObject;
import com.example.dungeonescape.game.Drawable;
import com.example.dungeonescape.player.Player;

/**
 * Blitz item that can be collected by player and activates hidden level in Brick Breaker game.
 */
public class Blitz extends GameObject implements Collectable, Drawable {

    /**
     * blitzShape - Rect representation of the object.
     * available - whether the item is still available to be picked up by the player.
     * blitzMode - state of the hidden level; whether it has been played, in progress or not yet
     * played.
     */
    private Rect blitzShape;
    private boolean available;
    private String blitzMode;

    /**
     * @param x x coordinate of the top left corner of the item.
     * @param y y coordinate of the top left corner of the item.
     * @param size the size of the item.
     */
    public Blitz(int x, int y, int size){
        super(x, y);
        setPaintColour(Color.RED);
        blitzShape = new Rect(x, y, x + size, y + size);
        available = true;
        blitzMode = "notStarted";

    }

    @Override
    public void draw(Canvas canvas){

        Path path = new Path();
        List<Point> points = createShapePoints();
        Point startPoint = points.get(0);
        path.moveTo(startPoint.x, startPoint.y);
        for (int i = 1; i < points.size(); i++ ){
            Point point = points.get(i);
            path.lineTo(point.x, point.y);
        }
        path.lineTo(startPoint.x, startPoint.y);
        path.close();
        canvas.drawPath(path, getPaint());
    }

    @Override
    public void collect(Player player) {
        player.addToSatchel(this);
    }

    /**
     * Helper method that creates the points required to draw a star shape.
     * @return list of Points that represent a star.
     */
    private List<Point> createShapePoints(){

        List<Point> shapePoints = new ArrayList<>();

        int size = blitzShape.width();

        int hMargin = size/8;
        int vMargin = size/4;

        int posX = getX();
        int posY = getY();

        Point a = new Point((posX + size/2), posY);
        shapePoints.add(a);
        Point b = new Point((posX + size/2 + hMargin), (int) (posY + vMargin * 1.5));
        shapePoints.add(b);
        Point c = new Point((posX + size), (int) (posY + vMargin * 1.5));
        shapePoints.add(c);
        Point d = new Point((posX + size/2 + 2*hMargin), (posY + size/2 + vMargin/2));
        shapePoints.add(d);
        Point e = new Point((posX + size/2 + 3*hMargin), (posY + size));
        shapePoints.add(e);
        Point f = new Point((posX + size/2), (posY + size - vMargin));
        shapePoints.add(f);
        Point g = new Point((posX + size/2 - 3*hMargin), (posY + size));
        shapePoints.add(g);
        Point h = new Point((posX + size/2 - 2*hMargin), (posY + size/2 + vMargin/2));
        shapePoints.add(h);
        Point i = new Point( posX, (int) (posY + vMargin * 1.5));
        shapePoints.add(i);
        Point j = new Point((posX + size/2 - hMargin), (int) (posY + vMargin * 1.5));
        shapePoints.add(j);

        return shapePoints;

    }

    public void update(int down, int height) {
        incY(down);
    }
    public void gotCollectable() {
        setY(0);
    }

    public Boolean getAvailableStatus(){
        return available;
    }

    @Override
    public void gotCollected(){
        available = false;
    }

    /**
     * Changes the state of the Blitz item's activation status
     * @param mode mode in ["started", "notstarted", "done"]
     */
    public void setBlitzMode(String mode){
        blitzMode = mode;
    }

    /**
     * Rerturns the state of the Blitz item's activation status.
     * @return String representing the state.
     */
    public String getBlitzMode(){
        return blitzMode;
    }


    public Rect getItemShape() {
        return blitzShape;
    }
}
