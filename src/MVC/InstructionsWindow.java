package MVC;

import javax.swing.*;

public class InstructionsWindow extends JFrame {
    public InstructionsWindow() {
        setTitle("Instructions");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("resources/images/icon_1.png");
        setIconImage(icon.getImage());

        JLabel instructionsLabel = new JLabel("<html>"
                + "<h2>  Battleship:</h2>"
                + "<p>   Random or Manual? Press Play to Begin. Choose your style and set sail!</p>"
                + "</html>");
        instructionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(instructionsLabel);
    }
}
