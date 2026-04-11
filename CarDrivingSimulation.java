import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

public class CarDrivingSimulation extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private int carX, carY; // Car position (center point)
    private final int carWidth = 50, carHeight = 100; // Car size
    private double speed = 0; // Current speed
    private double acceleration = 0.2; // Acceleration rate
    private final double maxSpeed = 10;
    private double steeringAngle = 0; // In degrees
    private final double maxSteeringAngle = 30; // Max steering angle
    private double steeringSpeed = 2; // How fast the steering angle changes
    private boolean accelerating = false;
    private boolean decelerating = false;
    private boolean turningLeft = false;
    private boolean turningRight = false;
    private boolean paused = false;

    private List<Rectangle> obstacles; // Obstacles on the road
    private Image backgroundImage;

    public CarDrivingSimulation() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.GRAY);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Initialize car position at bottom center
        carX = 400;
        carY = 500;

        // Load background image if available
        // backgroundImage = new ImageIcon("background.jpg").getImage();

        // Initialize obstacles
        obstacles = new ArrayList<>();
        obstacles.add(new Rectangle(300, 300, 50, 50));
        obstacles.add(new Rectangle(500, 200, 50, 50));
        obstacles.add(new Rectangle(400, 150, 50, 50));

        // Set up timer for animation
        timer = new Timer(30, this); // roughly 33 FPS
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(new Color(34, 139, 34)); // Greenish background for scenery
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Draw road
        g.setColor(Color.DARK_GRAY);
        g.fillRect(100, 0, 600, getHeight());

        // Draw lane lines
        g.setColor(Color.WHITE);
        for (int y = 0; y < getHeight(); y += 40) {
            g.fillRect(395, y, 10, 20);
        }

        // Draw obstacles
        g.setColor(Color.BLACK);
        for (Rectangle obstacle : obstacles) {
            g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        // Draw car with rotation
        Graphics2D g2d = (Graphics2D) g.create();
        AffineTransform transform = new AffineTransform();

        // Position the car's center
        int centerX = carX + carWidth / 2;
        int centerY = carY + carHeight / 2;

        // Apply transformations: translate to center, rotate, translate back
        transform.translate(centerX, centerY);
        transform.rotate(Math.toRadians(steeringAngle));
        transform.translate(-carWidth / 2, -carHeight / 2);
        g2d.setTransform(transform);

        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, carWidth, carHeight);

        // Draw a simple indicator for direction
        g2d.setColor(Color.BLACK);
        g2d.drawString("Speed: " + String.format("%.1f", speed), -50, -10);
        g2d.drawString("Use Arrow Keys to control", -50, 10);

        g2d.dispose();

        // Draw pause overlay if paused
        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Paused", getWidth() / 2 - 80, getHeight() / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (paused) return;

        // Update speed based on acceleration
        if (accelerating) {
            speed += acceleration;
        } else if (decelerating) {
            speed -= acceleration;
        }
        speed = Math.max(0, Math.min(speed, maxSpeed));

        // Update steering angle
        if (turningLeft) {
            steeringAngle -= steeringSpeed;
        } else if (turningRight) {
            steeringAngle += steeringSpeed;
        } else {
            // Gradually straighten the steering
            if (steeringAngle > 0) {
                steeringAngle -= steeringSpeed;
                if (steeringAngle < 0) steeringAngle = 0;
            } else if (steeringAngle < 0) {
                steeringAngle += steeringSpeed;
                if (steeringAngle > 0) steeringAngle = 0;
            }
        }
        // Clamp steering angle
        steeringAngle = Math.max(-maxSteeringAngle, Math.min(maxSteeringAngle, steeringAngle));

        // Update car position based on speed and steering
        // The car moves forward (up) with current speed
        double radians = Math.toRadians(steeringAngle);
        int deltaX = (int) (Math.sin(radians) * speed * -1); // negative because UI y-axis increases downward
        int deltaY = (int) (Math.cos(radians) * speed * -1);

        // Apply movement
        carX += deltaX;
        carY += deltaY;

        // Keep within bounds
        if (carX < 100) carX = 100;
        if (carX + carWidth > 700) carX = 700 - carWidth;
        if (carY < 0) carY = 0;
        if (carY + carHeight > getHeight()) carY = getHeight() - carHeight;

        // Check collisions with obstacles
        Rectangle carRect = new Rectangle(carX, carY, carWidth, carHeight);
        for (Rectangle obstacle : obstacles) {
            if (carRect.intersects(obstacle)) {
                // Collision detected - stop the car
                speed = 0;
                // Optional: add collision response like bounce back
                // For simplicity, just stop
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                accelerating = true;
                break;
            case KeyEvent.VK_DOWN:
                decelerating = true;
                break;
            case KeyEvent.VK_LEFT:
                turningLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                turningRight = true;
                break;
            case KeyEvent.VK_P:
                paused = !paused; // toggle pause
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                accelerating = false;
                break;
            case KeyEvent.VK_DOWN:
                decelerating = false;
                break;
            case KeyEvent.VK_LEFT:
                turningLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                turningRight = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Enhanced Car Driving Simulation");
        CarDrivingSimulation panel = new CarDrivingSimulation();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
