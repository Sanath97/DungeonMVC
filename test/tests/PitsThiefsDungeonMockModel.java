package tests;

import dungeonmodel.Directions;
import dungeonmodel.GeneralLocation;
import dungeonmodel.MonsterStatus;
import dungeonmodel.PitsThievesDungeon;
import dungeonmodel.Smell;
import dungeonmodel.Treasures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Class to mock Dungeon model
class PitsThiefsDungeonMockModel implements PitsThievesDungeon {

  private final Appendable mockModelLog;

  public PitsThiefsDungeonMockModel(Appendable log) {
    this.mockModelLog = log;
  }

  @Override
  public int getRowsInBoard() {
    try {
      mockModelLog.append("getRowsInBoard ").append("\n");
    } catch (IOException ignored) {

    }
    return 0;
  }

  @Override
  public int getColsInBoard() {
    try {
      mockModelLog.append("getColsInBoard ").append("\n");
    } catch (IOException ignored) {

    }
    return 0;
  }

  @Override
  public void movePlayer(Directions direction) {
    try {
      mockModelLog.append("movePlayer ").append(direction.toString()).append("\n");
    } catch (IOException ignored) {

    }
  }


  @Override
  public GeneralLocation getStartLocation() {
    return null;
  }

  @Override
  public GeneralLocation getEndLocation() {
    return null;
  }

  @Override
  public GeneralLocation getPlayerCurrentLocation() {
    return null;
  }

  @Override
  public boolean isGameOver() {
    try {
      mockModelLog.append("isGameOver ").append("\n");
    } catch (IOException ignored) {

    }
    return false;
  }

  @Override
  public String getPlayerCurrLocationInfo() {
    return null;
  }


  @Override
  public void pickTreasure(Treasures t) {
    try {
      mockModelLog.append("pickTreasure\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public void pickArrow() {
    try {
      mockModelLog.append("pickArrow\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public MonsterStatus shootArrow(int distance, Directions direction) {
    try {
      mockModelLog.append("shootArrow").append(String.valueOf(distance))
              .append(direction.toString()).append("\n");
    } catch (IOException ignored) {

    }
    return MonsterStatus.INJURED;
  }

  @Override
  public String getPlayerInfo() {
    return null;
  }

  @Override
  public boolean isPlayerAlive() {
    try {
      mockModelLog.append("isPlayerAlive\n");
    } catch (IOException ignored) {

    }
    return false;
  }

  @Override
  public GeneralLocation[][] getBoard() {
    return new GeneralLocation[0][];
  }

  @Override
  public boolean getWrapping() {
    return false;
  }

  @Override
  public int getInterConnectivity() {
    return 0;
  }

  @Override
  public int getTreasureArrowPercent() {
    return 0;
  }

  @Override
  public int getNumberOfMonsters() {
    return 0;
  }

  @Override
  public String getPlayerName() {
    return null;
  }

  @Override
  public void pickAllArrows() {
    try {
      mockModelLog.append("pickAllArrows\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public void pickAllTreasures() {
    try {
      mockModelLog.append("pickAllTreasures\n");
    } catch (IOException ignored) {

    }
  }

  @Override
  public int getPlayerArrowCount() {
    try {
      mockModelLog.append("getPlayerArrowCount ").append("\n");
    } catch (IOException ignored) {

    }
    return 0;
  }

  @Override
  public int getPlayerRubyCount() {
    try {
      mockModelLog.append("getPlayerRubyCount ").append("\n");
    } catch (IOException ignored) {

    }
    return 0;
  }

  @Override
  public int getPlayerDiamondCount() {
    try {
      mockModelLog.append("getPlayerDiamondCount ").append("\n");
    } catch (IOException ignored) {

    }
    return 0;
  }

  @Override
  public int getPlayerEmeraldCount() {
    try {
      mockModelLog.append("getPlayerEmeraldCount ").append("\n");
    } catch (IOException ignored) {

    }
    return 0;
  }

  @Override
  public int getCurrentLocationArrowCount() {
    try {
      mockModelLog.append("getCurrentLocationArrowCount ").append("\n");
    } catch (IOException ignored) {

    }
    return 0;
  }

  @Override
  public int getCurrentLocationRubyCount() {
    try {
      mockModelLog.append("getCurrentLocationRubyCount ").append("\n");
    } catch (IOException ignored) {

    }
    return 0;
  }

  @Override
  public int getCurrentLocationDiamondCount() {
    try {
      mockModelLog.append("getCurrentLocationDiamondCount ").append("\n");
    } catch (IOException ignored) {

    }
    return 0;
  }

  @Override
  public int getCurrentLocationEmeraldCount() {
    try {
      mockModelLog.append("getCurrentLocationEmeraldCount ").append("\n");
    } catch (IOException ignored) {

    }
    return 0;
  }

  @Override
  public Smell getSmellInLocation(GeneralLocation location) {
    return null;
  }

  @Override
  public List<Integer> getRandomSeed() {
    return null;
  }

  @Override
  public boolean hasPit(GeneralLocation location) {
    return false;
  }

  @Override
  public int getDeathReason() {
    return 0;
  }

  @Override
  public boolean hasThief(GeneralLocation location) {
    return false;
  }

  @Override
  public List<Directions> getPitsAroundThisLocation(GeneralLocation location) {
    return new ArrayList<>();
  }

  public Appendable getLog() {
    return this.mockModelLog;
  }

}

