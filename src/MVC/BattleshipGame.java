package MVC;

import javax.swing.SwingUtilities;

public class BattleshipGame {

	public static void main(String[] args) {
		SplashScreen splashScreen = new SplashScreen();
		splashScreen.showSplashScreen();
		// Simulate some initialization process
		try {
			Thread.sleep(3000); // Wait for 3 seconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Close the splash screen and open the game view
		splashScreen.closeSplashScreen();

		SwingUtilities.invokeLater(() -> {
			GameModel model = new GameModel();
			GameView view = new GameView(model);
			GameController controller = new GameController(model, view);
		});
	}
}
