package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import controller.DungeonGuiController;
import controller.DungeonGuiControllerImpl;
import dungeonmodel.Directions;
import org.junit.Test;

/**
 * Tests for the Dungeon GUI controller class.
 */
public class DungeonGuiControllerImplTest {

  @Test
  public void testControllerMoveCalledCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.move(Directions.WEST);
    String moveExpected = "movePlayer WEST\n";
    String refreshExpected = "switchToGameMode\n";
    assertTrue(mockLog.toString().contains(moveExpected));
    assertTrue(mockLog.toString().contains(refreshExpected));
  }

  @Test
  public void testControllerPickAllArrowsCalledCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.pickAllArrow();
    String pickUpExpected = "pickAllArrows\n";
    String refreshExpected = "switchToGameMode\n";
    assertTrue(mockLog.toString().contains(pickUpExpected));
    assertTrue(mockLog.toString().contains(refreshExpected));
  }

  @Test
  public void testControllerPickAllTreasuresCalledCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.pickAllTreasure();
    String pickUpExpected = "pickAllTreasures\n";
    String refreshExpected = "switchToGameMode\n";
    assertTrue(mockLog.toString().contains(pickUpExpected));
    assertTrue(mockLog.toString().contains(refreshExpected));
  }

  @Test
  public void testControllerShootCalledCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.shootArrow(Directions.WEST, 5);
    String pickUpExpected = "shootArrow5WEST\n";
    String refreshExpected = "switchToGameMode\n";
    assertTrue(mockLog.toString().contains(pickUpExpected));
    assertTrue(mockLog.toString().contains(refreshExpected));
  }

  @Test
  public void testControllerplayGameCalledCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.playGame();
    String expected = "makeVisible\n"
                      + "setSetupFeatures\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void testControllernewGameCalledCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.newGame();
    String expected = "switchToSetupMode\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void testControllercreateInvalidModelCalledCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.createModel(-7, 7, true, 2, 45, 3, "test");
    String expected = "showErrorMessageForModelCreation\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void testControllercreateValidModelCalledCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.createModel(7, 7, true, 2, 45, 3, "test");
    String expected = "setGameFeatures\n"
                      + "switchToGameMode\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void testControllercalledQuitCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.quit();
    String expected = "quit\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void testControllerReStartCalledCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.createModel(7, 7, true, 2, 45, 3, "test");
    controller.createModel(7, 7, true, 2, 45, 3, "test");
    String expected = "setGameFeatures\n"
                      + "switchToGameMode\n"
                      + "setGameFeatures\n"
                      + "switchToGameMode\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void testControllerSequenceCalledCorrectly() {
    StringBuilder mockLog = new StringBuilder();
    PitsThiefsDungeonMockModel mockDungeonModel = new PitsThiefsDungeonMockModel(mockLog);
    MockView mockView = new MockView(mockLog);
    DungeonGuiController controller = new DungeonGuiControllerImpl(mockDungeonModel, mockView);
    controller.newGame();
    controller.move(Directions.WEST);
    controller.shootArrow(Directions.WEST, 2);
    String expected = "switchToSetupMode\n";
    assertEquals(expected, mockLog.toString());
  }



}