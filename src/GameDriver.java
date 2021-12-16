import controller.DungeonConsoleController;
import controller.DungeonConsoleControllerImpl;
import controller.DungeonGuiController;
import controller.DungeonGuiControllerImpl;
import dungeonmodel.DungeonGame;
import dungeonmodel.DungeonGameImpl;
import dungeonmodel.GeneralPlayer;
import dungeonmodel.GeneralPlayerImpl;
import dungeonmodel.NumberGenerationInterface;
import dungeonmodel.RandomGeneration;
import view.GameView;
import view.IGameView;

import java.io.InputStreamReader;

/**
 * Driver class that enables users to play game in both console and view based interfaces. If
 * arguments were given while execution, console based mode is picked and if no arguments were
 * given Graphical User interface is picked.
 */
public class GameDriver {
  /**
   * Main function where control starts execution.
   *
   * @param args Arguments for main function. It includes: Player Name, number of rows, columns,
   *             interconnectivity, wrapping, treasureArrow percentage and
   *             difficulty(or Number of Monsters) in that order from the user to construct dungeon.
   */
  public static void main(String[] args) {
    NumberGenerationInterface random = new RandomGeneration();
    GeneralPlayer player;
    if (args.length == 0) {
      IGameView view = new GameView("Adventure");
      DungeonGuiController controller = new DungeonGuiControllerImpl(view);
      controller.playGame();
    } else if (args.length == 7) {
      String playerName = args[0];
      int rows = Integer.parseInt(args[1]);
      int cols = Integer.parseInt(args[2]);
      int interconnectivity = Integer.parseInt(args[3]);
      boolean wrapping = Boolean.parseBoolean(args[4]);
      int treasureArrowPercent = Integer.parseInt(args[5]);
      int difficulty = Integer.parseInt(args[6]);
      player = new GeneralPlayerImpl(playerName);
      NumberGenerationInterface randomGeneration = new RandomGeneration();
      Appendable out = System.out;
      Readable input = new InputStreamReader(System.in);
      DungeonGame game = new DungeonGameImpl(rows, cols, interconnectivity, wrapping,
              treasureArrowPercent, difficulty, player, randomGeneration);
      DungeonConsoleController controller = new DungeonConsoleControllerImpl(input, out);
      controller.playGame(game);
    } else {
      System.out.println("Invalid number of arguments");
      System.exit(0);
    }
  }
}

