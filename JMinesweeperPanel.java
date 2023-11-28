import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class JMinesweeperPanel extends JPanel {
		
	private static final int SMALL = 0;
	private static final int MEDIUM = 1;
	private static final int LARGE = 2;
	private static final int BUTTON_SIZE = 25;
	
	private JButton[][] mineButtons;
	private JButton newGameButton;
	private JButton resetGameButton;
	private JButton quitGameButton;
	private JLabel mineCountLabel;
	private int width;
	private int height;
	private int type;
	private boolean[][] flags;
	private JMinesweeperGame game;
	
	public JMinesweeperPanel(int type) {
		// initial setup and configuration
		this.type = type;
		game = new JMinesweeperGame(type);
		switch (type) {
			case SMALL: width = 8; height = 8; break;
			case MEDIUM: width = 16; height = 16; break;
			case LARGE: width = 30; height = 16; break;
		}
		flags = new boolean[height][width];
		//adjust panel size and set layout
        setPreferredSize(new Dimension(width*BUTTON_SIZE + 180, height*BUTTON_SIZE + 50));
        setLayout(null);
        // set up mine buttons
		mineButtons = new JButton[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				final int r = i; // necessary for lambda expression
				final int c = j; // necessary for lambda expression
				mineButtons[i][j] = new JButton();
				mineButtons[i][j].setBounds(25 + BUTTON_SIZE * j, 25 + BUTTON_SIZE * i, BUTTON_SIZE, BUTTON_SIZE);
				add(mineButtons[i][j]);
				mineButtons[i][j].addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e){
						click(r, c, e.getButton());
					}
				});
			}
		}
		// set up control buttons and label
		newGameButton = new JButton("New Game");
		resetGameButton = new JButton("Reset Game");
		quitGameButton = new JButton("Quit Game");
		mineCountLabel = new JLabel();
		updateMineCount();
		newGameButton.setBounds(40 + BUTTON_SIZE * width, 25, 120, 25);
		resetGameButton.setBounds(40 + BUTTON_SIZE * width, 75, 120, 25);
		quitGameButton.setBounds(40 + BUTTON_SIZE * width, 125, 120, 25);
		mineCountLabel.setBounds(50 + BUTTON_SIZE * width, 175, 120, 25);
		add(newGameButton);
		add(resetGameButton);
		add(quitGameButton);
		add(mineCountLabel);
		newGameButton.addActionListener(e -> {
			try {
				newGame();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		});   
		resetGameButton.addActionListener(e -> {
			try {
				resetGame();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		});   
		quitGameButton.addActionListener(e -> {
			try {
				quitGame();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		});   
	}
	
	public void click(int r, int c, int b) {
		if (b == MouseEvent.BUTTON1) {
			// left button, empty square --> reveal
			if (mineButtons[r][c].getIcon() == null) {
				if (game.isMine(r, c)) {
					mineButtons[r][c].setIcon(Icons.mineIcon());
					// announce failure
					failure();
				} else {
					int n = game.getAdjacentMines(r, c);
					if (n > 0) {
						mineButtons[r][c].setIcon(Icons.numberIcon(n));
					} else {
						mineButtons[r][c].setIcon(Icons.blueIcon());
						// reveal adjacents if blue
						reveal(r, c);
					}
				}
			}
		} else if (b == MouseEvent.BUTTON2 && mineButtons[r][c].getIcon() != null) {
			// center button, labeled square --> check if condition satisfied, reveal adjacent
			// count adjacent mine flags
			int count = 0;
			// above 
			if (r > 0) {
				if (flags[r-1][c]) {
					count++;
					if (!game.isMine(r-1, c)) {
						mineButtons[r-1][c].setIcon(Icons.mineIcon());
						failure();
					}
				} 
				// above left
				if (c > 0 && flags[r-1][c-1]) {
					count++;
					if (!game.isMine(r-1, c-1)) {
						mineButtons[r-1][c-1].setIcon(Icons.mineIcon());
						failure();
					}
				}
				// above right
				if (c < width - 1 && flags[r-1][c+1]) {
					count++;
					if (!game.isMine(r-1, c+1)) {
						mineButtons[r-1][c+1].setIcon(Icons.mineIcon());
						failure();
					}
				}
			}
			// below 
			if (r < height - 1) {
				if (flags[r+1][c]) {
					count++;
					if (!game.isMine(r+1, c)) {
						mineButtons[r+1][c].setIcon(Icons.mineIcon());
						failure();
					}
				} 
				// below left
				if (c > 0 && flags[r+1][c-1]) {
					count++;
					if (!game.isMine(r+1, c-1)) {
						mineButtons[r+1][c-1].setIcon(Icons.mineIcon());
						failure();
					}
				}
				// below right
				if (c < width - 1 && flags[r+1][c+1]) {
					count++;
					if (!game.isMine(r+1, c+1)) {
						mineButtons[r+1][c+1].setIcon(Icons.mineIcon());
						failure();
					}
				}
			}
			// left
			if (c > 0 && flags[r][c-1]) {
				count++;
				if (!game.isMine(r, c-1)) {
					mineButtons[r][c-1].setIcon(Icons.mineIcon());
					failure();
				}
			}
			// right
			if (c < width - 1 && flags[r][c+1]) {
				count++;
				if (!game.isMine(r, c+1)) {
					mineButtons[r][c+1].setIcon(Icons.mineIcon());
					failure();
				}
			}
			if (count == game.getAdjacentMines(r, c)) {
				// reveal all of the squares
				reveal(r, c);
			}
		} else if (b == MouseEvent.BUTTON3) {
			// right button, empty square --> flag as mine
			if (mineButtons[r][c].getIcon() == null) {
				mineButtons[r][c].setIcon(Icons.flagIcon());
				flags[r][c] = true;
			} else if (flags[r][c]) {
				// undo if labeled mine
				flags[r][c] = false;
				mineButtons[r][c].setIcon(null);
			}
		}
		// revalidate and repaint the panel
		revalidate();
        repaint();
		// update the flagged mine count
		updateMineCount();
		// run a check
		check();
	}
	
	public void updateMineCount() {
		int count = 0;
		switch (type) {
			case SMALL: count = 10; break;
			case MEDIUM: count = 40; break;
			case LARGE: count = 99; break;
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (flags[i][j]) {
					count--;
				}
			}
		}
		mineCountLabel.setText("Mines left: " + count);
	}
	
	public void check() {
		boolean success = true;
		// first check that mines left is zero
		int count = 0;
		switch (type) {
			case SMALL: count = 10; break;
			case MEDIUM: count = 40; break;
			case LARGE: count = 99; break;
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (flags[i][j]) {
					count--;
				}
			}
		}
		if (count != 0) return;
		// then check that all flags correspond to a mine in the game
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (flags[i][j] == true && game.isMine(i, j) == false) {
					return;
				}
			}
		}
		// reveal the remaining squares
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (mineButtons[i][j].getIcon() == null) {
					int n = game.getAdjacentMines(i, j);
					if (n > 0) {
						mineButtons[i][j].setIcon(Icons.numberIcon(n));
					} else {
						mineButtons[i][j].setIcon(Icons.blueIcon());
					}
				}
			}
		}
		// announce success
		String successMessage = "Would you like to play again?";
		int result = JOptionPane.showConfirmDialog(null,  successMessage, "Congratulations!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			newGame();
		} else {
			System.exit(0);
		}
	}
	
	public void reveal(int r, int c) {
		// reveal adjacents
		if (r > 0) {
			// above
			if (mineButtons[r-1][c].getIcon() == null) {
				boolean result = computeIcon(r-1, c);
				if (result) {
					reveal(r-1, c);
				}
			}
			// above left
			if (c > 0 && mineButtons[r-1][c-1].getIcon() == null) {
				boolean result = computeIcon(r-1, c-1);
				if (result) {
					reveal(r-1, c-1);
				}
			}
			// above right
			if (c < width - 1 && mineButtons[r-1][c+1].getIcon() == null) {
				boolean result = computeIcon(r-1, c+1);
				if (result) {
					reveal(r-1, c+1);
				}
			}
		}
		if (r < height - 1) {
			// below 
			if (mineButtons[r+1][c].getIcon() == null) {
				boolean result = computeIcon(r+1, c);
				if (result) {
					reveal(r+1, c);
				}
			} 
			// below left
			if (c > 0 && mineButtons[r+1][c-1].getIcon() == null) {
				boolean result = computeIcon(r+1, c-1);
				if (result) {
					reveal(r+1, c-1);
				}
			}
			// below right
			if (c < width - 1 && mineButtons[r+1][c+1].getIcon() == null) {
				boolean result = computeIcon(r+1, c+1);
				if (result) {
					reveal(r+1, c+1);
				}
			}
		}
		// left
		if (c > 0 && mineButtons[r][c-1].getIcon() == null) {
			boolean result = computeIcon(r, c-1);
			if (result) {
				reveal(r, c-1);
			}
		}
		// right
		if (c < width - 1 && mineButtons[r][c+1].getIcon() == null) {
			boolean result = computeIcon(r, c+1);
			if (result) {
				reveal(r, c+1);
			}
		}
	}
	
	public void failure() {
		String successMessage = "Would you like to play again?";
		int result = JOptionPane.showConfirmDialog(null,  successMessage, "Sorry, you lose!", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			newGame();
		} else {
			System.exit(0);
		}
	}
	
	public boolean computeIcon(int r, int c) {
		int n = game.getAdjacentMines(r, c);
		if (n > 0) {
			mineButtons[r][c].setIcon(Icons.numberIcon(n));
			return false;
		} else {
			mineButtons[r][c].setIcon(Icons.blueIcon());
			return true;
		}
	}
	
	public void newGame() {
		// reset all buttons and flags
		resetGame();
		// replace game
		game = new JMinesweeperGame(type);
	}
	
	public void resetGame() {
		// reset buttons / flags
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				mineButtons[r][c].setIcon(null);
				flags[r][c] = false;
			}
		}
		// reset mine count
		updateMineCount();
	}
	
	public void quitGame() {
		System.exit(0);
	}
	
	public static void main(String[] args) {	
		// allow user to choose size
		String[] choices = {"Small (8x8 10 mines)", "Medium (16x16 40 mines)", "Large (30x16 99 mines)"};
		String s = (String) JOptionPane.showInputDialog(null, "Choose your difficulty: ", "JMinesweeper", JOptionPane.PLAIN_MESSAGE, null, choices, choices[0]);
		if (s == null) System.exit(0);
		int difficulty;
		if (s.equals(choices[0])) {
			difficulty = SMALL;
		} else if (s.equals(choices[1])) {
			difficulty = MEDIUM;
		} else {
			difficulty = LARGE;
		}
		// set up the game
		JFrame frame = new JFrame("JMinesweeper Version 1.1 by Christopher Reis");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JMinesweeperPanel(difficulty));
        frame.pack();
        frame.setVisible(true);
	}
}
