package game.skills;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.AttackAction;
import game.utils.Status;

/**
 * A class that represents the Great Slam skill.
 * Created by:
 * @author Jun Kim
 */
public class GreatSlamSkill implements ActiveSkill {
    /** Initial hit rate of the weapon. */
    protected int initialHitRate;
    /** Number of turns available to use for this skill. */
    protected int skillDuration;
    /** Number of turns remaining to use for this skill. */
    protected int turnsRemaining;
    /** The weapon item that has this skill. */
    protected WeaponItem weaponItem;
    /** Boolean to check if the skill is active. */
    protected boolean skillActive = false;
    /** Damage multiplier of the weapon. */
    protected float damageMultiplier = 1.0f;
    protected AttackAction attackAction;


    private final Actor player;

    /**
     * Constructor of Great Slam skill.
     * @param attackAction the attack action that will be used
     * @param weaponItem the weapon item that will be used
     * @param player the player that will be used
     */
    public GreatSlamSkill(AttackAction attackAction, WeaponItem weaponItem, Actor player) {
        this.attackAction = attackAction;
        this.weaponItem = weaponItem;
        this.player = player;
    }

    @Override
    public String activateSkill(Actor actor, GameMap map) {
        actor.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.DECREASE, (int) (actor.getAttributeMaximum(BaseActorAttributes.STAMINA) * 0.05));
        if (actor.getAttribute(BaseActorAttributes.STAMINA) <= 0){
            return "Not enough stamina to use this skill!";
        }
        StringBuilder result = new StringBuilder();
        result.append(attackAction.execute(actor, map));
        for (Exit exit : map.locationOf(player).getExits()) {
            Actor anotherActor = exit.getDestination().getActor();
            if (anotherActor != null && anotherActor != attackAction.getTarget() && anotherActor.hasCapability(Status.IS_ENEMY)) {
                result.append(new AttackAction(anotherActor, exit.getName(), weaponItem, map).executeWithMultiplier(actor, map, 0.5));
            }
        }
        return result.toString();
    }

    public String toString(){
        return "Great Slam Skill";
    }
}
