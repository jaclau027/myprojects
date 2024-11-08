package game.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
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
 * A class which represents a type of enemy that is a Red Wolf.
 * Created by:
 * @author Jun Chew
 */
public class RedWolf extends Enemy implements WeatherSubscriber {
    Random random = new Random();
    private static final int BASE_DAMAGE = 15;
    private static final int SUNNY_DAMAGE_MULTIPLIER = 3;
    private static final double RAINY_SPAWN_RATE_MULTIPLIER = 1.5;
    private final Weather weather = Weather.getInstance();
    private int damage = BASE_DAMAGE;
    private final int spawnRate = this.getSpawnRate();

    /**
     * Constructor for RedWolf.
     */
    public RedWolf() {
        super("Red Wolf", 'r', 25, 30);
        update(weather.getCurrentWeather());
    }

    /**
     * A method to get the intrinsic weapon of the enemy
     * @return the intrinsic weapon of the enemy
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(damage, "bits", 80);
    }

    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        update(weather.getCurrentWeather());
        return super.playTurn(actions, lastAction, map, display);
    }

    /**
     * A method to execute when the enemy is unconscious
     * @param actor the perpetrator
     * @param map where the actor fell unconscious
     * @return a string of the actions executed
     */
    @Override
    public String unconscious(Actor actor, GameMap map) {
        String ret = "\n";
        String runeDrop = new DropAction(new Rune(50)).execute(this, map);
        ret += " " + runeDrop;
        if (random.nextInt(100) <  10) {
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

    /**
     * A method to update the enemy based on the weather
     * @param weatherType the weather type
     */
    @Override
    public void update(WeatherType weatherType) {
        if (weatherType == WeatherType.SUNNY) {
            damage = BASE_DAMAGE * SUNNY_DAMAGE_MULTIPLIER;
            this.setSpawnRate(spawnRate);
        } else {
            damage = BASE_DAMAGE;
            this.setSpawnRate((int) (spawnRate * RAINY_SPAWN_RATE_MULTIPLIER));
        }
    }
}
