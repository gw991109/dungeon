package com.example.dungeonescape.game;

import com.example.dungeonescape.game.GameData;

/** An Interface for any GameObjects that need to read from the MazeData class. */
public interface RetrieveData {

    /** Sets the gameData variable equal to the passed in GameData instance.
     *
     * @param gameData the instance of GameData that this GameObject will read from.
     */
    void setGameData(GameData gameData);
}
