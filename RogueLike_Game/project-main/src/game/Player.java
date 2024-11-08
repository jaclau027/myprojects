package game;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttribute;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.displays.Menu;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.positions.World;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.actions.AttackAction;
import game.items.Rune;
import game.reset.GameResetManager;
import game.reset.Resettable;
import game.utils.FancyMessage;
import game.utils.Status;

/**
 * Class representing the Player.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * Jun Chew
 */
public class Player extends Actor implements Resettable {

    private int balance;
    private Display display;

    /**
     * Constructor.
     *
     * @param name        Name to call the player in the UI
     * @param displayChar Character to represent the player in the UI
     * @param hitPoints   Player's starting number of hitpoints
     */
    public Player(String name, char displayChar, int hitPoints) {
        super(name, displayChar, hitPoints);
        this.addCapability(Status.HOSTILE_TO_ENEMY);
        this.addAttribute(BaseActorAttributes.STAMINA, new BaseActorAttribute(200));
        GameResetManager.getInstance().addResettable(this);
    }

    /**
     * Return the action can done by the player
     *
     * @param actions   The actions that the player can do
     * @param lastAction    The last action that the player did
     * @param map   The map that the player is in
     * @param display   The display that will be shown
     * @return  The action that the player will do
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (this.hasCapability(Status.DEFEATED_BOSS)){
            System.out.println("DEFEATED BOSS attribute is active!");
        }
        balance = this.getBalance();
        this.display = display;

//        increaseStamina(0.01);

        display.println(this.name + "\nHP: " + printHealth() + "\nStamina: " + printStamina());
        display.println("\nWallet Balance: " + this.getBalance());

        this.removeCapability(Status.HOLDING_GREAT_KNIFE);
        this.removeCapability(Status.HOLDING_GIANT_HAMMER);

        // Handle multi-turn Actions
        if (lastAction.getNextAction() != null)
            return lastAction.getNextAction();

        // return/print the console menu
        Menu menu = new Menu(actions);
        return menu.showMenu(this, display);
    }

    /**
     * Return the intrinsic weapon of the player.
     * @return  The intrinsic weapon of the player
     */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(15, "bonks", 80);
    }

    /**
     * Return player's health in formatted way.
     * @return  The health of the player
     */
    private String printHealth() {
        return this.getAttribute(BaseActorAttributes.HEALTH) + "/" +
                this.getAttributeMaximum(BaseActorAttributes.HEALTH);
    }

    /**
     * Return player's stamina in formatted way.
     * @return  The stamina of the player
     */
    private String printStamina() {
        return this.getAttribute(BaseActorAttributes.STAMINA) + "/" +
                this.getAttributeMaximum(BaseActorAttributes.STAMINA);
    }

    /**
     * Return player's maximum stamina.
     * @return  The maximum stamina of the player
     */
    private int getMaxStamina() {
        return this.getAttributeMaximum(BaseActorAttributes.STAMINA);
    }

    /**
     * Increase player's stamina by a percentage of the maximum stamina.
     * @param percentage    The percentage of the maximum stamina that will be added to the player's stamina
     */
    public void increaseStamina(double percentage) {
        this.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.INCREASE, (int) (getMaxStamina() * percentage));
    }

    /**
     * Returns a new collection of the Actions that the otherActor can do to the current Actor.
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return A collection of Actions.
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        if(otherActor.hasCapability(Status.IS_ENEMY)){
            actions.add(new AttackAction(this, direction, map));
        }
        return actions;
    }

    @Override
    public void reset() {
        this.deductBalance(balance);
        this.modifyAttribute(BaseActorAttributes.HEALTH, ActorAttributeOperations.INCREASE, this.getAttributeMaximum(BaseActorAttributes.HEALTH));
        this.modifyAttribute(BaseActorAttributes.STAMINA, ActorAttributeOperations.INCREASE, this.getAttributeMaximum(BaseActorAttributes.STAMINA));
    }

    public String unconscious(GameMap map) {
        handleUnconscious(map);
        return this + " ceased to exist.";
    }

    public String unconscious(Actor actor, GameMap map) {
        handleUnconscious(map);
        return this + " met their demise in the hand of " + actor;
    }

    public void handleUnconscious(GameMap map) {

        int x = map.locationOf(this).x();
        int y = map.locationOf(this).y();

        map.removeActor(this);

        Location spawnLocation = GameResetManager.getInstance().getSpawnMap().at(29, 5);
        spawnLocation.map().addActor(this, spawnLocation);

        GameResetManager.getInstance().execute();
        map.at(x, y).addItem(new Rune(balance));

        display.println(FancyMessage.YOU_DIED);
    }

}