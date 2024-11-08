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
 * A class representing a Healing Vial item in a game.
 * This item can be consumed by an actor to restore a portion of their health.
 * @author Ng Chen Ting
 */
public class HealingVial extends Item implements Buyable, Sellable, Upgradable, Consumable {

    private final Random random = new Random();
    private int healAmount;
    private boolean upgraded = false;
    private final Display display = new Display();

    /**
     * Constructor for the HealingVial class.
     * Initializes the name, display character, and stackable status of the item.
     */
    public HealingVial() {
        super("Healing Vial", 'a', true);
    }

    /**
     * Allows an actor to consume the Healing Vial to increase their health.
     * If the actor consumes the item from their inventory, it is removed.
     *
     * @param actor The actor consuming the item.
     */
    @Override
    public void consume(Actor actor, GameMap map) {
        actor.removeItemFromInventory(this);
        int maxHealthActor = actor.getAttributeMaximum(BaseActorAttributes.HEALTH);
        healAmount = upgraded ? (int) (maxHealthActor * 0.8) : (int) (maxHealthActor * 0.1);
        actor.modifyAttribute(BaseActorAttributes.HEALTH, ActorAttributeOperations.INCREASE,healAmount);
        display.println(actor + " gained " + healAmount + " health.");
        map.locationOf(actor).removeItem(this);
    }

    public int getBuyPrice() {
        return random.nextDouble() <= 0.25 ? (int) (1.5 * 100) : 100;
    }

    public int getSellPrice() {
        return random.nextDouble() <= 0.1 ? 35 * 2 : 35;
    }

    @Override
    public void addToInventory(Actor actor) {
        actor.addItemToInventory(this);
    }

    @Override
    public void removeFromInventory(Actor actor) {
        actor.removeItemFromInventory(this);
    }

    @Override
    public int getUpgradePrice(){
        return 250;
    }

    @Override
    public void upgrade(){
        this.upgraded = true;
    }

    @Override
    public ActionList allowableActions(Actor otherActor, Location location){
        ActionList actions = super.allowableActions(otherActor, location);
        if (this.hasCapability(Ability.CAN_SELL)) {
            actions.add(new SellAction(this));
        } if (otherActor.hasCapability(Status.IS_MAKER) && !upgraded) {
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
