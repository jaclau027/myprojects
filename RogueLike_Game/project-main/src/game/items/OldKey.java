package game.items;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.items.PickUpAction;
import game.utils.Ability;
import game.utils.Status;

/**
 * A class that represents an old key.
 * Created by:
 * @author Jun Chew
 */
public class OldKey extends Item {
    /**
     * Constructor to create an old key.
     */
    public OldKey() {
        super("Old Key", '-', true);
    }

    /**
     * Let actor have the capability to unlock gate once picking up the key.
     */
    @Override
    public PickUpAction getPickUpAction(Actor actor) {
        actor.addCapability(Ability.CAN_UNLOCK_GATE);
        return super.getPickUpAction(actor);
    }
}
