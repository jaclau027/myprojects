package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.items.Consumable;
import game.utils.Status;

/**
 * A class that represents a puddle.
 * Created by:
 * @author Jun Chew
 */
public class Puddle extends Ground implements Consumable {

    /**
     * Constructor to create a puddle.
     */
    public Puddle() {
        super('~');
    }

    @Override
    public void consume(Actor actor, GameMap map) {
        if (actor != null && actor.hasCapability(Status.HOSTILE_TO_ENEMY)) {
            int stamina = (int) (actor.getAttributeMaximum(BaseActorAttributes.STAMINA) * 0.01);
            actor.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.INCREASE, stamina);
            actor.modifyAttribute(BaseActorAttributes.HEALTH, ActorAttributeOperations.INCREASE, 1);
        }

    }

    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = super.allowableActions(actor, location, direction);
        if (location.containsAnActor()) {
        actions.add(new ConsumeAction(this)); }
        return actions;
    }

    public String toString() {
        return "Puddle";
    }
}
