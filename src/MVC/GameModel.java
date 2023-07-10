package MVC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
public class GameModel {
    private GameView gameView;
    private static final int DEFAULT_DIMENSION = 5;
    private static final int DEFAULT_NUM_OF_BOARDS = 2;
    private int dimension;
    private final int numOfBoards;
    private int numBoats;
    private int numTiles;
    private int playerHits ;
    private int computerHits = 0;
    private CellState[][] gridPlayer;
    private CellState[][] gridOpponent;
    private final Random rand;
    /**
     * Constructs a GameModel object with the default dimension and number of boards.
     */
    public GameModel() {
        this.dimension = DEFAULT_DIMENSION*2;
        this.numOfBoards = DEFAULT_NUM_OF_BOARDS;
        rand = new Random();
        initializeGrid(dimension);
    }
    /**
     * Makes a move on the specified board at the given row and column.
     *
     * @param boardIndex The index of the board (0 for player, 1 for opponent).
     * @param row        The row index of the move.
     * @param col        The column index of the move.
     * @return True if a boat was hit, false otherwise.
     */
    public boolean makeMove(int boardIndex, int row, int col) {
        if(boardIndex == 1) {  // if it's the player's move
            if(gridOpponent[row][col] == CellState.BOAT) {  // if player hits a boat
                gridOpponent[row][col] = CellState.HIT;
                playerHits++;
                return true;
            } else {
                gridOpponent[row][col] = CellState.MISS;
                return false;
            }
        } else {  // if it's the computer's move
            if(gridPlayer[row][col] == CellState.BOAT) {  // if computer hits a boat
                gridPlayer[row][col] = CellState.HIT;
                computerHits++;
                return true;
            } else {
                gridPlayer[row][col] = CellState.MISS;
                return false;
            }
        }
    }
    /**
     * Returns the dimension of the game grid.
     *
     * @return The dimension of the game grid.
     */
    public int getDimension() {
        return dimension;
    }
    /**
     * Returns the number of boards in the game.
     *
     * @return The number of boards in the game.
     */
    public int getNumOfBoards() {
        return this.numOfBoards;
    }
    /**
     * Sets the dimension of the game grid.
     *
     * @param selectedDimension The selected dimension.
     */
    public void setDimension(int selectedDimension) {
        this.dimension = selectedDimension;
        initializeGrid(dimension);
    }
    /**
     * Initializes the game grid with the specified dimension.
     *
     * @param dimension The dimension of the game grid.
     */
    private void initializeGrid(int dimension) {
        int boardSize = dimension;
        gridPlayer = new CellState[boardSize][boardSize];
        gridOpponent = new CellState[boardSize][boardSize];

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                gridPlayer[row][col] = CellState.EMPTY;
                gridOpponent[row][col] = CellState.EMPTY;
            }
        }
    }
    /**
     * Generates random boats on the game grid and returns the number of boats created.
     *
     * @param grid The game grid to generate boats on.
     * @return The number of boats generated.
     */
    private int generateNumberBoats(CellState[][] grid) {
        int arraySize = dimension/2;
        int[] boatSizes = new int[arraySize];
        int arrayIndex = 0;

        numBoats=0;

        for (int i = dimension/2; i > 0; i--) {
            boatSizes[arrayIndex] = i;
            arrayIndex++;
            for (int j = 1; j <= dimension/2 - i + 1; j++) {
                createRandomBoat(i, grid);
                numBoats++;
            }
        }

        // printing the array of I value
        System.out.print("i values: ");
        for (int val : boatSizes) {
            System.out.print(val + " ");
        }
        System.out.println();

        System.out.println("Total number of boats: " + numBoats);
        return numBoats;

    }
    /**
     * Calculates and returns the total number of tiles occupied by the boats.
     *
     * @return The total number of tiles occupied by the boats.
     */
    public int calculateTotalTiles() {
        numTiles = 0;

        for (int i = dimension / 2; i > 0; i--) {
            for (int j = 1; j <= dimension / 2 - i + 1; j++) {
                numTiles += i;
            }
        }

        return numTiles;
    }
    /**
     * Creates a random boat on the game grid with the specified size.
     *
     * @param boatSize    The size of the boat.
     * @param grid        The game grid.
     */
    private void createRandomBoat(int boatSize, CellState[][] grid) {
        int randRow, randCol;
        String orientation;
        boolean validPosition;

        do {
            randRow = rand.nextInt(dimension);
            randCol = rand.nextInt(dimension);
            // Randomly choose between "horizontal" and "vertical" orientation
            orientation = rand.nextBoolean() ? "horizontal" : "vertical";
            validPosition = canPlaceBoat(randRow, randCol, boatSize, grid, orientation);
        } while (!validPosition);

        if (orientation.equalsIgnoreCase("horizontal")) {
            // Place the boat horizontally
            for (int pos = 0; pos < boatSize; pos++) {
                grid[randRow][randCol + pos] = CellState.BOAT;
            }
        } else {
            // Place the boat vertically
            for (int pos = 0; pos < boatSize; pos++) {
                grid[randRow + pos][randCol] = CellState.BOAT;
            }

        }

    }
    /**
     * Places a boat manually on the player's grid at the specified position and orientation.
     *
     * @param row         The row index to place the boat.
     * @param col         The column index to place the boat.
     * @param boatSize    The size of the boat.
     * @param orientation The orientation of the boat ("horizontal" or "vertical").
     * @return True if the boat placement is valid, false otherwise.
     */
    public boolean placeBoatManually(int row, int col, int boatSize, String orientation) {
        boolean validPosition = canPlaceBoat(row, col, boatSize, gridPlayer, orientation);

        if (validPosition) {
            if (orientation.equalsIgnoreCase("horizontal")) {
                // Place the boat horizontally
                for (int pos = 0; pos < boatSize; pos++) {
                    gridPlayer[row][col + pos] = CellState.BOAT;
                }
            } else {
                // Place the boat vertically
                for (int pos = 0; pos < boatSize; pos++) {
                    gridPlayer[row + pos][col] = CellState.BOAT;
                }
            }
        }

        return validPosition;
    }
    /**
     * Checks if a boat can be placed at the specified position and orientation on the game grid.
     *
     * @param row         The row index to check.
     * @param col         The column index to check.
     * @param boatSize    The size of the boat.
     * @param grid        The game grid.
     * @param orientation The orientation of the boat ("horizontal" or "vertical").
     * @return True if the boat can be placed, false otherwise.
     */
    private boolean canPlaceBoat(int row, int col, int boatSize, CellState[][] grid, String orientation) {
        boolean validPosition = true;

        // Check if the boat can be placed in the orientation at the given position
        for (int pos = 0; pos < boatSize; pos++) {
            if (orientation.equalsIgnoreCase("horizontal")) {
                // Check horizontally
                if (col + pos >= dimension || grid[row][col + pos] != CellState.EMPTY) {
                    validPosition = false;
                    break;
                }
            } else {
                // Check vertically
                if (row + pos >= dimension || grid[row + pos][col] != CellState.EMPTY) {
                    validPosition = false;
                    break;
                }
            }
        }

        return validPosition;
    }
    /**
     * Places boats manually on the player's grid.
     */
    void placeManualBoats(){
        initializeGrid(dimension);
        generateNumberBoats(gridOpponent);
        printGrids();
    }
    /**
     * Places random boats on both the player's and opponent's grids.
     */
    void placeRandomBoats(){
        initializeGrid(dimension);
        generateNumberBoats(gridPlayer);
        generateNumberBoats(gridOpponent);
        printGrids();
    }
    /**
     * Prints the player's and opponent's grids.
     */
    public void printGrids() {
        System.out.println("Player's Grid:");
        printGrid(gridPlayer);
        System.out.println();

        System.out.println("Opponent's Grid:");
        printGrid(gridOpponent);
        System.out.println();
    }
    /**
     * Prints a grid.
     *
     * @param grid The grid to print.
     */
    private void printGrid(CellState[][] grid) {
        int boardSize = grid.length;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                System.out.print(grid[row][col] + " ");
            }
            System.out.println();
        }
    }
    /**
     * Enum representing the possible states of a cell in the game grid.
     */
    public enum CellState {
        EMPTY, BOAT, HIT, MISS
    }
    /**
     * Returns the player's grid.
     *
     * @return The player's grid.
     */
    public CellState[][] getGridPlayer() {
        return gridPlayer;
    }
    /**
     * Returns the number of hits made by the player.
     *
     * @return The number of hits made by the player.
     */
    public int getPlayerHits() {
        return playerHits;
    }
    /**
     * Returns the number of hits made by the computer opponent.
     *
     * @return The number of hits made by the computer opponent.
     */
    public int getComputerHits() {
        return computerHits;
    }
    /**
     * Returns the number of boats in the game.
     *
     * @return The number of boats in the game.
     */
    public int getNumBoats() {
        return numBoats;
    }
    public int getTotalTiles() {
        return calculateTotalTiles();
    }

}
/**
 * The ColorModel class represents a color selection dialog for the game.
 */
class ColorModel extends JFrame {
    private final JPanel colorPanel;
    /**
     * Constructs a ColorModel object.
     */
    public ColorModel() {
        setTitle("Color Model");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("resources/images/color.png");
        setIconImage(icon.getImage());

        colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS)); // Use vertical BoxLayout

        JPanel unselectedButtonPanel = new JPanel(); // Panel for unselectColorButton
        unselectedButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton unselectColorButton = new JButton("Unselected");
        unselectedButtonPanel.add(unselectColorButton);

        JButton unselectedColorFill = new JButton();
        unselectedColorFill.setEnabled(false);
//        unselectedColorFill.setBackground(Color.WHITE);
        unselectedButtonPanel.add(unselectedColorFill);

        JLabel unselectedColorLabel = new JLabel(); // Label to display selected color for unselectColorButton
        unselectedButtonPanel.add(unselectedColorLabel);

        unselectColorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(colorPanel, "Select a color", Color.WHITE);
            if (selectedColor != null) {
                unselectedColorFill.setBackground(selectedColor);
            }
        });

        JPanel waterButtonPanel = new JPanel(); // Panel for waterColorButton
        waterButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton waterColorButton = new JButton("     Water    ");
        waterButtonPanel.add(waterColorButton);

        JButton waterColorFill = new JButton();
        waterColorFill.setEnabled(false);
//      waterColorFill.setBackground(Color.WHITE);
        waterButtonPanel.add(waterColorFill);

        waterColorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(colorPanel, "Select a color", Color.DARK_GRAY);
            if (selectedColor != null) {
                waterColorFill.setBackground(selectedColor);
            }
        });

        JPanel shipButtonPanel = new JPanel(); // Panel for waterColorButton
        shipButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton shipColorButton = new JButton("      Ship      ");
        shipButtonPanel.add(shipColorButton);

        JButton shipColorFill = new JButton();
        shipColorFill.setEnabled(false);
//      shipColorFill.setBackground(Color.WHITE);
        shipButtonPanel.add(shipColorFill);

        shipColorButton.addActionListener(e -> {
            Color selectedColor = JColorChooser.showDialog(colorPanel, "Select a color", Color.DARK_GRAY);
            if (selectedColor != null) {
                shipColorFill.setBackground(selectedColor);
            }
        });

        colorPanel.add(unselectedButtonPanel); // Add buttonPanel to colorPanel
        colorPanel.add(waterButtonPanel); // Add waterButtonPanel to colorPanel
        colorPanel.add(shipButtonPanel);

        add(colorPanel);
    }
}
