package game.grounds;

import edu.monash.fit2099.engine.positions.Location;
import game.enemies.Enemy;

/**
 * An interface which allows enemies spawning.
 * Created by:
 * @author Jun Chew
 */
public interface Spawnable {

    void spawn(Location location, Enemy enemy); // Spawn an enemy at a location if the location

}
