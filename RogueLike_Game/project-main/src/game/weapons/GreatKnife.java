package game.weapons;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.ActivateSkillAction;
import game.actions.AttackAction;
import game.actions.SellAction;
import game.actions.UpgradeAction;
import game.items.Buyable;
import game.items.Sellable;
import game.items.Upgradable;
import game.skills.StabAndStepSkill;
import game.utils.Ability;
import game.utils.Status;

import java.util.Random;

/**
 * A class that represents the Great Knife weapon.
 * @author Jun Chew
 */
public class GreatKnife extends WeaponItem implements Buyable, Sellable, Upgradable {

    private Actor player;
    private final Random random = new Random();

    /**
     * Constructor of Great Knife weapon.
     */
    public GreatKnife() {
        super("Great Knife", '>', 75, "stabs", 70);
        this.addCapability(Status.IS_GREAT_KNIFE);
    }

    /**
     * Method to get allowable actions of Great Knife weapon.
     * @param otherActor the other actor
     * @param location the location of the other actor
     * @return a list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor otherActor, Location location) {
        ActionList actions = new ActionList();
        String direction = getDirection(otherActor, location);
        if (direction != null) {
            actions.add(new ActionList(new AttackAction(otherActor, direction,this, location.map())));
            actions.add(new ActivateSkillAction(this,new StabAndStepSkill(new AttackAction(otherActor, direction, this, location.map()))));
        } if (this.hasCapability(Ability.CAN_SELL)) {
            actions.add(new SellAction(this));
        }
        if (this.hasCapability(Ability.CAN_UPGRADE)){
            actions.add(new UpgradeAction(this));
        }
        return actions;
    }

    /**
     * Method to get attacking direction
     * @return the direction of the other actor or null if the other actor is not an enemy
     */
    private String getDirection(Actor otherActor, Location location){
        Location here = location.map().locationOf(player);
        for (Exit exit : here.getExits()) {
            Location destination = exit.getDestination();
            if (destination.getActor() == otherActor && otherActor.hasCapability(Status.IS_ENEMY)) {
                return exit.getName();
            }
        }
        return null;
    }

    /**
     * Tick method of Great Knife weapon.
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        this.player = actor;
        this.removeCapability(Ability.CAN_SELL);
        for (Exit exit : currentLocation.map().locationOf(actor).getExits()) {
            Actor anotherActor = exit.getDestination().getActor();
            if (anotherActor != null && anotherActor.hasCapability(Status.IS_MERCHANT)) {
                this.addCapability(Ability.CAN_SELL);
            }
            else if (anotherActor != null && anotherActor.hasCapability(Status.IS_MAKER)){
                this.addCapability(Ability.CAN_UPGRADE);
            }
        }
        for (Item item : actor.getItemInventory()) {
            if (item.hasCapability(Status.IS_GREAT_KNIFE)) {
                player.addCapability(Status.HOLDING_GREAT_KNIFE);
            }
        }
        super.tick(currentLocation, actor);
    }

    /**
     * Method to add Great Knife weapon to the inventory of the actor.
     * @param actor the actor to add the Great Knife weapon to
     */
    @Override
    public void addToInventory(Actor actor) {
        actor.addItemToInventory(this);
    }

    /**
     * Method to check if the transaction is successful.
     * @return true if the transaction is successful, false otherwise
     */
    @Override
    public boolean transactionSuccess() {
        return true;
    }

    /**
     * Method to get the buy price of Great Knife weapon.
     * @return the buy price of Great Knife weapon
     */
    @Override
    public int getBuyPrice() {
        return random.nextDouble() <= 0.05 ? 300 * 3 : 300;
    }

    /**
     * Method to remove Great Knife weapon from the inventory of the actor.
     * @param actor the actor to remove the Great Knife weapon from
     */
    @Override
    public void removeFromInventory(Actor actor) {
        actor.removeItemFromInventory(this);
    }

    /**
     * Method to get the sell price of Great Knife weapon.
     * @return the sell price of Great Knife weapon
     */
    @Override
    public int getSellPrice() {
        return random.nextDouble() <= 0.1 ? -175 : 175;
    }

    @Override
    public int getUpgradePrice(){
        return 2000;
    }

    @Override
    public void upgrade(){
        this.increaseHitRate(1);
    }

}