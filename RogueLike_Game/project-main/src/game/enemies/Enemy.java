package game.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.Behaviour;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.reset.GameResetManager;
import game.reset.Resettable;
import game.actions.AttackAction;
import game.behaviours.AttackBehaviour;
import game.behaviours.WanderBehaviour;
import game.utils.Status;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class for all enemies in the game.
 */
public abstract class Enemy extends Actor implements Resettable {

    /**
     * A map of behaviours for the enemy.
     */
    protected Map<Integer, Behaviour> behaviours = new HashMap<>();
    private int spawnRate;
    private GameMap map;

    /**
     * Constructor.
     *
     * @param name        the name of the Actor
     * @param displayChar the character that will represent the Actor in the display
     * @param hitPoints   the Actor's starting hit points
     * @param spawnRate   the spawn rate of the enemy
     */
    public Enemy(String name, char displayChar, int hitPoints, int spawnRate) {
        super(name, displayChar, hitPoints);
        this.spawnRate = spawnRate;
        this.addCapability(Status.IS_ENEMY);
        behaviours.put(2, new AttackBehaviour());
        behaviours.put(999, new WanderBehaviour());
        GameResetManager.getInstance().addResettable(this);
    }

    public void setSpawnRate(int spawnRate) {
        this.spawnRate = spawnRate;
    }
    /**
     * Returns the spawn rate of the enemy
     *
     * @return the spawn rate of the enemy
     */
    public int getSpawnRate() {
        return this.spawnRate;
    }

    /**
     * At each turn, select a valid action to perform.
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return the valid action that can be performed in that iteration or null if no valid action is found
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        this.map = map;
        for (Behaviour behaviour : behaviours.values()) {
            Action action = behaviour.getAction(this, map);
            if(action != null)
                return action;
        }
        return new DoNothingAction();
    }

    /**
     * The wandering undead can be attacked by any actor that has the HOSTILE_TO_ENEMY capability
     *
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return a list containing actions
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        if (otherActor.hasCapability(Status.HOSTILE_TO_ENEMY)){
            actions.add(new AttackAction(this, direction, map));
        }
        return actions;
    }

    @Override
    public void reset() {
        if (map != null)  map.removeActor(this);
    }
}