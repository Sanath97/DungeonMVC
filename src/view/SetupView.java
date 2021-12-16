package view;

import controller.DungeonGuiController;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

class SetupView extends Panel {
  private final JPanel mainPanel;
  private final JLabel playerNameLabel;
  private final JTextField playerNameField;
  private final JLabel rowsLabel;
  private final JTextField rowsInputField;
  private final JLabel colsLabel;
  private final JTextField colsInputField;
  private final JLabel interconnectivityLabel;
  private final JTextField interconnectivityField;
  private final JLabel wrappingLabel;
  private final ButtonGroup wrappingGroup;
  private final JRadioButton trueWrapping;
  private final JLabel treasureArrowLabel;
  private final JTextField treasureArrowField;
  private final JLabel monstersLabel;
  private final JTextField monstersField;
  private final JButton playGame;

  public SetupView() {
    mainPanel = new JPanel(new GridBagLayout());
    playerNameLabel = new JLabel("Player Name ");
    playerNameField = new JTextField();
    playerNameField.setPreferredSize(new Dimension(150, 20));

    rowsLabel = new JLabel("Number of Rows ");
    rowsInputField = new JTextField();
    rowsInputField.setText("8");
    rowsInputField.setPreferredSize(new Dimension(150, 20));

    colsLabel = new JLabel("Number of Columns ");
    colsInputField = new JTextField();
    colsInputField.setText("8");
    colsInputField.setPreferredSize(new Dimension(150, 20));

    interconnectivityLabel = new JLabel("Interconnectivity of maze ");
    interconnectivityField = new JTextField();
    interconnectivityField.setText("3");
    interconnectivityField.setPreferredSize(new Dimension(150, 20));

    wrappingLabel = new JLabel("Wrapping ");
    wrappingGroup = new ButtonGroup();
    trueWrapping = new JRadioButton("Yes");
    JRadioButton falseWrapping = new JRadioButton("No");
    falseWrapping.setSelected(true);
    wrappingGroup.add(trueWrapping);
    wrappingGroup.add(falseWrapping);

    treasureArrowLabel = new JLabel("Treasures/Arrows Percentage ");
    treasureArrowField = new JTextField();
    treasureArrowField.setText("88");
    treasureArrowField.setPreferredSize(new Dimension(150, 20));

    monstersLabel = new JLabel("Number of Monsters ");
    monstersField = new JTextField();
    monstersField.setText("3");
    monstersField.setPreferredSize(new Dimension(150, 20));

    playGame = new JButton("Play Game");

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridy = 0;
    constraints.gridx = 0;
    constraints.insets = new Insets(5,5,5,5);
    mainPanel.add(playerNameLabel, constraints);
    constraints.gridy = 0;
    constraints.gridx = 1;
    mainPanel.add(playerNameField, constraints);

    constraints.gridy = 1;
    constraints.gridx = 0;
    mainPanel.add(rowsLabel, constraints);
    constraints.gridy = 1;
    constraints.gridx = 1;
    mainPanel.add(rowsInputField, constraints);

    constraints.gridy = 2;
    constraints.gridx = 0;
    mainPanel.add(colsLabel, constraints);
    constraints.gridy = 2;
    constraints.gridx = 1;
    mainPanel.add(colsInputField, constraints);

    constraints.gridy = 3;
    constraints.gridx = 0;
    mainPanel.add(interconnectivityLabel, constraints);
    constraints.gridy = 3;
    constraints.gridx = 1;
    mainPanel.add(interconnectivityField, constraints);

    constraints.gridy = 4;
    constraints.gridx = 0;
    mainPanel.add(wrappingLabel, constraints);
    constraints.gridy = 4;
    constraints.gridx = 1;
    mainPanel.add(trueWrapping, constraints);

    constraints.gridy = 5;
    constraints.gridx = 1;
    mainPanel.add(falseWrapping, constraints);

    constraints.gridy = 6;
    constraints.gridx = 0;
    mainPanel.add(treasureArrowLabel, constraints);
    constraints.gridy = 6;
    constraints.gridx = 1;
    mainPanel.add(treasureArrowField, constraints);

    constraints.gridy = 7;
    constraints.gridx = 0;
    mainPanel.add(monstersLabel, constraints);
    constraints.gridy = 7;
    constraints.gridx = 1;
    mainPanel.add(monstersField, constraints);

    constraints.gridy = 9;
    constraints.gridx = 1;
    mainPanel.add(playGame, constraints);
    add(mainPanel);
    setVisible(true);
  }


  private int convertToInt(JTextField inputField) {
    return Integer.parseInt(inputField.getText());
  }

  private boolean validateIntegerInput(JTextField inputField, JLabel inputLabel) {
    StringBuilder errorMessage = new StringBuilder("Enter valid value for ");
    try {
      Integer.parseInt(inputField.getText());
    }
    catch (NumberFormatException exception) {
      JOptionPane.showMessageDialog(mainPanel,
              errorMessage.append(inputLabel.getText()).toString());
      return false;
    }
    return true;
  }

  public void setFeatures(DungeonGuiController dungeonGuiController) {
    playGame.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String playerName;
        StringBuilder errorMessage = new StringBuilder("Enter valid value for ");
        if (playerNameField.getText().length() == 0) {
          JOptionPane.showMessageDialog(mainPanel,
                  errorMessage.append(playerNameLabel.getText()).toString());
          return;
        }
        playerName = playerNameField.getText();

        int rows;
        if (!validateIntegerInput(rowsInputField, rowsLabel)) {
          return;
        }
        rows = convertToInt(rowsInputField);

        int cols;
        if (!validateIntegerInput(colsInputField, colsLabel)) {
          return;
        }
        cols = convertToInt(colsInputField);

        int interconnectivity;
        if (!validateIntegerInput(interconnectivityField, interconnectivityLabel)) {
          return;
        }
        interconnectivity = convertToInt(interconnectivityField);

        if (wrappingGroup.getSelection() == null) {
          JOptionPane.showMessageDialog(mainPanel,
                  errorMessage.append(wrappingLabel.getText()).toString());
          return;
        }
        boolean wrapping;
        wrapping = trueWrapping.isSelected();

        int treasureArrowPercent;
        if (!validateIntegerInput(treasureArrowField, treasureArrowLabel)) {
          return;
        }
        treasureArrowPercent = convertToInt(treasureArrowField);

        int numOfMonster;
        if (!validateIntegerInput(monstersField, monstersLabel)) {
          return;
        }
        numOfMonster = convertToInt(monstersField);


        dungeonGuiController.createModel(rows, cols, wrapping, interconnectivity,
                treasureArrowPercent, numOfMonster, playerName);
      }
    });
  }

  public void showErrorMessage(String message) {
    JOptionPane.showMessageDialog(mainPanel, message);
    return;
  }

}
