package MVC;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    public void showSplashScreen() {
        JLabel splashLabel = new JLabel(new ImageIcon(getClass().getResource("/splash.jpg")));
        getContentPane().add(splashLabel, BorderLayout.CENTER);
        pack();

        // Position the splash screen at the center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension splashSize = getSize();
        int x = (screenSize.width - splashSize.width) / 2;
        int y = (screenSize.height - splashSize.height) / 2;
        setLocation(x, y);

        setVisible(true);
    }

    public void closeSplashScreen() {
        setVisible(false);
        dispose();
    }
}
