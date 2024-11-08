package game.enemies.factories;

import game.enemies.Enemy;
import game.enemies.HollowSoldier;
import game.enemies.WanderingUndead;

public class WanderingUndeadFactory implements EnemyFactory {
    /**
     * Creates a WanderingUndead enemy.
     * @return
     */

    @Override
    public Enemy createEnemy() {
        return new WanderingUndead();
    }
}

