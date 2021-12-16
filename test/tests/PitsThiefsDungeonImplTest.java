package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import dungeonmodel.Directions;
import dungeonmodel.GeneralLocation;
import dungeonmodel.GeneralPlayer;
import dungeonmodel.GeneralPlayerImpl;
import dungeonmodel.NumberGenerationInterface;
import dungeonmodel.PitsThievesDungeon;
import dungeonmodel.PitsThievesDungeonImpl;
import dungeonmodel.PredictiveGeneration;
import dungeonmodel.RandomGeneration;
import org.junit.Test;

/**
 * Class for testing PitsThiefsDungeonImpl class, this includes tests for new Pits and thieves
 * features.
 */
public class PitsThiefsDungeonImplTest {

  @Test
  public void testPitsDistribution() {
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    PitsThievesDungeon game = new PitsThievesDungeonImpl(6, 6, 2, false,
            60, 5, testPlayer, new RandomGeneration());
    int pitsCount = 0;
    for (int i = 0; i < game.getRowsInBoard(); i++) {
      for (int j = 0; j < game.getColsInBoard(); j++) {
        GeneralLocation currLocation = game.getBoard()[i][j];
        if (game.hasPit(currLocation)) {
          pitsCount++;
        }
      }
    }
    assertEquals(pitsCount, 5);
  }

  @Test
  public void testThievesistribution() {
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    PitsThievesDungeon game = new PitsThievesDungeonImpl(8, 8, 2, false,
            60, 8, testPlayer, new RandomGeneration());
    int thievesCount = 0;
    for (int i = 0; i < game.getRowsInBoard(); i++) {
      for (int j = 0; j < game.getColsInBoard(); j++) {
        GeneralLocation currLocation = game.getBoard()[i][j];
        if (game.hasThief(currLocation)) {
          thievesCount++;
        }
      }
    }
    assertEquals(thievesCount, 8);
  }

  @Test
  public void testIfThiefStealsTreasures() {
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    NumberGenerationInterface predictive = new PredictiveGeneration(4, 62, 64, 51,
            15, 35, 43, 63, 73, 95, 1, 111, 59, 13, 105, 82, 0, 28, 86, 21, 80, 43, 73, 30, 88, 13,
            32, 79, 69, 9, 77, 71, 78, 2, 34, 27, 82, 109, 88, 28, 102, 36, 63, 53, 33, 86, 48, 20,
            99, 24, 30, 34, 15, 45, 75, 97, 106, 13, 6, 81, 44, 5, 49, 62, 99, 90, 81, 26, 38, 91,
            32, 99, 71, 51, 28, 0, 82, 104, 2, 29, 66, 19, 53, 0, 51, 76, 88, 24, 34, 65, 92, 66,
            96, 98, 18, 55, 85, 45, 34, 101, 82, 65, 68, 7, 63, 17, 92, 84, 67, 39, 3, 50, 1, 18,
            48, 36, 47, 77, 101, 103, 82, 93, 22, 5, 54, 39, 100, 84, 106, 60, 72, 15, 104, 83, 43,
            30, 49, 81, 1, 100, 7, 103, 103, 53, 52, 23, 34, 50, 104, 36, 22, 99, 16, 70, 56, 79,
            65, 71, 11, 59, 85, 93, 86, 13, 108, 36, 7, 91, 67, 87, 33, 69, 98, 75, 17, 108, 111,
            73, 31, 109, 1, 69, 31, 110, 95, 47, 70, 20, 6, 54, 42, 63, 73, 4, 19, 99, 35, 26, 30,
            3, 88, 72, 32, 27, 105, 25, 22, 10, 87, 73, 75, 42, 30, 84, 36, 94, 99, 29, 85, 104, 25,
            103, 102, 4, 44, 4, 16, 20, 96, 71, 82, 72, 4, 56, 32, 57, 80, 68, 106, 99, 110, 29, 59,
            13, 85, 21, 56, 54, 63, 51, 88, 46, 45, 64, 3, 110, 65, 17, 35, 101, 40, 1, 7, 96, 106,
            57, 87, 36, 42, 111, 1, 91, 73, 9, 18, 25, 71, 92, 48, 10, 89, 86, 68, 107, 54, 99, 5,
            65, 59, 48, 47, 89, 63, 26, 83, 93, 0, 2, 56, 0, 92, 111, 47, 55, 12, 44, 36, 3, 7, 1,
            1, 0, 6, 2, 2, 1, 5, 6, 2, 0, 2, 0, 6, 7, 7, 2, 1, 2, 6, 2, 3, 3, 0, 2, 5, 3, 1, 2, 1,
            3, 2, 0, 3, 5, 5, 3, 1, 0, 1, 1, 5, 3, 3, 1, 2, 2, 0, 3, 0, 0, 1, 0, 6, 2, 3, 1, 0, 0,
            5, 3, 1, 0, 2, 3, 7, 2, 4, 1, 0, 7, 3, 3, 2, 2, 0, 0, 1, 2, 0, 2, 7, 4, 2, 2, 0, 7, 7,
            4, 2, 2, 2, 0, 0, 2, 3, 3, 0, 1, 7, 2, 2, 1, 1, 21, 12, 16, 8, 32, 0, 1, 7, 19, 26, 11,
            2, 6, 2, 4, 5, 3, 6, 0, 2, 4, 5, 6, 7, 3, 0, 2, 3, 7, 6, 3, 1, 7, 1, 5, 2, 1, 6, 5, 1,
            1, 4, 3, 6, 0, 5, 4, 3, 3, 3, 3, 5, 4, 0, 7, 3, 7, 3, 1, 2, 2, 2, 4, 5, 4, 0, 3, 6, 1,
            2, 2, 0, 1, 0, 0, 2, 4, 6, 3, 1, 1, 3, 2, 5, 1, 1, 0, 3, 7, 1, 2, 3, 5, 1, 3, 0, 2, 1,
            2, 3, 7, 6, 7, 7, 3, 6, 2, 3, 7, 6, 1, 6, 3, 5, 7, 3, 6, 5, 6, 4, 2, 1, 1, 2, 6, 5, 7,
            0, 5, 2, 1, 3, 2, 0, 2, 0, 2, 6, 2, 0, 3, 1, 0, 6, 2, 4, 0, 7, 7, 0, 2, 3, 3, 2, 2, 5,
            2, 7, 2, 2, 5, 4, 1, 4, 0, 0, 6, 7, 7, 4, 3, 24, 27, 22, 11, 2, 5, 8, 15, 4, 1, 15, 10,
            11, 15, 23, 20, 9, 27);

    PitsThievesDungeon game = new PitsThievesDungeonImpl(8, 8, 2, false,
            60, 8, testPlayer, predictive);
    assertEquals(game.getPlayerEmeraldCount(), 0);
    game.movePlayer(Directions.SOUTH);
    assertTrue(game.hasThief(game.getBoard()[5][4]));
    game.movePlayer(Directions.WEST);
    game.pickAllTreasures();
    assertEquals(game.getPlayerEmeraldCount(), 1);
    game.movePlayer(Directions.EAST);
    assertEquals(game.getPlayerEmeraldCount(), 0);
  }

  @Test
  public void testIfPlayerFallsIntoPitAndDies() {
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    NumberGenerationInterface predictive = new PredictiveGeneration(4, 62, 64, 51,
            15, 35, 43, 63, 73, 95, 1, 111, 59, 13, 105, 82, 0, 28, 86, 21, 80, 43, 73, 30, 88, 13,
            32, 79, 69, 9, 77, 71, 78, 2, 34, 27, 82, 109, 88, 28, 102, 36, 63, 53, 33, 86, 48, 20,
            99, 24, 30, 34, 15, 45, 75, 97, 106, 13, 6, 81, 44, 5, 49, 62, 99, 90, 81, 26, 38, 91,
            32, 99, 71, 51, 28, 0, 82, 104, 2, 29, 66, 19, 53, 0, 51, 76, 88, 24, 34, 65, 92, 66,
            96, 98, 18, 55, 85, 45, 34, 101, 82, 65, 68, 7, 63, 17, 92, 84, 67, 39, 3, 50, 1, 18,
            48, 36, 47, 77, 101, 103, 82, 93, 22, 5, 54, 39, 100, 84, 106, 60, 72, 15, 104, 83, 43,
            30, 49, 81, 1, 100, 7, 103, 103, 53, 52, 23, 34, 50, 104, 36, 22, 99, 16, 70, 56, 79,
            65, 71, 11, 59, 85, 93, 86, 13, 108, 36, 7, 91, 67, 87, 33, 69, 98, 75, 17, 108, 111,
            73, 31, 109, 1, 69, 31, 110, 95, 47, 70, 20, 6, 54, 42, 63, 73, 4, 19, 99, 35, 26, 30,
            3, 88, 72, 32, 27, 105, 25, 22, 10, 87, 73, 75, 42, 30, 84, 36, 94, 99, 29, 85, 104, 25,
            103, 102, 4, 44, 4, 16, 20, 96, 71, 82, 72, 4, 56, 32, 57, 80, 68, 106, 99, 110, 29, 59,
            13, 85, 21, 56, 54, 63, 51, 88, 46, 45, 64, 3, 110, 65, 17, 35, 101, 40, 1, 7, 96, 106,
            57, 87, 36, 42, 111, 1, 91, 73, 9, 18, 25, 71, 92, 48, 10, 89, 86, 68, 107, 54, 99, 5,
            65, 59, 48, 47, 89, 63, 26, 83, 93, 0, 2, 56, 0, 92, 111, 47, 55, 12, 44, 36, 3, 7, 1,
            1, 0, 6, 2, 2, 1, 5, 6, 2, 0, 2, 0, 6, 7, 7, 2, 1, 2, 6, 2, 3, 3, 0, 2, 5, 3, 1, 2, 1,
            3, 2, 0, 3, 5, 5, 3, 1, 0, 1, 1, 5, 3, 3, 1, 2, 2, 0, 3, 0, 0, 1, 0, 6, 2, 3, 1, 0, 0,
            5, 3, 1, 0, 2, 3, 7, 2, 4, 1, 0, 7, 3, 3, 2, 2, 0, 0, 1, 2, 0, 2, 7, 4, 2, 2, 0, 7, 7,
            4, 2, 2, 2, 0, 0, 2, 3, 3, 0, 1, 7, 2, 2, 1, 1, 21, 12, 16, 8, 32, 0, 1, 7, 19, 26, 11,
            2, 6, 2, 4, 5, 3, 6, 0, 2, 4, 5, 6, 7, 3, 0, 2, 3, 7, 6, 3, 1, 7, 1, 5, 2, 1, 6, 5, 1,
            1, 4, 3, 6, 0, 5, 4, 3, 3, 3, 3, 5, 4, 0, 7, 3, 7, 3, 1, 2, 2, 2, 4, 5, 4, 0, 3, 6, 1,
            2, 2, 0, 1, 0, 0, 2, 4, 6, 3, 1, 1, 3, 2, 5, 1, 1, 0, 3, 7, 1, 2, 3, 5, 1, 3, 0, 2, 1,
            2, 3, 7, 6, 7, 7, 3, 6, 2, 3, 7, 6, 1, 6, 3, 5, 7, 3, 6, 5, 6, 4, 2, 1, 1, 2, 6, 5, 7,
            0, 5, 2, 1, 3, 2, 0, 2, 0, 2, 6, 2, 0, 3, 1, 0, 6, 2, 4, 0, 7, 7, 0, 2, 3, 3, 2, 2, 5,
            2, 7, 2, 2, 5, 4, 1, 4, 0, 0, 6, 7, 7, 4, 3, 24, 27, 22, 11, 2, 5, 8, 15, 4, 1, 15, 10,
            11, 15, 23, 20, 9, 27, 0);
    PitsThievesDungeon game = new PitsThievesDungeonImpl(8, 8, 2, false,
            60, 8, testPlayer, predictive);
    assertTrue(game.isPlayerAlive());
    game.movePlayer(Directions.SOUTH);
    game.movePlayer(Directions.WEST);
    game.movePlayer(Directions.WEST);
    assertTrue(game.hasPit(game.getBoard()[5][2]));
    assertFalse(game.isPlayerAlive());
  }

  @Test
  public void testIfPlayerFallsIntoPitAndLives() {
    GeneralPlayer testPlayer = new GeneralPlayerImpl("testPlayer");
    NumberGenerationInterface predictive = new PredictiveGeneration(4, 62, 64, 51,
            15, 35, 43, 63, 73, 95, 1, 111, 59, 13, 105, 82, 0, 28, 86, 21, 80, 43, 73, 30, 88, 13,
            32, 79, 69, 9, 77, 71, 78, 2, 34, 27, 82, 109, 88, 28, 102, 36, 63, 53, 33, 86, 48, 20,
            99, 24, 30, 34, 15, 45, 75, 97, 106, 13, 6, 81, 44, 5, 49, 62, 99, 90, 81, 26, 38, 91,
            32, 99, 71, 51, 28, 0, 82, 104, 2, 29, 66, 19, 53, 0, 51, 76, 88, 24, 34, 65, 92, 66,
            96, 98, 18, 55, 85, 45, 34, 101, 82, 65, 68, 7, 63, 17, 92, 84, 67, 39, 3, 50, 1, 18,
            48, 36, 47, 77, 101, 103, 82, 93, 22, 5, 54, 39, 100, 84, 106, 60, 72, 15, 104, 83, 43,
            30, 49, 81, 1, 100, 7, 103, 103, 53, 52, 23, 34, 50, 104, 36, 22, 99, 16, 70, 56, 79,
            65, 71, 11, 59, 85, 93, 86, 13, 108, 36, 7, 91, 67, 87, 33, 69, 98, 75, 17, 108, 111,
            73, 31, 109, 1, 69, 31, 110, 95, 47, 70, 20, 6, 54, 42, 63, 73, 4, 19, 99, 35, 26, 30,
            3, 88, 72, 32, 27, 105, 25, 22, 10, 87, 73, 75, 42, 30, 84, 36, 94, 99, 29, 85, 104, 25,
            103, 102, 4, 44, 4, 16, 20, 96, 71, 82, 72, 4, 56, 32, 57, 80, 68, 106, 99, 110, 29, 59,
            13, 85, 21, 56, 54, 63, 51, 88, 46, 45, 64, 3, 110, 65, 17, 35, 101, 40, 1, 7, 96, 106,
            57, 87, 36, 42, 111, 1, 91, 73, 9, 18, 25, 71, 92, 48, 10, 89, 86, 68, 107, 54, 99, 5,
            65, 59, 48, 47, 89, 63, 26, 83, 93, 0, 2, 56, 0, 92, 111, 47, 55, 12, 44, 36, 3, 7, 1,
            1, 0, 6, 2, 2, 1, 5, 6, 2, 0, 2, 0, 6, 7, 7, 2, 1, 2, 6, 2, 3, 3, 0, 2, 5, 3, 1, 2, 1,
            3, 2, 0, 3, 5, 5, 3, 1, 0, 1, 1, 5, 3, 3, 1, 2, 2, 0, 3, 0, 0, 1, 0, 6, 2, 3, 1, 0, 0,
            5, 3, 1, 0, 2, 3, 7, 2, 4, 1, 0, 7, 3, 3, 2, 2, 0, 0, 1, 2, 0, 2, 7, 4, 2, 2, 0, 7, 7,
            4, 2, 2, 2, 0, 0, 2, 3, 3, 0, 1, 7, 2, 2, 1, 1, 21, 12, 16, 8, 32, 0, 1, 7, 19, 26, 11,
            2, 6, 2, 4, 5, 3, 6, 0, 2, 4, 5, 6, 7, 3, 0, 2, 3, 7, 6, 3, 1, 7, 1, 5, 2, 1, 6, 5, 1,
            1, 4, 3, 6, 0, 5, 4, 3, 3, 3, 3, 5, 4, 0, 7, 3, 7, 3, 1, 2, 2, 2, 4, 5, 4, 0, 3, 6, 1,
            2, 2, 0, 1, 0, 0, 2, 4, 6, 3, 1, 1, 3, 2, 5, 1, 1, 0, 3, 7, 1, 2, 3, 5, 1, 3, 0, 2, 1,
            2, 3, 7, 6, 7, 7, 3, 6, 2, 3, 7, 6, 1, 6, 3, 5, 7, 3, 6, 5, 6, 4, 2, 1, 1, 2, 6, 5, 7,
            0, 5, 2, 1, 3, 2, 0, 2, 0, 2, 6, 2, 0, 3, 1, 0, 6, 2, 4, 0, 7, 7, 0, 2, 3, 3, 2, 2, 5,
            2, 7, 2, 2, 5, 4, 1, 4, 0, 0, 6, 7, 7, 4, 3, 24, 27, 22, 11, 2, 5, 8, 15, 4, 1, 15, 10,
            11, 15, 23, 20, 9, 27, 1);
    PitsThievesDungeon game = new PitsThievesDungeonImpl(8, 8, 2, false,
            60, 8, testPlayer, predictive);
    assertTrue(game.isPlayerAlive());
    game.movePlayer(Directions.SOUTH);
    game.movePlayer(Directions.WEST);
    game.movePlayer(Directions.WEST);
    assertTrue(game.hasPit(game.getBoard()[5][2]));
    assertTrue(game.isPlayerAlive());
  }

}