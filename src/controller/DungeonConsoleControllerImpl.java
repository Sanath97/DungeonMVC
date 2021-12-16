package controller;

import dungeonmodel.Directions;
import dungeonmodel.DungeonGame;
import dungeonmodel.MonsterStatus;
import dungeonmodel.Treasures;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class represents a controller for the dungeon model. End user can
 * access this class to play the game in the console text format. It reads
 * input from Readable object and writes output to an Appendable object.
 */
public class DungeonConsoleControllerImpl implements DungeonConsoleController {
  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructs object for DungeonConsoleController class.
   * @param in a Readable object to read input from.
   * @param out an Appendable object to write output to.
   */
  public DungeonConsoleControllerImpl(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  private Directions getEquivalentDirection(String s) {
    Map<String, Directions> directionsMap = new HashMap<>();
    directionsMap.put("north", Directions.NORTH);
    directionsMap.put("south", Directions.SOUTH);
    directionsMap.put("east", Directions.EAST);
    directionsMap.put("west", Directions.WEST);
    return directionsMap.get(s.toLowerCase());
  }

  private String getPickUpItem(String input) {
    if (input.equalsIgnoreCase("arrow")) {
      return input.toLowerCase();
    }
    else if (input.equalsIgnoreCase("ruby") || input.equalsIgnoreCase("sapphire")
            || input.equalsIgnoreCase("diamond")) {
      return "treasure";
    }
    return null;
  }

  private Treasures getEquivalentTreasure(String input) {
    Map<String, Treasures> treasuresMap = new HashMap<>();
    treasuresMap.put("ruby", Treasures.RUBIES);
    treasuresMap.put("diamond", Treasures.DIAMONDS);
    treasuresMap.put("sapphire", Treasures.SAPPHIRE);
    return treasuresMap.get(input.toLowerCase());
  }


  @Override
  public void playGame(DungeonGame game) {
    if (game == null) {
      throw new IllegalArgumentException("Game object cannot be null");
    }
    try {
      out.append("Welcome to the Dungeon game. All the best.\n");
      while (true) {
        if (game.isGameOver()) {
          if (game.isPlayerAlive()) {
            out.append("Yay! Player has won! Successfully reached end");
          }
          else {
            out.append("Chomp chomp chomp, player is eaten by monster. Try Again.");
          }
          break;
        }
        out.append(game.getPlayerCurrLocationInfo());
        out.append("Move, Pickup, or Shoot (M-P-S)/(m-p-s)? \n");
        String input = scan.next();
        if (input.equalsIgnoreCase("q")) {
          out.append("Quitting the game!");
          break;
        }
        if (input.equalsIgnoreCase("m")) {
          out.append("Where to?");
          out.append("\n");
          String direction = scan.next();
          Directions dir = getEquivalentDirection(direction);
          if (dir == null) {
            out.append("Give a valid direction.\n");
            continue;
          }
          try {
            game.movePlayer(dir);
          }
          catch (IllegalArgumentException exception) {
            out.append(exception.getMessage());
            out.append("\n");
          }
        }
        else if (input.equalsIgnoreCase("p")) {
          out.append("What?");
          out.append("\n");
          String type = scan.next();
          if (getPickUpItem(type) == null) {
            out.append("Give a valid input to pick. Valid Inputs: [arrow, diamond, ruby,"
                    + " sapphire]\n");
            continue;
          }
          if (getPickUpItem(type).equals("arrow")) {
            try {
              game.pickArrow();
            } catch (IllegalArgumentException exception) {
              out.append(exception.getMessage());
              out.append("\n");
              continue;
            }
          }
          else {
            try {
              game.pickTreasure(getEquivalentTreasure(type));
            }
            catch (IllegalArgumentException exception) {
              out.append(exception.getMessage());
              out.append("\n");
              continue;
            }
          }
          out.append(game.getPlayerInfo());
        }
        else if (input.equalsIgnoreCase("s")) {
          out.append("Number of Caves?(1-5)");
          out.append("\n");
          String numOfCaves = scan.next();
          int numberOfCaves = 0;
          try {
            numberOfCaves = Integer.parseInt(numOfCaves);
          } catch (NumberFormatException exception) {
            out.append("Give a valid number.");
            out.append("\n");
            continue;
          }
          out.append("Where to?\n");
          String direction = scan.next();
          Directions dir = getEquivalentDirection(direction);
          if (dir == null) {
            out.append("Give a valid direction.\n");
            continue;
          }
          try {
            MonsterStatus result = game.shootArrow(numberOfCaves,
                    getEquivalentDirection(direction));
            if (result.equals(MonsterStatus.DEAD)) {
              out.append("You killed a Monster.");
            }
            else if (result.equals(MonsterStatus.INJURED)) {
              out.append("You injured a Monster.");
            }
            else {
              out.append("You shot into darkness");
            }
            out.append("\n");
          } catch (IllegalStateException | IllegalArgumentException exception) {
            out.append(exception.getMessage());
            out.append("\n");
          }
        }
        else {
          out.append("Give a valid command. Valid commands: (M-P-S)/(m-p-s) \n");
        }
      }
    }
    catch (IOException exception) {
      throw new IllegalStateException("Append failed" + exception);
    }
  }
}
