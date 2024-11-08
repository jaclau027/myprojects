package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.grounds.LockedGate;
import game.utils.Ability;
import game.utils.Status;

/**
 * Action for unlocking a gate.
 * Created by:
 * @author Jun Chew
 */
public class UnlockGateAction extends Action {

    private final LockedGate lockedGate;

    /**
     * Constructor.
     *
     * @param lockedGate the locked gate to be unlocked
     */
    public UnlockGateAction(LockedGate lockedGate) {
        this.lockedGate = lockedGate;
    }

    /**
     * Perform the Action.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description of the action after it is processed
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (actor.hasCapability(Ability.CAN_UNLOCK_GATE)) {
            this.lockedGate.setUnlocked();
            return "Gate unlocked.";
        }
        return "Gate is locked shut.";
    }

    /**
     * Returns a descriptive string
     *
     * @param actor The actor performing the action.
     * @return a string, e.g. "Player unlocks the gate"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " unlocks the gate";
    }
}