package game.grounds;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.utils.Ability;

/** A class that represents the void.
 * @author Jun Chew
 * */
public class Void extends Ground {

    /** Constructor. */
    public Void() {
        super('+');
    }

    /** Kills the actor at the given location.
     * @param location the location of the actor
     * */
    @Override
    public void tick(Location location) {
        super.tick(location);
        if (location.containsAnActor() && !location.getActor().hasCapability(Ability.CAN_BE_ON_VOID)) {
            location.getActor().unconscious(location.map());
        }
    }
}
