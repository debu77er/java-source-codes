import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Color;

public class SimplePaint extends JFrame {

    private DrawArea drawArea;
    public Color currentColor = Color.BLACK;

    public SimplePaint() {
        setTitle("Simple MSPaint");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Initialize drawing area
        drawArea = new DrawArea();
        add(drawArea, BorderLayout.CENTER);

        // Create control panel with color buttons and clear button
        JPanel controls = new JPanel();

        // Add color buttons
        String[] colors = {"Black", "Red", "Green", "Blue", "Yellow", "Orange", "Pink"};
        for (String colorName : colors) {
            JButton btn = new JButton(colorName);
            btn.setBackground(getColorByName(colorName));
            btn.setForeground(Color.WHITE);
            btn.addActionListener(e -> {
                currentColor = getColorByName(colorName);
                drawArea.setCurrentColor(currentColor);
            });
            controls.add(btn);
        }

        // Clear button
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> drawArea.clear());
        controls.add(clearBtn);

        add(controls, BorderLayout.NORTH);
    }

    private Color getColorByName(String name) {
        switch (name.toLowerCase()) {
            case "black": return Color.BLACK;
            case "red": return Color.RED;
            case "green": return Color.GREEN;
            case "blue": return Color.BLUE;
            case "yellow": return Color.YELLOW;
            case "orange": return Color.ORANGE;
            case "pink": return Color.PINK;
            default: return Color.BLACK;
        }
    }

    // Inner class for drawing area
    class DrawArea extends JPanel {
        private Image image;
        private Graphics2D g2;
        private int prevX, prevY;

        public DrawArea() {
            setDoubleBuffered(false);
            setBackground(Color.WHITE);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    prevX = e.getX();
                    prevY = e.getY();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int currX = e.getX();
                    int currY = e.getY();
                    if (g2 != null) {
                        g2.setColor(currentColor);
                        g2.setStroke(new BasicStroke(2));
                        g2.drawLine(prevX, prevY, currX, currY);
                        repaint();
                        prevX = currX;
                        prevY = currY;
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image == null) {
                // Create the drawing buffer
                image = createImage(getWidth(), getHeight());
                g2 = (Graphics2D) image.getGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                clear();
            }
            g.drawImage(image, 0, 0, null);
        }

        public void clear() {
            if (g2 != null) {
                g2.setPaint(Color.WHITE);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setPaint(currentColor);
                repaint();
            }
        }

        public void setCurrentColor(Color color) {
            this.currentColor = color;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimplePaint paint = new SimplePaint();
            paint.setVisible(true);
        });
    }
}
