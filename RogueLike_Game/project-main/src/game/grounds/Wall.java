package game.grounds;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Ground;

/**
 * Created by:
 * @author Riordan D. Alfredo
 * Modified by:
 * Jun Chew
 */

/** A class that represents a wall.*/
public class Wall extends Ground {

    /** Constructor. */
    public Wall() {
        super('#');
    }

    /** Returns false as the actor cannot enter the wall.
     * @param actor the Actor to check
     * @return false
     * */
    @Override
    public boolean canActorEnter(Actor actor) {
        return false;
    }
}
