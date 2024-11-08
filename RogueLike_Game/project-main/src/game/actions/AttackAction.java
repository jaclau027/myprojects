package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.utils.Status;

import java.util.Random;

/**
 * Action for attacking an enemy.
 */
public class AttackAction extends Action {

    /**
     * The Actor that is to be attacked
     */
    private final Actor target;

    /**
     * The direction of incoming attack.
     */
    private final String direction;

    /**
     * Random number generator
     */
    private final Random rand = new Random();

    /**
     * Weapon used for the attack
     */
    private Weapon weapon;

    private final GameMap map;


    /**
     * Constructor.
     *
     * @param target the Actor to attack
     * @param direction the direction where the attack should be performed (only used for display purposes)
     */
    public AttackAction(Actor target, String direction, Weapon weapon, GameMap map) {
        this.target = target;
        this.direction = direction;
        this.weapon = weapon;
        this.map = map;
    }

    /**
     * Constructor with intrinsic weapon as default
     *
     * @param target the actor to attack
     * @param direction the direction where the attack should be performed (only used for display purposes)
     */
    public AttackAction(Actor target, String direction, GameMap map) {
        this.target = target;
        this.direction = direction;
        this.map = map;
    }

    /**
     * Perform the Action.
     *
     * @param actor The actor performing the action.
     * @param map The map the actor is on.
     * @return a description of the action after it is processed
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        return performAttack(actor, map, 1.0);
    }

    /**
     * Perform the attack sction with a damage multiplier.
     * @param actor the actor performing the attack
     * @param map the map the actor is on
     * @param multiplier the multiplier to be applied to the damage
     * @return a description of the action after it is processed
     */
    public String executeWithMultiplier(Actor actor, GameMap map, double multiplier) {
        return performAttack(actor, map, multiplier);
    }

    /**
     * Perform the attack.
     * @param actor the actor performing the attack
     * @param map the map the actor is on
     * @param multiplier the multiplier to be applied to the damage
     * @return a description of the action after it is processed
     */
    private String performAttack(Actor actor, GameMap map, double multiplier) {
        if (weapon == null) {
            weapon = actor.getIntrinsicWeapon();
        }
        if (!(rand.nextInt(100) <= weapon.chanceToHit())) {
            return actor + " misses " + target + ".";
        }
        int damage = (int) (weapon.damage() * multiplier);
        String result = actor + " " + weapon.verb() + " " + target + " for " + damage + " damage.";

        target.hurt(damage);
        if (!target.isConscious()) {
            result += "\n" + target.unconscious(actor, map);
            if (!target.hasCapability(Status.HOSTILE_TO_ENEMY)) {
                map.removeActor(target);
            }
        }

        return result;
    }

    /**
     * Method to get the target of the attack.
     * @return the target of the attack
     */
    public Actor getTarget() {
        return this.target;
    }

    /**
     * Method to get the direction of the attack.
     * @return the direction of the attack
     */
    public String getDirection(){
        return this.direction;
    }

    /**
     * Returns a descriptive string
     *
     * @param actor The actor performing the action.
     * @return a string, e.g. "Player attacks the Zombie at (12, 5) with a Shotgun"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " attacks " + target + " at " + (weapon != null ? this.map.locationOf(this.target) : direction) + " with " + (weapon != null ? weapon : "Intrinsic Weapon");
    }
}