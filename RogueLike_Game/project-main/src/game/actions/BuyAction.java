package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.Buyable;

/**
 * Action for buying an item.
 * Created by:
 * @author Jun Chew
 */
public class BuyAction extends Action {

    private final Buyable item;
    private final int price;

    /**
     * Constructor of BuyAction.
     * @param item the item to be bought
     */
    public BuyAction(Buyable item) {
        this.item = item;
        this.price = item.getBuyPrice();
    }

    /**
     * Method to perform the Buy Action.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description of the action after it is processed
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (actor.getBalance() >= price) {
            if (item.transactionSuccess()) {
                actor.deductBalance(price);
                item.addToInventory(actor);
                return menuDescription(actor);
            }
            else {
                actor.deductBalance(price);
                return "The trader took your runes without giving the item!";
            }
        }
        return "Not enough runes!";
    }

    /**
     * Returns a descriptive string
     * @param actor The actor performing the action.
     * @return a string, e.g. "Player buys Broadsword with price 35"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " buys " + this.item + " with price " + price;
    }
}
