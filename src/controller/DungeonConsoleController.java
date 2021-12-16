package controller;

import dungeonmodel.DungeonGame;

/**
 * Represents a Console Controller for Dungeon model: handles user commands by executing them using
 * the model; conveys response from the model back to user via Console.
 */
public interface DungeonConsoleController {
  /**
   * Method creates and sets up the dungeon as per the model specifications and allows user to
   * play the game.
   * @param game DungeonGame model object.
   * @throws IllegalStateException if invalid Appendable object is passed
   */
  void playGame(DungeonGame game);
}
