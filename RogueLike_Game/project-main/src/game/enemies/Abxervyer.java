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
import game.behaviours.WanderBehaviour;
import game.utils.Ability;
import game.weather.Weather;
import game.actions.TravelAction;
import game.behaviours.FollowBehaviour;
import game.grounds.LockedGate;
import game.items.Rune;
import game.utils.Status;
import java.util.List;
import java.util.ArrayList;

/**
 * A class that represents the final boss of the game (Abxervyer).
 * Created by:
 * @author Ting Ting
 */
public class Abxervyer extends Enemy {

    private final Weather weather;
    private final GameMap ancientWoods;
    private final GameMap overGrownSanctuary;
    private Actor player;

    /**
     * Constructor for Abxervyer.
     * @param ancientWoods - ancientWoods GameMap object.
     * @param overgrownSanctuary - overgrownSanctuary GameMap object.
     */
    public Abxervyer(GameMap ancientWoods, GameMap overgrownSanctuary) {
        super("Abxervyer, The Forest Keeper", 'Y', 2000, 0);
        super.behaviours.put(999, new WanderBehaviour());
        this.weather = Weather.getInstance();
        this.ancientWoods = ancientWoods;
        this.overGrownSanctuary = overgrownSanctuary;
        this.addCapability(Status.IS_BOSS);
        this.addCapability(Ability.CAN_BE_ON_VOID);
    }

    /**
     * Method to get the intrinsic weapon of Abxervyer.
     * @return the intrinsic weapon of Abxervyer
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(80, "hits", 25);
    }

    /**
     * Method to execute when Abxervyer is unconscious.
     * @param actor the perpetrator
     * @param map where the actor fell unconscious
     * @return a string of the actions executed
     */
    @Override
    public String unconscious(Actor actor, GameMap map) {
        String result = new DropAction(new Rune(2000)).execute(this, map);
        List<TravelAction> actionList = new ArrayList<>();
        actionList.add(new TravelAction(this.ancientWoods.at(21, 4), "Ancient Woods"));
        actionList.add(new TravelAction(this.overGrownSanctuary.at(15, 4), "Overgrown Sanctuary"));
        LockedGate lockedGate = new LockedGate(actionList);
        map.locationOf(actor).setGround(lockedGate);
        player.addCapability(Status.DEFEATED_BOSS);
        weather.resetToDefault();
        return result + super.unconscious(actor, map);
    }

    /**
     * Method to execute when Abxervyer is playing its turn.
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return the action that Abxervyer will do
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        display.println(weather.changeWeather());
        display.println(String.valueOf(weather));
        return super.playTurn(actions, lastAction, map, display);
    }

    /**
     * Method to get the allowable actions of Abxervyer.
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return a collection of actions
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = super.allowableActions(otherActor, direction, map);
        if (otherActor.hasCapability(Status.HOSTILE_TO_ENEMY)){
            this.behaviours.put(1, new FollowBehaviour(otherActor));
            player = otherActor;
        }
        return actions;
    }

    @Override
    public void reset() {
        this.modifyAttribute(BaseActorAttributes.HEALTH, ActorAttributeOperations.INCREASE, this.getAttributeMaximum(BaseActorAttributes.HEALTH));
    }
}