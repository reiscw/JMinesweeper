# JMinesweeper
A Java implementation of the classic Minesweeper game

Note that this game really works best with an external, three-button mouse (in other words, not with the standard Mac touchpad).  The left button will reveal a square if it has not already been clicked.  The right button will flag an empty square (or unflag it if already flagged).  The middle button works as an auto-reveal; if the number of adjacent flagged mines matches the correct number of the square, it will reveal all of the remaining adjacent squares.

The puzzles are completely random and not guaranteed to be solvable without guessing.

On most systems (Linux/MacOS) giving executable permissions to buildJMinesweeper and executing it will create a JAR file. On Windows, buildJMinesweeper can be modified to a .BAT file for the same purpose.
 
