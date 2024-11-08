package game.utils;

/**
 * Use this enum to represent abilities.
 * Example #1: if the player is capable jumping over walls, you can attach Ability.WALL_JUMP to the player class
 */
public enum Ability {
    CAN_UNLOCK_GATE, // use this ability to tell that the actor can unlock the gate
    CAN_SELL, // use this ability to tell that the actor can sell items
    CAN_UPGRADE,
    CAN_BE_ON_VOID,
    CAN_CONSUME
}
