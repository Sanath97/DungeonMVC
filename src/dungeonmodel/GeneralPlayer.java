package dungeonmodel;

import java.util.List;

/**
 * A GeneralPlayer interface represents all the functionalities a player of dungeon game should
 * have. It represents player who plays the game.
 */
public interface GeneralPlayer {

  /**
   * Returns the player name.
   * @return name
   */
  String getPlayerName();

  /**
   * Returns treasures owned by the player.
   * @return treasures owned by player.
   */
  List<Treasures> getTreasuresOwned();

}
