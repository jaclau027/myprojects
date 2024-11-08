package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.actions.SellAction;
import game.actions.UpgradeAction;
import game.utils.Ability;
import game.utils.Status;
import java.util.Random;

/**
 * A class representing a Refreshing Flask item in a game.
 * This item can be consumed to replenish a portion of an actor's stamina.
 * @author Ng Chen Ting
 */
public class RefreshingFlask extends Item implements Buyable, Sellable, Upgradable, Consumable {

    private final Random random = new Random();
    private boolean upgraded = false;
    private final Display display = new Display();


    /**
     * Constructor for the RefreshingFlask class.
     * Initializes the name, display character, and stackable status of the item.
     */
    public RefreshingFlask() {
        super("Refreshing Flask", 'u', true);
    }

    /**
     * Allows an actor to consume the Refreshing Flask to increase their stamina.
     * If the actor consumes the item from their inventory, it is removed.
     *
     * @param actor The actor consuming the item.
     */
    @Override
    public void consume(Actor actor, GameMap map) {
        actor.removeItemFromInventory(this);
        int maxStamina = actor.getAttributeMaximum(BaseActorAttributes.STAMINA);
        int staminaIncrease = upgraded ? maxStamina : (int) (maxStamina * 0.2);
        actor.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.INCREASE, staminaIncrease);
        display.println("Actor increases stamina of " + staminaIncrease + " points.");
        map.locationOf(actor).removeItem(this);
    }

    @Override
    public void addToInventory(Actor actor) {
        actor.addItemToInventory(this);
    }

    @Override
    public int getBuyPrice() {
        return random.nextDouble() <= 0.1 ? (int) (0.8 * 75) : 75;
    }

    @Override
    public int getSellPrice() {
        return random.nextDouble() <= 0.5 ? 0 : 25;
    }

    @Override
    public int getUpgradePrice() {
        return 175;
    }

    @Override
    public void upgrade() {
        this.upgraded = true;
    }

    @Override
    public void removeFromInventory(Actor actor) {
        actor.removeItemFromInventory(this);
    }

    @Override
    public ActionList allowableActions(Actor otherActor, Location location){
        ActionList actions = super.allowableActions(otherActor, location);
        if (this.hasCapability(Ability.CAN_SELL)) {
            actions.add(new SellAction(this));
        } if (otherActor.hasCapability(Status.IS_MAKER) && !upgraded) {
            actions.add(new UpgradeAction(this));
        }
        if (this.hasCapability(Ability.CAN_UPGRADE) && !this.upgraded){
            actions.add(new UpgradeAction(this));
        }
        return actions;
    }

    public ActionList allowableActions(Actor owner) {
        ActionList actions = super.allowableActions(owner);
        actions.add(new ConsumeAction(this));
        return actions;
    }
    @Override
    public void tick(Location currentLocation, Actor actor) {
        this.removeCapability(Ability.CAN_SELL);
        for (Exit exit : currentLocation.map().locationOf(actor).getExits()) {
            Actor anotherActor = exit.getDestination().getActor();
            if (anotherActor != null && anotherActor.hasCapability(Status.IS_MERCHANT)) {
                this.addCapability(Ability.CAN_SELL);
            }
        }
        super.tick(currentLocation, actor);
    }

    @Override
    public boolean transactionSuccess() {
        return true;
    }
}