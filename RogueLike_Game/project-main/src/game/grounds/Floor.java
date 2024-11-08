package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;
import game.utils.Status;

/**
 * A class that represents the floor inside a building.
 * Created by:
 * @author Riordan D. Alfredo
 * Modified by:
 * Jun Chew
 */
public class Floor extends Ground {
    /**
     * Constructor.
     */
    public Floor() {
        super('_');
    }

    /**
     * Returns true if the actor is not an enemy.
     *
     * @param actor the Actor to check
     * @return true if the actor is not an enemy
     */
    @Override
    public boolean canActorEnter(Actor actor) {
        return !actor.hasCapability(Status.IS_ENEMY);
    }
}