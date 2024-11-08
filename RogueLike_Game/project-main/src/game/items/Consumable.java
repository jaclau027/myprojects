package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.ConsumeAction;

/**
 * An abstract class representing an item that can be consumed.
 * @author Ng Chen Ting
 */
public interface Consumable {


//    private String name;
//    private char displayChar;
//
//    private boolean portable;
//    /**
//     * Constructor
//     *
//     * @param name The name of the consumable
//     * @param displayChar The character to display on the map
//     * @param portable True if the item can be picked up
//     */
//    public Consumable(String name, char displayChar, boolean portable){
//        this.name = name;
//        this.displayChar = displayChar;
//        this.portable = portable;
//    }

    /**
     * Consume the item.
     */
    void consume(Actor actor, GameMap map);

    /**
     * Add Consume Item Action to a list of allowable actions that can be performed on this item by a given actor
     *
     * @param actor The actor interacting with the item.
     * @return An ActionList containing allowable actions for the actor.
     */
//    public ActionList allowableActions(Actor actor) {
//        ActionList actions = new ActionList();
//        actions.add(new ConsumeAction());
//        return actions;
//    }
}
