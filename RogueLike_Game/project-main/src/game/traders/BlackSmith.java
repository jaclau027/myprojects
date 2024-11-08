package game.traders;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.actions.SpeakAction;
import game.utils.Status;
import java.util.ArrayList;
import java.util.Random;


public class BlackSmith extends Actor implements Speakable {

    private ArrayList<String> dialogue = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Constructor of Blacksmith trader.
     */
    public BlackSmith() {
        super("BlackSmith", 'B', 100000000);
        this.addCapability(Status.IS_MAKER);
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
     * Method to get a random element in the dialogue attribute.
     * @return a string that contains a dialogue
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
        dialogue.add("I used to be an adventurer like you, but then …. Nevermind, let’s get back to smithing.");
        dialogue.add("It’s dangerous to go alone. Take my creation with you on your adventure!");
        dialogue.add("Ah, it’s you. Let’s get back to make your weapons stronger.");
    }

    /**
     * Method to get allowable actions of Blacksmith.
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return a list of allowable actions
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = super.allowableActions(otherActor, direction, map);

        // Resets all dialogue to check all conditions again
        dialogue = new ArrayList<>();

        if (!otherActor.hasCapability(Status.IS_ENEMY) && otherActor.hasCapability(Status.HOLDING_GREAT_KNIFE)) {
            dialogue.add("Hey now, that’s a weapon from a foreign land that I have not seen for so long. I can upgrade it for you if you wish.");
        }
        if (!otherActor.hasCapability(Status.IS_ENEMY) && otherActor.hasCapability(Status.DEFEATED_BOSS)) {
            dialogue.add("Somebody once told me that a sacred tree rules the land beyond the ancient woods until this day.");
        }
        else if (!otherActor.hasCapability(Status.IS_ENEMY) && !otherActor.hasCapability(Status.DEFEATED_BOSS)) {
            dialogue.add("Beyond the burial ground, you’ll come across the ancient woods ruled by Abxervyer. Use my creation to slay them and proceed further!");
        }

        // Add basic dialogues
        generateDialogue();
        actions.add(new SpeakAction(getDialogue(), this));
        return actions;
    }
}