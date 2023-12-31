package MVC;

import java.util.Random;
public class GameModel {
    private static final int DEFAULT_DIMENSION = 5;
    private static final int DEFAULT_NUM_OF_BOARDS = 2;
    private int dimension;
    private final int numOfBoards;
    private int numBoats;
    private int playerHits ;
    private int computerHits = 0;
    private CellState[][] gridPlayer;
    private CellState[][] gridOpponent;
    private final Random rand;
    private String gameString;
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
            if(gridOpponent[row][col] == CellState.B) {  // if player hits a boat
                gridOpponent[row][col] = CellState.H;
                playerHits++;
                return true;
            } else {
                gridOpponent[row][col] = CellState.M;
                return false;
            }
        } else {  // if it's the computer's move
            if(gridPlayer[row][col] == CellState.B) {  // if computer hits a boat
                gridPlayer[row][col] = CellState.H;
                computerHits++;
                return true;
            } else {
                gridPlayer[row][col] = CellState.M;
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
        gridPlayer = new CellState[dimension][dimension];
        gridOpponent = new CellState[dimension][dimension];

        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                gridPlayer[row][col] = CellState.E;
                gridOpponent[row][col] = CellState.E;
            }
        }
    }
    /**
     * Generates random boats on the game grid and returns the number of boats created.
     *
     * @param grid The game grid to generate boats on.
     */
    private void generateNumberBoats(CellState[][] grid) {
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

    }
    /**
     * Calculates and returns the total number of tiles occupied by the boats.
     *
     * @return The total number of tiles occupied by the boats.
     */
    public int calculateTotalTiles() {
        int numTiles = 0;

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
                grid[randRow][randCol + pos] = CellState.B;
            }
        } else {
            // Place the boat vertically
            for (int pos = 0; pos < boatSize; pos++) {
                grid[randRow + pos][randCol] = CellState.B;
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
                    gridPlayer[row][col + pos] = CellState.B;
                }
            } else {
                // Place the boat vertically
                for (int pos = 0; pos < boatSize; pos++) {
                    gridPlayer[row + pos][col] = CellState.B;
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
                if (col + pos >= dimension || grid[row][col + pos] != CellState.E) {
                    validPosition = false;
                    break;
                }
            } else {
                // Check vertically
                if (row + pos >= dimension || grid[row + pos][col] != CellState.E) {
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
        printGridToString(gridPlayer);
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
        for (CellState[] cellStates : grid) {
            for (int col = 0; col < boardSize; col++) {
                System.out.print(cellStates[col] + " ");
            }
            System.out.println();
        }
    }
    /**
     * Enum representing the possible states of a cell in the game grid.
     */
    public enum CellState {
        E, B, H, M
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
    /**
     * Returns the total number of tiles occupied by the boats.
     * @return The total number of tiles occupied by the boats.
     */
    public int getTotalTiles() {
        return calculateTotalTiles();
    }
    /**
     * Converts the grid to a string representation.
     * @param grid The game grid.
     */
    private synchronized void printGridToString(CellState[][] grid) {
        gameString = "";  // Reset gameString to an empty string
        StringBuilder stringBuilder = new StringBuilder();
        int boardSize = grid.length;
        for (CellState[] cellStates : grid) {
            for (int col = 0; col < boardSize; col++) {
                stringBuilder.append(cellStates[col]);
            }
        }
        gameString = stringBuilder.toString();
    }
    /**
     * Returns a string representation of the player's grid.
     * @return A string representation of the player's grid.
     */
    public synchronized String printGridsString() {
        printGridToString(gridPlayer);
        return gameString;
    }
}
