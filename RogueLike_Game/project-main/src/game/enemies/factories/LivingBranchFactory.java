package game.enemies.factories;

import game.enemies.Enemy;
import game.enemies.LivingBranch;

/**
 * A factory for creating LivingBranch enemies.
 */
public class LivingBranchFactory implements EnemyFactory {
    /**
     * Creates a LivingBranch enemy.
     * @return a new LivingBranch instance
     */
    @Override
    public Enemy createEnemy() {
        return new LivingBranch();
    }
}