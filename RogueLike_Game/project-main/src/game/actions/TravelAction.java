package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.MoveActorAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * Action for travelling to another map.
 * Created by:
 * @author Jun Chew
 * */
public class TravelAction extends MoveActorAction {
    private Location moveToLocation;
    private String direction;

    /**
     * Constructor.
     *
     * @param moveToLocation the location to move to
     * @param direction the direction where the travel should be performed (only used for display purposes)
     */
    public TravelAction(Location moveToLocation, String direction) {
        super(moveToLocation, direction);
        this.moveToLocation = moveToLocation;
        this.direction = direction;
    }

    /**
     * Perform the Action.
     *
     * @param actor The actor performing the action.
     * @param currentMap The map the actor is on.
     * @return a description of the action after it is processed
     */
    @Override
    public String execute(Actor actor, GameMap currentMap) {
        currentMap.moveActor(actor, moveToLocation);
        return menuDescription(actor);
    }

    /**
     * Returns a descriptive string
     *
     * @param actor The actor performing the action.
     * @return a string, e.g. "Player travels to the next map"
     */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " travels to " + direction;
    }
}
