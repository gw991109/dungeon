package com.example.dungeonescape.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Player Manager. It contains all player data.
 *
 * It controls the statistics that determines pass/fail conditions.
 *
 */

public class PlayerManager implements Serializable {
    private List<Player> players;


    public PlayerManager() {
        /* Sets the initial total time elapsed in the Game to 0. */
        players = new ArrayList<>();

    }
    public List<String> getPlayerNames() {
        List<String> arr = new ArrayList<>();
        for (Player player: players) {
            arr.add(player.getName());
        }
        return arr;
    }
    public void addPlayer(Player player) {
        players.add(player);
    }


    public Player getPlayer(String name) {
        if (players.size() != 0) {
            Player p = players.get(0);

            for (Player player: players) {
                if (player.getName().equals(name)) {
                    p = player;
                    break;
                }
            }
            return p;
        }
        else {
            return null;
        }
    }
}
