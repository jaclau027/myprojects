package game.enemies.factories;

import game.enemies.EldentreeGuardian;
import game.enemies.Enemy;

/***
 * Factory class for EldentreeGuardian
 * Created by:
 * @author Jun Chew
 */
public class EldentreeGuardianFactory implements EnemyFactory{

    /***
     * Create a new EldentreeGuardian
     * @return a new EldentreeGuardian
     */
    @Override
    public Enemy createEnemy() {
        return new EldentreeGuardian();
    }
}
