package game.skills;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.AttackAction;

/**
 * A class that represents the Focus skill of Broadsword.
 * Created by:
 * @author Jun Chew
 */
public class FocusSkill  implements ActiveSkill{

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
     * Constructor.
     * @param weaponItem the weapon item that has this skill
     */
    public FocusSkill(WeaponItem weaponItem) {
        this.weaponItem = weaponItem;
        this.initialHitRate = weaponItem.chanceToHit();
    }
    public String toString(){
        return "Focus Skill";
    }


    @Override
    public String activateSkill(Actor actor, GameMap map) {
        actor.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.DECREASE, (int) (actor.getAttributeMaximum(BaseActorAttributes.STAMINA) * 0.2));
        if (actor.getAttribute(BaseActorAttributes.STAMINA) <= 0){
            return "Not enough stamina to use this skill!";
        }
        this.skillActive = true;
        this.weaponItem.updateHitRate(90);
        this.skillDuration = 5;
        this.damageMultiplier += 0.1f;
        weaponItem.updateDamageMultiplier(damageMultiplier);
        this.turnsRemaining = this.skillDuration;
        return actor + " takes a deep breath and focuses all their might!";
    }

    public void tick() {
        if (this.skillActive) {
            if (this.turnsRemaining > 0) {
                this.turnsRemaining--;
            } else {
                deactivate();
            }
        }
    }

    /**
     * Deactivates the skill.
     */
    public void deactivate() {
        this.skillActive = false;
        this.weaponItem.updateDamageMultiplier(1.0f);
        this.weaponItem.updateHitRate(initialHitRate);
    }
}
