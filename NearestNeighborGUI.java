import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class NearestNeighborGUI extends JFrame {

    private DrawPanel drawPanel;
    private List<Point> points;
    private List<Point> path;

    public NearestNeighborGUI() {
        setTitle("Nearest Neighbor Algorithm");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        points = new ArrayList<>();
        path = new ArrayList<>();

        // Create GUI components
        drawPanel = new DrawPanel();
        JButton computeButton = new JButton("Compute Nearest Neighbor Path");
        JButton resetButton = new JButton("Reset");

        // Add action listeners
        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                points.add(e.getPoint());
                path.clear();
                drawPanel.repaint();
            }
        });

        computeButton.addActionListener(e -> {
            if (points.size() > 1) {
                computeNearestNeighborPath();
                drawPanel.repaint();
            }
        });

        resetButton.addActionListener(e -> {
            points.clear();
            path.clear();
            drawPanel.repaint();
        });

        // Layout
        JPanel controlPanel = new JPanel();
        controlPanel.add(computeButton);
        controlPanel.add(resetButton);

        add(drawPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void computeNearestNeighborPath() {
        List<Point> unvisited = new ArrayList<>(points);
        List<Point> resultPath = new ArrayList<>();

        // Start from the first point
        Point current = unvisited.remove(0);
        resultPath.add(current);

        while (!unvisited.isEmpty()) {
            Point nearest = null;
            double minDist = Double.MAX_VALUE;
            for (Point p : unvisited) {
                double dist = current.distance(p);
                if (dist < minDist) {
                    minDist = dist;
                    nearest = p;
                }
            }
            unvisited.remove(nearest);
            resultPath.add(nearest);
            current = nearest;
        }

        this.path = resultPath;
    }

    private class DrawPanel extends JPanel {
        public DrawPanel() {
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw points
            g.setColor(Color.BLUE);
            for (Point p : points) {
                g.fillOval(p.x - 5, p.y - 5, 10, 10);
            }

            // Draw path
            if (path != null && path.size() > 1) {
                g.setColor(Color.RED);
                for (int i = 0; i < path.size() - 1; i++) {
                    Point p1 = path.get(i);
                    Point p2 = path.get(i + 1);
                    g.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NearestNeighborGUI gui = new NearestNeighborGUI();
            gui.setVisible(true);
        });
    }
}
