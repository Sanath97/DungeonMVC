package dungeonmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represents the actual dungeon which includes pits and thieves features.
 */
public class PitsThievesDungeonImpl extends DungeonGameImpl implements PitsThievesDungeon {

  /**
   * Constructs a new dungeon game object.
   *
   * @param rows                      Number of rows in dungeon.
   * @param cols                      Number of cols in dungeon.
   * @param interconnectivity         Interconnectivity of dungeon.
   * @param wrapping                  Represents if dungeon should be wrapped or not.
   * @param treasureArrowPercent      Percentage of caves that should be treasured.
   *                                  Also, adds arrows of given percent in both caves and tunnels.
   * @param difficulty                Represents number of monsters in dungeon.
   * @param player                    Player playing the game.
   * @param numberGenerationInterface Number Generator interface.
   * @throws IllegalArgumentException if rows are negative
   *                                  or if cols are negative
   *                                  or if interconnectivity are negative
   *                                  or if treasureArrowPercent is negative or greater than 100.
   *                                  or if difficulty is less than or equal to zero.
   *                                  or if player is null
   *                                  or if numberGenerationInterface is null.
   */
  public PitsThievesDungeonImpl(int rows, int cols, int interconnectivity, boolean wrapping,
                                int treasureArrowPercent, int difficulty, GeneralPlayer player,
                                NumberGenerationInterface numberGenerationInterface) {
    super(rows, cols, interconnectivity, wrapping, treasureArrowPercent, difficulty, player,
            numberGenerationInterface);
    addPits();
    addThieves();
  }


  // Adds pits to the dungeon.
  private void addPits() {
    Location end = (Location) this.getEndLocation();
    Location start = (Location) this.getStartLocation();
    List<GeneralLocation> caves = this.getCavesInDungeon();
    int pitsCount = this.getNumberOfMonsters();
    int cavesCount = caves.size();
    if (pitsCount >= cavesCount) {
      // Since end and start shouldn't have pits.
      pitsCount = cavesCount - 2;
    }
    while (pitsCount > 0) {
      int randomCave = this.numberGenerationInterface.getIntInRange(0, caves.size() - 1);
      Location caveLocation = (Location) caves.get(randomCave);
      if (caveLocation.equals(end) || caveLocation.equals(start) || caveLocation.locationHasPit()) {
        continue;
      } else {
        caveLocation.addPit();
        pitsCount--;
      }
    }
  }

  // Adds thieves to the dungeon.
  private void addThieves() {
    Location end = (Location) this.getEndLocation();
    Location start = (Location) this.getStartLocation();
    List<GeneralLocation> tunnels = this.getTunnelsInDungeon();
    int thievesCount = this.getNumberOfMonsters();
    int tunnelsCount = tunnels.size();
    if (thievesCount >= tunnelsCount) {
      // Since end and start shouldn't have thieves.
      thievesCount = tunnelsCount;
    }
    while (thievesCount > 0) {
      int randomCave = this.numberGenerationInterface.getIntInRange(0, tunnelsCount - 1);
      Location caveLocation = (Location) tunnels.get(randomCave);
      if (caveLocation.equals(end) || caveLocation.equals(start)
          || caveLocation.locationHasThief()) {
        continue;
      } else {
        caveLocation.addThief();
        thievesCount--;
      }
    }
  }

  @Override
  public List<Directions> getPitsAroundThisLocation(GeneralLocation location) {
    Map<Directions, GeneralLocation> pitsDirs = location
            .getPossibleDirectionMoves(this.getRowsInBoard(), this.getColsInBoard());
    List<Directions> pitsDirsList = new ArrayList<>();
    for (Map.Entry<Directions, GeneralLocation> entry : pitsDirs.entrySet()) {
      Location loc = (Location) entry.getValue();
      if (loc.locationHasPit()) {
        pitsDirsList.add(entry.getKey());
      }
    }
    return pitsDirsList;
  }

  @Override
  public void movePlayer(Directions directions) {
    super.movePlayer(directions);
    GeneralPlayerImpl concretePlayer = (GeneralPlayerImpl) this.player;
    if (hasPit(playerCurrentLocation)) {
      int playerLife = this.numberGenerationInterface.getIntInRange(0, 2);
      if (playerLife == 0) {
        concretePlayer.killPlayer();
        concretePlayer.setDeathReason(2);
      }
    }
    if (hasThief(playerCurrentLocation)) {
      concretePlayer.removeTreasures();
    }
  }
}
