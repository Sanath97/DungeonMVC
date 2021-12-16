package dungeonmodel;

/**
 * A single game of DungeonGame is played by one player traversing from start location in maze to
 * the end location. DungeonGame interface represents all the methods that are applicable to run
 * the Dungeon game.
 */
public interface DungeonGame extends ReadOnlyDungeon {

  /**
   * This method moves player in the given direction. If player cannot be moved in the given
   * direction method throws an exception.
   * @param direction direction to move the player.
   * @throws IllegalArgumentException if player cannot be moved in the given direction or
   *                                  null is passed for direction.
   */
  void movePlayer(Directions direction);

  /**
   * Method shoots an arrow for the given distance in the specified direction.
   * @param distance Number of caves arrow should travel.
   * @param direction Direction in which arrow to be shot.
   * @return MonsterStatus, specifying if arrow killed/injured or unaffected a Monster.
   * @throws IllegalArgumentException if invalid distance is provided.
   * @throws IllegalStateException if player shoots though one doesn't have any arrows.
   */
  MonsterStatus shootArrow(int distance, Directions direction);

  /**
   * Method picks up the treasure passed if it is present in the current location.
   * @param t Treasure to be picked up.
   * @throws IllegalArgumentException If current location doesn't have any treasures or the given
   *                                  treasure.
   */
  void pickTreasure(Treasures t);

  /**
   * Method picks up a single arrow if it is present in the current location.
   * @throws IllegalArgumentException If current location doesn't have any arrows.
   */
  void pickArrow();

  /**
   * Method picks up all the arrows if any present in the current location.
   */
  void pickAllArrows();

  /**
   * Method picks up all the treasures if any present in the current location.
   */
  void pickAllTreasures();

}
