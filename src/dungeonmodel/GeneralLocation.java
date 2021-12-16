package dungeonmodel;

import java.util.List;
import java.util.Map;

/**
 * This interface represents any location in the dungeon in general. It includes all the methods
 * that can be operated on a location object.
 */
public interface GeneralLocation {

  /**
   * Returns the location details.
   * @return location
   */
  Point2D getLocation();

  /**
   * Returns the adjacency list for the current location.
   * @return adjacency list.
   */
  List<GeneralLocation> getAdjacencyList();

  /**
   * Function that checks if given location is cave.
   * @return true if location is cave.
   *         false if it is tunnel.
   */
  boolean isCave();

  /**
   * Returns treasures present in the location.
   * @return treasure list in the location.
   */
  List<Treasures> getTreasuresList();

  /**
   * Returns possible moves from the current location.
   * @return map of direction and adjacent location.
   */
  Map<Directions, GeneralLocation> getPossibleDirectionMoves(int rowCount, int colCount);


  /**
   * Returns number of arrows present in a given location.
   * @return Number of arrows in the location.
   */
  int getArrowCount();

  /**
   * Returns if the current location has Monster.
   * @return true if location has monster, else false.
   */
  boolean hasMonster();

  /**
   * Returns if the current location is visited.
   * @return true if location is visited, else false.
   */
  boolean getVisited();

}
