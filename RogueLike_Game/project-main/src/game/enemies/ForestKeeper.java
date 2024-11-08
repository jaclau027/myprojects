package game.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.items.DropAction;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.weather.Weather;
import game.weather.WeatherSubscriber;
import game.weather.WeatherType;
import game.behaviours.FollowBehaviour;
import game.items.HealingVial;
import game.items.Rune;
import game.utils.Status;

import java.util.Random;

/**
 * A class that represents an enemy of the game (Forest Keeper)
 * Created by:
 * @author Jun Chew
 */
public class ForestKeeper extends Enemy implements WeatherSubscriber {

    private static final int SUNNY_SPAWN_RATE_MULTIPLIER = 2;
    private static final int RAINY_HEALTH_INCREASE = 10;
    private final Weather weather = Weather.getInstance();
    private final int baseSpawnRate = this.getSpawnRate();
    Random random = new Random();

    /**
     * Constructor for ForestKeeper.
     */
    public ForestKeeper() {
        super("Forest Keeper", '8', 125, 15);
        update(weather.getCurrentWeather());
    }

    /**
     * A method to get the intrinsic weapon of the enemy
     * @return the intrinsic weapon of the enemy
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(25, "whacks", 75);
    }

    /**
     * A method to execute when the enemy is unconscious
     * @param actor the perpetrator
     * @param map where the actor fell unconscious
     * @return a string describing the action
     */
    @Override
    public String unconscious(Actor actor, GameMap map) {
        String ret = "\n";
        String runeDrop = new DropAction(new Rune(50)).execute(this, map);
        ret += " " + runeDrop;
        if (random.nextInt(100) < 20) {
            String healingVialDrop = new DropAction(new HealingVial()).execute(this, map);
            ret += " " + healingVialDrop;
        }
        return super.unconscious(actor, map) + ret;
    }

    /**
     * A method to execute when the enemy is dead
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return the action to be executed
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        update(weather.getCurrentWeather());
        return super.playTurn(actions, lastAction, map, display);
    }

    /**
     * A method to update the enemy based on the weather
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return an ActionList containing the allowable actions
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = super.allowableActions(otherActor, direction, map);
        if (otherActor.hasCapability(Status.HOSTILE_TO_ENEMY)) {
            this.behaviours.put(1, new FollowBehaviour(otherActor));
        }
        return actions;
    }

    /**
     * A method to update the enemy based on the weather
     * @param weatherType the weather type
     */
    @Override
    public void update(WeatherType weatherType) {
        if (weatherType == WeatherType.SUNNY) {
            this.setSpawnRate(baseSpawnRate * SUNNY_SPAWN_RATE_MULTIPLIER);
        } else {
            this.setSpawnRate(baseSpawnRate);
            this.modifyAttribute(BaseActorAttributes.HEALTH, ActorAttributeOperations.INCREASE, RAINY_HEALTH_INCREASE);
        }
    }
}
