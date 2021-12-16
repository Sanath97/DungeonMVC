package dungeonmodel;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class represents the actual dungeon game and all the methods that can be operated on the
 * dungeon game.
 */
public class DungeonGameImpl implements DungeonGame {
  private final int rows;
  private final int cols;
  private final int interconnectivity;
  private final boolean wrapping;
  protected final GeneralPlayer player;
  private final GeneralLocation[][] dungeon;
  private GeneralLocation start;
  private GeneralLocation end;
  private final int[] parentNodes;
  private final int treasureArrowPercent;
  protected final NumberGenerationInterface numberGenerationInterface;
  protected GeneralLocation playerCurrentLocation;
  private final int difficulty;

  /**
   * Constructs a new dungeon game object.
   * @param rows Number of rows in dungeon.
   * @param cols Number of cols in dungeon.
   * @param interconnectivity Interconnectivity of dungeon.
   * @param wrapping Represents if dungeon should be wrapped or not.
   * @param treasureArrowPercent Percentage of caves that should be treasured.
   *                        Also, adds arrows of given percent in both caves and tunnels.
   * @param player Player playing the game.
   * @param numberGenerationInterface Number Generator interface.
   * @param difficulty Represents number of monsters in dungeon.
   * @throws IllegalArgumentException  if rows are negative
   *                                or if cols are negative
   *                                or if interconnectivity are negative
   *                                or if treasureArrowPercent is negative or greater than 100.
   *                                or if difficulty is less than or equal to zero.
   *                                or if player is null
   *                                or if numberGenerationInterface is null.
   */
  public DungeonGameImpl(int rows, int cols, int interconnectivity, boolean wrapping,
                         int treasureArrowPercent,
                         int difficulty,
                         GeneralPlayer player,
                         NumberGenerationInterface numberGenerationInterface) {
    if (rows < 0 || cols < 0) {
      throw new IllegalArgumentException("Number of rows, columns cannot be negative");
    }
    if (interconnectivity < 0) {
      throw new IllegalArgumentException("Interconnectivity cannot be negative");
    }
    if (treasureArrowPercent < 0 || treasureArrowPercent > 100) {
      throw new IllegalArgumentException("Treasure percentage cannot be negative or greater than "
              + "100");
    }
    if (difficulty <= 0) {
      throw new IllegalArgumentException("Difficulty/Number of Monsters cannot be negative");
    }
    if (player == null) {
      throw new IllegalArgumentException("Player cannot be null. Pass a valid player object");
    }
    if (numberGenerationInterface == null) {
      throw new IllegalArgumentException("Number generation interface cannot be null");
    }
    this.rows = rows;
    this.cols = cols;
    this.interconnectivity = interconnectivity;
    this.treasureArrowPercent = treasureArrowPercent;
    this.difficulty = difficulty;
    this.wrapping = wrapping;
    this.player = player;
    this.dungeon = new GeneralLocation[this.rows][this.cols];
    this.parentNodes = new int[this.rows * this.cols];
    this.numberGenerationInterface = numberGenerationInterface;
    this.start = null;
    this.end = null;
    this.playerCurrentLocation = null;
    createDungeon();
    ((Location)this.playerCurrentLocation).setVisited();
  }


  // Creates dungeon for the dungeon game.
  private void createDungeon() {
    // Creating locations in dungeon.
    for (int row = 0; row < this.rows; row++) {
      for (int col = 0; col < this.cols; col++) {
        this.dungeon[row][col] = new Location(new Point2D(row, col));
      }
    }
    List<LocationMap> potentialPaths;
    potentialPaths = makePotentialPaths(wrapping);
    List<LocationMap> unusedPaths;
    unusedPaths = buildMst(potentialPaths);
    if (this.interconnectivity > unusedPaths.size()) {
      throw new IllegalArgumentException(String.format("Interconnectivity of %d is not possible for"
              + " the dungeon of size: (%d, %d)", this.interconnectivity, this.rows, this.cols));
    }
    incorporateInterConnectivity(unusedPaths);
    addTreasuresToDungeon();
    getStartEndLocations();
    addMonstersToDungeon();
    addArrowsToDungeon();
  }

  // Returns nCr value.
  private BigInteger nCombinationOfTwo(int n) {
    return (factorial(n)).divide(factorial(2).multiply(factorial(n - 2)));
  }

  // Returns factorial of an integer.
  private BigInteger factorial(int number) {
    // Initialize result
    BigInteger f = new BigInteger("1");

    for (int i = 2; i <= number; i++) {
      f = f.multiply(BigInteger.valueOf(i));
    }
    return f;
  }

  @Override
  public boolean isPlayerAlive() {
    return ((GeneralPlayerImpl) this.player).getPlayerStatus();
  }

  @Override
  public boolean isGameOver() {
    GeneralPlayerImpl player = (GeneralPlayerImpl) this.player;
    return this.playerCurrentLocation.equals(this.end) || !player.getPlayerStatus();
  }

  // Gets possible directions from a given location.
  protected List<Directions> getPossibleDirections() {
    Map<Directions, GeneralLocation> dirsPossible =
            this.playerCurrentLocation.getPossibleDirectionMoves(this.rows, this.cols);
    ArrayList<Directions> directions = new ArrayList<>();
    directions.addAll(dirsPossible.keySet());
    directions.sort(Comparator.comparing(Enum::toString));
    return directions;
  }

  @Override
  public boolean hasPit(GeneralLocation location) {
    Location location1 = (Location) location;
    return location1.locationHasPit();

  }

  @Override
  public Smell getSmellInLocation(GeneralLocation location) {
    Location concretePlayerLoc = (Location) location;
    return concretePlayerLoc.getSmellInCave();
  }

  @Override
  public List<Integer> getRandomSeed() {
    return ((RandomGeneration)this.numberGenerationInterface).getRandomValuesList();
  }


  @Override
  public String getPlayerCurrLocationInfo() {
    GeneralLocation playerCurrentLocation = this.playerCurrentLocation;
    StringBuilder stringBuilder = new StringBuilder();
    Location location = (Location) playerCurrentLocation;
    Smell smell = location.getSmellInCave();
    if (smell != null) {
      stringBuilder.append("You smell something ").append(smell).append(" here\n");
    }
    stringBuilder.append("You are in a ");
    if (playerCurrentLocation.isCave()) {
      stringBuilder.append("cave\n");
    }
    else {
      stringBuilder.append("tunnel\n");
    }
    if (getCumulativeTreasures(playerCurrentLocation.getTreasuresList()).size() > 0) {
      stringBuilder.append("You find ");
      Map<Treasures, Integer> cumTreasures =
              getCumulativeTreasures(playerCurrentLocation.getTreasuresList());
      for (Map.Entry<Treasures, Integer> entry : cumTreasures.entrySet()) {
        stringBuilder.append(entry.getValue()).append(" ").append(entry.getKey()).append(", ");
      }
      stringBuilder.append(" here\n");
    }
    if (location.getArrowCount() > 0) {
      stringBuilder.append("You find ");
      stringBuilder.append(location.getArrowCount());
      stringBuilder.append(" arrows here\n");
    }
    stringBuilder.append("Doors lead to ");
    stringBuilder.append(this.getPossibleDirections()).append("\n");
    return stringBuilder.toString();
  }

  // Cumulate treasures and returns them with count.
  private Map<Treasures, Integer> getCumulativeTreasures(List<Treasures> treasuresList) {
    Map<Treasures, Integer> cumulativeTreasures = new TreeMap<>();
    for (int i = 0; i < treasuresList.size(); i++) {
      Treasures t = treasuresList.get(i);
      if (cumulativeTreasures.containsKey(t)) {
        cumulativeTreasures.put(t, cumulativeTreasures.get(t) + 1);
      }
      else {
        cumulativeTreasures.put(t, 1);
      }
    }
    return cumulativeTreasures;
  }

  // Computes and returns start and end locations.
  private void getStartEndLocations() {
    List<LocationMap> consideredEdges = new ArrayList<>();
    List<GeneralLocation> caves = getCavesInDungeon();
    BigInteger numOfCombinations = nCombinationOfTwo(caves.size());
    numOfCombinations = numOfCombinations.divide(BigInteger.TWO);
    if (caves.size() < 2) {
      throw new IllegalArgumentException("Two nodes with 5 units apart are not found for this "
                                         + "dungeonmodel");
    }
    int randomCave1 = this.numberGenerationInterface.getIntInRange(0, caves.size() - 1);
    int randomCave2 = this.numberGenerationInterface.getIntInRange(0, caves.size() - 1);
    while (findDistance(caves.get(randomCave1), caves.get(randomCave2)) <= 5
            && numOfCombinations.compareTo(BigInteger.ZERO) > 0) {
      if (!consideredEdges.contains(new LocationMap(caves.get(randomCave1),
              caves.get(randomCave2)))) {
        numOfCombinations = numOfCombinations.subtract(BigInteger.ONE);
        consideredEdges.add(new LocationMap(caves.get(randomCave1), caves.get(randomCave2)));
      }
      randomCave1 = this.numberGenerationInterface.getIntInRange(0, caves.size() - 1);
      randomCave2 = this.numberGenerationInterface.getIntInRange(0, caves.size() - 1);
    }

    if (numOfCombinations.compareTo(BigInteger.ZERO) <= 0) {
      throw new IllegalArgumentException("Two nodes with 5 units apart are not found for this "
                                         + "dungeonmodel");
    }
    this.start = caves.get(randomCave1);
    this.playerCurrentLocation = this.start;
    this.end = caves.get(randomCave2);
  }


  // Find distance between two given points.
  // Reference: https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
  private int findDistance(GeneralLocation source, GeneralLocation destination) {
    boolean[] visited = new boolean[this.rows * this.cols];
    // Create a queue for BFS.
    LinkedList<GeneralLocation> queue = new LinkedList<>();
    int[] distance = new int[this.rows * this.cols];
    // Mark the current node as visited and enqueue it
    visited[getEquivalentOneDIndex(source.getLocation().getX(), source.getLocation().getY())] =
            true;
    distance[getEquivalentOneDIndex(source.getLocation().getX(), source.getLocation().getY())] = 0;
    queue.add(source);
    while (queue.size() != 0) {
      // Dequeue a vertex from queue and print it
      source = queue.poll();

      // Get all adjacent vertices of the dequeued vertex s
      // If a adjacent has not been visited, then mark it
      // visited and enqueue it
      List<GeneralLocation> adjacencies = source.getAdjacencyList();
      ListIterator<GeneralLocation> iterator = adjacencies.listIterator();

      while (iterator.hasNext()) {
        GeneralLocation next = iterator.next();
        int oneDIndex = getEquivalentOneDIndex(next.getLocation().getX(),
                next.getLocation().getY());
        if (!visited[oneDIndex]) {
          distance[oneDIndex] = distance[getEquivalentOneDIndex(source.getLocation().getX(),
                  source.getLocation().getY())] + 1;
          visited[oneDIndex] = true;
          queue.add(next);
        }
      }
    }
    return distance[getEquivalentOneDIndex(destination.getLocation().getX(),
            destination.getLocation().getY())];
  }

  // Adds specified percentage of treasures to dungeon.
  private void addTreasuresToDungeon() {
    List<GeneralLocation> listOfCaves;
    listOfCaves = getCavesInDungeon();
    double currentreasurizedCaves = 0;
    int requiredtreasurizedCaves = (int) ((int)(listOfCaves.size())
                * (((this.treasureArrowPercent) / 100.0)));

    while (currentreasurizedCaves != requiredtreasurizedCaves) {
      int randomCaveRow = this.numberGenerationInterface.getIntInRange(0, this.rows - 1);
      int randomCaveCol = this.numberGenerationInterface
              .getIntInRange(0, this.cols - 1);
      Location location1 =  (Location)this.dungeon[randomCaveRow][randomCaveCol];
      if (location1.getTreasuresList().size() > 0) {
        continue;
      }
      int randomNumberOfTreasures = this.numberGenerationInterface.getIntInRange(1, 3);
      while (randomNumberOfTreasures > 0) {
        int randomTreasure = this.numberGenerationInterface.getIntInRange(0, 3);
        if (randomTreasure == 0) {
          location1.addTreasures(Treasures.DIAMONDS);
        } else if (randomTreasure == 1) {
          location1.addTreasures(Treasures.RUBIES);
        } else if (randomTreasure == 2) {
          location1.addTreasures(Treasures.SAPPHIRE);
        }
        randomNumberOfTreasures--;
      }
      currentreasurizedCaves++;
    }
  }

  //  Add requested number of monsters to dungeon.
  private void addMonstersToDungeon() {
    Location end = (Location) this.end;
    // Adding monster to end location.
    end.addMonster();
    List<GeneralLocation> caves = this.getCavesInDungeon();
    // Subtracting 1, since monster is allotted to end.
    int tempMonsterCount = this.difficulty - 1;
    int cavesCount = caves.size();
    if (tempMonsterCount >= cavesCount) {
      // Since end has monster and start shouldn't have monster.
      tempMonsterCount = cavesCount - 2;
    }
    else {
      tempMonsterCount = this.difficulty - 1;
    }
    while (tempMonsterCount > 0) {
      int randomCave = this.numberGenerationInterface.getIntInRange(0, caves.size() - 1);
      Location caveLocation = (Location) caves.get(randomCave);
      if (caveLocation.equals(this.start)) {
        continue;
      }
      if (!caveLocation.hasMonster()) {
        caveLocation.addMonster();
        tempMonsterCount--;
      }
    }
  }

  //  Add requested number of arrows to dungeon.
  private void addArrowsToDungeon() {
    double currentArrowedLocations = 0;
    int requiredArrowLocations = (int) ((int)(this.rows * this.cols)
            * (((this.treasureArrowPercent) / 100.0)));

    while (currentArrowedLocations != requiredArrowLocations) {
      int randomCaveRow = this.numberGenerationInterface.getIntInRange(0, this.rows - 1);
      int randomCaveCol = this.numberGenerationInterface
              .getIntInRange(0, this.cols - 1);
      Location currLocation = (Location) this.dungeon[randomCaveRow][randomCaveCol];
      if (currLocation.getArrowCount() > 0) {
        continue;
      }
      int randomNumberOfArrows = this.numberGenerationInterface.getIntInRange(1, 3);
      currLocation.addArrows(randomNumberOfArrows);
      currentArrowedLocations++;
    }
  }

  @Override
  public void pickTreasure(Treasures t) {
    if (t == null) {
      throw new IllegalArgumentException("Treasure cannot be null.");
    }
    GeneralLocation location = this.playerCurrentLocation;
    if (location.getTreasuresList().size() == 0) {
      throw new IllegalArgumentException("Current location doesn't have any treasures");
    }
    if (!location.getTreasuresList().contains(t)) {
      throw new IllegalArgumentException("Give from a valid treasure in the location");
    }
    GeneralPlayerImpl concretePlayer = (GeneralPlayerImpl)this.player;
    concretePlayer.addTreasures(t);
    Location location1 =  (Location)location;
    location1.removeTreasure(t);
  }

  @Override
  public void pickArrow() {
    Location location = (Location) this.playerCurrentLocation;
    if (location.getArrowCount() == 0) {
      throw new IllegalArgumentException("Current location don't have arrows");
    }
    GeneralPlayerImpl concretePlayer = (GeneralPlayerImpl)this.player;
    concretePlayer.addArrow();
    location.removeArrow();
  }

  // Given entry location gets exit location for tunnel.
  private GeneralLocation getExitLocationForTunnel(GeneralLocation tunnelLocation,
                                                   GeneralLocation enteredLocation) {
    List<GeneralLocation> tunnelAdjacencies = tunnelLocation.getAdjacencyList();
    if (tunnelAdjacencies.get(0).equals(enteredLocation)) {
      return tunnelAdjacencies.get(1);
    }
    return tunnelAdjacencies.get(0);
  }

  @Override
  public MonsterStatus shootArrow(int distance, Directions direction) {

    checkIfGameOver();
    GeneralPlayerImpl concretePlayer = (GeneralPlayerImpl) this.player;

    if (distance <= 0 || distance > 4) {
      throw new IllegalArgumentException("Valid distances are (1 - 4). Both inclusive.");
    }
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null.");
    }

    if (concretePlayer.getArrow() == 0) {
      throw new IllegalStateException("Player doesn't have any arrows to shoot.");
    }
    // Remove arrow as one is used.
    ((GeneralPlayerImpl) this.player).removeArrow();
    Map<Directions, GeneralLocation> possibleDirections =
            this.playerCurrentLocation.getPossibleDirectionMoves(this.rows, this.cols);
    GeneralLocation arrowLocation = this.playerCurrentLocation;
    if (!possibleDirections.containsKey(direction)) {
      return MonsterStatus.UNAFFECTED;
    }

    Directions enteringDirection = direction;
    GeneralLocation prevLocation = arrowLocation;
    arrowLocation = possibleDirections.get(direction);
    boolean flag = true;
    boolean enteredWhile = false;
    while (distance > 0) {
      enteredWhile = true;
      possibleDirections =
              arrowLocation.getPossibleDirectionMoves(this.rows, this.cols);
      if (!arrowLocation.isCave()) {
        GeneralLocation temp = prevLocation;
        GeneralLocation nextLocation = getExitLocationForTunnel(arrowLocation, temp);
        for (Map.Entry<Directions, GeneralLocation> entry : possibleDirections.entrySet()) {
          if (nextLocation.equals(entry.getValue())) {
            enteringDirection = entry.getKey();
          }
        }
        prevLocation = arrowLocation;
        arrowLocation = nextLocation;
      } else if (arrowLocation.isCave()) {
        prevLocation = arrowLocation;
        arrowLocation = possibleDirections.get(enteringDirection);
        enteringDirection = getOppositeDirection(enteringDirection);
        distance--;
        if (!possibleDirections.containsKey(enteringDirection)) {
          flag = false;
          break;
        }
      }
    }
    if (!flag) {
      return MonsterStatus.UNAFFECTED;
    }
    Location concreteArrowLocation;
    if (enteredWhile) {
      concreteArrowLocation = (Location) prevLocation;
    }
    else {
      concreteArrowLocation = (Location) arrowLocation;
    }
    if (concreteArrowLocation.hasMonster()) {
      // Make damage to monster.
      concreteArrowLocation.damageMonster();
      if (concreteArrowLocation.getMonster().getPotentialDamage() == 0) {
        return MonsterStatus.DEAD;
      }
      else if (concreteArrowLocation.getMonster().getPotentialDamage() == 0.5) {
        return MonsterStatus.INJURED;
      }
    }
    else {
      return MonsterStatus.UNAFFECTED;
    }
    return null;
  }

  @Override
  public String getPlayerInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Player owns below treasures: \n");
    stringBuilder.append(getCumulativeTreasures(this.player.getTreasuresOwned()));
    stringBuilder.append("\n");
    stringBuilder.append("Player owns: ");
    GeneralPlayerImpl concretePlayer = (GeneralPlayerImpl)this.player;
    stringBuilder.append(concretePlayer.getArrow());
    stringBuilder.append(" arrows\n");
    return stringBuilder.toString();
  }

  private Directions getOppositeDirection(Directions direction) {
    Map<Directions, Directions> directionsMap = new HashMap<>();
    directionsMap.put(Directions.SOUTH, Directions.NORTH);
    directionsMap.put(Directions.NORTH, Directions.SOUTH);
    directionsMap.put(Directions.WEST, Directions.EAST);
    directionsMap.put(Directions.EAST, Directions.WEST);
    return directionsMap.get(direction);
  }

  @Override
  public int getDeathReason() {
    return ((GeneralPlayerImpl)this.player).getDeathReason();
  }

  @Override
  public boolean hasThief(GeneralLocation location) {
    return ((Location)location).locationHasThief();
  }

  @Override
  public void movePlayer(Directions direction) {
    if (direction == null) {
      throw new IllegalArgumentException("Direction cannot be null.");
    }

    checkIfGameOver();
    GeneralPlayerImpl concretePlayer = (GeneralPlayerImpl) this.player;

    Location playerLocation = (Location) this.playerCurrentLocation;
    Map<Directions, GeneralLocation> possibleDirections =
            playerLocation.getPossibleDirectionMoves(this.rows, this.cols);
    if (!possibleDirections.containsKey(direction)) {
      throw new IllegalArgumentException("Given direction is not possible from current location");
    }

    this.playerCurrentLocation = possibleDirections.get(direction);
    ((Location)this.playerCurrentLocation).setVisited();
    Monster monster = ((Location)(this.playerCurrentLocation)).getMonster();
    if (monster != null) {
      if (monster.getPotentialDamage() == 1) {
        concretePlayer.killPlayer();
        concretePlayer.setDeathReason(1);
      }
      if (monster.getPotentialDamage() == 0.5) {
        int playerLife = this.numberGenerationInterface.getIntInRange(0, 1);
        if (playerLife == 0) {
          concretePlayer.killPlayer();
          concretePlayer.setDeathReason(1);
        }
      }
    }
  }


  // Get caves list in dungeon.
  protected List<GeneralLocation> getCavesInDungeon() {
    List<GeneralLocation> listOfCaves;
    listOfCaves = new ArrayList<>();
    for (int row = 0; row < this.rows; row++) {
      for (int col = 0; col < this.cols; col++) {
        if (this.dungeon[row][col].isCave()) {
          listOfCaves.add(this.dungeon[row][col]);
        }
      }
    }
    return listOfCaves;
  }

  // Get tunnels list in dungeon.
  protected List<GeneralLocation> getTunnelsInDungeon() {
    List<GeneralLocation> listOfTunnels;
    listOfTunnels = new ArrayList<>();
    for (int row = 0; row < this.rows; row++) {
      for (int col = 0; col < this.cols; col++) {
        if (!this.dungeon[row][col].isCave()) {
          listOfTunnels.add(this.dungeon[row][col]);
        }
      }
    }
    return listOfTunnels;
  }

  @Override
  public GeneralLocation getStartLocation() {
    // Deep copying start location before returning.
    GeneralLocation finalLoc = new Location(this.start.getLocation());
    Location concreteFinalLoc;
    concreteFinalLoc = (Location)finalLoc;
    Location startLoc = (Location) this.start;
    for (int i = 0; i < this.start.getAdjacencyList().size(); i++) {
      concreteFinalLoc.updateAdjacencies(this.start.getAdjacencyList().get(i));
    }
    for (int i = 0; i < this.start.getTreasuresList().size(); i++) {
      concreteFinalLoc.addTreasures(this.start.getTreasuresList().get(i));
    }
    concreteFinalLoc.addArrows(startLoc.getArrowCount());
    double actualMonsterHealth;
    if (startLoc.getMonster() != null) {
      actualMonsterHealth = startLoc.getMonster().getPotentialDamage();
      concreteFinalLoc.addMonster();
      concreteFinalLoc.getMonster().assignDamage(actualMonsterHealth);
    }
    return concreteFinalLoc;
  }

  @Override
  public GeneralLocation getEndLocation() {
    // Deep copying end location before returning.
    GeneralLocation finalLoc = new Location(this.end.getLocation());
    Location concreteFinalLoc;
    concreteFinalLoc = (Location)finalLoc;
    Location endLoc = (Location) this.end;
    for (int i = 0; i < this.end.getAdjacencyList().size(); i++) {
      concreteFinalLoc.updateAdjacencies(this.end.getAdjacencyList().get(i));
    }
    for (int i = 0; i < this.end.getTreasuresList().size(); i++) {
      concreteFinalLoc.addTreasures(this.end.getTreasuresList().get(i));
    }
    concreteFinalLoc.addArrows(endLoc.getArrowCount());
    double actualMonsterHealth;
    if (endLoc.getMonster() != null) {
      actualMonsterHealth = endLoc.getMonster().getPotentialDamage();
      concreteFinalLoc.addMonster();
      concreteFinalLoc.getMonster().assignDamage(actualMonsterHealth);
    }
    return finalLoc;
  }

  @Override
  public GeneralLocation getPlayerCurrentLocation() {
    GeneralLocation finalLoc = new Location(this.playerCurrentLocation.getLocation());
    Location concreteFinalLoc;
    concreteFinalLoc = (Location)finalLoc;
    Location playerLoc = (Location) this.playerCurrentLocation;
    for (int i = 0; i < this.playerCurrentLocation.getAdjacencyList().size(); i++) {
      concreteFinalLoc.updateAdjacencies(this.playerCurrentLocation.getAdjacencyList().get(i));
    }
    for (int i = 0; i < this.playerCurrentLocation.getTreasuresList().size(); i++) {
      concreteFinalLoc.addTreasures(this.playerCurrentLocation.getTreasuresList().get(i));
    }
    concreteFinalLoc.addArrows(playerLoc.getArrowCount());
    double actualMonsterHealth;
    if (playerLoc.getMonster() != null) {
      actualMonsterHealth = playerLoc.getMonster().getPotentialDamage();
      concreteFinalLoc.addMonster();
      concreteFinalLoc.getMonster().assignDamage(actualMonsterHealth);
    }
    return finalLoc;
  }

  // Incorporates interconnectivity specified.
  private void incorporateInterConnectivity(List<LocationMap> unusedPaths) {
    int interConnectivity = this.interconnectivity;
    Map<LocationMap, Boolean> pathsUsed = getMapFromPaths(unusedPaths);
    while (interConnectivity > 0) {
      int randomIndex = this.numberGenerationInterface.getIntInRange(0, unusedPaths.size() - 1);
      LocationMap path = unusedPaths.get(randomIndex);
      while (pathsUsed.get(path) == Boolean.TRUE) {
        randomIndex = this.numberGenerationInterface.getIntInRange(0, unusedPaths.size() - 1);
        path = unusedPaths.get(randomIndex);
      }
      GeneralLocation sourceLocation = path.getSourceLocation();
      GeneralLocation destLocation = path.getDestLocation();
      ((Location) (sourceLocation)).updateAdjacencies(destLocation);
      ((Location) (destLocation)).updateAdjacencies(sourceLocation);
      interConnectivity--;
    }
  }

  // Gets map from the list of paths.
  private Map<LocationMap, Boolean> getMapFromPaths(List<LocationMap> paths) {
    // This map is to track paths that are used in randomization.
    Map<LocationMap, Boolean> pathsUsed = new HashMap<>();

    // Initially all of them are unused, hence set to False.
    for (LocationMap path : paths) {
      pathsUsed.put(path, Boolean.FALSE);
    }
    return pathsUsed;
  }

  // This method builds Minimum spanning tree.
  private List<LocationMap> buildMst(List<LocationMap> potentialPaths) {
    int edgeCount = 0;
    int verticesCount = this.rows * this.cols;
    Map<LocationMap, Boolean> pathsUsed = getMapFromPaths(potentialPaths);

    // Initialize sets of disjoint sets.
    for (int i = 0; i < verticesCount; i++) {
      this.parentNodes[i] = i;
    }

    while (edgeCount != verticesCount - 1) {
      int randomIndex = this.numberGenerationInterface.getIntInRange(0, potentialPaths.size() - 1);
      LocationMap path = potentialPaths.get(randomIndex);
      while (pathsUsed.get(path) == Boolean.TRUE) {
        randomIndex = this.numberGenerationInterface.getIntInRange(0, potentialPaths.size() - 1);
        path = potentialPaths.get(randomIndex);
      }
      GeneralLocation sourceLocation = path.getSourceLocation();
      GeneralLocation destLocation = path.getDestLocation();
      int sourceRow = sourceLocation.getLocation().getX();
      int sourceCol = sourceLocation.getLocation().getY();
      int destRow = destLocation.getLocation().getX();
      int destCol = destLocation.getLocation().getY();
      int oneDIndexOfSource = getEquivalentOneDIndex(sourceRow, sourceCol);
      int oneDIndexOfDest = getEquivalentOneDIndex(destRow, destCol);
      if (find(oneDIndexOfSource) != find(oneDIndexOfDest)) {
        union1(oneDIndexOfSource, oneDIndexOfDest);
        edgeCount++;
        ((Location) (sourceLocation)).updateAdjacencies(destLocation);
        ((Location) (destLocation)).updateAdjacencies(sourceLocation);
        pathsUsed.put(path, Boolean.TRUE);
      }
    }
    List<LocationMap> unusedPotentialPaths;
    unusedPotentialPaths = new ArrayList<>();
    // looping over pathsUsed map.
    for (LocationMap location : pathsUsed.keySet()) {
      if (!pathsUsed.get(location)) {
        unusedPotentialPaths.add(location);
      }
    }
    return unusedPotentialPaths;
  }

  // Dumps dungeon to screen, this is used for debugging purposes.
  private String printGraph() {
    StringBuilder graphInfo = new StringBuilder();
    for (int row = 0; row < this.rows; row++) {
      for (int col = 0; col < this.cols; col++) {
        graphInfo.append(String.format("Vertex %d, %d is connected to %s \n", row, col,
                this.dungeon[row][col].getAdjacencyList()));
      }
    }
    return graphInfo.toString();
  }

  // Returns equivalent one dimensional index for a 2D index.
  private int getEquivalentOneDIndex(int sourceRow, int sourceCol) {
    return ((this.cols) * (sourceRow)) + sourceCol;
  }

  // Find set of vertex i`
  private int find(int i) {
    while (this.parentNodes[i] != i) {
      i = parentNodes[i];
    }
    return i;
  }

  // Does union of i and j. It returns
  // false if i and j are already in same
  // set.
  private void union1(int i, int j) {
    int a = find(i);
    int b = find(j);
    parentNodes[a] = b;
  }

  // Method computes and stores all the potential paths in dungeon.
  private List<LocationMap> makePotentialPaths(boolean wrapping) {
    List<LocationMap> potentialPaths;
    potentialPaths = new ArrayList<>();
    LocationMap currentMap;
    for (int row = 0; row < this.rows; row++) {
      for (int col = 0; col < this.cols; col++) {
        int eastIndex = col + 1;
        int westIndex = col - 1;
        int northIndex = row - 1;
        if (eastIndex <= this.cols - 1) {
          currentMap = new LocationMap(this.dungeon[row][col], this.dungeon[row][eastIndex]);
          if (!potentialPaths.contains(currentMap)) {
            potentialPaths.add(currentMap);
          }
        }
        if (westIndex >= 0) {
          currentMap = new LocationMap(this.dungeon[row][col], this.dungeon[row][westIndex]);
          if (!potentialPaths.contains(currentMap)) {
            potentialPaths.add(currentMap);
          }
        }
        if (northIndex >= 0) {
          currentMap = new LocationMap(this.dungeon[row][col], this.dungeon[northIndex][col]);
          if (!potentialPaths.contains(currentMap)) {
            potentialPaths.add(currentMap);
          }
        }
        int southIndex = row + 1;
        if (southIndex <= this.rows - 1) {
          currentMap = new LocationMap(this.dungeon[row][col], this.dungeon[southIndex][col]);
          if (!potentialPaths.contains(currentMap)) {
            potentialPaths.add(currentMap);
          }
        }
      }
    }
    // If Wrapping is true, add edge nodes connections as well.
    if (wrapping) {
      // Connecting north and south nodes.
      for (int col = 0; col < this.cols; col++) {
        potentialPaths.add(new LocationMap(this.dungeon[0][col], this.dungeon[this.rows - 1][col]));
      }
      // Connecting east and west nodes.
      for (int row = 0; row < this.rows; row++) {
        potentialPaths.add(new LocationMap(this.dungeon[row][0], this.dungeon[row][this.cols - 1]));
      }
    }
    return potentialPaths;
  }

  @Override
  public int getRowsInBoard() {
    return this.rows;
  }

  @Override
  public int getColsInBoard() {
    return this.cols;
  }

  @Override
  public GeneralLocation[][] getBoard() {
    return Arrays.stream(this.dungeon).map(GeneralLocation[]::clone)
            .toArray(GeneralLocation[][]::new);
  }

  @Override
  public boolean getWrapping() {
    return this.wrapping;
  }

  @Override
  public int getInterConnectivity() {
    return this.interconnectivity;
  }

  @Override
  public int getTreasureArrowPercent() {
    return this.treasureArrowPercent;
  }

  @Override
  public int getNumberOfMonsters() {
    return this.difficulty;
  }

  @Override
  public String getPlayerName() {
    return player.getPlayerName();
  }


  private void checkIfGameOver() {
    GeneralPlayerImpl concretePlayer = (GeneralPlayerImpl) this.player;
    if (isGameOver() && !concretePlayer.getPlayerStatus()) {
      throw new IllegalStateException("Game is over. Player is no more alive.");
    }

    else if (isGameOver() && concretePlayer.getPlayerStatus()) {
      throw new IllegalStateException("Game is over. Player has won.");
    }
  }

  @Override
  public void pickAllArrows() {
    checkIfGameOver();
    GeneralPlayerImpl concretePlayer = (GeneralPlayerImpl) this.player;
    Location location = (Location) this.playerCurrentLocation;
    if (location.getArrowCount() == 0) {
      throw new IllegalArgumentException("Current location don't have arrows");
    }
    concretePlayer.addAllArrows(location.getArrowCount());
    location.removeAllArrows();
  }

  @Override
  public void pickAllTreasures() {
    checkIfGameOver();
    GeneralPlayerImpl concretePlayer = (GeneralPlayerImpl) this.player;
    GeneralLocation location = this.playerCurrentLocation;
    if (location.getTreasuresList().size() == 0) {
      throw new IllegalArgumentException("Current location doesn't have any treasures");
    }
    concretePlayer.addAllTreasures(location.getTreasuresList());
    Location location1 =  (Location)location;
    location1.removeAllTreasures();
  }

  @Override
  public int getPlayerArrowCount() {
    GeneralPlayerImpl concretePlayer = (GeneralPlayerImpl)this.player;
    return concretePlayer.getArrow();
  }

  @Override
  public int getPlayerRubyCount() {
    if (getCumulativeTreasures(this.player.getTreasuresOwned()).get(Treasures.RUBIES) == null) {
      return  0;
    }
    else  {
      return getCumulativeTreasures(this.player.getTreasuresOwned()).get(Treasures.RUBIES);
    }
  }

  @Override
  public int getPlayerDiamondCount() {
    if (getCumulativeTreasures(this.player.getTreasuresOwned()).get(Treasures.DIAMONDS) == null) {
      return  0;
    }
    else  {
      return getCumulativeTreasures(this.player.getTreasuresOwned()).get(Treasures.DIAMONDS);
    }
  }

  @Override
  public int getPlayerEmeraldCount() {
    if (getCumulativeTreasures(this.player.getTreasuresOwned()).get(Treasures.SAPPHIRE) == null) {
      return  0;
    }
    else  {
      return getCumulativeTreasures(this.player.getTreasuresOwned()).get(Treasures.SAPPHIRE);
    }
  }

  @Override
  public int getCurrentLocationArrowCount() {
    GeneralLocation playerCurrentLocation = this.playerCurrentLocation;
    Location location = (Location) playerCurrentLocation;
    if (location.getArrowCount() > 0) {
      return location.getArrowCount();
    }
    return 0;
  }

  @Override
  public int getCurrentLocationRubyCount() {
    if (getCumulativeTreasures(playerCurrentLocation.getTreasuresList())
                .get(Treasures.RUBIES) == null) {
      return  0;
    }
    else  {
      return getCumulativeTreasures(playerCurrentLocation.getTreasuresList())
              .get(Treasures.RUBIES);
    }
  }

  @Override
  public int getCurrentLocationDiamondCount() {
    if (getCumulativeTreasures(playerCurrentLocation.getTreasuresList())
                .get(Treasures.DIAMONDS) == null) {
      return  0;
    }
    else  {
      return getCumulativeTreasures(playerCurrentLocation.getTreasuresList())
              .get(Treasures.DIAMONDS);
    }
  }

  @Override
  public int getCurrentLocationEmeraldCount() {
    if (getCumulativeTreasures(playerCurrentLocation.getTreasuresList())
                .get(Treasures.SAPPHIRE) == null) {
      return  0;
    }
    else  {
      return getCumulativeTreasures(playerCurrentLocation.getTreasuresList())
              .get(Treasures.SAPPHIRE);
    }
  }

}
