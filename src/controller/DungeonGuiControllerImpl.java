package controller;

import dungeonmodel.Directions;
import dungeonmodel.GeneralLocation;
import dungeonmodel.GeneralPlayer;
import dungeonmodel.GeneralPlayerImpl;
import dungeonmodel.MonsterStatus;
import dungeonmodel.NumberGenerationInterface;
import dungeonmodel.PitsThievesDungeonImpl;
import dungeonmodel.PitsThievesDungeon;
import dungeonmodel.PredictiveGeneration;
import dungeonmodel.RandomGeneration;
import view.IGameView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Graphical User Interface controller for the dungeon model. End user can
 * access this class to play the game via GUI. It reads input from and displays the output
 * to GUI.
 */
public class DungeonGuiControllerImpl implements DungeonGuiController {

  private PitsThievesDungeon model;
  private PitsThievesDungeon reUsableModel;
  private final IGameView view;
  private List<Integer> randomSeed;
  private static final String SPACE_STRING = " ";

  /**
   * Constructs object for the DungeonGuiControllerImpl class.
   * @param view GameView object.
   */
  public DungeonGuiControllerImpl(IGameView view) {
    this.view = view;
    this.randomSeed = new ArrayList<>();
  }

  /**
   * Constructs object for DungeonGuiControllerImpl class.
   * @param model PitsThievesDungeon Model.
   * @param view GameView object.
   */
  public DungeonGuiControllerImpl(PitsThievesDungeon model, IGameView view) {
    this.model = model;
    this.view = view;
  }

  @Override
  public void playGame() {
    this.view.makeVisible();
    this.view.setSetupFeatures(this);
  }

  @Override
  public void createModel(int rows, int cols, boolean wrapping, int interconnectivity,
                                 int treasurePercent, int numOfMonster, String playerName) {
    GeneralPlayer player = new GeneralPlayerImpl(playerName);
    NumberGenerationInterface random = new RandomGeneration();
    try {
      this.model = new PitsThievesDungeonImpl(rows, cols, interconnectivity, wrapping,
              treasurePercent, numOfMonster, player, random);
    }
    catch (IllegalArgumentException exception) {
      this.view.showErrorMessageForModelCreation(exception.getMessage());
      return;
    }

    this.randomSeed = this.model.getRandomSeed();
    this.reUsableModel = new PitsThievesDungeonImpl(this.model.getRowsInBoard(),
            model.getColsInBoard(), model.getInterConnectivity(), model.getWrapping(),
            model.getTreasureArrowPercent(), model.getNumberOfMonsters(),
            new GeneralPlayerImpl(playerName), new PredictiveGeneration(this.randomSeed));
    this.view.setGameFeatures(this);
    int[] playerPossessions = getPlayerPossessions();
    int[] locationInfo = getLocationPossessions();

    this.view.switchToGameMode(this.model, this, playerPossessions, locationInfo,
            SPACE_STRING, String.join(",",
                    this.getPitsAroundThisLocation(this.model.getPlayerCurrentLocation())));
  }

  private int[] getPlayerPossessions() {
    return new int[]{this.model.getPlayerRubyCount(), this.model.getPlayerEmeraldCount(),
            this.model.getPlayerDiamondCount(), this.model.getPlayerArrowCount()};
  }

  private int[] getLocationPossessions() {
    return new int[]{this.model.getCurrentLocationRubyCount(),
            this.model.getCurrentLocationEmeraldCount(),
            this.model.getCurrentLocationDiamondCount(), this.model.getCurrentLocationArrowCount()};
  }

  @Override
  public void move(Directions directions) {
    StringBuilder status = new StringBuilder();
    int[] playerPossessions = getPlayerPossessions();
    int[] locationInfo = getLocationPossessions();

    try {
      this.model.movePlayer(directions);
      playerPossessions = getPlayerPossessions();
      locationInfo = getLocationPossessions();
      status.append("Player moved to").append(SPACE_STRING).append(directions);
    } catch (IllegalArgumentException | IllegalStateException exception) {
      status.append(exception.getMessage());
    }
    finally {
      this.view.switchToGameMode(this.model, this, playerPossessions, locationInfo,
              status.toString(),
              this.getPitsAroundThisLocation(this.model.getPlayerCurrentLocation()));
      if (model.isGameOver() && model.isPlayerAlive()) {
        this.view.showResultDialogue(true, model.getDeathReason());
      } else if (model.isGameOver() && !model.isPlayerAlive()) {
        this.view.showResultDialogue(false, model.getDeathReason());
      }
    }
  }

  @Override
  public void pickAllArrow() {
    StringBuilder status = new StringBuilder();
    int[] playerPossessions = getPlayerPossessions();
    int[] locationInfo = getLocationPossessions();
    try {
      this.model.pickAllArrows();
      playerPossessions = getPlayerPossessions();
      locationInfo = getLocationPossessions();
      status.append("Picked arrows in the location");
    } catch (IllegalArgumentException | IllegalStateException exception) {
      status.append(exception.getMessage());
    } finally {
      this.view.switchToGameMode(this.model, this, playerPossessions, locationInfo,
              status.toString(),
              this.getPitsAroundThisLocation(this.model.getPlayerCurrentLocation()));
    }
  }

  @Override
  public void pickAllTreasure() {
    StringBuilder status = new StringBuilder();
    int[] playerPossessions = getPlayerPossessions();
    int[] locationInfo = getLocationPossessions();
    try {
      this.model.pickAllTreasures();
      playerPossessions = getPlayerPossessions();
      locationInfo = getLocationPossessions();
      status.append("Picked all treasures in the location");
    } catch (IllegalArgumentException | IllegalStateException exception) {
      status.append(exception.getMessage());
    } finally {
      this.view.switchToGameMode(this.model, this, playerPossessions, locationInfo,
              status.toString(),
              this.getPitsAroundThisLocation(this.model.getPlayerCurrentLocation()));
    }

  }

  @Override
  public void shootArrow(Directions directions, int distance) {
    StringBuilder status = new StringBuilder();
    int[] playerPossessions = getPlayerPossessions();
    int[] locationInfo = getLocationPossessions();
    try {
      MonsterStatus shootStatus = this.model.shootArrow(distance, directions);
      playerPossessions = getPlayerPossessions();
      locationInfo = getLocationPossessions();
      if (shootStatus.equals(MonsterStatus.DEAD)) {
        status.append("You killed a Monster.");
      } else if (shootStatus.equals(MonsterStatus.INJURED)) {
        status.append("You injured a Monster");
      } else if (shootStatus.equals(MonsterStatus.UNAFFECTED)) {
        status.append("You shot into darkness");
      }
    } catch (IllegalArgumentException | IllegalStateException exception) {
      status.append(exception.getMessage());
    } finally {
      this.view.switchToGameMode(this.model, this, playerPossessions, locationInfo,
              status.toString(),
              this.getPitsAroundThisLocation(this.model.getPlayerCurrentLocation()));
    }
  }

  @Override
  public void newGame() {
    this.view.switchToSetupMode(this);
  }

  @Override
  public void quit() {
    this.view.quit();
  }


  @Override
  public void reuseGame() {
    int[] playerPossessions;
    int[] locationInfo;
    this.model = new PitsThievesDungeonImpl(reUsableModel.getRowsInBoard(),
            reUsableModel.getColsInBoard(), reUsableModel.getInterConnectivity(),
            reUsableModel.getWrapping(), reUsableModel.getTreasureArrowPercent(),
            reUsableModel.getNumberOfMonsters(),
            new GeneralPlayerImpl(reUsableModel.getPlayerName()),
            new PredictiveGeneration(this.randomSeed));
    StringBuilder status = new StringBuilder();
    playerPossessions = getPlayerPossessions();
    locationInfo = getLocationPossessions();
    status.append("Created new Game");
    this.view.switchToGameMode(this.model, this, playerPossessions, locationInfo,
            status.toString(), this.getPitsAroundThisLocation(this.model.getPlayerCurrentLocation())
    );
  }

  @Override
  public String getPitsAroundThisLocation(GeneralLocation location) {
    StringBuilder pitsDirString = new StringBuilder(SPACE_STRING).append("towards")
            .append(SPACE_STRING);
    List<Directions> directionsList = this.model.getPitsAroundThisLocation(
            this.model.getPlayerCurrentLocation());
    if (directionsList.size() == 0) {
      return new StringBuilder("None").append(SPACE_STRING).append("around").toString();
    }
    for (Directions directions : directionsList) {
      pitsDirString.append(directions.toString()).append(SPACE_STRING);
    }
    return pitsDirString.toString();
  }

}
