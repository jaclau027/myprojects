package game.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.ActivateSkillAction;
import game.actions.AttackAction;
import game.actions.SellAction;
import game.items.Sellable;
import game.skills.GreatSlamSkill;
import game.utils.Ability;
import game.utils.Status;

import java.util.List;

/**
 * A class that represents the Giant Hammer weapon.
 * Created by:
 * @author Jun Kim
 */
public class GiantHammer extends WeaponItem implements Sellable {
    private Actor player;

    /**
     * Constructor of Giant Hammer weapon.
     */
    public GiantHammer() {
        super("Giant Hammer", 'P', 160, "slams", 90);
        this.addCapability(Status.IS_GIANT_HAMMER);
    }

    /**
     * Method to get allowable actions of Giant Hammer weapon.
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
            actions.add(new ActivateSkillAction(this,new GreatSlamSkill(new AttackAction(otherActor, direction, this, location.map()), this, player)));
        } if (this.hasCapability(Ability.CAN_SELL)) {
            actions.add(new SellAction(this));
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
     * Method to tick the Giant Hammer weapon.
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
        }
        for (Item item : player.getItemInventory()) {
            if (item.hasCapability(Status.IS_GIANT_HAMMER)) {
                this.addCapability(Status.HOLDING_GIANT_HAMMER);
            }
        }
        super.tick(currentLocation, actor);
    }

    /**
     * Method to get the sell price of the Giant Hammer weapon.
     * @return the sell price of the Giant Hammer weapon
     */
    @Override
    public int getSellPrice() {
        return 250;
    }

    /**
     * Method to remove Giant Hammer weapon from the inventory of the actor.
     * @param actor the actor to remove the Giant Hammer weapon from
     */
    @Override
    public void removeFromInventory(Actor actor) {
        actor.removeItemFromInventory(this);
    }


}