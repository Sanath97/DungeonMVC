package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import controller.DungeonConsoleController;
import controller.DungeonConsoleControllerImpl;
import dungeonmodel.DungeonGame;
import dungeonmodel.DungeonGameImpl;
import dungeonmodel.GeneralPlayer;
import dungeonmodel.GeneralPlayerImpl;
import dungeonmodel.NumberGenerationInterface;
import dungeonmodel.PredictiveGeneration;
import dungeonmodel.RandomGeneration;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;

/**
 * This class includes tests for DungeonController class. This tests dungeon controller.
 */
public class DungeonConsoleControllerTest {
  private DungeonGame game;
  private Appendable gameLog;
  private GeneralPlayer player;

  @Before
  public void setup() {
    player = new GeneralPlayerImpl("testPlayer");
    NumberGenerationInterface random = new RandomGeneration();
    game = new DungeonGameImpl(5, 5, 1, false, 50, 5, player,
            random);
    gameLog = new StringBuilder();
  }

  @Test
  public void playGameInvalidModel() {
    StringReader input = new StringReader("m");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    try {
      controller.playGame(null);
    }
    catch (IllegalArgumentException exception) {
      assertEquals("Game object cannot be null", exception.getMessage());
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testFailingAppendable() {
    // Testing when something goes wrong with the Appendable
    // Here we are passing it a mock of an Appendable that always fails
    StringReader input = new StringReader("m north q");
    Appendable failingGameLog = new FailingAppendable();
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, failingGameLog);
    controller.playGame(game);
  }

  @Test
  public void playGameTestQuit() {
    StringReader input = new StringReader("q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    controller.playGame(game);
    assertTrue(gameLog.toString().contains("Quitting the game!"));
  }

  @Test
  public void playGameInvalidCommand() {
    StringReader input = new StringReader("r q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Give a valid command. Valid commands: (M-P-S)/(m-p-s) \n";
    try {
      controller.playGame(game);
    }

    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void playGameInvalidDirectionForMoveString() {
    StringReader input = new StringReader("m invalidDirection q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Give a valid direction.\n";
    try {
      controller.playGame(game);
    }
    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void playGameUnexistingDirectionForMove() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,4,
            8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,1,
            0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    StringReader input = new StringReader("m east q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Given direction is not possible from current location\n";
    controller.playGame(game);
    assertTrue(gameLog.toString().contains(expected));
  }

  @Test
  public void playGameInvalidDirectionForMoveNumber() {
    StringReader input = new StringReader("m 2 q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Give a valid direction.\n";
    try {
      controller.playGame(game);
    }
    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void playGameInvalidPickUpItemString() {
    StringReader input = new StringReader("p gun q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Give a valid input to pick. Valid Inputs: [arrow, diamond, ruby, "
            + "sapphire]\n";
    try {
      controller.playGame(game);
    }
    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void playGameInvalidPickUpItemNumber() {
    StringReader input = new StringReader("p 1 q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Give a valid input to pick. Valid Inputs: [arrow, diamond, ruby, "
            + "sapphire]\n";
    try {
      controller.playGame(game);
    }
    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void testPickArrowFromEmptyArrowLocation() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,4,
            8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,1,
            0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    StringReader input = new StringReader("p arrow q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Welcome to the Dungeon game. All the best.\n"
            + "You smell something MOREPUNGENT here\n"
            + "You are in a cave\n"
            + "You find 1 RUBIES,  here\n"
            + "Doors lead to [NORTH, SOUTH, WEST]\n"
            + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
            + "What?\n"
            + "Current location don't have arrows\n"
            + "You smell something MOREPUNGENT here\n"
            + "You are in a cave\n"
            + "You find 1 RUBIES,  here\n"
            + "Doors lead to [NORTH, SOUTH, WEST]\n"
            + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
            + "Quitting the game!";
    controller.playGame(game);
    assertEquals(expected, gameLog.toString());
  }

  @Test
  public void testPickUnAvailableTreasure() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    StringReader input = new StringReader("p diamond q");
    String expected = "Welcome to the Dungeon game. All the best.\n"
            + "You smell something MOREPUNGENT here\n"
            + "You are in a cave\n"
            + "You find 1 RUBIES,  here\n"
            + "Doors lead to [NORTH, SOUTH, WEST]\n"
            + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
            + "What?\n"
            + "Give from a valid treasure in the location\n"
            + "You smell something MOREPUNGENT here\n"
            + "You are in a cave\n"
            + "You find 1 RUBIES,  here\n"
            + "Doors lead to [NORTH, SOUTH, WEST]\n"
            + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
            + "Quitting the game!";
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    controller.playGame(game);
    assertEquals(expected, gameLog.toString());
  }

  @Test
  public void testIfInvalidInputDoNotEndGame() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,4,
            8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,1,
            0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    StringReader input = new StringReader("r m invalidd p gun p rubies s -5 south s 2 northe q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    controller.playGame(game);
    String expected = "Welcome to the Dungeon game. All the best.\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Give a valid command. Valid commands: (M-P-S)/(m-p-s) \n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "Give a valid direction.\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Give a valid input to pick. Valid Inputs: [arrow, diamond, ruby, "
                      + "sapphire]\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Give a valid input to pick. Valid Inputs: [arrow, diamond, ruby, "
                      + "sapphire]\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Number of Caves?(1-5)\n"
                      + "Where to?\n"
                      + "Valid distances are (1 - 4). Both inclusive.\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Number of Caves?(1-5)\n"
                      + "Where to?\n"
                      + "Give a valid direction.\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Quitting the game!";
    assertEquals(expected, gameLog.toString());
  }

  @Test
  public void testShootWhenArrowsAreZero() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,4,
            8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,1,
            0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    StringReader input = new StringReader("s 1 west s 1 west s 1 south s 2 north q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    controller.playGame(game);
    String expected = "Welcome to the Dungeon game. All the best.\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Number of Caves?(1-5)\n"
                      + "Where to?\n"
                      + "You injured a Monster.\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Number of Caves?(1-5)\n"
                      + "Where to?\n"
                      + "You killed a Monster.\n"
                      + "You smell something LESSPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Number of Caves?(1-5)\n"
                      + "Where to?\n"
                      + "You injured a Monster.\n"
                      + "You smell something LESSPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Number of Caves?(1-5)\n"
                      + "Where to?\n"
                      + "Player doesn't have any arrows to shoot.\n"
                      + "You smell something LESSPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Quitting the game!";
    assertEquals(expected, gameLog.toString());
  }

  @Test
  public void testPickFromEmptyTreasureList() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    String expected = "Welcome to the Dungeon game. All the best.\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a tunnel\n"
                      + "You find 2 arrows here\n"
                      + "Doors lead to [EAST, NORTH]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Current location doesn't have any treasures\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a tunnel\n"
                      + "You find 2 arrows here\n"
                      + "Doors lead to [EAST, NORTH]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Quitting the game!";
    StringReader input = new StringReader("m west p diamond q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    controller.playGame(game);
    assertEquals(expected, gameLog.toString());
  }


  @Test
  public void playGameInvalidShootDistanceasString() {
    StringReader input = new StringReader("s invalid north q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Give a valid number.";
    try {
      controller.playGame(game);
    }
    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void playGameInvalidShootDistancesNegativeNumber() {
    StringReader input = new StringReader("s -8 north q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Give a valid number.";
    try {
      controller.playGame(game);
    }
    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void playGameInvalidShootDistancesPositiveNumber() {
    StringReader input = new StringReader("s 8 north q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Valid distances are (1 - 4). Both inclusive.";
    try {
      controller.playGame(game);
    }
    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void playGameInvalidShootDirectionAsString() {
    StringReader input = new StringReader("s 3 northe q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Give a valid direction.";
    try {
      controller.playGame(game);
    }
    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void playGameInvalidShootDirectionAsNumber() {
    StringReader input = new StringReader("s 3 4 q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Give a valid direction.";
    try {
      controller.playGame(game);
    }
    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void playGameInvalidDirectionForMoveWithIntForDirection() {
    StringReader input = new StringReader("m 2 q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Give a valid direction.";
    try {
      controller.playGame(game);
    }
    catch (IllegalArgumentException exception) {
      assertTrue(gameLog.toString().contains(expected));
    }
  }

  @Test
  public void testplayGamePlayerWins() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,4,
            8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,1,
            0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    StringReader input = new StringReader("p ruby m north p arrow p arrow p arrow m east m east"
                                          + " m south p arrow m west p sapphire m south p arrow s "
                                          + "1 east s 1 east p arrow m east q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Welcome to the Dungeon game. All the best.\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Player owns below treasures: \n"
                      + "{RUBIES=1}\n"
                      + "Player owns: 3 arrows\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 3 arrows here\n"
                      + "Doors lead to [EAST, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Player owns below treasures: \n"
                      + "{RUBIES=1}\n"
                      + "Player owns: 4 arrows\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 2 arrows here\n"
                      + "Doors lead to [EAST, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Player owns below treasures: \n"
                      + "{RUBIES=1}\n"
                      + "Player owns: 5 arrows\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 arrows here\n"
                      + "Doors lead to [EAST, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Player owns below treasures: \n"
                      + "{RUBIES=1}\n"
                      + "Player owns: 6 arrows\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "Doors lead to [EAST, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "Doors lead to [EAST, NORTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "You smell something LESSPUNGENT here\n"
                      + "You are in a tunnel\n"
                      + "Doors lead to [SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "You are in a tunnel\n"
                      + "You find 3 arrows here\n"
                      + "Doors lead to [NORTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Player owns below treasures: \n"
                      + "{RUBIES=1}\n"
                      + "Player owns: 7 arrows\n"
                      + "You are in a tunnel\n"
                      + "You find 2 arrows here\n"
                      + "Doors lead to [NORTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "You smell something LESSPUNGENT here\n"
                      + "You are in a tunnel\n"
                      + "You find 1 SAPPHIRE,  here\n"
                      + "Doors lead to [EAST, SOUTH]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Player owns below treasures: \n"
                      + "{RUBIES=1, SAPPHIRE=1}\n"
                      + "Player owns: 7 arrows\n"
                      + "You smell something LESSPUNGENT here\n"
                      + "You are in a tunnel\n"
                      + "Doors lead to [EAST, SOUTH]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a tunnel\n"
                      + "You find 3 arrows here\n"
                      + "Doors lead to [EAST, NORTH]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Player owns below treasures: \n"
                      + "{RUBIES=1, SAPPHIRE=1}\n"
                      + "Player owns: 8 arrows\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a tunnel\n"
                      + "You find 2 arrows here\n"
                      + "Doors lead to [EAST, NORTH]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Number of Caves?(1-5)\n"
                      + "Where to?\n"
                      + "You injured a Monster.\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a tunnel\n"
                      + "You find 2 arrows here\n"
                      + "Doors lead to [EAST, NORTH]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Number of Caves?(1-5)\n"
                      + "Where to?\n"
                      + "You killed a Monster.\n"
                      + "You are in a tunnel\n"
                      + "You find 2 arrows here\n"
                      + "Doors lead to [EAST, NORTH]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "What?\n"
                      + "Player owns below treasures: \n"
                      + "{RUBIES=1, SAPPHIRE=1}\n"
                      + "Player owns: 7 arrows\n"
                      + "You are in a tunnel\n"
                      + "You find 1 arrows here\n"
                      + "Doors lead to [EAST, NORTH]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "Yay! Player has won! Successfully reached end";
    controller.playGame(game);
    assertEquals(expected, gameLog.toString());
  }

  @Test
  public void testPlayGamePlayerLoses() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    StringReader input = new StringReader("m west m north");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    String expected = "Welcome to the Dungeon game. All the best.\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a cave\n"
                      + "You find 1 RUBIES,  here\n"
                      + "Doors lead to [NORTH, SOUTH, WEST]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "You smell something MOREPUNGENT here\n"
                      + "You are in a tunnel\n"
                      + "You find 2 arrows here\n"
                      + "Doors lead to [EAST, NORTH]\n"
                      + "Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n"
                      + "Where to?\n"
                      + "Chomp chomp chomp, player is eaten by monster. Try Again.";
    controller.playGame(game);
    assertEquals(expected, gameLog.toString());
  }

  @Test
  public void testMockModelCallTomovePlayer() {
    StringReader input = new StringReader("m north q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    DungeonGame mockModel = new MockDungeonModel();
    controller.playGame(mockModel);
    assertEquals("Function: movePlayer, Direction: NORTH",
            MockDungeonModel.VALIDATIONSTRING);
  }

  @Test
  public void testMockModelCallTopickArrow() {
    StringReader input = new StringReader("p arrow q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    DungeonGame mockModel = new MockDungeonModel();
    controller.playGame(mockModel);
    assertEquals("Function: pickArrow",
            MockDungeonModel.VALIDATIONSTRING);
  }

  @Test
  public void testMockModelCallTopickTreasure() {
    StringReader input = new StringReader("p diamond q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    DungeonGame mockModel = new MockDungeonModel();
    controller.playGame(mockModel);
    assertEquals("Function: pickTreasure Treasure: DIAMONDS",
            MockDungeonModel.VALIDATIONSTRING);
  }

  @Test
  public void testMockModelCallToShoot() {
    StringReader input = new StringReader("s 3 north q");
    DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, gameLog);
    DungeonGame mockModel = new MockDungeonModel();
    controller.playGame(mockModel);
    assertEquals("Function: shootArrow, Distance: 3 Direction: NORTH",
            MockDungeonModel.VALIDATIONSTRING);
  }

}