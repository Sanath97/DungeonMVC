package dungeonmodel;

import java.util.List;

/**
 * This interface represents read only dungeon for the inital dungeon i.e one without new features.
 * It is used for reading model information from the view.
 */
public interface ReadOnlyDungeon {
  /**
   * Returns the number of rows in the Dungeon.
   * @return number of rows.
   */
  int getRowsInBoard();

  /**
   * Returns the number of columns in the Dungeon.
   * @return number of columns.
   */
  int getColsInBoard();

  /**
   * Returns the start location for the dungeon.
   * @return player traversal start location.
   */
  GeneralLocation getStartLocation();

  /**
   * Returns the end location for the dungeon.
   * @return player traversal end location.
   */
  GeneralLocation getEndLocation();

  /**
   * Returns the players current location.
   * @return player current location.
   */
  GeneralLocation getPlayerCurrentLocation();

  /**
   * Checks if game has ended.
   * @return Returns true if game has false otherwise.
   */
  boolean isGameOver();

  /**
   * Returns the players current locations' information. It includes if current location is a
   * tunnel or a cave, doors leading from current location, treasures/arrows if any present in
   * the current location.
   * @return player current location information.
   */
  String getPlayerCurrLocationInfo();


  /**
   * Method returns player information, this includes treasures, arrows player has collected.
   * @return Player information.
   */
  String getPlayerInfo();

  /**
   * Checks if player is alive.
   * @return Returns true if player is alive, false otherwise.
   */
  boolean isPlayerAlive();

  /**
   * Gets the board of the created dungeon.
   * @return Returns a 2D array of locations.
   */
  GeneralLocation[][] getBoard();

  /**
   * Method returns the wrapping used for dungeon creation.
   * @return wrapping value.
   */
  boolean getWrapping();

  /**
   * Method returns the interconnectivity used for dungeon creation.
   * @return interconnectivity value.
   */
  int getInterConnectivity();

  /**
   * Method returns the treasure arrow percentage used for dungeon creation.
   * @return treasure arrow percentage.
   */
  int getTreasureArrowPercent();

  /**
   * Method returns the number of monsters.
   * @return number of monsters.
   */
  int getNumberOfMonsters();

  /**
   * Method returns the player name.
   * @return name of player.
   */
  String getPlayerName();

  /**
   * Method returns the arrows owned by player.
   * @return number of arrows player has.
   */
  int getPlayerArrowCount();

  /**
   * Method returns the ruby count owned by player.
   * @return number of rubies player has.
   */
  int getPlayerRubyCount();

  /**
   * Method returns the diamonds count owned by player.
   * @return number of diamonds player has.
   */
  int getPlayerDiamondCount();

  /**
   * Method returns the emerald count owned by player.
   * @return number of emeralds player has.
   */
  int getPlayerEmeraldCount();

  /**
   * Method returns the arrows present in current location.
   * @return number of arrows.
   */
  int getCurrentLocationArrowCount();

  /**
   * Method returns the rubies present in current location.
   * @return number of rubies.
   */
  int getCurrentLocationRubyCount();

  /**
   * Method returns the diamonds present in current location.
   * @return number of diamonds.
   */
  int getCurrentLocationDiamondCount();

  /**
   * Method returns the emeralds present in current location.
   * @return number of emeralds.
   */
  int getCurrentLocationEmeraldCount();

  /**
   * Method returns smell intensity in a given location.
   * @param location Given locaation.
   * @return Smell in the given location.
   */
  Smell getSmellInLocation(GeneralLocation location);

  /**
   * Method returns random seed used for model creation.
   * @return List of random numbers used.
   */
  List<Integer> getRandomSeed();

  /**
   * Method checks if given location has a pit.
   * @param location Given location.
   * @return true if pit is present else false.
   */
  boolean hasPit(GeneralLocation location);

  /**
   * Method returns an integer that specifies the reason for the death of the player.
   * @return reason for the death,
   *         1 if player is killed by Monster.
   *         2 if player dies by falling in pit.
   */
  int getDeathReason();

  /**
   * Method checks if given location has thief.
   * @param location Given location.
   * @return true if thief is present else false.
   */
  boolean hasThief(GeneralLocation location);
}
