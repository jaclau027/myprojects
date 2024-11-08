package game.items;

import edu.monash.fit2099.engine.actors.Actor;

/**
 * An interface that represents a buyable item.
 */
public interface Buyable {
    void addToInventory(Actor actor); // Add the item to the actor's inventory
    boolean transactionSuccess(); // Check if the transaction is successful
    int getBuyPrice(); // Get the buy price of the item

}
