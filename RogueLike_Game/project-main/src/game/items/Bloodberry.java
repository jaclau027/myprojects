package game.items;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.actions.SellAction;
import game.utils.Ability;
import game.utils.Status;

/**
 * A class that represents the Bloodberry item.
 * Created by:
 * @author Jun Chew
 */
public class Bloodberry extends Item implements Sellable, Consumable {

    public Bloodberry() {
        super("Bloodberry", '*', true);

    }

    @Override
    public void consume(Actor actor, GameMap map) {
        actor.removeItemFromInventory(this);
        actor.modifyAttributeMaximum(BaseActorAttributes.HEALTH, ActorAttributeOperations.INCREASE, 5);
        map.locationOf(actor).removeItem(this);
    }

    @Override
    public int getSellPrice() {
        return 10;
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
        }

        return actions;
    }

    @Override
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
                break;
            }
        }
        super.tick(currentLocation, actor);
    }

}
