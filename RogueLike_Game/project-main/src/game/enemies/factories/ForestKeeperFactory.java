package game.enemies.factories;

import game.enemies.Enemy;
import game.enemies.ForestKeeper;

public class ForestKeeperFactory implements EnemyFactory {

    @Override
    public Enemy createEnemy() {
        return new ForestKeeper();
    }

}
