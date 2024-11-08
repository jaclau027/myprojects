package game.enemies.factories;

import game.enemies.Enemy;
import game.enemies.RedWolf;

public class RedWolfFactory implements EnemyFactory {

    @Override
    public Enemy createEnemy() {
        return new RedWolf();
    }
}
