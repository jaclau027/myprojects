package game.enemies;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.items.HealingVial;
import game.items.RefreshingFlask;
import game.items.Rune;

import java.util.Random;

/**
 * A type of enemy that is a Hollow Soldier.
 */
public class HollowSoldier extends Enemy {
    Random random = new Random();

    /**
     * Constructor.
     */
    public HollowSoldier() {
        super("Hollow Soldier", '&', 200, 10);
    }

    /**
     * A method to get the intrinsic weapon of the enemy.
     *
     * @return the intrinsic weapon of the enemy.
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(50, "whacks", 50);
    }

    /**
     * Returns the unconscious action of the enemy.
     *
     * @param actor the Actor acting
     * @param map   the GameMap containing the Actor
     * @return a string describing the action
     */
    @Override
    public String unconscious(Actor actor, GameMap map) {
        String ret = "\n";
        String runeDrop = new DropAction(new Rune(100)).execute(this, map);
        ret += " " + runeDrop;
        if (random.nextInt(100) < 10) {
            String healingVialDrop = new DropAction(new HealingVial()).execute(this, map);
            ret += " " + healingVialDrop;
        }
        if (random.nextInt(100) < 30) {
            String refreshingFlaskDrop = new DropAction(new RefreshingFlask()).execute(this, map);
            ret += " " + refreshingFlaskDrop;
        }
        return super.unconscious(actor, map) + ret;
    }

}
