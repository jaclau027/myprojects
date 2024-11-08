package game.enemies;


import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.behaviours.FollowBehaviour;
import game.items.HealingVial;
import game.items.RefreshingFlask;
import game.items.Rune;
import game.utils.Ability;
import game.utils.Status;

import java.util.Random;

/**
 * A class that represents an EldentreeGuardian enemy
 * Created by:
 * @author Ting Ting
 */
public class EldentreeGuardian extends Enemy {
    Random random = new Random();

    /**
     * Constructor.
     */
    public EldentreeGuardian() {
        super("Eldentree Guardian", 'e', 250, 20);
        this.addCapability(Ability.CAN_BE_ON_VOID);
    }

    /**
     * A method to get the intrinsic weapon of the enemy.
     *
     * @return the intrinsic weapon of the enemy.
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(50, "hits", 80);
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
        String runeDrop = new DropAction(new Rune(250)).execute(this, map);
        ret += " " + runeDrop;
        if (random.nextInt(100) < 15) {
            String refreshingFlaskDrop = new DropAction(new RefreshingFlask()).execute(this, map);
            ret += " " + refreshingFlaskDrop;
        }
        if (random.nextInt(100) < 25) {
            String healingVialDrop = new DropAction(new HealingVial()).execute(this, map);
            ret += " " + healingVialDrop;
        }
        return super.unconscious(actor, map) + ret;
    }

    /**
     * A method to get the allowable actions of the enemy
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return an ActionList containing the allowable actions
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = super.allowableActions(otherActor, direction, map);
        if (otherActor.hasCapability(Status.HOSTILE_TO_ENEMY)){
            this.behaviours.put(1, new FollowBehaviour(otherActor));
        }
        return actions;
    }
}