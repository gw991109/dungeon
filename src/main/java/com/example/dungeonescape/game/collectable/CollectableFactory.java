package com.example.dungeonescape.game.collectable;

/**
 * As there could be more collectible items throughout the levels, a factory class for the
 * collectible instances is made to satisfy the open/closed principle. Future collectible items
 * may be added by implementing the collectible interface and then adding them to this factory.
 */
public class CollectableFactory {

    /**
     *
     * @param collectableType a string telling the factory what type of collectible to be
     *                        produced and returned.
     * @param x the x-coordinate of the collectible.
     * @param y the y-coordinate of the collectible.
     * @param size size of the collectible.
     * @return return one of the collectibles according to request, or null if the provided string
     * is not an option.
     */
    public Collectable getCollectable(String collectableType, int x, int y, int size) {
        if (collectableType.equalsIgnoreCase("coin")) {
            return new Coin(x, y, size);

        } else if (collectableType.equalsIgnoreCase("gem")) {
            return new Gem(x, y, size);

        } else if (collectableType.equalsIgnoreCase("potion")) {
            return new Potion(x, y, size);
        } else if (collectableType.equalsIgnoreCase("blitz")){
            return new Blitz(x, y, size);
        }

        return null;
    }
}
