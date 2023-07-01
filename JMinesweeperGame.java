public class JMinesweeperGame {
	
	private boolean[][] mines;
	
	private final int WIDTH;
	private final int HEIGHT;
	private final int MINE_COUNT;
	
	private static final int SMALL = 0;
	private static final int MEDIUM = 1;
	private static final int LARGE = 2;
	
	public JMinesweeperGame(int type) {
		switch (type) {
			case 0: WIDTH = 8; HEIGHT = 8; MINE_COUNT = 10; break;
			case 1: WIDTH = 16; HEIGHT = 16; MINE_COUNT = 40; break;
			case 2: WIDTH = 30; HEIGHT = 16; MINE_COUNT = 99; break;
			default: WIDTH = 0; HEIGHT = 0; MINE_COUNT = 0; break;
		}
		mines = new boolean[HEIGHT][WIDTH];
		for (int i = 0; i < MINE_COUNT; i++) {
			boolean done = false;
			while (!done) {
				int r = (int)(Math.random()*HEIGHT);
				int c = (int)(Math.random()*WIDTH);
				if (mines[r][c] == false) {
					mines[r][c] = true;
					done = true;
				}
			}
		}
	}
	
	public boolean isMine(int r, int c) {
		return mines[r][c];
	}
	
	public int getAdjacentMines(int r, int c) {
		int count = 0;
		// above 
		if (r > 0) {
			if (mines[r-1][c]) {
				count++;
			} 
			// above left
			if (c > 0 && mines[r-1][c-1]) {
				count++;
			}
			// above right
			if (c < WIDTH - 1 && mines[r-1][c+1]) {
				count++;
			}
		}
		// below 
		if (r < HEIGHT - 1) {
			if (mines[r+1][c]) {
				count++;
			} 
			// below left
			if (c > 0 && mines[r+1][c-1]) {
				count++;
			}
			// below right
			if (c < WIDTH - 1 && mines[r+1][c+1]) {
				count++;
			}
		}
		// left
		if (c > 0 && mines[r][c-1]) {
			count++;
		}
		// right
		if (c < WIDTH - 1 && mines[r][c+1]) {
			count++;
		}
		return count;
	}
}
