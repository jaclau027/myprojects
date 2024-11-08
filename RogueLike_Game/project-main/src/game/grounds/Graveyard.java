package game.grounds;

import game.enemies.factories.EnemyFactory;

/**
 * A class that represents the graveyard.
 * Created by:
 * @author Jun Chew
 */
public class Graveyard extends Environment {

    /**
     * Constructor to create a graveyard.
     * @param enemyFactory the factory to create enemies
     */
    public Graveyard(EnemyFactory enemyFactory) {
        super('n', enemyFactory);
    }
}

