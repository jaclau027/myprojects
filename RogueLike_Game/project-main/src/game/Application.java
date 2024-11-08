package game;

import java.util.Arrays;
import java.util.List;

import edu.monash.fit2099.engine.actors.attributes.ActorAttributeOperations;
import edu.monash.fit2099.engine.actors.attributes.BaseActorAttributes;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.FancyGroundFactory;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.World;
import game.actions.TravelAction;
import game.enemies.Abxervyer;
import game.enemies.WanderingUndead;
import game.enemies.factories.*;
import game.grounds.*;
import game.grounds.Void;
import game.items.Bloodberry;
import game.items.HealingVial;
import game.items.OldKey;
import game.items.RefreshingFlask;
import game.reset.GameResetManager;
import game.traders.BlackSmith;
import game.traders.Traveller;
import game.utils.FancyMessage;
import game.weapons.Broadsword;
import game.weapons.GiantHammer;
import game.weapons.GreatKnife;

/**
 * The main class to start the game.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * Jun Chew
 */
public class Application {

    /**
     * The main method to start the game.
     * @param args  The arguments
     */
    public static void main(String[] args) {

        World world = new World(new Display());

        FancyGroundFactory groundFactory = new FancyGroundFactory(new Dirt(),
                new Wall(), new Floor(), new Puddle(), new Void());

        // Initialize the maps
        GameMap theAbandonedVillage = initializeAbandonedVillage(groundFactory, world);
        GameMap burialGround = initializeBurialGround(groundFactory, world);
        GameMap ancientWoods = initializeAncientWoods(groundFactory, world);
        GameMap abxervyerBattleMap = initializeAbxervyerBattleMap(groundFactory, world);
        GameMap overgrownSanctuary = initializeOvergrownSanctuary(groundFactory, world);

        displayTitle();

        // Populate the maps
        populateAbandonedVillage(theAbandonedVillage, burialGround);
        populateBurialGround(burialGround, theAbandonedVillage, ancientWoods);
        populateAncientWoods(ancientWoods, theAbandonedVillage, abxervyerBattleMap);
        populateAbxervyerBattleMap(abxervyerBattleMap, ancientWoods, overgrownSanctuary);
        populateOvergrownSanctuaryMap(overgrownSanctuary, abxervyerBattleMap);

        // Add player to game
        addPlayerToGame(world, theAbandonedVillage);
        GameResetManager.getInstance().setSpawnMap(theAbandonedVillage);

        world.run();
    }

    /**
     * Method to initialize the Abandoned Village map
     * @param groundFactory The ground factory
     * @param world The game world
     * @return The Abandoned Village map
     */
    private static GameMap initializeAbandonedVillage(FancyGroundFactory groundFactory, World world) {
        List<String> map = Arrays.asList(
                "...........................................................",
                "...#######.................................................",
                "...#__.......................................++++..........",
                "...#..___#...................................+++++++.......",
                "...###.###................#######..............+++.........",
                "..........................#_____#................+++.......",
                "........~~................#_____#.................+........",
                ".........~~~..............###_###................++........",
                "...~~~~~~~~....+++.........................................",
                "....~~~~~........+++++++..................###..##...++++...",
                "~~~~~~~..............+++..................#___..#...++.....",
                "~~~~~~.................++.................#..___#....+++...",
                "~~~~~~~~~.................................#######.......++.");
        GameMap villageMap = new GameMap(groundFactory, map);
        world.addGameMap(villageMap);
        return villageMap;
    }

    /**
     * Method to initialize the Burial Ground map
     * @param groundFactory The ground factory
     * @param world The game world
     * @return The Burial Ground map
     */
    private static GameMap initializeBurialGround(FancyGroundFactory groundFactory, World world) {
        List<String> map = Arrays.asList(
                "...........+++++++........~~~~~~++....~~",
                "...........++++++.........~~~~~~+.....~~",
                "............++++...........~~~~~......++",
                "............+.+.............~~~.......++",
                "..........++~~~.......................++",
                ".........+++~~~....#######...........+++",
                ".........++++~.....#_____#.........+++++",
                "..........+++......#_____#........++++++",
                "..........+++......###_###.......~~+++++",
                "..........~~.....................~~...++",
                "..........~~~..................++.......",
                "...........~~....~~~~~.........++.......",
                "......~~....++..~~~~~~~~~~~......~......",
                "....+~~~~..++++++++~~~~~~~~~....~~~.....",
                "....+~~~~..++++++++~~~..~~~~~..~~~~~....");
        GameMap burialMap = new GameMap(groundFactory, map);
        world.addGameMap(burialMap);
        return burialMap;
    }

    /**
     * Method to initialize the Ancient Woods map
     * @param groundFactory The ground factory
     * @param world The game world
     * @return The Ancient Woods map
     */
    private static GameMap initializeAncientWoods(FancyGroundFactory groundFactory, World world) {
        List<String> map = Arrays.asList(
                "....+++..............................+++++++++....~~~....~~~",
                "+...+++..............................++++++++.....~~~.....~~",
                "++...............#######..............++++.........~~.......",
                "++...............#_____#...........................~~~......",
                "+................#_____#............................~~......",
                ".................###_###............~...............~~.....~",
                "...............................~.+++~~..............~~....~~",
                ".....................~........~~+++++...............~~~...~~",
                "....................~~~.........++++............~~~~~~~...~~",
                "....................~~~~.~~~~..........~........~~~~~~.....~",
                "++++...............~~~~~~~~~~~........~~~.......~~~~~~......",
                "+++++..............~~~~~~~~~~~........~~~........~~~~~......");
        GameMap woodsMap = new GameMap(groundFactory, map);
        world.addGameMap(woodsMap);
        return woodsMap;
    }

    /**
     * Method to initialize the Abxervyer Battle Map
     * @param groundFactory The ground factory
     * @param world The game world
     * @return The Abxervyer Battle Map
     */
    private static GameMap initializeAbxervyerBattleMap(FancyGroundFactory groundFactory, World world) {
        List<String> map = Arrays.asList(
                "~~~~.......+++......~+++++..............",
                "~~~~.......+++.......+++++..............",
                "~~~++......+++........++++..............",
                "~~~++......++...........+..............+",
                "~~~~~~...........+.......~~~++........++",
                "~~~~~~..........++++....~~~~++++......++",
                "~~~~~~...........+++++++~~~~.++++.....++",
                "~~~~~..............++++++~~...+++.....++",
                "......................+++......++.....++",
                ".......................+~~............++",
                ".......................~~~~...........++",
                "........................~~++...........+",
                ".....++++...............+++++...........",
                ".....++++~..............+++++...........",
                "......+++~~.............++++...........~",
                ".......++..++++.......................~~",
                "...........+++++......................~~",
                "...........++++++.....................~~",
                "..........~~+++++......................~",
                ".........~~~~++++..................~~..~");
        GameMap battleMap = new GameMap(groundFactory, map);
        world.addGameMap(battleMap);
        return battleMap;
    }

    private static GameMap initializeOvergrownSanctuary(FancyGroundFactory groundFactory, World world) {
        List<String> map = Arrays.asList(
                "++++.....++++........++++~~~~~.......~~~..........",
                "++++......++.........++++~~~~.........~...........",
                "+++..................+++++~~.......+++............",
                "....................++++++......++++++............",
                "...................++++........++++++~~...........",
                "...................+++.........+++..~~~...........",
                "..................+++..........++...~~~...........",
                "~~~...........................~~~..~~~~...........",
                "~~~~............+++..........~~~~~~~~~~...........",
                "~~~~............+++.........~~~~~~~~~~~~..........",
                "++~..............+++.......+~~........~~..........",
                "+++..............+++......+++..........~~.........",
                "+++..............+++......+++..........~~.........",
                "~~~..............+++......+++..........~~~........",
                "~~~~.............+++......+++..........~~~........");
        GameMap overgrownSanctuaryMap = new GameMap(groundFactory, map);
        world.addGameMap(overgrownSanctuaryMap);
        return overgrownSanctuaryMap;
    }

    /**
     * Method to display the title of the game
     */
    private static void displayTitle() {
        for (String line : FancyMessage.TITLE.split("\n")) {
            new Display().println(line);
            try {
                Thread.sleep(200);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Method to populate the Abandoned Village map
     * @param theAbandonedVillage The Abandoned Village map
     * @param burialGround The Burial Ground map
     */
    private static void populateAbandonedVillage(GameMap theAbandonedVillage, GameMap burialGround) {

        theAbandonedVillage.at(27, 6).addItem(new Broadsword());

        LockedGate lockedGate = new LockedGate(new TravelAction(burialGround.at(20, 0), "Burial Ground"));
        theAbandonedVillage.at(29, 9).setGround(lockedGate);
        theAbandonedVillage.at(52, 1).setGround(new Graveyard(new WanderingUndeadFactory()));
        theAbandonedVillage.at(30, 5).addActor(new BlackSmith());
    }

    /**
     * Method to populate the Burial Ground map
     * @param burialGround The Burial Ground map
     * @param theAbandonedVillage The Abandoned Village map
     * @param ancientWoods The Ancient Woods map
     */
    private static void populateBurialGround(GameMap burialGround, GameMap theAbandonedVillage, GameMap ancientWoods) {

        LockedGate lockedGate1 = new LockedGate(new TravelAction(theAbandonedVillage.at(25, 0), "The Abandoned Village"));
        LockedGate lockedGate2 = new LockedGate(new TravelAction(ancientWoods.at(21, 4), "Ancient Woods"));

        burialGround.at(5, 3).setGround(new Graveyard(new HollowSoldierFactory()));
        burialGround.at(19, 0).setGround((lockedGate1));
        burialGround.at(21, 0).setGround(lockedGate2);
    }

    /**
     * Method to populate the Ancient Woods map
     * @param ancientWoods The Ancient Woods map
     * @param burialGround The Burial Ground map
     * @param abxervyerBattleMap The Abxervyer Battle Map
     */
    private static void populateAncientWoods(GameMap ancientWoods, GameMap burialGround, GameMap abxervyerBattleMap) {

        ancientWoods.at(20, 3).addActor(new Traveller());
        ancientWoods.at(32, 0).addItem(new Bloodberry());

        LockedGate lockedGate3 = new LockedGate(new TravelAction(burialGround.at(0, 0), "Burial Ground"));
        LockedGate lockedGate4 = new LockedGate(new TravelAction(abxervyerBattleMap.at(6, 0), "Abxervyer Battle Map"));

        ancientWoods.at(15, 0).setGround(lockedGate3);
        ancientWoods.at(18, 0).setGround(lockedGate4);
        ancientWoods.at(2, 1).setGround(new EmptyHut(new ForestKeeperFactory()));
        ancientWoods.at(41, 8).setGround(new Bush(new RedWolfFactory()));
    }

    /**
     * Method to populate the Abxervyer Battle Map
     * @param abxervyerBattleMap The Abxervyer Battle Map
     * @param ancientWoods The Ancient Woods map
     */
    private static void populateAbxervyerBattleMap(GameMap abxervyerBattleMap, GameMap ancientWoods, GameMap overgrownSanctuary) {

        abxervyerBattleMap.at(10, 9).setGround(new EmptyHut(new ForestKeeperFactory()));
        abxervyerBattleMap.at(12, 11).setGround(new EmptyHut(new ForestKeeperFactory()));
        abxervyerBattleMap.at(20, 15).setGround(new Bush(new RedWolfFactory()));
        abxervyerBattleMap.at(26, 18).setGround(new Bush(new RedWolfFactory()));
        abxervyerBattleMap.at(32, 1).setGround(new Bush(new RedWolfFactory()));
        abxervyerBattleMap.at(7, 7).addActor(new Abxervyer(ancientWoods, overgrownSanctuary));

    }

    private static void populateOvergrownSanctuaryMap(GameMap overgrownSanctuary, GameMap abxervyerBattleMap) {

        overgrownSanctuary.at(14, 4).setGround(new LockedGate(new TravelAction(abxervyerBattleMap.at(6, 0), "Abxervyer Battle Map")));
        overgrownSanctuary.at(15,5).setGround(new EmptyHut(new EldentreeGuardianFactory()));
        overgrownSanctuary.at(4,4).setGround(new Graveyard(new HollowSoldierFactory()));
        overgrownSanctuary.at(6,6).setGround(new Bush(new LivingBranchFactory()));
        overgrownSanctuary.at(7,7).addItem(new OldKey());
        overgrownSanctuary.at(7,6).setGround(new Void());
    }

    /**
     * Method to add the player to the game
     * @param world The game world
     * @param theAbandonedVillage The Abandoned Village map
     */
    private static void addPlayerToGame(World world, GameMap theAbandonedVillage) {

        Player player = new Player("The Abstracted One", '@', 150);

        player.addBalance(0);

//        player.addItemToInventory(new OldKey());
//        player.addItemToInventory(new Broadsword());
//        player.addItemToInventory(new Bloodberry());
//        player.addItemToInventory(new RefreshingFlask());
//        player.addItemToInventory(new HealingVial());
//        player.addItemToInventory(new GreatKnife());
//        player.addItemToInventory(new GiantHammer());

        world.addPlayer(player, theAbandonedVillage.at(29, 5));
    }
}