package game.utils;

/**
 * Use this enum class to represent a status.
 * Example #1: if the player is sleeping, you can attack a Status.SLEEP to the player class
 * Created by:
 * @author Riordan D. Alfredo
 */
public enum Status {
    HOSTILE_TO_ENEMY, // use this status to tell that the actor is hostile to enemy
    IS_ENEMY, // use this status to tell that the actor is an enemy
    UNLOCKED, // use this status to tell that the actor has unlocked the gate
    IS_MERCHANT, // use this status to tell that the actor is a trader
    IS_MAKER,
    HOLDING_GREAT_KNIFE,
    DEFEATED_BOSS,
    IS_GREAT_KNIFE,
    HOLDING_GIANT_HAMMER,
    IS_GIANT_HAMMER,
    IS_BOSS,
    REMOVED
}