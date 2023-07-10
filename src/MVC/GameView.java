package MVC;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.*;
public class GameView extends JFrame {
	private final GameModel gameModel;
	private final GameController gameController;
	private JDialog designDialog;
	private int dim=5;
	private JPanel[] gridPanel;
	private JButton[][][] gridButtons;
	private JPanel controlPanel;
	private JPanel playerGridPanel;
	private JPanel opponentGridPanel;
	private JMenuBar menuBar;
	private JMenu fileMenu, helpMenu;
	private JMenuItem newItem, solutionItem, exitItem;
	private JMenuItem colorItem, aboutItem;
	private boolean designMode = false;
	private boolean gameMode = false;
	private AtomicLong startTime;
	private JComboBox<Integer> boatSizeChoiceBox;
	private JComboBox<String> boatDirectionChoiceBox;
	public void showSplashScreen() {
		// Create a JFrame as splash screen
		JFrame splashScreen = new JFrame();
		splashScreen.setUndecorated(true);

		// Load the image
		JLabel splashLabel = new JLabel();
		ImageIcon imageIcon = new ImageIcon("src/game_about.jpg");
		splashLabel.setIcon(imageIcon);

		// Add image to the frame
		splashScreen.getContentPane().add(splashLabel);
		splashScreen.pack();

		// Center the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int locationX = (screenSize.width ) ;
		int locationY = (screenSize.height) ;
		splashScreen.setLocation(locationX, locationY);

		// Show the splash screen
		splashScreen.setVisible(true);

		// Close the splash screen after some time
		try {
			Thread.sleep(5000);  // Show splash for 5 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		splashScreen.dispose();
	}

	public GameView(GameModel gameModel) {
		this.gameModel = gameModel;
		this.gameController = new GameController(gameModel, this);
		int rows = gameModel.getDimension();
		int columns = gameModel.getDimension();
		int numOfBoards = gameModel.getNumOfBoards();

		menuBar = new JMenuBar();
		fileMenu = new JMenu("Game");
		helpMenu = new JMenu("Help");

		newItem = new JMenuItem("New");
		solutionItem = new JMenuItem("Solution");
		exitItem = new JMenuItem("Exit");

		colorItem = new JMenuItem("Colors");
		aboutItem = new JMenuItem("About");

		fileMenu.add(newItem);
		fileMenu.add(solutionItem);
		fileMenu.add(exitItem);

		helpMenu.add(colorItem);
		helpMenu.add(aboutItem);

		menuBar.add(fileMenu);
		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		setGameIcon();
		setSize(1250, 600);
		setLocationRelativeTo(null);

		setTitle("Battleship");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		gridPanel = new JPanel[numOfBoards];
		gridButtons = new JButton[numOfBoards][rows][columns];

		createGridPanels(rows, columns);
		createControlPanel();

		setLayout(new GridLayout(1, 1));
		add(controlPanel);
		showSplashScreen();
		setVisible(true);
	}
	static class InstructionsWindow extends JFrame {
		public InstructionsWindow() {
			setTitle("Instructions");
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setSize(400, 300);
			setLocationRelativeTo(null);

			ImageIcon icon = new ImageIcon("resources/images/icon_1.png");
			setIconImage(icon.getImage());

			JLabel instructionsLabel = new JLabel("<html>"
					+ "<h2>&nbsp;&nbsp;Instructions:</h2>"
					+ "<p>&nbsp;&nbsp;&nbsp;Each player has to place their ships on their grid without the &nbsp;&nbsp;&nbsp;opponent knowing the placement.</p>"
					+ "</html>");
			instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
			add(instructionsLabel);
		}
	}
	public JMenuItem getNewItem() {
		return newItem;
	}
	public JMenuItem getSolutionItem() {
		return solutionItem;
	}
	public JMenuItem getExitItem() {
		return exitItem;
	}
	public JMenuItem getColorItem() {
		return colorItem;
	}
	public JMenuItem getAboutItem() {
		return aboutItem;
	}
	private void createGridPanels(int rows, int columns) {
	    // Create the player grid panel
	    playerGridPanel = new JPanel(new GridLayout(rows + 2, columns + 1));
	    playerGridPanel.setPreferredSize(new Dimension(500, 500));
	    playerGridPanel.removeAll(); // Clear the existing grid panel
	    playerGridPanel.add(createLabelButton("Player"));

	    for (int j = 0; j < columns; j++) {
	        JButton labelButton = new JButton(String.valueOf(j + 1));
	        labelButton.setEnabled(false);
	        playerGridPanel.add(labelButton);
	    }

	    char labelChar = 'A';

	    for (int j = 0; j < rows; j++) {
	        JButton labelButton = new JButton(String.valueOf(labelChar));
	        labelButton.setEnabled(false);
	        playerGridPanel.add(labelButton);
	        labelChar++;

	        for (int k = 0; k < columns; k++) {
	            gridButtons[0][j][k] = new JButton(); // Assuming gridButtons is a 3D array
	            playerGridPanel.add(gridButtons[0][j][k]);
			}
	    }

	    // Add a progress bar below the player grid panel
	    JProgressBar playerProgressBar = new JProgressBar(0, 100);
	    playerProgressBar.setStringPainted(true);
	    playerProgressBar.setValue(gameModel.getPlayerHits());
	    playerGridPanel.add(playerProgressBar);

	    add(playerGridPanel);

	    // Create the opponent grid panel
	    opponentGridPanel = new JPanel(new GridLayout(rows + 2, columns + 1));
	    opponentGridPanel.setPreferredSize(new Dimension(500, 500));
	    opponentGridPanel.removeAll(); // Clear the existing grid panel

	    opponentGridPanel.add(createLabelButton("Opponent"));

	    for (int j = 0; j < columns; j++) {
	        JButton labelButton = new JButton(String.valueOf(j + 1));
	        labelButton.setEnabled(false);
	        opponentGridPanel.add(labelButton);
	    }

	    labelChar = 'A';

	    for (int j = 0; j < rows; j++) {
	        JButton labelButton = new JButton(String.valueOf(labelChar));
	        labelButton.setEnabled(false);
	        opponentGridPanel.add(labelButton);
	        labelChar++;

	        for (int k = 0; k < columns; k++) {
	            gridButtons[1][j][k] = new JButton(); // Assuming gridButtons is a 3D array
	            opponentGridPanel.add(gridButtons[1][j][k]);
	        }
	    }

	    // Add a progress bar below the opponent grid panel
	    JProgressBar opponentProgressBar = new JProgressBar(0, 100);
	    opponentProgressBar.setStringPainted(true);
	    opponentProgressBar.setValue(gameModel.getComputerHits());
	    opponentGridPanel.add(opponentProgressBar);

	    add(opponentGridPanel);

	    setVisible(true);
	}
	private void createControlPanel() {
		controlPanel = new JPanel();
		controlPanel.setLayout(new GridBagLayout());
	    
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;

		// Panel 1
		controlPanel.add(playerGridPanel,constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;

		// Panel 2
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

		// Logo Panel
		JPanel logoPanel = new JPanel();
		logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JLabel logoLabel = new JLabel();
		ImageIcon logoIcon = new ImageIcon("resources/images/logo.png");
		logoLabel.setIcon(logoIcon);

		logoPanel.add(logoLabel);
		centerPanel.add(logoPanel);

		// languageSelectorPanel
		JPanel languageSelectorPanel = new JPanel();
		languageSelectorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel languageSelectorLabel = new JLabel("Languages: ");
		languageSelectorLabel.setPreferredSize(new Dimension(75, 25));

		languageSelectorPanel.add(languageSelectorLabel);
		String[] languages = { "English", "French" };
		JComboBox<String> languageComboBox = new JComboBox<>(languages);
		languageComboBox.setPreferredSize(new Dimension(80, 25));
		languageSelectorPanel.add(languageComboBox);
		centerPanel.add(languageSelectorPanel);

		// Design and Random Buttons
		JPanel designRandomPanel = new JPanel();
		designRandomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton designButton = new JButton("   Design  ");
		designButton.addActionListener(e -> {
			switchToDesignMode();
			gameModel.placeManualBoats();
		});

		JButton randomLayoutButton = new JButton(" Random ");
		randomLayoutButton.addActionListener(e -> {
			System.out.println("Random Layout button clicked");
			gameModel.placeRandomBoats();
			gameController.updatePlayer();
		});

		designRandomPanel.add(designButton);
		designRandomPanel.add(randomLayoutButton);
		centerPanel.add(designRandomPanel);

		// Dimension Choice Box
		JPanel dimensionPanel = new JPanel();
		dimensionPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel dimensionLabel = new JLabel("Board Dimension:");
		JComboBox<Integer> dimensionChoiceBox = new JComboBox<>();
		for (int i = 1; i <= 10; i++) {
			dimensionChoiceBox.addItem(i);
		}
		dimensionChoiceBox.addActionListener(e -> {
			if (gameModel == null) {
				System.out.println("gameModel is null");
				return;
			}
			Integer selectedItem = (Integer) dimensionChoiceBox.getSelectedItem();
			if (selectedItem != null) {
				int selectedDimension = selectedItem;
				dim=selectedItem;
				System.out.println("Dimension set to: " + selectedDimension);
				gameModel.setDimension(selectedDimension*2); // update dimension in game model
				if (gridPanel == null || gridButtons == null) {
					System.out.println("gridPanel or gridButtons is null");
					return;
				}
				redrawBoard();
				gameController.attachButtonListeners();

			}
		});

		dimensionPanel.add(dimensionLabel);
		dimensionPanel.add(dimensionChoiceBox);
		centerPanel.add(dimensionPanel);

		// History Text Area
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

		JTextArea historyTextArea = new JTextArea();
		JScrollPane historyScrollPane = new JScrollPane(historyTextArea);
		historyTextArea.setWrapStyleWord(true); //Fit text in the box size
		historyTextArea.setLineWrap(true);
		bottomPanel.add(historyScrollPane);

		// Timer
        JPanel timeLayout = new JPanel();
        timeLayout.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton timerButton = new JButton();
        timerButton.setPreferredSize(new Dimension(85, 25));
        timerButton.setFocusable(false); // Set the button as non-focusable
        timerButton.setEnabled(false); // Disable the button
        timerButton.setBackground(Color.WHITE); // Set the background color to white

        JLabel timeLabel = new JLabel("Time: ");
        JLabel timerLabel = new JLabel("00:00:00");

        timeLayout.add(timeLabel);
        timerButton.add(timerLabel);
        timeLayout.add(timerButton);

        startTime = new AtomicLong(0);

        int delay = 1000;
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsedTime = System.currentTimeMillis() - startTime.get();
                String formattedTime = formatTime(elapsedTime);
                timerLabel.setText(formattedTime);
            }
        });
        bottomPanel.add(timeLayout);
		
		// Reset and Play Buttons
		JPanel resetPlayPanel = new JPanel();
		resetPlayPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Reset button clicked");
				redrawBoard();
                startTime.set(System.currentTimeMillis()); // Set the start time using AtomicLong's set() method
                timer.start(); // Start the timer
            }
        });

		JButton playButton = new JButton("Play");
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Play button clicked");
				gameMode = true;
                startTime.set(System.currentTimeMillis()); // Set the start time using AtomicLong's set() method
                timer.start(); // Start the timer
            }
        });

		resetPlayPanel.add(resetButton);
		resetPlayPanel.add(playButton);

		bottomPanel.add(resetPlayPanel);

		// Add top and bottom panels to center panel
		centerPanel.add(bottomPanel);
	    
		controlPanel.add(centerPanel, constraints);
		constraints.gridx = 2;
		constraints.gridy = 0;

		// Panel 3
		controlPanel.add(opponentGridPanel, constraints);

		setVisible(true);
	}
    private String formatTime(long milliseconds) {
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000 * 60)) % 60;
        long hours = (milliseconds / (1000 * 60 * 60)) % 24;

        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return formattedTime;
    }
	private void switchToDesignMode() {
		designMode = true;

		// Create the design panel
		JPanel designPanel = createDesignPanel();

		// Create a main panel to hold both the playerGridPanel and designPanel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		// Add both panels to mainPanel
		mainPanel.add(playerGridPanel);
		mainPanel.add(designPanel);

		// Create a non-modal JDialog containing the mainPanel
		designDialog = new JDialog(this, "Design Mode", false);
		designDialog.setContentPane(mainPanel);
		designDialog.pack();
		designDialog.setLocationRelativeTo(this);
		designDialog.setVisible(true);
	}
	private JPanel createDesignPanel() {
		// Create a JPanel for the design panel
		JPanel designPanel = new JPanel();
		designPanel.setLayout(new BoxLayout(designPanel, BoxLayout.Y_AXIS));
		this.boatSizeChoiceBox = new JComboBox<>();
			for (int i = 1; i <= dim; i++) { // let user select boat size up to totalBoats
				this.boatSizeChoiceBox.addItem(i);
			}
		designPanel.add(new JLabel("Select Boat Size:"));
		designPanel.add(boatSizeChoiceBox);

		// Create a box for selecting boat direction
		this.boatDirectionChoiceBox = new JComboBox<>();
		this.boatDirectionChoiceBox.addItem("Horizontal");
		this.boatDirectionChoiceBox.addItem("Vertical");

		designPanel.add(new JLabel("Select Boat Direction:"));
		designPanel.add(this.boatDirectionChoiceBox);

		// Create a "Place" button
		JButton placeButton = new JButton("Place");
		placeButton.addActionListener(e -> {
			Integer selectedItem = (Integer) boatSizeChoiceBox.getSelectedItem();
			if (selectedItem != null) {
				int boatSize = selectedItem;
				String boatDirection = (String) boatDirectionChoiceBox.getSelectedItem();
				gameController.setBoatSizeAndDirection(boatSize, boatDirection);
				System.out.println("Boat Size: " + boatSize + " Boat Direction: " + boatDirection);
			}
		});
		designPanel.add(placeButton);

		// Create the back button
		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			switchToGameMode();
			designDialog.dispose();
			revalidate();
			repaint();
		});
		designPanel.add(backButton);

		return designPanel;
	}
	private void switchToGameMode() {
		designMode = false;
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		controlPanel.add(playerGridPanel,constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		// Repaint and revalidate the JFrame
		this.repaint();
		this.revalidate();
	}
	private JButton createLabelButton(String string) {
		JButton labelButton = new JButton(string);
		labelButton.setEnabled(false);
		labelButton.setFocusable(false);
		return labelButton;
	}
    private void setGameIcon() {
        ImageIcon icon = new ImageIcon("resources/images/icon_1.png");
        setIconImage(icon.getImage());
    }
	public JButton[][][] getGridButtons() {
		return gridButtons;
	}
	public void updatePlayerGridColors(GameModel gameModel) {
		GameModel.CellState[][] gridPlayer = gameModel.getGridPlayer();
		int boardIndex = 0; // Player's board

		for (int row = 0; row < gridPlayer.length; row++) {
			for (int col = 0; col < gridPlayer[row].length; col++) {
				Color color;

				switch (gridPlayer[row][col]) {
					case BOAT:
						color = Color.GREEN;
						break;
					case HIT:
						color = Color.RED;
						break;
					case MISS:
						color = Color.BLUE;
						break;
					case EMPTY:
					default:
						color = this.getBackground();
						break;
				}
				gridButtons[boardIndex][row][col].setBackground(color);
				gridButtons[boardIndex][row][col].setContentAreaFilled(false);
				gridButtons[boardIndex][row][col].setOpaque(true);
			}
		}
		this.revalidate();
		this.repaint();
	}
	protected void redrawBoard() {
		// Remove only the controlPanel from the frame
		getContentPane().remove(controlPanel);
		// Update rows, columns and numOfBoards based on the updated gameModel
		int rows = gameModel.getDimension();
		int columns = gameModel.getDimension();
		int numOfBoards = gameModel.getNumOfBoards();
		// Create new gridPanel and gridButtons with updated size
		gridPanel = new JPanel[numOfBoards];
		gridButtons = new JButton[numOfBoards][rows][columns];
		// Re-initialize gridPanels
		createGridPanels(rows, columns);
		// Re-initialize controlPanel
		createControlPanel();
		// Add controlPanel to the frame again
		add(controlPanel);
		// Refresh the frame
		revalidate();
		repaint();
	}
	public boolean getDesignMode() {
		return this.designMode;
	}
	public boolean getGameMode() {
		return this.gameMode;
	}

	public int getBoatSize() {
		return (Integer) this.boatSizeChoiceBox.getSelectedItem();
	}
	public String getBoatDirection() {
		return (String) this.boatDirectionChoiceBox.getSelectedItem();
	}

}