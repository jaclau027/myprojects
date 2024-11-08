package game.grounds;

import game.enemies.factories.EnemyFactory;

/**
 * A class that represents an empty hut.
 */
public class EmptyHut extends Environment {

    /**
     * Constructor to create an empty hut.
     * @param enemyFactory the factory to create enemies
     */
    public EmptyHut(EnemyFactory enemyFactory) {
        super('h', enemyFactory);
    }
}

