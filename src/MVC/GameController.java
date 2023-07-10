package MVC;

import java.awt.*;
import java.util.Random;
import javax.swing.*;
/**
 * The GameController class handles the game logic and user interactions for the Battleship game.
 */
public class GameController {
	private final GameModel model;
	private final GameView view;
	private Integer currentBoatSize = null;
	private String currentBoatDirection = null;
	private boolean playerTurn;
	/**
	 * Constructs a GameController object with the specified GameModel and GameView.
	 *
	 * @param model The GameModel object representing the game model.
	 * @param view  The GameView object representing the game view.
	 */
	public GameController(GameModel model, GameView view) {
		this.model = model;
		this.view = view;
		this.playerTurn = true;
		attachButtonListeners();
		if (view.getNewItem() != null) {
			view.getNewItem().addActionListener(e -> resetGame());
		}
		if (view.getSolutionItem() != null) {
			view.getSolutionItem().addActionListener(e -> showInstructions());
		}
		if (view.getExitItem() != null) {
			view.getExitItem().addActionListener(e -> System.exit(0));
		}
		if (view.getColorItem() != null) {
			view.getColorItem().addActionListener(e -> {
				ColorModel colorSelection = new ColorModel();
				colorSelection.setVisible(true);
			});
		}
	}
	/**
	 * Attaches button listeners to the grid buttons.
	 */
	public void attachButtonListeners() {
		JButton[][][] gridButtons = view.getGridButtons();

		if (gridButtons != null) {
			for (int i = 0; i < gridButtons.length; i++) {
				for (int j = 0; j < gridButtons[i].length; j++) {
					for (int k = 0; k < gridButtons[i][j].length; k++) {
						final int boardIndex = i;
						final int row = j;
						final int column = k;
						gridButtons[i][j][k].addActionListener(e -> handleCellClicked(boardIndex, row, column));
					}
				}
			}
		}
	}
	/**
	 * Handles the event when a cell is clicked on the grid.
	 *
	 * @param boardIndex The index of the board (player's or opponent's).
	 * @param row        The row index of the clicked cell.
	 * @param col        The column index of the clicked cell.
	 */
	public void handleCellClicked(int boardIndex, int row, int col) {
		JButton[][][] gridButtons = view.getGridButtons();

		if (view.getDesignMode()) {
			// In design mode, and it is the player's board
			if (boardIndex == 0) {
				int boatSize = view.getBoatSize();
				String direction = view.getBoatDirection();

				// Try to place a boat at the clicked location
				boolean success = model.placeBoatManually(row, col, boatSize, direction);

				// If the placement was successful, change the color of the buttons
				if (success) {
					System.out.println("Boat placed at Row "+row+" Col "+col+"!");
					for (int i = 0; i < boatSize; i++) {
						if ("Horizontal".equals(direction)) {
							if (col + i < gridButtons[boardIndex][row].length) {
								gridButtons[boardIndex][row][col + i].setBackground(Color.GREEN);
								gridButtons[boardIndex][row][col].setContentAreaFilled(false);
								gridButtons[boardIndex][row][col].setOpaque(true);
							}
						} else {
							if (row + i < gridButtons[boardIndex].length) {
								gridButtons[boardIndex][row + i][col].setBackground(Color.GREEN);
								gridButtons[boardIndex][row][col].setContentAreaFilled(false);
								gridButtons[boardIndex][row][col].setOpaque(true);
							}
						}
					}
				} else {
					System.out.println("Cannot place a boat at Row "+row+" Col "+col+".");
				}
			}
		} else if(boardIndex == 1) { // Not in design mode and it's opponent's board
			boolean hit = model.makeMove(boardIndex, row, col);
			if (hit) {
				System.out.println("You hit a boat on board " + boardIndex +" Row "+row+" Col "+col+ "!");
				gridButtons[boardIndex][row][col].setBackground(Color.RED);
			} else {
				System.out.println("You missed on board " + boardIndex +" Row "+row+" Col "+col+ ".");
				gridButtons[boardIndex][row][col].setBackground(Color.BLUE);
			}
			gridButtons[boardIndex][row][col].setContentAreaFilled(false);
			gridButtons[boardIndex][row][col].setOpaque(true);
			UIManager.put("Button.disabledText", Color.BLACK);

			// After player's move, call the computer move
			computerMakeMove();
		}
	}
	/**
	 * Sets the boat size and direction for the player's move.
	 *
	 * @param size      The size of the boat.
	 * @param direction The direction of the boat.
	 */
	public void setBoatSizeAndDirection(Integer size, String direction) {
		this.currentBoatSize = size;
		this.currentBoatDirection = direction;
	}
	/**
	 * Resets the game by redrawing the board and attaching button listeners.
	 */
	public void resetGame() {
		view.redrawBoard();
		attachButtonListeners();
	}
	/**
	 * Shows the game instructions.
	 */
	public void showInstructions() {
		GameView.InstructionsWindow instructionsWindow = new GameView.InstructionsWindow();
		instructionsWindow.setVisible(true);
	}
	/**
	 * Updates the player's grid colors based on the current game model.
	 */
	public void updatePlayer(){
		view.updatePlayerGridColors(model);
	}
	/**
	 * Makes a move for the computer opponent.
	 */
	public void computerMakeMove() {
		Random random = new Random();
		int row, col;
		GameModel.CellState[][] playerGrid = model.getGridPlayer();
		do {
			row = random.nextInt(model.getDimension());
			col = random.nextInt(model.getDimension());
		} while (playerGrid[row][col] == GameModel.CellState.HIT || playerGrid[row][col] == GameModel.CellState.MISS);

		boolean hit = model.makeMove(0, row, col); // Making move on player's board
		if (hit) {
			System.out.println("Computer hit a boat at Row "+row+" Col "+col+"!");
			view.getGridButtons()[0][row][col].setBackground(Color.RED);
		} else {
			System.out.println("Computer missed at Row "+row+" Col "+col+".");
			view.getGridButtons()[0][row][col].setBackground(Color.BLUE);
		}
		view.getGridButtons()[0][row][col].setContentAreaFilled(false);
		view.getGridButtons()[0][row][col].setOpaque(true);
		UIManager.put("Button.disabledText", Color.BLACK);
	}
}
