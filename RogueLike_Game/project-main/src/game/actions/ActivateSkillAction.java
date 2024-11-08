package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.skills.ActiveSkill;

public class ActivateSkillAction extends Action {

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
    protected ActiveSkill skill;

    public ActivateSkillAction(WeaponItem weapon, ActiveSkill skill){
        this.weaponItem = weapon;
        this.skill = skill;
    }

    @Override
    public String execute(Actor actor, GameMap map) {

        return this.skill.activateSkill(actor, map);
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " activates the " + this.skill;
    }

}
