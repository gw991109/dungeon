package com.example.dungeonescape.platformer.entities;

import java.io.Serializable;

/**
 * Represents the ball that bounces on the platforms.
 */
class Character extends PlatformerObject implements Serializable{

    /** The speed of the ball. */
    private int speed = 5;

    /** If the player has touched the bottom of the screen. */
    private boolean start;

    /** The number of platforms this Character has bounced off of. */
    private int gameScore;


    Character(int x, int y, int size, PlatformerManager manager){
        super(x,y,size,manager);
        setShape();
        start = false;
        this.gameScore = 0;
    }
    /** @param colour the colour we want to set to
     * Sets the colour of the paint.
     * */
    void setColour(int colour){

        getPaint().setColor(colour);
    }

    /** Returns the GameScore.
     *
     * @return the integer GameScore.
     */
    int getGameScore(){
        return this.gameScore;
    }

    /** Sets the GameScore.
     *
     * @param gameScore the new GameScore.
     */
    void setGameScore(int gameScore){
        this.gameScore = gameScore;
    }

    /** Moves the Character. */
    void move() {
        double gravity = 3.0;
        if (speed >= 0) {
            speed += gravity;
        } else {
            speed += gravity;
        }

        if (getY() + getSize() / 2 + speed - getGridHeight() > 0 && !start) {
            setY(getGridHeight());
            speed = - 75;
            incY(speed);
        } else{
            incY(speed);
        }
        setShape();
    }
    /** Moves the Character down a few units. */
    @Override
    void update(int down) {
        setY(550 - down);
    }

    /** @return the speed. */
    int getSpeed() {
        return speed;
    }

    /** Makes the player bounce on platform. */
    void bounce(Platform platform) {
        this.gameScore += 1;
        setY(platform.getY() - (getSize() / 2));
        speed = -75;
        incY(speed);
        start = true;
        setShape();
    }

    /** Checks if your Player is alive.
     *
     * @return boolean if the Player is alive.
     */
    boolean isAlive() {
        return !start || getY() <= getGridHeight();
    }


    /** Moves the Character 50 units to the left. */
    void moveLeft() {
        if (getX() - 100 <= 0) {
            setX(getGridWidth() - 50);
        } else {
            incX(- 50);
        }
    }

    /** Moves the Character 50 units to the right. */
    void moveRight() {
        if (getX() + 100 >= getGridWidth()) {
            setX(0);
        } else {
            incX(50);
        }
    }
}