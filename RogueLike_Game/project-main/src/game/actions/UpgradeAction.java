package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.Upgradable;

/**
 * Action for upgrading an item.
 * Created by:
 * @author Junmin Kim
 */
public class UpgradeAction extends Action {

    private final Upgradable item;
    private final int price;

    /**
     * Constructor of UpgradeAction.
     * @param upgradable - the item to be upgraded
     */
    public UpgradeAction(Upgradable upgradable) {
        this.item = upgradable;
        this.price = item.getUpgradePrice();
    }

    /**
     * Method to perform the Upgrade Action.
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description of the action after it is processed
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String description;
        if (actor.getBalance() >= price){
            item.upgrade();
            actor.deductBalance(price);
            description = menuDescription(actor);
        }
        else {
            description = "Not enough Runes to upgrade this item.";
        }
        return description;
    }

    /**
     * Returns a descriptive string
     * @param actor The actor performing the action.
     * @return a string, e.g. "Player upgrades HealingVial"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " upgrades " + this.item + " with price " + price;
    }
}