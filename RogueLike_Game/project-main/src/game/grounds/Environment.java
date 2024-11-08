package game.grounds;
import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.enemies.Enemy;
import game.enemies.factories.EnemyFactory;

import java.util.Random;

/**
 * A class that which is a type of ground and can spawn enemies.
 * Created by:
 * @author Jun Chew
 */
public abstract class Environment extends Ground implements Spawnable {

    private final Random rand = new Random();
    private final EnemyFactory enemyFactory;

    /**
     * Constructor to create an environment.
     * @param displayChar the character to display for this type of ground
     * @param enemyFactory the factory to create enemies
     */
    public Environment(char displayChar, EnemyFactory enemyFactory) {
        super(displayChar);
        this.enemyFactory = enemyFactory;
    }

    /**
     * Spawn an enemy at a location if the location does not contain an actor and the spawn rate is met.
     * @param location the location to spawn the enemy
     * @param enemy the enemy to spawn
     */
    @Override
    public void spawn(Location location, Enemy enemy) {
        if (!location.containsAnActor() && rand.nextInt(100) <= enemy.getSpawnRate()) {
            location.map().addActor(enemy, location);
        }
    }

    /**
     * Tick the environment and spawn an enemy at the location if the spawn rate is met.
     * @param location The location of the Ground
     */
    @Override
    public void tick(Location location) {
        super.tick(location);
        spawn(location, enemyFactory.createEnemy());
    }
}

