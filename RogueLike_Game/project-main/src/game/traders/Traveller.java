package game.traders;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.BuyAction;
import game.actions.SpeakAction;
import game.items.HealingVial;
import game.items.RefreshingFlask;
import game.utils.Status;
import game.weapons.Broadsword;
import game.weapons.GreatKnife;

import java.util.ArrayList;
import java.util.Random;

/**
 * A class that represents the Traveller trader.
 * Created by:
 * @author Jun Chew
 */
public class Traveller extends Actor implements Speakable {

    private ArrayList<String> dialogue = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Constructor of Traveller trader.
     */
    public Traveller() {
        super("Traveller", 'ඞ', 100000000);
        this.addCapability(Status.IS_MERCHANT);
    }

    /**
     * Select and return an action to perform on the current turn.
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return the Action to be performed
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        return new DoNothingAction();
    }

    /**
     * Method to get allowable actions of Traveller trader.
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return a list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = super.allowableActions(otherActor, direction, map);
        actions.add(new BuyAction(new HealingVial()));
        actions.add(new BuyAction(new RefreshingFlask()));
        actions.add(new BuyAction(new Broadsword()));
        actions.add(new BuyAction(new GreatKnife()));

        // Resets all dialogue to check all conditions again
        dialogue = new ArrayList<>();
        if (!otherActor.hasCapability(Status.IS_ENEMY) && otherActor.hasCapability(Status.HOLDING_GIANT_HAMMER)) {
            dialogue.add("Ooh, that’s a fascinating weapon you got there. I will pay a good price for it. You wouldn't get this price from any other guy.");
        } if (!otherActor.hasCapability(Status.IS_ENEMY) && !otherActor.hasCapability(Status.DEFEATED_BOSS)) {
            dialogue.add("You know the rules of this world, and so do I. Each area is ruled by a lord. Defeat the lord of this area, Abxervyer, and you may proceed to the next area.");
        } if (!otherActor.hasCapability(Status.IS_ENEMY) && otherActor.hasCapability(Status.DEFEATED_BOSS) && otherActor.hasCapability(Status.HOLDING_GIANT_HAMMER)) {
            dialogue.add("Congratulations on defeating the lord of this area. I noticed you still hold on to that hammer. Why don’t you sell it to me? We've known each other for so long. I can tell you probably don’t need that weapon any longer.");
        }

        // Add basic dialogues
        generateDialogue();
        actions.add(new SpeakAction(getDialogue(), this));
        return actions;
    }

    /**
     * Method to that loads default dialogues to the dialogue attribute.
     */
    @Override
    public String getDialogue() {
        return dialogue.get(random.nextInt(dialogue.size()));
    }

    /**
     * Method to that loads default dialogues to the dialogue attribute.
     */
    @Override
    public void generateDialogue() {
        dialogue.add("Of course, I will never give you up, valuable customer!");
        dialogue.add("I promise I will never let you down with the quality of the items that I sell.");
        dialogue.add("You can always find me here. I'm never gonna run around and desert you, dear customer!");
        dialogue.add("I'm never gonna make you cry with unfair prices.");
        dialogue.add("Trust is essential in this business. I promise I’m never gonna say goodbye to a valuable customer like you.");
        dialogue.add("Don't worry, I’m never gonna tell a lie and hurt you.");
    }
}