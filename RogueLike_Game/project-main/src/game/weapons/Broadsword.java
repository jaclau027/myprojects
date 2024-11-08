package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.PickUpAction;
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
import game.skills.ActiveSkill;
import game.skills.FocusSkill;
import game.utils.Ability;
import game.utils.Status;

import java.util.Random;

/**
 * A class that represents the Broadsword weapon.
 * Created by:
 * @author Jun Chew
 */
public class Broadsword extends WeaponItem implements Buyable, Sellable, Upgradable {
    private Actor player;
    private FocusSkill focusSkill = new FocusSkill(this);
    private ActivateSkillAction activateSkillAction = new ActivateSkillAction(this, focusSkill);
    private int upgradedDamage = 0;

    private final Random random = new Random();

    /**
     * Constructor which creates a Broadsword weapon.
     */
    public Broadsword() {
        super("Broadsword", '1', 110, "slashes", 80);
    }

    /**
     * Returns the allowable actions for the actor to perform.
     * @param owner The actor holding this item.
     * @return an ActionList containing the allowable actions.
     */
    public ActionList allowableActions(Actor owner){
        ActionList actions = super.allowableActions(owner);
        actions.add(activateSkillAction);
        return actions;
    }

    /**
     * Method to get allowable actions of Broadsword weapon.
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
        } if (this.hasCapability(Ability.CAN_SELL)) {
            actions.add(new SellAction(this));
        } if (otherActor.hasCapability(Status.IS_MAKER)) {
            actions.add(new UpgradeAction(this));
        }
        return actions;
    }

    /**
     * Accessor for damage done by this weapon.
     *
     * @return the damage
     */
    @Override
    public int damage() {
        return Math.round(super.damage() + this.upgradedDamage);
    }

    /**
     * Method to get attacking direction
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
     * Method to tick the Broadsword weapon.
     * @param currentLocation The location of the actor carrying this Item.
     * @param actor The actor carrying this Item.
     */
    @Override
    public void tick(Location currentLocation, Actor actor) {
        focusSkill.tick();
        this.player = actor;
        this.removeCapability(Ability.CAN_SELL);
        for (Exit exit : currentLocation.map().locationOf(actor).getExits()) {
            Actor anotherActor = exit.getDestination().getActor();
            if (anotherActor != null && anotherActor.hasCapability(Status.IS_MERCHANT)) {
                this.addCapability(Ability.CAN_SELL);
            }
        }
        super.tick(currentLocation, actor);
    }

    /**
     * Returns the PickUpAction for the actor to perform.
     * @param actor The actor holding this item.
     * @return a PickUpAction.
     */
    @Override
    public PickUpAction getPickUpAction(Actor actor) {
        PickUpAction pickUpAction = super.getPickUpAction(actor);
        activateSkillAction = new ActivateSkillAction(this, new FocusSkill(this));
        return pickUpAction;
    }

    /**
     * Method to add Broadsword weapon to the inventory of the actor.
     * @param actor the actor
     */
    @Override
    public void addToInventory(Actor actor) {
        actor.addItemToInventory(this);
    }

    /**
     * Method to get the sell price of the Broadsword weapon.
     * @return the sell price of the Broadsword weapon
     */
    @Override
    public int getSellPrice() {
        return 100;
    }

    /**
     * Method to remove Broadsword weapon from the inventory of the actor.
     * @param actor the actor
     */
    @Override
    public void removeFromInventory(Actor actor) {
        actor.removeItemFromInventory(this);
    }

    /**
     * Method to get the buy price of the Broadsword weapon.
     * @return the buy price of the Broadsword weapon
     */
    @Override
    public int getBuyPrice() {
        return 250;
    }

    @Override
    public int getUpgradePrice(){
        return 1000;
    }

    @Override
    public void upgrade(){
        this.upgradedDamage += 10;
    }

    /**
     * Method to check if the transaction is successful.
     * @return true if the transaction is successful, false otherwise
     */
    @Override
    public boolean transactionSuccess() {
        return !(random.nextDouble() <= 0.05);
    }

}
