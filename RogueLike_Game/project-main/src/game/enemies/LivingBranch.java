package game.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.items.Bloodberry;
import game.items.Rune;
import game.utils.Ability;

import java.util.Random;

/**
 * A type of enemy that is a LivingBranch.
 */
public class LivingBranch extends Enemy {
    Random random = new Random();

    /**
     * Constructor.
     */
    public LivingBranch() {
        super("Living Branch", '?', 75, 90);
        super.behaviours.remove(999);
        this.addCapability(Ability.CAN_BE_ON_VOID);
    }

    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        for (Behaviour behaviour : behaviours.values()) {
            Action action = behaviour.getAction(this, map);
            if (action != null)
                return action;
        }
        return new DoNothingAction();
    }

    /**
     * A method to get the intrinsic weapon of the enemy.
     *
     * @return the intrinsic weapon of the enemy.
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(250, "slaps", 90);
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
        String runeDrop = new DropAction(new Rune(500)).execute(this, map);
        ret += " " + runeDrop;
        if (random.nextInt(100) < 50) {
            String bloodberryDrop = new DropAction(new Bloodberry()).execute(this, map);
            ret += " " + bloodberryDrop;
        }
        return super.unconscious(actor, map) + ret;
    }

}
