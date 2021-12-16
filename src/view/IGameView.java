package view;

import controller.DungeonGuiController;
import dungeonmodel.PitsThievesDungeon;

/**
 * This interface represents a view for displaying Dungeon Game. It provides visual interface
 * that allows users to play the game and also displays confirmation messages back.
 */
public interface IGameView  {

  /**
   * This method refreshes the whole game view with the new updated information.
   * @param model PitsThievesDungeon Model.
   * @param controller DungeonGuiController Controller.
   * @param playerPossessions Player Possessions and its count.
   * @param locationInfo Location info and its count.
   * @param status Status of the action performed.
   * @param pitsDirections Directions which has a pit.
   */
  void switchToGameMode(PitsThievesDungeon model, DungeonGuiController controller,
                        int[] playerPossessions, int[] locationInfo, String status,
                        String pitsDirections);

  /**
   * This method refreshes the configuration of setup view.
   * @param dungeonGuiController DungeonGuiController Controller.
   */
  void setSetupFeatures(DungeonGuiController dungeonGuiController);

  /**
   * Make the view visible to start the dungeon game.
   */
  void makeVisible();

  /**
   * Get the set of feature callbacks that the view can use.
   * @param dungeonGuiController DungeonGuiController Controller.
   */
  void setGameFeatures(DungeonGuiController dungeonGuiController);

  /**
   * This method switches to the setup mode from the game view when user creates new game.
   * @param controller DungeonGuiController Controller.
   */
  void switchToSetupMode(DungeonGuiController controller);

  /**
   * This method shows the result dialogue once game ends.
   * @param winStatus win status.
   * @param deathReason reason for death,
   */
  void showResultDialogue(boolean winStatus, int deathReason);

  /**
   * This method shows the error message dialogue.
   * @param message Error Message.
   */
  void showErrorMessageForModelCreation(String message);

  /**
   * This method quits the game.
   */
  void quit();
}
