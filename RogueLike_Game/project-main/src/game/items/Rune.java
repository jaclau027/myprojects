package game.items;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actions.ConsumeAction;
import game.reset.GameResetManager;
import game.reset.Resettable;
import game.utils.Status;

/**
 * An interface that represents a sellable item.
 * Created by:
 * @author Jun Chew
 */
public class Rune extends Item implements Resettable, Consumable {

    private final int value;

    /**
     * Constructor.
     * @param value the value of the runes
     */
    public Rune(int value) {
        super("Rune", '$', true);
        this.value = value;
        GameResetManager.getInstance().addResettable(this);
    }

    /**
     * Consume the runes and add the value to the actor's balance.
     * @param actor the actor that consumes the runes
     */
    @Override
    public void consume(Actor actor, GameMap map) {
        actor.removeItemFromInventory(this);
        actor.addBalance(this.value);
        map.locationOf(actor).removeItem(this);
    }

    public ActionList allowableActions(Actor owner) {
        ActionList actions = super.allowableActions(owner);
        actions.add(new ConsumeAction(this));
        return actions;
    }

    @Override
    public void tick(Location currentLocation) {
        if (this.hasCapability(Status.REMOVED)) currentLocation.removeItem(this);
    }
    @Override
    public void reset() {
        this.addCapability(Status.REMOVED);
    }
}