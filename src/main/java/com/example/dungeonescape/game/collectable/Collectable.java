package com.example.dungeonescape.game.collectable;

import android.graphics.Rect;
import com.example.dungeonescape.player.Player;

public interface Collectable {
    /**
     * @return whether the item is still available for the player to collect.
     */
    Boolean getAvailableStatus();

    /**
     * Registers that the item has been collected by the player.
     */
    void gotCollected();

    /**
     * @return the Rect representation of the object.
     */
    Rect getItemShape();


    void update(int down, int height);

    void gotCollectable();

    /**
     * Adds the item to the player's satchel once collected.
     * @param player the user playing the game. 
     */
    void collect(Player player);

}
