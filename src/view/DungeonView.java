package view;

import controller.DungeonGuiController;
import dungeonmodel.Directions;
import dungeonmodel.GeneralLocation;
import dungeonmodel.PitsThievesDungeon;
import dungeonmodel.PitsThievesReadOnly;
import dungeonmodel.Smell;
import dungeonmodel.Treasures;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;



class DungeonView extends JPanel {
  private final PitsThievesReadOnly model;
  private final DungeonGuiController controller;


  public DungeonView(PitsThievesDungeon model, DungeonGuiController controller) {
    this.model = model;
    this.controller = controller;
    GridBagLayout layout = new GridBagLayout();
    this.setLayout(layout);
    renderDungeon();
    this.setFocusable(true);
    this.requestFocusInWindow();
    setVisible(true);
  }

  private void renderDungeon() {
    String monsterImagePath = new StringBuilder("images/otyugh.png").toString();
    String playerImagePath = new StringBuilder("images/player.png").toString();
    String rubyImagePath = new StringBuilder("images/ruby.png").toString();
    String emeraldImagePath = new StringBuilder("images/emerald.png").toString();
    String diamondImagePath = new StringBuilder("images/diamond.png").toString();
    String blackImagePath = new StringBuilder("images/black.png").toString();
    String lessPungentPath = new StringBuilder("images/stench01.png").toString();
    String morePungentPath = new StringBuilder("images/stench02.png").toString();
    String pitPath = new StringBuilder("images/pit1.png").toString();
    String thiefImagePath = new StringBuilder("images/thief.png").toString();
    String arrowImagePath;
    GridBagConstraints constraints = new GridBagConstraints();

    for (int i = 0; i < model.getRowsInBoard(); i++) {
      for (int j = 0; j < model.getColsInBoard(); j++) {
        GeneralLocation currLocation = model.getBoard()[i][j];
        Image newimg = null;
        BufferedImage directionImage = null;
        StringBuilder relativePath = new StringBuilder("images/");
        StringBuilder fileName = new StringBuilder();
        try {
          if (currLocation.getVisited()) {
            Map<Directions, GeneralLocation> possibleDirs = currLocation.getPossibleDirectionMoves(
                    model.getRowsInBoard(), model.getColsInBoard());
            List<Directions> list = new ArrayList<>(possibleDirs.keySet());

            for (Directions dir : list) {
              fileName.append(dir.toString().charAt(0));
            }
            char[] chars = fileName.toString().toCharArray();
            Arrays.sort(chars);
            String sorted = new String(chars);
            StringBuilder finalFileName = new StringBuilder(sorted).append(".png");
            relativePath.append(finalFileName);
            directionImage = ImageIO.read(ClassLoader.getSystemResource(relativePath.toString()));
            newimg = directionImage.getScaledInstance(directionImage.getWidth(),
                    directionImage.getHeight(),
                    java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

            Smell smell = this.model.getSmellInLocation(currLocation);

            if (this.model.hasPit(currLocation)) {
              newimg = overlay(toBufferedImage(newimg), pitPath, 45, 8);
            }

            if (this.model.hasThief(currLocation)) {
              newimg = overlay(toBufferedImage(newimg), thiefImagePath, 45, 8);
            }

            if (smell != null && smell.equals(Smell.MOREPUNGENT)) {
              newimg = overlay(toBufferedImage(newimg), morePungentPath, 15, 15);
            }
            if (smell != null && smell.equals(Smell.LESSPUNGENT)) {
              newimg = overlay(toBufferedImage(newimg), lessPungentPath, 15, 15);
            }

            if (currLocation.getArrowCount() > 0) {
              if (currLocation.isCave()) {
                arrowImagePath = new StringBuilder("images/arrow-black.png").toString();
                newimg = overlay(toBufferedImage(newimg), arrowImagePath, 25, 8);
              } else {
                arrowImagePath = new StringBuilder("images/arrow-white.png").toString();
                newimg = overlay(toBufferedImage(newimg), arrowImagePath, 40, 35);
              }
            }

            List<Treasures> treasuresList = currLocation.getTreasuresList();
            int x = 30;
            if (treasuresList.contains(Treasures.RUBIES)) {
              newimg = overlay(toBufferedImage(newimg), rubyImagePath, x, x);
              x = x + 10;
            }
            if (treasuresList.contains(Treasures.DIAMONDS)) {
              newimg = overlay(toBufferedImage(newimg), diamondImagePath, x, x);
              x = x + 10;
            }
            if (treasuresList.contains(Treasures.SAPPHIRE)) {
              newimg = overlay(toBufferedImage(newimg), emeraldImagePath, x, x);
            }

            if (currLocation.hasMonster()) {
              newimg = overlay(toBufferedImage(newimg), monsterImagePath, 15, 15);
            }

            if (currLocation.equals(model.getPlayerCurrentLocation())) {
              newimg = overlay(toBufferedImage(newimg), playerImagePath, 15, 15);
            }

          } else {
            directionImage = ImageIO.read(ClassLoader.getSystemResource(blackImagePath));
            newimg = directionImage.getScaledInstance(directionImage.getWidth() * 2,
                    directionImage.getHeight() * 2,
                    java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
          }
        } catch (IOException ignored) {

        }

        newimg = newimg.getScaledInstance(directionImage.getWidth() * 2,
                directionImage.getHeight() * 2,
                java.awt.Image.SCALE_SMOOTH); // scale it the smooth way

        JPanel blockPanel = new JPanel();
        blockPanel.setName(String.valueOf(Integer.parseInt((i + 1) + "" + (j + 1))));
        constraints.gridx = j;
        constraints.gridy = i;
        constraints.insets = new Insets(-3, -3, -3, -3);
        JLabel label = new JLabel(new ImageIcon(newimg));
        blockPanel.add(label);
        blockPanel.setSize(directionImage.getWidth() * 4, directionImage.getHeight() * 4);
        add(blockPanel, constraints);

        blockPanel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            int row = Integer.parseInt(String.valueOf(blockPanel.getName().charAt(0)));
            int col = Integer.parseInt(String.valueOf(blockPanel.getName().charAt(1)));
            int playerRow = model.getPlayerCurrentLocation().getLocation().getX() + 1;
            int playerCol = model.getPlayerCurrentLocation().getLocation().getY() + 1;
            if (row == playerRow - 1 && col == playerCol) {
              controller.move(Directions.NORTH);
            }
            if (row == playerRow + 1 && col == playerCol) {
              controller.move(Directions.SOUTH);
            }
            if (row == playerRow && col == playerCol - 1) {
              controller.move(Directions.WEST);
            }
            if (row == playerRow && col == playerCol + 1) {
              controller.move(Directions.EAST);
            }
          }
        });
      }
    }
  }

  private BufferedImage overlay(BufferedImage starting, String fpath, int offset1, int offset2)
          throws IOException {
    BufferedImage overlay = ImageIO.read(ClassLoader.getSystemResource(fpath));
    Image temp = overlay.getScaledInstance(overlay.getWidth() / 2,
            overlay.getHeight() / 2,
            Image.SCALE_SMOOTH); // scale it the smooth way

    int w = Math.max(starting.getWidth(), overlay.getWidth());
    int h = Math.max(starting.getHeight(), overlay.getHeight());
    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics g = combined.getGraphics();
    g.drawImage(starting, 0, 0, null);
    g.drawImage(temp, offset1, offset2, null);
    return combined;
  }


  // Reference: https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage
  private BufferedImage toBufferedImage(Image img) {
    if (img instanceof BufferedImage) {
      return (BufferedImage) img;
    }
    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null),
            img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
  }

}



