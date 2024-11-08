package game.enemies.factories;

import game.enemies.Enemy;
import game.enemies.HollowSoldier;

/**
 * A factory for creating HollowSoldier enemies.
 */
public class HollowSoldierFactory implements EnemyFactory {
    /**
     * Creates a HollowSoldier enemy.
     * @return
     */
    @Override
    public Enemy createEnemy() {
        return new HollowSoldier();
    }
}
