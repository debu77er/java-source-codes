
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BallisticCurveFlightGenerator extends JFrame {
    private JTextField velocityField;
    private JTextField angleField;
    private JTextArea resultArea;

    public BallisticCurveFlightGenerator() {
        setTitle("Ballistic Curve Flight Generator");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel velocityLabel = new JLabel("Enter Velocity (m/s):");
        velocityField = new JTextField(10);
        JLabel angleLabel = new JLabel("Enter Angle (degrees):");
        angleField = new JTextField(10);
        JButton calculateButton = new JButton("Calculate");
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTrajectory();
            }
        });

        add(velocityLabel);
        add(velocityField);
        add(angleLabel);
        add(angleField);
        add(calculateButton);
        add(new JScrollPane(resultArea));
    }

    private void calculateTrajectory() {
        double velocity = Double.parseDouble(velocityField.getText());
        double angle = Double.parseDouble(angleField.getText());
        double radians = Math.toRadians(angle);
        double timeOfFlight = (2 * velocity * Math.sin(radians)) / 9.81;
        double range = (velocity * Math.cos(radians)) * timeOfFlight;

        resultArea.setText("Time of Flight: " + timeOfFlight + " seconds\n");
        resultArea.append("Range: " + range + " meters\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BallisticCurveFlightGenerator frame = new BallisticCurveFlightGenerator();
            frame.setVisible(true);
        });
    }
}
