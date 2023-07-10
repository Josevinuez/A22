package MVC;

import javax.swing.*;
import java.awt.*;

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
                Colors.unselectedColor = selectedColor;
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
                Colors.waterColor = selectedColor;
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
                Colors.shipColor = selectedColor;
            }
        });

        colorPanel.add(unselectedButtonPanel); // Add buttonPanel to colorPanel
        colorPanel.add(waterButtonPanel); // Add waterButtonPanel to colorPanel
        colorPanel.add(shipButtonPanel);

        add(colorPanel);
    }
    public static class Colors {
        public static Color unselectedColor = Color.WHITE;
        public static Color waterColor = Color.BLUE;
        public static Color shipColor = Color.RED;
    }
}

