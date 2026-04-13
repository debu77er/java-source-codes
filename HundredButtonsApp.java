import javax.swing.*;
import java.awt.*;

public class HundredButtonsApp {

    public static void main(String[] args) {
        // Ensure GUI creation runs on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("100 Buttons GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800); // Adjust size as needed

        // Use a grid layout with 10 rows and 10 columns
        int rows = 10;
        int cols = 10;
        JPanel panel = new JPanel(new GridLayout(rows, cols, 5, 5)); // 5px gaps

        // Add 100 buttons
        for (int i = 1; i <= 100; i++) {
            JButton button = new JButton(String.valueOf(i));
            // Optional: add action listener to handle button clicks
            button.addActionListener(e -> {
                JOptionPane.showMessageDialog(frame, "Button " + button.getText() + " clicked!");
            });
            panel.add(button);
        }

        frame.add(new JScrollPane(panel)); // Add scroll pane in case window is small
        frame.setLocationRelativeTo(null); // Center the window
        frame.setVisible(true);
    }
}
