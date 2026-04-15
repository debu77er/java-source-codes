import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class PolygonDiagonalsApp extends JFrame {

    private JTextField inputField;
    private DrawPanel drawPanel;

    public PolygonDiagonalsApp() {
        setTitle("Regular Polygon with Diagonals");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JLabel label = new JLabel("Number of edges:");
        inputField = new JTextField(5);
        JButton drawButton = new JButton("Draw");

        topPanel.add(label);
        topPanel.add(inputField);
        topPanel.add(drawButton);

        add(topPanel, BorderLayout.NORTH);

        drawPanel = new DrawPanel();
        add(drawPanel, BorderLayout.CENTER);

        drawButton.addActionListener((ActionEvent e) -> {
            try {
                int n = Integer.parseInt(inputField.getText());
                if (n < 3) {
                    JOptionPane.showMessageDialog(this, "Polygon must have at least 3 edges.");
                    return;
                }
                drawPanel.setSides(n);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid number.");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PolygonDiagonalsApp app = new PolygonDiagonalsApp();
            app.setVisible(true);
        });
    }
}

// ================= DRAW PANEL =================

class DrawPanel extends JPanel {

    private int sides = 0;

    public void setSides(int sides) {
        this.sides = sides;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (sides < 3) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int radius = Math.min(width, height) / 3;
        int centerX = width / 2;
        int centerY = height / 2;

        List<Point2D> vertices = calculateVertices(centerX, centerY, radius, sides);

        // Draw diagonals first (so polygon edges appear on top)
        g2d.setColor(Color.LIGHT_GRAY);
        drawDiagonals(g2d, vertices);

        // Draw polygon edges
        g2d.setColor(Color.BLACK);
        drawPolygon(g2d, vertices);
    }

    private List<Point2D> calculateVertices(int cx, int cy, int r, int n) {
        List<Point2D> points = new ArrayList<>();

        double angleStep = 2 * Math.PI / n;
        double startAngle = -Math.PI / 2; // start from top

        for (int i = 0; i < n; i++) {
            double angle = startAngle + i * angleStep;
            double x = cx + r * Math.cos(angle);
            double y = cy + r * Math.sin(angle);
            points.add(new Point2D.Double(x, y));
        }

        return points;
    }

    private void drawPolygon(Graphics2D g2d, List<Point2D> vertices) {
        int n = vertices.size();
        for (int i = 0; i < n; i++) {
            Point2D p1 = vertices.get(i);
            Point2D p2 = vertices.get((i + 1) % n);
            g2d.drawLine((int) p1.getX(), (int) p1.getY(),
                         (int) p2.getX(), (int) p2.getY());
        }
    }

    private void drawDiagonals(Graphics2D g2d, List<Point2D> vertices) {
        int n = vertices.size();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                // Skip edges and same points
                if (j == i) continue;
                if (j == (i + 1) % n) continue;
                if (i == (j + 1) % n) continue;

                Point2D p1 = vertices.get(i);
                Point2D p2 = vertices.get(j);

                g2d.drawLine((int) p1.getX(), (int) p1.getY(),
                             (int) p2.getX(), (int) p2.getY());
            }
        }
    }
}