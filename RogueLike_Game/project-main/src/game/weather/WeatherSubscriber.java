package game.weather;

/**
 * Created by
 * @author Jun Chew
 */

/**
 * An interface that represents a weather subscriber.
 */
public interface WeatherSubscriber {

    /**
     * Update the weather type.
     * @param weatherType the weather type
     */
    void update(WeatherType weatherType);

}
