package game.reset;

import edu.monash.fit2099.engine.positions.GameMap;

import java.util.ArrayList;

/**
 * A class that represents a game reset manager
 * Created by:
 * @author Ting Ting
 */
public class GameResetManager {
    /**
     * The map where the player spawns
     */
    private GameMap spawnMap;
    private final ArrayList<Resettable> resettables = new ArrayList<>();
    private static final GameResetManager instance = new GameResetManager();

    /**
     * A function which returns the instance of the GameResetManager
     * @return
     */
    public static GameResetManager getInstance() { return instance; }

    /**
     * A function which executes and resets all the resettables
     */
    public void execute() { resettables.forEach(Resettable::reset); }

    /**
     * A function which adds a resettable to the resettables list
     * @param resettable
     */
    public void addResettable(Resettable resettable) { resettables.add(resettable); }

    /**
     * A function which returns the spawn map
     * @return The map where the player spawns
     */
    public GameMap getSpawnMap() { return spawnMap; }

    /**
     * A function which sets the spawn map
     * @param map The map where the player spawns
     */
    public void setSpawnMap(GameMap map) { this.spawnMap = map; }

}