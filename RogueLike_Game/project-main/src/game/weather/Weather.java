package game.weather;

/**
 * Created by
 * @author Ting Ting
 *
 * A class that represents the weather.
 */
public class Weather {

    private WeatherType currentWeather;
    private int turnsUntilChange;

    /**
     * Constructor which sets the initial weather to sunny.
     */
    private Weather() {
        this.currentWeather = WeatherType.SUNNY;
        this.turnsUntilChange = 3;
    }

    /**
     * Get the single instance of Weather.
     * @return The single instance of Weather
     */
    public static Weather getInstance() {
        return WeatherHolder.INSTANCE;
    }

   /**
    * Get the current weather.
    * @return The current weather
    */
    public WeatherType getCurrentWeather() {
        return currentWeather;
    }

    /**
     * Change the weather every 3 turns.
     */
    public String changeWeather() {
        String targetWeather = "";
        if (--turnsUntilChange <= 0) {
            turnsUntilChange = 3; // Reset the turns until the next weather change
            targetWeather = toggleWeather();
        }
        return targetWeather;
    }

    /**
     * Toggle the weather between sunny and rainy.
     */
    private String toggleWeather() {
        if (currentWeather == WeatherType.SUNNY) {
            currentWeather = WeatherType.RAINY;
        } else {
            currentWeather = WeatherType.SUNNY;
        }
        return "\nThe weather has been changed" ;
    }

    /**
     * Get weather in a String format
     */
    public String toString() {
        String weather;
        if (currentWeather == WeatherType.SUNNY) {
            weather = "sunny";
        } else if (currentWeather == WeatherType.RAINY) {
            weather = "rainy";
        } else {
            weather = "back to default";
        }
        return "The weather is currently " + weather + "\n";
    }

    /**
     * Reset the weather to the default value (assuming DEFAULT as a WeatherType).
     */
    public void resetToDefault() {
        currentWeather = WeatherType.DEFAULT;
    }

    /**
     * A private static class to hold the single instance of Weather.
     */
    private static class WeatherHolder {
        private static final Weather INSTANCE = new Weather();
    }
}
