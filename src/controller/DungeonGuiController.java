package controller;

import dungeonmodel.Directions;
import dungeonmodel.GeneralLocation;

/**
 * Represents a Graphical Interface Controller for Dungeon model: handles user clicks/actions in a
 * GUI by executing them using the model; conveys response from the model back to user through GUI.
 */
public interface DungeonGuiController {

  /**
   * Method creates and sets up the initial configuration view and enables user to start playing
   * the game via the Graphical User Interface.
   */
  void playGame();

  /**
   * Method creates the dungeon model as per the user entered configuration from the initial Setup
   * View page.
   * @param rows Rows in dungeon model.
   * @param cols Columns in dungeon model.
   * @param wrapping Represents if dungeon should be wrapped or not.
   * @param interconnectivity Interconnectivity of dungeon.
   * @param treasurePercent Percentage of caves that should be treasured.
   * @param numOfMonster Number of monsters in dungeon.
   * @param playerName Name of the Player.
   */
  void createModel(int rows, int cols, boolean wrapping, int interconnectivity, int treasurePercent,
                          int numOfMonster, String playerName);

  /**
   * This method handles player movement from the user interface by leveraging the model.
   * @param directions direction to move the player.
   */
  void move(Directions directions);

  /**
   * This method picks all arrows present in the current player location.
   */
  void pickAllArrow();

  /**
   * This method picks all the treasures present in the current player location.
   */
  void pickAllTreasure();

  /**
   * This method handles quitting the game when user requests the same via the Graphical user
   * interface.
   */
  void quit();

  /**
   * This method handles shooting the arrow from the User Interface by leveraging the model.
   * @param directions Direction in which arrow to be shot.
   * @param distance Number of caves arrow should travel.
   */
  void shootArrow(Directions directions, int distance);

  /**
   * This method handles game restart with the exact same configuration and state as the previous
   * game.
   */
  void reuseGame();

  /**
   * This method provides information if pits are present around the given location.
   * @return A string representing directions.
   */
  String getPitsAroundThisLocation(GeneralLocation location);

  /**
   * This method handles creating a new game from the Graphical User Interface.
   */
  void newGame();
}
