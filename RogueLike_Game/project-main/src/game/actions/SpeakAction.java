package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

public class SpeakAction extends Action {
    private final String spokenSentence;
    private final Actor target;

    public SpeakAction(String spokenSentence, Actor target) {
        this.spokenSentence = spokenSentence;
        this.target = target;
    }

    @Override
    public String execute(Actor actor, GameMap map) {
        return target + ": " + spokenSentence;
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " listens to " + target;
    }
}
