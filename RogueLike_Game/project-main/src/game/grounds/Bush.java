package game.grounds;

import game.enemies.factories.EnemyFactory;

/**
 * A class that represents a bush.
 * Created By
 * @author Jun Chew
 */
public class Bush extends Environment {

    /**
     * Constructor to create a bush.
     * @param enemyFactory the factory to create enemies
     */
    public Bush(EnemyFactory enemyFactory) {
        super('m', enemyFactory);
    }
}