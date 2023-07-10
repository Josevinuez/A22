package MVC;

import javax.swing.SwingUtilities;

public class BattleshipGame {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			GameModel model = new GameModel();
			GameView view = new GameView(model);
			GameController controller = new GameController(model, view);
		});
	}
}
