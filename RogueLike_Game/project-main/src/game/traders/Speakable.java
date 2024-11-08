package game.traders;

public interface Speakable {
    /**
     * Method to get a random element in the dialogue attribute.
     * @return a string that contains a dialogue
     */
    String getDialogue();
    /**
     * Method to that loads default dialogues to the dialogue attribute.
     */
    void generateDialogue();
}
