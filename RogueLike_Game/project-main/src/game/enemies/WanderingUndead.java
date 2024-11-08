package game.enemies;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.items.HealingVial;
import game.items.OldKey;
import game.items.Rune;

import java.util.Random;

/**
 * A type of enemy that is a Wandering Undead.
 */
public class WanderingUndead extends Enemy {
    Random random = new Random();

    /**
     * Constructor.
     */
    public WanderingUndead() {
        super("Wandering Undead", 't', 100, 25);
    }

    /**
     * Method to get the intrinsic weapon of the enemy.
     *
     * @return the intrinsic weapon of the enemy.
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(30, "whacks", 50);
    }

    /**
     * Actions to be executed when enemy is unconscious.
     *
     * @return the unconscious action of the enemy.
     */
    @Override
    public String unconscious(Actor actor, GameMap map) {
        String ret = "\n";
        String runeDrop = new DropAction(new Rune(100)).execute(this, map);
        ret += " " + runeDrop;
        if (random.nextInt(100) < 25) {
            String oldKeyDrop = new DropAction(new OldKey()).execute(this, map);
            ret += " " + oldKeyDrop;
        }
        if (random.nextInt(100) < 20) {
            String healingVialDrop = new DropAction(new HealingVial()).execute(this, map);
            ret += " " + healingVialDrop;
        }
        return super.unconscious(actor, map) + ret;
    }
}
