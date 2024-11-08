package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.Sellable;
import java.util.Random;

/**
 * Action for selling an item.
 * Created by:
 * @author Jun Chew
 */
public class SellAction extends Action {

    private final Sellable item;
    private final int price;

    /**
     * Constructor of SellAction.
     * @param sellable the item to be sold
     */
    public SellAction(Sellable sellable) {
        this.item = sellable;
        this.price = item.getSellPrice();
    }

    /**
     * Method to perform the Sell Action.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description of the action after it is processed
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        item.removeFromInventory(actor);

        if (price <= 0) {
            int maximumDeductibleAmount = Math.min(actor.getBalance(), -price);
            actor.addBalance(-maximumDeductibleAmount);
        } else {
            actor.addBalance(price);
        }

        return actor + " sells " + item + " for " + price + " runes.";
    }

    /**
     * Returns a descriptive string
     * @param actor The actor performing the action.
     * @return a string, e.g. "Player sells Broadsword"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " sells " + item ;
    }
}