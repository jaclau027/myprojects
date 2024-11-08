package game.enemies.factories;

import game.enemies.Enemy;

/**
 * An interface for enemy factories.
 */
public interface EnemyFactory {
    /**
     * Creates an enemy.
     * @return
     */
    Enemy createEnemy();
}
