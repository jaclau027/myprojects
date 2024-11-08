package game.grounds;

import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.reset.GameResetManager;
import game.reset.Resettable;
import game.actions.TravelAction;
import game.actions.UnlockGateAction;
import game.utils.Status;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a locked gate.
 * Created by:
 * @author Jun Chew
 */
public class LockedGate extends Ground implements Resettable {

    private final List<TravelAction> travelActions;

    /**
     * Constructor.
     *
     * @param travelAction the action to travel to the next map
     */
    public LockedGate(TravelAction travelAction) {
        super('=');
        List<TravelAction> actionList = new ArrayList<>();
        actionList.add(travelAction);
        this.travelActions = actionList;
        GameResetManager.getInstance().addResettable(this);
    }

    /**
     * Constructor.
     *
     * @param travelActions list of actions to travel to the next maps
     */
    public LockedGate(List<TravelAction> travelActions) {
        super('=');
        this.travelActions = travelActions;
    }

    /**
     * Sets the gate to be unlocked.
     */
    public void setUnlocked() {
        this.addCapability(Status.UNLOCKED);
    }

    /**
     * Check if the gate is unlocked.
     *
     * @return true if the gate is unlocked
     */
    public boolean isUnlocked() {
        return this.hasCapability(Status.UNLOCKED);
    }

    /**
     * Check if the actor can enter the gate.
     *
     * @param actor the Actor to check
     * @return true if the gate is unlocked
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return isUnlocked();
    }

    /**
     * Returns the allowable actions for the actor.
     *
     * @param actor the Actor acting
     * @param location the current Location
     * @param direction the direction of the Ground from the Actor
     * @return the allowable actions for the actor
     */
    @Override
    public ActionList allowableActions(Actor actor, Location location, String direction) {
        ActionList actions = super.allowableActions(actor, location, direction);
        for(TravelAction action : this.travelActions) {
            if (isUnlocked()) {
                actions.add(action);
            } else { actions.add(new UnlockGateAction(this)); break; }
        }
        return actions;
    }

    @Override
    public void reset() {
        this.removeCapability(Status.UNLOCKED);
    }
}