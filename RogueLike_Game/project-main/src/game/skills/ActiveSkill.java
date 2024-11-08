package game.skills;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

public interface ActiveSkill {
    String activateSkill(Actor actor, GameMap map);

}
