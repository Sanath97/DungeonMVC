package tests;

import dungeonmodel.Directions;
import dungeonmodel.DungeonGame;
import dungeonmodel.GeneralLocation;
import dungeonmodel.MonsterStatus;
import dungeonmodel.Smell;
import dungeonmodel.Treasures;

import java.util.List;

// Class to mock Dungeon model
class MockDungeonModel implements DungeonGame {

  public static String VALIDATIONSTRING;

  @Override
  public int getRowsInBoard() {
    return 0;
  }

  @Override
  public int getColsInBoard() {
    return 0;
  }

  @Override
  public void movePlayer(Directions direction) {
    VALIDATIONSTRING = null;
    StringBuilder sb = new StringBuilder();
    sb.append("Function: movePlayer, Direction: ").append(direction);
    VALIDATIONSTRING = sb.toString();
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
    return false;
  }

  @Override
  public String getPlayerCurrLocationInfo() {
    return null;
  }


  @Override
  public void pickTreasure(Treasures t) {
    VALIDATIONSTRING = null;
    StringBuilder sb = new StringBuilder();
    sb.append("Function: pickTreasure ").append("Treasure: ").append(t);
    VALIDATIONSTRING = sb.toString();
  }

  @Override
  public void pickArrow() {
    VALIDATIONSTRING = null;
    StringBuilder sb = new StringBuilder();
    sb.append("Function: pickArrow");
    VALIDATIONSTRING = sb.toString();
  }

  @Override
  public void pickAllArrows() {
    VALIDATIONSTRING = null;
    StringBuilder sb = new StringBuilder();
    sb.append("Function: pickAllArrows");
    VALIDATIONSTRING = sb.toString();
  }

  @Override
  public void pickAllTreasures() {
    VALIDATIONSTRING = null;
    StringBuilder sb = new StringBuilder();
    sb.append("Function: pickAllTreasures");
    VALIDATIONSTRING = sb.toString();
  }

  @Override
  public MonsterStatus shootArrow(int distance, Directions direction) {
    VALIDATIONSTRING = null;
    StringBuilder sb = new StringBuilder();
    sb.append("Function: shootArrow, Distance: ")
            .append(distance).append(" Direction: ").append(direction);
    VALIDATIONSTRING = sb.toString();
    return MonsterStatus.INJURED;
  }

  @Override
  public String getPlayerInfo() {
    return null;
  }

  @Override
  public boolean isPlayerAlive() {
    VALIDATIONSTRING = null;
    StringBuilder sb = new StringBuilder();
    sb.append("Function: isPlayerAlive");
    VALIDATIONSTRING = sb.toString();
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
  public int getPlayerArrowCount() {
    return 0;
  }

  @Override
  public int getPlayerRubyCount() {
    return 0;
  }

  @Override
  public int getPlayerDiamondCount() {
    return 0;
  }

  @Override
  public int getPlayerEmeraldCount() {
    return 0;
  }

  @Override
  public int getCurrentLocationArrowCount() {
    return 0;
  }

  @Override
  public int getCurrentLocationRubyCount() {
    return 0;
  }

  @Override
  public int getCurrentLocationDiamondCount() {
    return 0;
  }

  @Override
  public int getCurrentLocationEmeraldCount() {
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

}

