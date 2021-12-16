package dungeonmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a single location in the dungeon. It includes all the methods that can be
 * operated on a single location.
 */
public class Location implements GeneralLocation {
  private final Point2D location;
  private final List<GeneralLocation> adjacencyList;
  private final List<Treasures> treasuresList;
  private Monster monster;
  private int arrowCount;
  private boolean isVisited;
  private boolean hasPit;
  private boolean hasThieve;

  Location(Point2D location) {
    if (location == null) {
      throw new IllegalArgumentException("Null cannot be passed");
    }
    this.location = location;
    this.adjacencyList = new ArrayList<>();
    this.treasuresList = new ArrayList<>();
    this.monster = null;
    this.arrowCount = 0;
    this.isVisited = false;
    this.hasPit = false;
    this.hasThieve = false;
  }

  protected void addPit() {
    this.hasPit = true;
  }

  protected boolean locationHasPit() {
    return this.hasPit;
  }

  protected void addThief() {
    this.hasThieve = true;
  }

  protected boolean locationHasThief() {
    return this.hasThieve;
  }

  protected void setVisited() {
    this.isVisited = true;
  }


  @Override
  public boolean getVisited() {
    return this.isVisited;
  }

  // Add arrows to location.
  protected void addArrows(int count) {
    this.arrowCount = count;
  }

  // Remove arrows from location.
  protected void removeArrow() {
    this.arrowCount--;
  }

  // Remove all arrows from location.
  protected void removeAllArrows() {
    this.arrowCount = 0;
  }

  @Override
  public int getArrowCount() {
    return this.arrowCount;
  }

  // Damages monster if present at current location.
  protected void damageMonster() {
    this.monster.reducedDamage();
  }

  // Returns monster if present at current location.
  protected Monster getMonster() {
    return this.monster;
  }

  // Adds monster to current location.
  protected void addMonster() {
    this.monster = new Otyugh();
  }

  // Checks if current location has monster.
  @Override
  public boolean hasMonster() {
    return this.monster != null && this.monster.getPotentialDamage() > 0;
  }

  @Override
  public Point2D getLocation() {
    return new Point2D(this.location.getX(), this.location.getY());
  }

  @Override
  public boolean equals(Object other)  {
    if (other instanceof GeneralLocation) {
      return this.getLocation().equals(((Location) other).getLocation());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.location.getX(), this.location.getX());
  }

  // updates adjacencies for current location.
  protected void updateAdjacencies(GeneralLocation location) {
    this.adjacencyList.add(location);
  }

  @Override
  public List<GeneralLocation> getAdjacencyList() {
    return List.copyOf(this.adjacencyList);
  }


  // Adds treasure to current location.
  protected void addTreasures(Treasures treasure) {
    this.treasuresList.add(treasure);
  }

  // Gets smell in current location.
  protected Smell getSmellInCave() {
    List<GeneralLocation> twoDistantAwayLocations = new ArrayList<>();
    for (int i = 0; i < this.adjacencyList.size(); i++) {
      Location location = (Location) this.adjacencyList.get(i);
      for (int j = 0; j < location.getAdjacencyList().size(); j++) {
        Location location1 = (Location) location.getAdjacencyList().get(j);
        if (!twoDistantAwayLocations.contains(location1)) {
          twoDistantAwayLocations.add(location1);
        }
      }
      if (location.hasMonster()) {
        return Smell.MOREPUNGENT;
      }
    }
    int monsterCount = 0;
    for (int i = 0; i < twoDistantAwayLocations.size(); i++) {
      Location location = (Location) twoDistantAwayLocations.get(i);
      if (location.hasMonster()) {
        monsterCount++;
      }
    }
    if (monsterCount == 1) {
      return Smell.LESSPUNGENT;
    }
    else if (monsterCount > 1) {
      return Smell.MOREPUNGENT;
    }
    else {
      return null;
    }
  }

  @Override
  public List<Treasures> getTreasuresList() {
    return List.copyOf(this.treasuresList);
  }

  @Override
  public Map<Directions, GeneralLocation> getPossibleDirectionMoves(int rowCount, int colCount) {
    Map<Directions, GeneralLocation> locationMap = new HashMap<>();
    int currentX = this.location.getX();
    int currentY = this.location.getY();
    for (int i = 0; i < this.adjacencyList.size(); i++) {
      int adjacentX = this.adjacencyList.get(i).getLocation().getX();
      int adjacentY = this.adjacencyList.get(i).getLocation().getY();
      if ((adjacentX == currentX + 1)) {
        locationMap.put(Directions.SOUTH, this.getAdjacencyList().get(i));
      }
      else if (adjacentX == currentX - 1 ) {
        locationMap.put(Directions.NORTH, this.getAdjacencyList().get(i));
      }
      else if (adjacentY == currentY + 1 ) {
        locationMap.put( Directions.EAST, this.getAdjacencyList().get(i));
      }
      else if (adjacentY == currentY - 1 ) {
        locationMap.put(Directions.WEST, this.getAdjacencyList().get(i));
      }
      if (currentX - adjacentX == - (rowCount - 1) ) {
        locationMap.put(Directions.NORTH, this.getAdjacencyList().get(i));
      }
      else if (currentX - adjacentX == rowCount - 1) {
        locationMap.put(Directions.SOUTH, this.getAdjacencyList().get(i));
      }
      if (currentY - adjacentY == - (colCount - 1) ) {
        locationMap.put(Directions.WEST, this.getAdjacencyList().get(i));
      }
      else if (currentY - adjacentY == colCount - 1) {
        locationMap.put(Directions.EAST, this.getAdjacencyList().get(i));
      }
    }
    return locationMap;
  }

  @Override
  public boolean isCave() {
    return this.adjacencyList.size() != 2;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(this.location.toString())
            .append(", Treasures: ")
            .append(this.treasuresList);
    if (this.getArrowCount() > 0) {
      stringBuilder.append(", Has ").append(this.arrowCount).append(" Arrows");
    }
    else {
      stringBuilder.append(", No Arrows");
    }
    if (this.hasPit) {
      stringBuilder.append("Has pit.");
    }
    return stringBuilder.toString();
  }

  // Removes treasure from current location.
  protected void removeTreasure(Treasures t) {
    this.treasuresList.remove(t);
  }

  // Removes treasure from current location.
  protected void removeAllTreasures() {
    this.treasuresList.clear();
  }


}
