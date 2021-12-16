package view;

import controller.DungeonGuiController;
import dungeonmodel.Directions;
import dungeonmodel.PitsThievesDungeon;
import dungeonmodel.PitsThievesReadOnly;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.border.LineBorder;

/**
 * This class represents view for the dungeon game. It reads inputs from the user and outputs
 * responses to the user.
 */
public class GameView extends JFrame implements IGameView {

  private final JPanel mainPanel;
  private SetupView setupView;
  private PitsThievesReadOnly model;
  private final JMenuBar menuBar;
  private final JMenuItem rows;
  private final JMenuItem cols;
  private final JMenuItem wrapping;
  private final JMenuItem interconnectivity;
  private final JMenuItem treasureArrow;
  private final JMenuItem numOfMonsters;
  private final JMenuItem numOfPits;
  private final JMenuItem numOfThieves;
  private final JMenuItem restart;
  private final JMenuItem reuseGame;
  private final JMenuItem quit;
  private JPanel playerDescriptionPanel;
  private JPanel locationDescriptionPanel;
  private JPanel gameViewPanel;
  private JPanel descriptionPanel;
  private final JPanel gameStatusPanel;
  private boolean isShootAction;
  private final JLabel statusLabel;


  /**
   * Constructs the game view object.
   * @param caption Title to create the JFrame.
   */
  public GameView(String caption) {
    super(caption);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    setupView = new SetupView();
    add(setupView);
    setupView.setVisible(true);

    // create a menubar
    menuBar = new JMenuBar();

    // create menu items
    rows = new JMenuItem("");
    cols = new JMenuItem("");
    wrapping = new JMenuItem("");
    interconnectivity = new JMenuItem("");
    treasureArrow = new JMenuItem("");
    numOfMonsters = new JMenuItem("");
    numOfPits = new JMenuItem("");
    numOfThieves = new JMenuItem("");
    // create configuration menu
    JMenu configurations = new JMenu("Game Configuration");
    configurations.add(rows);
    configurations.add(cols);
    configurations.add(wrapping);
    configurations.add(interconnectivity);
    configurations.add(treasureArrow);
    configurations.add(numOfMonsters);
    configurations.add(numOfPits);
    configurations.add(numOfThieves);
    // add menu items to menu
    menuBar.add(configurations);

    restart = new JMenuItem("New Game");
    reuseGame = new JMenuItem("Restart");
    quit = new JMenuItem("Quit");
    // create options menu
    JMenu options = new JMenu("Options");
    options.add(restart);
    options.add(reuseGame);
    options.add(quit);
    menuBar.add(options);
    this.setJMenuBar(menuBar);
    menuBar.setVisible(false);

    playerDescriptionPanel = new JPanel();
    locationDescriptionPanel = new JPanel();
    gameViewPanel = new JPanel();
    gameStatusPanel = new JPanel();
    descriptionPanel = new JPanel();
    statusLabel = new JLabel();

    mainPanel = new JPanel();
    mainPanel.setBorder(LineBorder.createBlackLineBorder());
    gameViewPanel.setVisible(false);
    isShootAction = false;
    pack();
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }


  @Override
  public void switchToSetupMode(DungeonGuiController controller) {
    gameViewPanel.setVisible(false);
    this.menuBar.setVisible(false);
    this.setupView = new SetupView();
    this.setupView.setFeatures(controller);
    add(this.setupView);
  }


  @Override
  public void showErrorMessageForModelCreation(String message) {
    this.setupView.showErrorMessage(message);
  }

  private Image readImage(String imagePath) {
    Image image = null;
    try {
      BufferedImage bufferedImage = ImageIO.read(ClassLoader.getSystemResource(imagePath));
      image = bufferedImage.getScaledInstance(bufferedImage.getWidth(),
              bufferedImage.getHeight(),
              java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
    }
    catch (Exception ignored) {

    }
    return image;
  }

  private void setPlayerDescriptionDetails(int[] playerPossessions, int[] locationInfo,
                                          String status, String pitsDirections) {

    if (locationDescriptionPanel != null) {
      remove(locationDescriptionPanel);
    }
    locationDescriptionPanel = new JPanel();
    locationDescriptionPanel.setLayout(new GridBagLayout());
    GridBagConstraints locationConstraints = new GridBagConstraints();
    locationConstraints.gridx = 0;
    locationConstraints.gridy = 0;
    JLabel labelHead = new JLabel("Location description");
    labelHead.setFont(new Font("Sans-Serif", Font.PLAIN, 30));
    locationDescriptionPanel.removeAll();

    String rubyImagePath = new StringBuilder("images/ruby.png").toString();
    String emeraldImagePath = new StringBuilder("images/emerald.png").toString();
    String diamondImagePath = new StringBuilder("images/diamond.png").toString();
    String arrowImagePath = new StringBuilder("images/arrow-black.png").toString();
    Image ruby = readImage(rubyImagePath);
    Image emerald = readImage(emeraldImagePath);
    Image diamond = readImage(diamondImagePath);
    Image arrow = readImage(arrowImagePath);
    setLocPlayerDetails(locationInfo, ruby, emerald, diamond, arrow, locationConstraints,
            labelHead, locationDescriptionPanel);
    locationConstraints.gridx = 0;
    locationConstraints.gridy = 5;
    String pitPath = new StringBuilder("images/pit1.png").toString();
    Image pit = readImage(pitPath);
    locationDescriptionPanel.add(new JLabel(new ImageIcon(pit)), locationConstraints);
    locationConstraints.gridx = 1;
    locationConstraints.gridy = 5;
    locationDescriptionPanel.add(new JLabel((pitsDirections)), locationConstraints);
    add(locationDescriptionPanel);

    if (playerDescriptionPanel != null) {
      remove(playerDescriptionPanel);
    }
    playerDescriptionPanel = new JPanel();
    playerDescriptionPanel.setLayout(new GridBagLayout());
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = 0;
    constraints.gridy = 0;
    JLabel playerLabelHead = new JLabel(new StringBuilder("Welcome ").append(model.getPlayerName())
            .append("! You own").toString());
    playerLabelHead.setFont(new Font("Sans-Serif", Font.PLAIN, 30));
    setLocPlayerDetails(playerPossessions, ruby, emerald, diamond, arrow, constraints,
            playerLabelHead, playerDescriptionPanel);
    add(playerDescriptionPanel);

    statusLabel.setText("");
    statusLabel.setText(status);
    statusLabel.setFont(new Font("Sans-Serif", Font.PLAIN, 18));
    gameStatusPanel.add(statusLabel);

    if (descriptionPanel != null) {
      remove(descriptionPanel);
    }
    descriptionPanel = new JPanel();
    BoxLayout boxLayout = new BoxLayout(descriptionPanel, BoxLayout.Y_AXIS);
    descriptionPanel.setLayout(boxLayout);
    descriptionPanel.add(playerDescriptionPanel);
    descriptionPanel.add(locationDescriptionPanel);
    descriptionPanel.add(gameStatusPanel);
    add(descriptionPanel);
  }

  private void setLocPlayerDetails(int[] displayInfo, Image ruby, Image emerald, Image diamond,
                                   Image arrow, GridBagConstraints constraints, JLabel labelHead,
                                   JPanel descriptionPanel) {
    descriptionPanel.add(labelHead, constraints);
    constraints.gridx = 0;
    constraints.gridy = 1;
    descriptionPanel.add(new JLabel(new ImageIcon(ruby)), constraints);
    constraints.gridx = 1;
    constraints.gridy = 1;
    descriptionPanel.add(new JLabel(String.valueOf(displayInfo[0])), constraints);
    constraints.gridx = 0;
    constraints.gridy = 2;
    descriptionPanel.add(new JLabel(new ImageIcon(emerald)), constraints);
    constraints.gridx = 1;
    constraints.gridy = 2;
    descriptionPanel.add(new JLabel(String.valueOf(displayInfo[1])), constraints);
    constraints.gridx = 0;
    constraints.gridy = 3;
    descriptionPanel.add(new JLabel(new ImageIcon(diamond)), constraints);
    constraints.gridx = 1;
    constraints.gridy = 3;
    descriptionPanel.add(new JLabel(String.valueOf(displayInfo[2])), constraints);
    constraints.gridx = 0;
    constraints.gridy = 4;
    descriptionPanel.add(new JLabel(new ImageIcon(arrow)), constraints);
    constraints.gridx = 1;
    constraints.gridy = 4;
    descriptionPanel.add(new JLabel(String.valueOf(displayInfo[3])), constraints);
  }

  @Override
  public void showResultDialogue(boolean winStatus, int deathReason) {
    if (winStatus) {
      JOptionPane.showMessageDialog(this, "You reached end and won!",
              "Congratulations", JOptionPane.INFORMATION_MESSAGE, null);
    }
    else  {
      StringBuilder message = new StringBuilder();
      if (deathReason == 1) {
        message.append("You were eaten by Monster. ");
      }
      else if (deathReason == 2) {
        message.append("You fell into pit and dead. ");
      }
      message.append("Check options to retry.");
      JOptionPane.showMessageDialog(this, message.toString(),
              "Uh-oh", JOptionPane.INFORMATION_MESSAGE, null);
    }
  }


  @Override
  public void switchToGameMode(PitsThievesDungeon model, DungeonGuiController controller,
                               int[] playerPossessions, int[] locationInfo, String status,
                               String pitsDirection) {

    this.setupView.setVisible(false);
    this.model = model;

    mainPanel.removeAll();
    gameStatusPanel.removeAll();
    playerDescriptionPanel.removeAll();
    locationDescriptionPanel.removeAll();

    if (this.gameViewPanel != null) {
      remove(this.gameViewPanel);
    }

    this.gameViewPanel = new JPanel();
    DungeonView dungeonView = new DungeonView(model, controller);
    this.menuBar.setVisible(true);
    mainPanel.add(dungeonView);
    JScrollPane scrollPane = new JScrollPane(mainPanel);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(5);
    scrollPane.getVerticalScrollBar().setUnitIncrement(5);
    this.getContentPane().add(scrollPane);

    setPlayerDescriptionDetails(playerPossessions, locationInfo, status, pitsDirection);

    gameViewPanel.setLayout(new BorderLayout());
    gameViewPanel.add(scrollPane, BorderLayout.CENTER);
    gameViewPanel.add(descriptionPanel, BorderLayout.WEST);

    this.rows.setText(new StringBuilder("Rows ").append(model.getRowsInBoard()).toString());
    this.cols.setText(new StringBuilder("Columns ").append(model.getColsInBoard()).toString());
    this.wrapping.setText(new StringBuilder("Wrapping ").append(model.getWrapping()).toString());
    this.interconnectivity.setText(new StringBuilder("InterConnectivity ")
            .append(model.getInterConnectivity()).toString());
    this.treasureArrow.setText(new StringBuilder("Treasure/Arrow Percent ")
            .append(model.getTreasureArrowPercent()).toString());
    this.numOfMonsters.setText(new StringBuilder("Number of Monsters ")
            .append(model.getNumberOfMonsters()).toString());
    this.numOfPits.setText(new StringBuilder("Number of Pits ")
            .append(model.getNumberOfMonsters()).toString());
    this.numOfThieves.setText(new StringBuilder("Number of Thieves ")
            .append(model.getNumberOfMonsters()).toString());
    gameViewPanel.setVisible(true);
    add(gameViewPanel);
    this.setFocusable(true);
    this.requestFocus();
    revalidate();
  }

  @Override
  public void setSetupFeatures(DungeonGuiController dungeonGuiController) {
    setupView.setFeatures(dungeonGuiController);
  }

  @Override
  public void quit() {
    System.exit(0);
  }

  private int readShootDistance() {
    String result = (String)JOptionPane.showInputDialog(
            mainPanel,
            "Choose distance to shoot",
            "Shooting Distance",
            JOptionPane.PLAIN_MESSAGE,
            null,
            null,
            "1"
    );
    int res = 0;
    try {
      res = Integer.parseInt(result);
    }
    catch (NumberFormatException exception) {
      showErrorMessage("Give a valid number.");
    }
    return res;
  }

  @Override
  public void setGameFeatures(DungeonGuiController dungeonGuiController) {
    restart.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dungeonGuiController.newGame();
      }
    });

    reuseGame.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dungeonGuiController.reuseGame();
      }
    });

    quit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dungeonGuiController.quit();
      }
    });

    mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "UP");
    mainPanel.getActionMap().put("UP", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!isShootAction) {
          dungeonGuiController.move(Directions.NORTH);
        }
        else {
          dungeonGuiController.shootArrow(Directions.NORTH, readShootDistance());
          isShootAction = false;
        }
      }
    });

    mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "DOWN");
    mainPanel.getActionMap().put("DOWN", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!isShootAction) {
          dungeonGuiController.move(Directions.SOUTH);
        }
        else {
          dungeonGuiController.shootArrow(Directions.SOUTH, readShootDistance());
          isShootAction = false;
        }
      }
    });

    mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "LEFT");
    mainPanel.getActionMap().put("LEFT", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!isShootAction) {
          dungeonGuiController.move(Directions.WEST);
        }
        else {
          dungeonGuiController.shootArrow(Directions.WEST, readShootDistance());
          isShootAction = false;
        }
      }
    });

    mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "RIGHT");
    mainPanel.getActionMap().put("RIGHT", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!isShootAction) {
          dungeonGuiController.move(Directions.EAST);
        }
        else {
          dungeonGuiController.shootArrow(Directions.EAST, readShootDistance());
          isShootAction = false;
        }
      }
    });

    mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0, false), "T");
    mainPanel.getActionMap().put("T", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dungeonGuiController.pickAllTreasure();
      }
    });

    mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "A");
    mainPanel.getActionMap().put("A", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dungeonGuiController.pickAllArrow();
      }
    });

    mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "S");
    mainPanel.getActionMap().put("S", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        isShootAction = true;
      }
    });
  }


  private void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(this, message,
            "Message", JOptionPane.INFORMATION_MESSAGE, null);
  }
}
