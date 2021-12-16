package tests;

import controller.DungeonGuiController;
import dungeonmodel.PitsThievesDungeon;
import view.IGameView;

import java.io.IOException;

// Mock view for testing the view and controller interactions.
class MockView implements IGameView {

  private Appendable mockViewLog;

  public MockView(Appendable log) {
    this.mockViewLog = log;
  }

  @Override
  public void switchToGameMode(PitsThievesDungeon model, DungeonGuiController controller,
                               int[] playerPossessions, int[] locationInfo, String status,
                               String pitsDirections) {
    try {
      mockViewLog.append("switchToGameMode\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public void setSetupFeatures(DungeonGuiController dungeonGuiController) {
    try {
      mockViewLog.append("setSetupFeatures\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public void makeVisible() {
    try {
      mockViewLog.append("makeVisible\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public void setGameFeatures(DungeonGuiController dungeonGuiController) {
    try {
      mockViewLog.append("setGameFeatures\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public void switchToSetupMode(DungeonGuiController controller) {
    try {
      mockViewLog.append("switchToSetupMode\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public void showResultDialogue(boolean winStatus, int deathReason) {
    try {
      mockViewLog.append("showResultDialogue\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public void showErrorMessageForModelCreation(String message) {
    try {
      mockViewLog.append("showErrorMessageForModelCreation\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public void quit() {
    try {
      mockViewLog.append("quit\n");
    } catch (IOException ignored) {

    }
  }

}
