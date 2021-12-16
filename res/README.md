# Overview
Dungeon is a simulation of real life maze games. This game creates a maze to 
traverse for a player and asks the player to move from a start point in the 
maze to end point. The maze consists of caves and tunnels, tunnels are used 
as passage from one cave to another. Caves and Tunnels includes arrows. Caves 
also host a few treasures. Through the course of traversal 
there will be a few monsters in the caves which can kill the player if he 
enters a cave that has them. Player can use arrows possessed to shoot and 
kill the Monsters before entering cave they are in. They can detect Monster
presence by smell. A player is won the game if he kills all monsters that come 
in the way and successfully reaches end location. If player is eaten by a 
Monster in the mean path, player is lost.

### Extra Credit details:
Along with arrows and treasures dungeon also includes thieves and pits. The 
number of Pits and Thieves are equal to the number of Monsters in the dungeon.
If a player is in a location that has pit, then player's survival has a 50% chance.
That is, if players' location has pit then user may live or die by falling in pit
with 50% chance. If a player is in a location that has a thief, then all his
treasures are stolen.


## Features
- Reads all the dungeon parameters from the Graphical User Interface and 
creates the specified dungeon.
- Supports creation of both wrapping and non-wrapping dungeons.
- Allocates any of three types of treasures: Diamond, Ruby, Sapphires to given percentage of caves in the dungeon.
- Allocates specified number of Monsters to the dungeon.
- Randomly selects start and end locations to traverse for the user.
- Allows player to pick up treasures in the cave while traversing through it.
- Allows player to pick up arrows in the cave while traversing through it.
- Supports movement of the player from one location to another.
- Details the player collected treasures and arrows information if player picks any of the item.
- Details the information of location where player is in.
- Enables player to shoot an arrow in the specified direction to a specified distance.
- Suppport extra credit features: Adding pits and thieves to dungeon.


## How To Run
This program is run using DungeonMVC.jar. This jar enables users to run both
the graphical and console based versions. While running the jar if there are no
arguments given then graphical interface version of game is picked up. If any
of arguments are provided then console based version is ran.

Console based version reads inputs: Player Name, number of rows, columns, interconnectivity, 
wrapping, treasureArrow percentage and difficulty(or Number of Monsters) in 
that order from the user to construct dungeon. Once the dungeon is created
user can follow the prompt and play the game until it ends or user quits.
Go to directory where jar file is present and run below command. 

Graphical based version is self-explanatory and user starts by filling a 
configuration form to create dungeon followed by playing the game on the created
dungeon. 

To run Graphical based version

```
java -jar DungeonMVC.jar
```

To run Console based version
```sh
java -jar DungeonMVC.jar <PlayerName> <NumberOfRowsInDungeon> <NumberOfColsInDungeon> <InterConnectivity> <Wrapping> <TreasureArrowPercentage> <Difficulty(Number Of Monsters)>
```


## How to use the Program
User can download DungeonMVC.jar and use any of the above commands to use the 
program by either giving arguments in the specified order or completely ignoring
them.
Using Graphical Interface:
- GUI will open up with a configuration form and asks user to enter all the 
required parameters and creates dungeon based on them.
- Once dungeon is created, user can view created dungeon. However, only the user
visited blocks are visible initially and are revealed as player moves along.
- To make a move, user can either click on the neighbouring cells or can use the
keyboard arrow keys to move in the appropriate direction.
- To pick up arrows, user should hit 'A' key in the keyboard whilst staying in the
location that has arrows. This action picks up all the arrows present in that location.
If there are no arrows and user tries picking up then an error message is 
displayed to the bottom left of the game screen.
- To pick up treasures, user should hit 'T' key in the keyboard whilst staying in the
  location that has treasures. This action picks up all the treasures present in that location.
  If there are no treasures and user tries picking up then an error message is
  displayed to the bottom left of the game screen.
- To shoot an arrow, user should hit 'S' key in the keyboard followed by any of 
the direction keys on the keyboard. This action will pop up a panel and asks
for the distance to shoot.
- To start a new game, user can user the Options menu in the menu bar. This will
read all the configuration parameters and start a new game.
- To restart the same game, user can user the Options menu in the menu bar. This
will restart the same game from the beginning.
- User can view all the game configuration parameters using Game Configuration
menu in the menu bar.
- Response for all the actions made by user are displayed to the bottom left
of the game screen.

## Description of Examples
1. Setup Page.png: This screenshot shows how the setup page in GUI looks like.
2. Error_Thrown_For_Invalid_Input_In_Setup.png: This screenshot shows how GUI
looks when a error is thrown in setup page.
3. Game_Layout.png: This screenshot shows the game layout after successful 
creation of the dungeon. It shows player and location description to the left.
4. Game_View_showing_Configuration_Options_Menu.png: This screenshot shows the 
game view with look of Game configurations option menu.
5. Game_View_showing_General_Options_Menu.png: This screenshot shows the
game view with look of General Options menu.
6. Game_View_while_playing_game.png: This screenshot shows the game view with
in the middle of game. At the bottom left of this screenshot user can see the
status of every action player takes while playing the game.
7. Game_view_reading_shooting_distance.png: This screenshot shows the game view
when shooting distance is read.
8. Game_view_when_player_dies.png: This screenshot shows how game view looks 
when player dies.
9. Game_view_when_player_wins.png: This screenshot shows how game view looks
when player reaches end location and wins the game.


## Design Changes
From the initial design, below are changes made to final design. 

1. I have added a Read only model for the dungeon so that model can use the same.
2. Used Square Pattern and created new interface to implement new extra credit
features: adding pits, thieves. This pattern helped me to avoid modifying tests
as new functionality is added.
3. Added two new classes that extends JPanel to represent Dungeon view and setup
view.


## Assumptions
1. Player can only pickup all arrows at a time.
2. Player can only pick all treasures at a time.
3. Any cave/tunnel can have a maximum of 3 arrows.
4. Each cave can have a maximum of 3 treasures.
5. If number of monsters given are more than the caves present in dungeon, only accommodative number of monsters are placed and remaining are discarded.
6. Number of pits and thieves are equal to number of monsters.
7. If number of pits given are more than the caves present in dungeon, only accommodative number of pits are placed and remaining are discarded.
8. Thieves are only present in tunnels.
9. Pits are only present in caves.
10. Player can escape from pits with a 50% chance of life.
11. Player can only shoot to a maximum distance of 4 caves.


## Limitations
1. We can also provide option to pick individual arrows/treasures to the user.
2. Current implementation only allows arrows to travel 4 caves at maximum. 


## Citations
1. Referred to https://www.geeksforgeeks.org/union-find/ for implementing union find data structure in kruskals algorithm.
2. Referred to https://www.geeksforgeeks.org/minimum-number-of-edges-between-two-vertices-of-a-graph/ for finding distance between two nodes in the graph.
3. Referred to https://docs.oracle.com/javase/tutorial/uiswing/layout/grid.html
4. Referred to https://docs.oracle.com/javase/tutorial/uiswing/layout/gridbag.html
5. Referred to https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage



