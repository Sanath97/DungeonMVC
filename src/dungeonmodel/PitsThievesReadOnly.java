package dungeonmodel;

import java.util.List;

/**
 * This interface represents a read only model for the new dungeon with thieves and pets. It is
 * used for reading model information from the view.
 */
public interface PitsThievesReadOnly extends ReadOnlyDungeon {

  /**
   * Method returns list of directions where pits are found from the given location.
   * @param location Given location to look for.
   * @return List of directions where pits are present.
   */
  List<Directions> getPitsAroundThisLocation(GeneralLocation location);
}
