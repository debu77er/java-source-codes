import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A beautiful Java 11 GUI example demonstrating custom styling and layout.
 */
public class BeautifulGuiExample extends JFrame {

    public BeautifulGuiExample() {
        setTitle("Beautiful Java 11 GUI");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new GradientBackgroundPanel());

        // Use BorderLayout for main content
        setLayout(new BorderLayout(20, 20));

        // Create a header label
        JLabel headerLabel = new JLabel("Welcome to Your Beautiful App!", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(headerLabel, BorderLayout.NORTH);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Create styled buttons
        JButton primaryButton = createStyledButton("Get Started");
        JButton secondaryButton = createStyledButton("Learn More");

        // Add action listeners
        primaryButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Getting Started!"));
        secondaryButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Learn more about the app."));

        // Add buttons to panel
        buttonPanel.add(primaryButton);
        buttonPanel.add(secondaryButton);

        add(buttonPanel, BorderLayout.CENTER);
    }

    /**
     * Creates a styled JButton with custom colors and font.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 130, 180)); // Steel Blue
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237)); // Cornflower Blue
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });

        return button;
    }

    /**
     * Custom panel with gradient background.
     */
    class GradientBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Create gradient from top to bottom
            Color color1 = new Color(58, 123, 213);
            Color color2 = new Color(58, 96, 115);
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        // Ensure GUI creation is on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            BeautifulGuiExample app = new BeautifulGuiExample();
            app.setVisible(true);
        });
    }
}
