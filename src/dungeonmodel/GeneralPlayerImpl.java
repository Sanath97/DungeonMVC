package dungeonmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a player of this game and all the operations that
 * can be done on a player.
 */
public class GeneralPlayerImpl implements GeneralPlayer {
  private final String name;
  private final List<Treasures> treasuresOwned;
  private int arrowsHold;
  private boolean isAlive;
  private int deathReason;

  /**
   * Constructs a player object.
   * @param name Name of the player.
   */
  public GeneralPlayerImpl(String name) {
    this.name = name;
    this.treasuresOwned = new ArrayList<>();
    this.arrowsHold = 3;
    this.isAlive = true;
    this.deathReason = 0;
  }

  // Changes player life status to death.
  protected void killPlayer() {
    this.isAlive = false;
  }

  // set death reason.
  protected void setDeathReason(int deathReason) {
    this.deathReason = deathReason;
  }

  // gets death reason.
  protected int getDeathReason() {
    return this.deathReason;
  }

  // Returns current player life status.
  protected boolean getPlayerStatus() {
    return this.isAlive;
  }

  @Override
  public String getPlayerName() {
    return this.name;
  }


  @Override
  public List<Treasures> getTreasuresOwned() {
    return List.copyOf(this.treasuresOwned);
  }

  // Adds treasure to the player.
  protected void addTreasures(Treasures t) {
    this.treasuresOwned.add(t);
  }

  // Adds all treasure to the player.
  protected void addAllTreasures(List<Treasures> treasuresList) {
    this.treasuresOwned.addAll(treasuresList);
  }

  // Adds arrow to current location.
  protected void addArrow() {
    this.arrowsHold = this.arrowsHold + 1;
  }

  // Adds all arrows to current location.
  protected void addAllArrows(int arrowCount) {
    this.arrowsHold = this.arrowsHold + arrowCount;
  }

  // Removes arrow from current location.
  protected void removeArrow() {
    this.arrowsHold = this.arrowsHold - 1;
  }

  // Removes arrow from current location.
  protected void removeTreasures() {
    this.treasuresOwned.clear();
  }


  // Gets number of arrows in current location.
  protected int getArrow() {
    return this.arrowsHold;
  }

}
