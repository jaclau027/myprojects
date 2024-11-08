package game.items;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * An interface that represents a sellable item.
 */
public interface Sellable {
    void removeFromInventory(Actor actor); // Remove the item from the actor's inventory
    int getSellPrice(); // Get the sell price of the item
}
