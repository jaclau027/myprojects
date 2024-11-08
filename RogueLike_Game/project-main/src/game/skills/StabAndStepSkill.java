package game.skills;

import edu.monash.fit2099.engine.actions.MoveActorAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.AttackAction;

/**
 * A class that represents the Stab and Step skill.
 * Created by:
 * @author Jun Chew
 */
public class StabAndStepSkill implements ActiveSkill{

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

    /**
     * Constructor of Stab and Step skill.
     * @param attackAction the attack action that will be used
    */
    public StabAndStepSkill(AttackAction attackAction) {
        this.attackAction = attackAction;
    }

    @Override
    public String activateSkill(Actor actor, GameMap map) {
        actor.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.DECREASE, (int) (actor.getAttributeMaximum(BaseActorAttributes.STAMINA) * 0.25));
        if (actor.getAttribute(BaseActorAttributes.STAMINA) <= 0){
            return "Not enough stamina to use this skill!";
        }
        String result = actor + " uses stab and step";
        for (Exit exit : map.locationOf(actor).getExits()) {
            if (!exit.getDestination().containsAnActor() && exit.getDestination().canActorEnter(actor)) {
                result += System.lineSeparator() + actor + " moves to " + exit.getName();
                result += attackAction.execute(actor, map);
                new MoveActorAction(exit.getDestination(), exit.getName()).execute(actor,map);
                break;
            }
        }
        return result;
    }

    public String toString(){
        return "Stab and Step Skill";
    }
}
