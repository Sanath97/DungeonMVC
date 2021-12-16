package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import dungeonmodel.Directions;
import dungeonmodel.DungeonGame;
import dungeonmodel.DungeonGameImpl;
import dungeonmodel.GeneralLocation;
import dungeonmodel.GeneralPlayer;
import dungeonmodel.GeneralPlayerImpl;
import dungeonmodel.MonsterStatus;
import dungeonmodel.NumberGenerationInterface;
import dungeonmodel.PredictiveGeneration;
import dungeonmodel.RandomGeneration;
import dungeonmodel.Treasures;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * This class includes tests for dungeon game model.
 */

public class DungeonGameImplTest {
  private GeneralPlayer player;
  private NumberGenerationInterface random;

  @Before
  public void setUp() {
    player = new GeneralPlayerImpl("testPlayer");
    random = new RandomGeneration();
  }

  @Test
  public void testConstructorInvalidRows() {
    try {
      new DungeonGameImpl(-4, 5, 1, false,
              45, 2, player, random);
    }
    catch (IllegalArgumentException exception) {
      assertEquals("Number of rows, columns cannot be negative", exception.getMessage());
    }
  }

  @Test
  public void testConstructorInvalidCols() {
    try {
      new DungeonGameImpl(4, -5, 1, false,
              45, 2, player, random);
    }
    catch (IllegalArgumentException exception) {
      assertEquals("Number of rows, columns cannot be negative", exception.getMessage());
    }
  }

  @Test
  public void testConstructorInvalidInterConnectivity() {
    try {
      new DungeonGameImpl(4, 5, -1, false,
              45, 2, player, random);
    }
    catch (IllegalArgumentException exception) {
      assertEquals("Interconnectivity cannot be negative", exception.getMessage());
    }
  }

  @Test
  public void testConstructorNegativeTreasurePercent() {
    try {
      new DungeonGameImpl(4, 5, 1, false,
              -45, 2, player, random);
    }
    catch (IllegalArgumentException exception) {
      assertEquals("Treasure percentage cannot be negative or greater than "
                   + "100", exception.getMessage());
    }
  }

  @Test
  public void testConstructorInvalidPositiveTreasurePercent() {
    try {
      new DungeonGameImpl(4, 5, 1, false,
              145, 2, player, random);
    }
    catch (IllegalArgumentException exception) {
      assertEquals("Treasure percentage cannot be negative or greater than "
                   + "100", exception.getMessage());
    }
  }

  @Test
  public void testInvalidDifficulty() {
    try {
      new DungeonGameImpl(4, 5, 1, false,
              45, 0, player, random);
    }
    catch (IllegalArgumentException exception) {
      assertEquals("Difficulty/Number of Monsters cannot be negative",
              exception.getMessage());
    }
  }

  @Test
  public void testInvalidPlayer() {
    try {
      new DungeonGameImpl(4, 5, 1, false,
              45, 4, null, random);
    }
    catch (IllegalArgumentException exception) {
      assertEquals("Player cannot be null. Pass a valid player object",
              exception.getMessage());
    }
  }

  @Test
  public void testInvalidNumberGeneration() {
    try {
      new DungeonGameImpl(4, 5, 1, false,
              45, 4, player, null);
    }
    catch (IllegalArgumentException exception) {
      assertEquals("Number generation interface cannot be null",
              exception.getMessage());
    }
  }

  @Test
  public void testPlayerName() {
    assertEquals("testPlayer", player.getPlayerName());
  }

  @Test
  public void testRowsInDungeon() {
    NumberGenerationInterface numberGenerationInterface1 = new RandomGeneration();
    GeneralPlayer player1 = new GeneralPlayerImpl("testPlayer");
    DungeonGame game1 = new DungeonGameImpl(6, 6, 2, false,
            80, 5, player1, numberGenerationInterface1);
    assertEquals(6, game1.getRowsInBoard());
  }

  @Test
  public void testColsInDungeon() {
    NumberGenerationInterface numberGenerationInterface1 = new RandomGeneration();
    GeneralPlayer player1 = new GeneralPlayerImpl("testPlayer");
    DungeonGame game1 = new DungeonGameImpl(5, 5, 2, false,
            80, 5, player1, numberGenerationInterface1);
    assertEquals(5, game1.getColsInBoard());
  }

  @Test
  public void testPlayerLocationInfo() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    game.movePlayer(Directions.SOUTH);
    game.movePlayer(Directions.WEST);
    String expected = "You smell something LESSPUNGENT here\n"
                      + "You are in a cave\n"
                      + "Doors lead to [EAST]\n";
    assertEquals(expected, game.getPlayerCurrLocationInfo());
  }

  @Test
  public void testUnAvailableDirectionForMove() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    try {
      game.movePlayer(Directions.EAST);
    } catch (IllegalArgumentException exception) {
      assertEquals("Given direction is not possible from current location",
              exception.getMessage());
    }
  }

  @Test
  public void testNullDirectionForMove() {
    DungeonGame game1 = new DungeonGameImpl(5, 5,2, true,
            45, 4, player, random);
    try {
      game1.movePlayer(null);
    } catch (IllegalArgumentException exception) {
      assertEquals("Direction cannot be null.",
              exception.getMessage());
    }
  }

  @Test
  public void testPickNullTreasure() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    try {
      game.pickTreasure(null);
    } catch (IllegalArgumentException exception) {
      assertEquals("Treasure cannot be null.",
              exception.getMessage());
    }
  }

  @Test
  public void testShootWithNullDirection() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    try {
      game.shootArrow(2, null);
    } catch (IllegalArgumentException exception) {
      assertEquals("Direction cannot be null.",
              exception.getMessage());
    }
  }

  @Test
  public void testPickUnAvailableTreasure() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    try {
      game.pickTreasure(Treasures.DIAMONDS);
    } catch (IllegalArgumentException exception) {
      assertEquals("Give from a valid treasure in the location",
              exception.getMessage());
    }
  }

  @Test
  public void testPickFromEmptyTreasureList() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    try {
      game.movePlayer(Directions.WEST);
      game.pickTreasure(Treasures.DIAMONDS);
    } catch (IllegalArgumentException exception) {
      assertEquals("Current location doesn't have any treasures",
              exception.getMessage());
    }
  }

  @Test
  public void testPickArrowFromEmptyArrowLocation() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    try {
      game.pickArrow();
    } catch (IllegalArgumentException exception) {
      assertEquals("Current location don't have arrows",
              exception.getMessage());
    }
  }

  @Test
  public void testInvalidNegativeDistanceForShoot() {
    DungeonGame game1 = new DungeonGameImpl(5, 5,2, true,
            45, 4, player, random);
    try {
      game1.shootArrow(-2, Directions.WEST);
    } catch (IllegalArgumentException exception) {
      assertEquals("Valid distances are (1 - 4). Both inclusive.",
              exception.getMessage());
    }
  }

  @Test
  public void testInvalidPositiveDistanceForShoot() {
    DungeonGame game1 = new DungeonGameImpl(5, 5,2, true,
            45, 4, player, random);
    try {
      game1.shootArrow(9, Directions.WEST);
    } catch (IllegalArgumentException exception) {
      assertEquals("Valid distances are (1 - 4). Both inclusive.",
              exception.getMessage());
    }
  }

  @Test
  public void testShootWhenArrowsAreZero() {
    DungeonGame game1 = new DungeonGameImpl(5, 5,2, true,
            45, 4, player, random);
    game1.shootArrow(1, Directions.WEST);
    game1.shootArrow(1, Directions.WEST);
    game1.shootArrow(1, Directions.SOUTH);
    try {
      game1.shootArrow(2, Directions.WEST);
    } catch (IllegalStateException exception) {
      assertEquals("Player doesn't have any arrows to shoot.",
              exception.getMessage());
    }
  }

  @Test
  public void testIfSmellIsMorePungent() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    // This validates that there exists a Monster that is one distant away.
    assertEquals(game.shootArrow(1, Directions.WEST), MonsterStatus.INJURED);
    // if above statement is true, then current cave should smell more pungent.
    assertTrue(game.getPlayerCurrLocationInfo().contains("You smell something MOREPUNGENT here"));
  }

  @Test
  public void testIfSmellIsLessPungent() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    game.movePlayer(Directions.NORTH);
    game.movePlayer(Directions.EAST);
    // This validates that there exists a Monster that is one distant away.
    assertEquals(game.shootArrow(2, Directions.WEST), MonsterStatus.INJURED);
    // if above statement is true, then current cave should smell less pungent.
    assertTrue(game.getPlayerCurrLocationInfo().contains("You smell something LESSPUNGENT here"));
  }

  @Test
  public void testPlayerDescription() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    String expected = "Player owns below treasures: \n"
                      + "{}\n"
                      + "Player owns: 3 arrows\n";
    assertEquals(game.getPlayerInfo(), expected);
    game.pickTreasure(Treasures.RUBIES);
    expected = "Player owns below treasures: \n"
               + "{RUBIES=1}\n"
               + "Player owns: 3 arrows\n";
    assertEquals(expected, game.getPlayerInfo());
    game.movePlayer(Directions.NORTH);
    game.pickArrow();
    expected = "Player owns below treasures: \n"
               + "{RUBIES=1}\n"
               + "Player owns: 4 arrows\n";
    assertEquals(expected, game.getPlayerInfo());
  }

  @Test
  public void testPlayerpickingupTreasure() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    String expected = "Player owns below treasures: \n"
                      + "{}\n"
                      + "Player owns: 3 arrows\n";
    assertEquals(game.getPlayerInfo(), expected);
    game.pickTreasure(Treasures.RUBIES);
    expected = "Player owns below treasures: \n"
               + "{RUBIES=1}\n"
               + "Player owns: 3 arrows\n";
    assertEquals(expected, game.getPlayerInfo());
  }

  @Test
  public void testCrookedArrowTraversalViaATunnel() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    // (2, 1) is start location and is connected to south and west.
    // (3, 0) has a monster and shooting from this location should move arrow to appropriate
    // direction when traversed via (3, 1) which is a tunnel.
    assertEquals(game.shootArrow(1, Directions.SOUTH), MonsterStatus.INJURED);
  }

  @Test
  public void testPlayerpickingupArrow() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    game.movePlayer(Directions.NORTH);
    game.pickArrow();
    //game.pickTreasure(Treasures.);
    String expected = "Player owns below treasures: \n"
                      + "{}\n"
                      + "Player owns: 4 arrows\n";
    assertEquals(expected, game.getPlayerInfo());
  }

  @Test
  public void testPlayerStartswithThreeArrows() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    String expected = "Player owns below treasures: \n"
                      + "{}\n"
                      + "Player owns: 3 arrows\n";
    assertEquals(expected, game.getPlayerInfo());
  }

  @Test
  public void testStartHasNoMonster() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    // Since player didn't shoot any arrow till now and is still alive being in the
    // start location. We can assume that location doesn't have any monster.
    assertTrue(game.isPlayerAlive());
  }

  @Test
  public void testEndHasMonster() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    game.movePlayer(Directions.NORTH);
    game.movePlayer(Directions.EAST);
    game.movePlayer(Directions.EAST);
    game.movePlayer(Directions.SOUTH);
    game.movePlayer(Directions.WEST);
    game.movePlayer(Directions.SOUTH);
    // Moving to end location. This should kill player, if monster really exists.
    game.movePlayer(Directions.EAST);
    assertEquals(game.getPlayerCurrentLocation(), game.getEndLocation());
    assertFalse(game.isPlayerAlive());
  }

  @Test
  public void testPlayerGetsKilledByMonster() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    game.movePlayer(Directions.SOUTH);
    game.movePlayer(Directions.WEST);
    assertFalse(game.isPlayerAlive());
    assertTrue(game.isGameOver());
  }

  @Test
  public void testIfShootisReducingArrow() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    String expected = "Player owns below treasures: \n"
                      + "{}\n"
                      + "Player owns: 3 arrows\n";
    assertEquals(expected, game.getPlayerInfo());
    game.shootArrow(1, Directions.NORTH);
    expected = "Player owns below treasures: \n"
               + "{}\n"
               + "Player owns: 2 arrows\n";
    // Assert if player arrows are reduced by 2.
    assertEquals(expected, game.getPlayerInfo());

  }

  @Test
  public void testShootIntoDarkness() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    assertEquals(game.shootArrow(1, Directions.NORTH), MonsterStatus.UNAFFECTED);
  }

  @Test
  public void testShootWorksCorrectly() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    game.movePlayer(Directions.NORTH);
    game.movePlayer(Directions.EAST);
    assertEquals(game.shootArrow(1, Directions.NORTH), MonsterStatus.INJURED);
  }

  @Test
  public void testTwoArrowsRequiredToKillMonster() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    String expected = "Player owns below treasures: \n"
                      + "{}\n"
                      + "Player owns: 3 arrows\n";
    assertEquals(expected, game.getPlayerInfo());
    game.movePlayer(Directions.SOUTH);
    // Assert if monster is injured for first arrow.
    assertEquals(game.shootArrow(1, Directions.WEST), MonsterStatus.INJURED);
    // Assert if monster is injured for second arrow.
    assertEquals(game.shootArrow(1, Directions.WEST), MonsterStatus.DEAD);
    expected = "Player owns below treasures: \n"
               + "{}\n"
               + "Player owns: 1 arrows\n";
    // Assert if player arrows are reduced by 2.
    assertEquals(expected, game.getPlayerInfo());
  }

  @Test
  public void testMonstersCountInDungeon() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    Set<GeneralLocation> traversedLocations = new HashSet<>();
    int monstersCount = 0;
    int cavesWithMoreThanOneMonster = 0;
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    if (game.shootArrow(1, Directions.WEST) == MonsterStatus.INJURED) {
      monstersCount++;
    }
    // Kill Monster
    game.shootArrow(1, Directions.WEST);
    game.movePlayer(Directions.WEST);
    // Checking if player is alive after moving to west cave.
    // If player is alive, then it means there is no monster in that cave. And there exists
    // only one monster in the cave.
    if (!game.isPlayerAlive()) {
      cavesWithMoreThanOneMonster++;
    }
    // Check if game is not over as player has not reached end.
    assertFalse(game.isGameOver());
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.NORTH);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.pickArrow();
    if (game.shootArrow(1, Directions.NORTH) == MonsterStatus.INJURED) {
      monstersCount++;
    }
    // Kill Monster
    game.shootArrow(1, Directions.NORTH);
    game.movePlayer(Directions.NORTH);
    // Checking if player is alive after moving to west cave.
    // If player is alive, then it means there is no monster in that cave. And there exists
    // only one monster in the cave.
    if (!game.isPlayerAlive()) {
      cavesWithMoreThanOneMonster++;
    }
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.pickArrow();
    game.pickArrow();
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    if (game.shootArrow(1, Directions.NORTH) == MonsterStatus.INJURED) {
      monstersCount++;
    }
    assertEquals(game.shootArrow(1, Directions.NORTH), MonsterStatus.DEAD);
    game.movePlayer(Directions.NORTH);
    // Checking if player is alive after moving to west cave.
    // If player is alive, then it means there is no monster in that cave. And there exists
    // only one monster in the cave.
    if (!game.isPlayerAlive()) {
      cavesWithMoreThanOneMonster++;
    }
    traversedLocations.add(game.getPlayerCurrentLocation());
    if (game.shootArrow(1, Directions.EAST) == MonsterStatus.INJURED) {
      monstersCount++;
    }
    game.shootArrow(1, Directions.EAST);
    game.movePlayer(Directions.EAST);
    // Checking if player is alive after moving to west cave.
    // If player is alive, then it means there is no monster in that cave. And there exists
    // only one monster in the cave.
    if (!game.isPlayerAlive()) {
      cavesWithMoreThanOneMonster++;
    }
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());

    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    traversedLocations.add(game.getPlayerCurrentLocation());
    if (game.shootArrow(1, Directions.EAST) == MonsterStatus.INJURED) {
      monstersCount++;
    }
    game.shootArrow(1, Directions.EAST);
    game.movePlayer(Directions.EAST);
    // Checking if player is alive after moving to west cave.
    // If player is alive, then it means there is no monster in that cave. And there exists
    // only one monster in the cave.
    if (!game.isPlayerAlive()) {
      cavesWithMoreThanOneMonster++;
    }
    traversedLocations.add(game.getPlayerCurrentLocation());

    // Check if we have traversed all nodes in the dungeon.
    assertEquals(16, traversedLocations.size());

    // Check if total monsters in dungeon are 5.Since user has given 5.
    assertEquals(5, monstersCount);

    // Check if player reached end location.
    assertEquals(game.getPlayerCurrentLocation(), game.getEndLocation());

    // Check if caves with more than one monster exists.
    assertEquals(0, cavesWithMoreThanOneMonster);

    // Check if player is alive and game has ended.
    assertTrue(game.isGameOver());
    assertTrue(game.isPlayerAlive());
  }

  @Test
  public void testIfMonstersAreAddedToCaveInDungeon() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    Set<GeneralLocation> traversedLocations = new HashSet<>();
    int monstersInTunnel = 0;
    int monstersInCave = 0;
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.shootArrow(1, Directions.WEST);
    // Kill Monster
    game.shootArrow(1, Directions.WEST);
    game.movePlayer(Directions.WEST);
    if (game.getPlayerCurrentLocation().isCave()) {
      monstersInCave++;
    }
    else {
      monstersInTunnel++;
    }
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.NORTH);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.pickArrow();
    game.shootArrow(1, Directions.NORTH);
    // Kill Monster
    game.shootArrow(1, Directions.NORTH);
    game.movePlayer(Directions.NORTH);
    if (game.getPlayerCurrentLocation().isCave()) {
      monstersInCave++;
    }
    else {
      monstersInTunnel++;
    }
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.pickArrow();
    game.pickArrow();
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.shootArrow(1, Directions.NORTH);
    assertEquals(game.shootArrow(1, Directions.NORTH), MonsterStatus.DEAD);
    game.movePlayer(Directions.NORTH);
    if (game.getPlayerCurrentLocation().isCave()) {
      monstersInCave++;
    }
    else {
      monstersInTunnel++;
    }
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.shootArrow(1, Directions.EAST);
    game.shootArrow(1, Directions.EAST);
    game.movePlayer(Directions.EAST);
    if (game.getPlayerCurrentLocation().isCave()) {
      monstersInCave++;
    }
    else {
      monstersInTunnel++;
    }
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());

    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.shootArrow(1, Directions.EAST);
    game.shootArrow(1, Directions.EAST);
    game.movePlayer(Directions.EAST);
    if (game.getPlayerCurrentLocation().isCave()) {
      monstersInCave++;
    }
    else {
      monstersInTunnel++;
    }
    traversedLocations.add(game.getPlayerCurrentLocation());
    // Check if we have traversed all nodes in the dungeon.
    assertEquals(16, traversedLocations.size());

    // Check if total monsters count in cave in dungeon is 5.Since user has given 5.
    assertEquals(5, monstersInCave);

    // Check if total monsters count in tunnel in dungeon is 0.Since user has given 5.
    assertEquals(0, monstersInTunnel);
  }

  @Test
  public void testPlayerEscapingWithFiftyPercentChanceInMonsterExistingDungeon() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3,1);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    game.movePlayer(Directions.SOUTH);
    // Validates that a monster exists in the West Cave.
    assertEquals(game.shootArrow(1, Directions.WEST), MonsterStatus.INJURED);
    // Moving player to West direction whilst Monster exists there.
    game.movePlayer(Directions.WEST);
    // Checking if player is alive since we gave a chance of living.
    assertTrue(game.isPlayerAlive());
  }

  @Test
  public void testPlayerDeathWithFiftyPercentChanceInMonsterExistingDungeon() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3,0);
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, player, numberGenerationInterface);
    game.movePlayer(Directions.SOUTH);
    // Validates that a monster exists in the West Cave.
    assertEquals(game.shootArrow(1, Directions.WEST), MonsterStatus.INJURED);
    // Moving player to West direction whilst Monster exists there.
    game.movePlayer(Directions.WEST);
    // Checking if player is alive since we gave a chance of living.
    assertFalse(game.isPlayerAlive());
  }

  private Set<GeneralLocation> checkIfNodehasArrows(GeneralLocation location,
                                   Set<GeneralLocation> arrowedLocations) {
    if (location.getArrowCount() > 0) {
      arrowedLocations.add(location);
    }
    return arrowedLocations;
  }

  @Test
  public void testArrowDistribution() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    Set<GeneralLocation> traversedLocations = new HashSet<>();
    Set<GeneralLocation> arrowedLocations = new HashSet<>();
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.shootArrow(1, Directions.WEST);
    // Kill Monster
    game.shootArrow(1, Directions.WEST);
    game.movePlayer(Directions.WEST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.NORTH);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.pickArrow();
    game.shootArrow(1, Directions.NORTH);
    // Kill Monster
    game.shootArrow(1, Directions.NORTH);
    game.movePlayer(Directions.NORTH);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.movePlayer(Directions.EAST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.pickArrow();
    game.pickArrow();
    game.movePlayer(Directions.EAST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.shootArrow(1, Directions.NORTH);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    assertEquals(game.shootArrow(1, Directions.NORTH), MonsterStatus.DEAD);
    game.movePlayer(Directions.NORTH);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.shootArrow(1, Directions.EAST);
    game.shootArrow(1, Directions.EAST);
    game.movePlayer(Directions.EAST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());

    game.movePlayer(Directions.EAST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.shootArrow(1, Directions.EAST);
    game.shootArrow(1, Directions.EAST);
    game.movePlayer(Directions.EAST);
    checkIfNodehasArrows(game.getPlayerCurrentLocation(), arrowedLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    // Check if we have traversed all nodes in the dungeon.
    assertEquals(16, traversedLocations.size());

    // Check if total arrowed nodes are 9.
    assertEquals(9, arrowedLocations.size());
  }

  private Set<GeneralLocation> checkIfNodehasTreasures(GeneralLocation location,
                                                    Set<GeneralLocation> treasuredLocations) {
    if (location.getTreasuresList().size() > 0 && location.isCave()) {
      treasuredLocations.add(location);
    }
    return treasuredLocations;
  }

  @Test
  public void testTreasureDistribution() {
    // Treasure distribution, validates treasure only in cave and count of treasures
    // as per given percentage.
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    Set<GeneralLocation> traversedLocations = new HashSet<>();
    Set<GeneralLocation> treasuredLocations = new HashSet<>();
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.shootArrow(1, Directions.WEST);
    // Kill Monster
    game.shootArrow(1, Directions.WEST);
    game.movePlayer(Directions.WEST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.NORTH);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.pickArrow();
    game.shootArrow(1, Directions.NORTH);
    // Kill Monster
    game.shootArrow(1, Directions.NORTH);
    game.movePlayer(Directions.NORTH);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.movePlayer(Directions.EAST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.pickArrow();
    game.pickArrow();
    game.pickArrow();
    game.movePlayer(Directions.EAST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.shootArrow(1, Directions.NORTH);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    assertEquals(game.shootArrow(1, Directions.NORTH), MonsterStatus.DEAD);
    game.movePlayer(Directions.NORTH);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.shootArrow(1, Directions.EAST);
    game.shootArrow(1, Directions.EAST);
    game.movePlayer(Directions.EAST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());

    game.movePlayer(Directions.EAST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.EAST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.WEST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.movePlayer(Directions.SOUTH);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    game.shootArrow(1, Directions.EAST);
    game.shootArrow(1, Directions.EAST);
    game.movePlayer(Directions.EAST);
    checkIfNodehasTreasures(game.getPlayerCurrentLocation(), treasuredLocations);
    traversedLocations.add(game.getPlayerCurrentLocation());
    // Check if we have traversed all nodes in the dungeon.
    assertEquals(16, traversedLocations.size());

    // Check if total treasured nodes are 4.
    assertEquals(4, treasuredLocations.size());
  }

  @Test
  public void testMovePlayer() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    assertEquals(game.getPlayerCurrentLocation().toString(), "(2, 1), Treasures: [RUBIES], "
                                                             + "No Arrows");
    game.movePlayer(Directions.NORTH);
    assertEquals(game.getPlayerCurrentLocation().toString(), "(1, 1), Treasures: [], "
                                                             + "Has 3 Arrows");
    game.movePlayer(Directions.SOUTH);
    assertEquals(game.getPlayerCurrentLocation().toString(), "(2, 1), Treasures: [RUBIES], "
                                                             + "No Arrows");
  }

  @Test
  public void testIfStartAndEndAreCaves() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    assertTrue(game.getPlayerCurrentLocation().isCave());
    assertTrue(game.getEndLocation().isCave());
  }

  @Test
  public void testIfArrowIsInBothTunnelCave() {
    NumberGenerationInterface numberGenerationInterface = new PredictiveGeneration(
            17,18,21,2,17,11,1,1,14,9,13,8,4,21,9,15,23,2,23,9,0,2,0,17,21,21,0,7,
            4,8,0,10,7,6,18,19,5,1,0,2,2,2,2,2,1,1,1,3,1,2,2,2,2,2,3,3,2,3,7,4,0,1,5,7,2,2,5,5,6,1,
            1,0,3,1,1,0,2,2,3,3,3,1,0,1,1,0,1,1,3,3,3,2,0,2,1,0,2,0,0,2,3,3,2,3,3,0,1,3,3,2,3);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(4, 4, 2, false,
            60, 5, testPlayer, numberGenerationInterface);
    game.movePlayer(Directions.WEST);
    // Check if moved location is a tunnel
    assertFalse(game.getPlayerCurrentLocation().isCave());
    // Check if tunnel has arrows.
    assertTrue(game.getPlayerCurrentLocation().getArrowCount() > 0);
    game.movePlayer(Directions.NORTH);
    // Check if moved location is a cave
    assertTrue(game.getPlayerCurrentLocation().isCave());
    // Check if cave has arrows.
    assertTrue(game.getPlayerCurrentLocation().getArrowCount() > 0);
  }

  @Test
  public void testWrappingDungeon() {
    NumberGenerationInterface random = new PredictiveGeneration(39,22,26,9,22,45,21,
            39,15,5,21,12,39,5,19,4,15,43,48,7,46,48,45,27,9,12,36,49,32,11,29,12,49,2,38,37,47,15,
            12,15,22,44,20,6,34,1,0,3,2,0,0,1,4,2,2,0,0,0,2,2,1,3,3,1,3,1,2,2,2,0,1,1,1,0,0,1,2,1,3,
            3,9,5,8,0,1,2,2,2,1,1,2,3,2,1,1,3,0,2,3,0,4,2,4,2,3,1,1,1,0,3,1,0,1,2,1,3,1,1,3,2,1,1,4,
            2,0,0,1,3,1,1);
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(5, 5, 1, true,
            50, 4, testPlayer, random);
    game.movePlayer(Directions.SOUTH);
    assertEquals("(3, 0), Treasures: [], No Arrows" ,game.getPlayerCurrentLocation().toString());
    game.movePlayer(Directions.WEST);
    assertEquals("(3, 4), Treasures: [], No Arrows" ,game.getPlayerCurrentLocation().toString());
  }

  @Test
  public void testValidatePlayerStartsAtStartLocation() {
    NumberGenerationInterface random = new RandomGeneration();
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    DungeonGame game = new DungeonGameImpl(5, 5, 2, false,
            60, 5, testPlayer, random);
    assertEquals(game.getPlayerCurrentLocation(), game.getStartLocation());
  }


  @Test
  public void testToValidatePathLengthAtleast5() {
    NumberGenerationInterface random = new RandomGeneration();
    GeneralPlayer player = new GeneralPlayerImpl("PlayerR");
    DungeonGame game = new DungeonGameImpl(6, 6, 0, false,40, 4,
            player, random);
    assertTrue(findDistance(game.getStartLocation(), game.getEndLocation(), 6,
            6) >= 5);
  }

  @Test
  public void testErrorThrownIfPathLenghtNotPossible() {
    NumberGenerationInterface random = new RandomGeneration();
    GeneralPlayer player = new GeneralPlayerImpl("PlayerR");
    try {
      new DungeonGameImpl(2, 2, 0, false,40, 4,
              player, random);
    }
    catch (IllegalArgumentException exception) {
      assertEquals("Two nodes with 5 units apart are not found for this "
                   + "dungeon", exception.getMessage());
    }
  }


  private int getEquivalentOneDIndex(int sourceRow, int sourceCol, int totalCols) {
    return ((totalCols) * (sourceRow)) + sourceCol;
  }

  // Find distance between two given points.
  // Reference: https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
  private int findDistance(GeneralLocation source, GeneralLocation destination, int rows,
                           int cols) {
    boolean[] visited = new boolean[rows * cols];
    // Create a queue for BFS
    LinkedList<GeneralLocation> queue = new LinkedList<>();
    int[] distance = new int[rows * cols];
    // Mark the current node as visited and enqueue it
    visited[getEquivalentOneDIndex(source.getLocation().getX(), source.getLocation().getY(),
            cols)] = true;
    distance[getEquivalentOneDIndex(source.getLocation().getX(), source.getLocation().getY(),
            cols)] = 0;
    queue.add(source);
    while (queue.size() != 0) {
      // Dequeue a vertex from queue and print it
      source = queue.poll();

      // Get all adjacent vertices of the dequeued vertex s
      // If a adjacent has not been visited, then mark it
      // visited and enqueue it
      List<GeneralLocation> adjacencies = source.getAdjacencyList();
      ListIterator<GeneralLocation> iterator = adjacencies.listIterator();

      while (iterator.hasNext()) {
        GeneralLocation next = iterator.next();
        int oneDIndex = getEquivalentOneDIndex(next.getLocation().getX(),
                next.getLocation().getY(), cols);
        if (!visited[oneDIndex]) {
          distance[oneDIndex] = distance[getEquivalentOneDIndex(source.getLocation().getX(),
                  source.getLocation().getY(), cols)] + 1;
          visited[oneDIndex] = true;
          queue.add(next);
        }
      }
    }
    return distance[getEquivalentOneDIndex(destination.getLocation().getX(),
            destination.getLocation().getY(), cols)];
  }
}