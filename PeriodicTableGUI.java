import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Define a class for Element data
class Element {
    String name;
    String symbol;
    int atomicNumber;

    public Element(String name, String symbol, int atomicNumber) {
        this.name = name;
        this.symbol = symbol;
        this.atomicNumber = atomicNumber;
    }
}

public class PeriodicTableGUI extends JFrame {

    // Sample data for some elements
    private Element[] elements = {
            new Element("Hydrogen", "H", 1),
            new Element("Helium", "He", 2),
            new Element("Lithium", "Li", 3),
            new Element("Beryllium", "Be", 4),
            new Element("Boron", "B", 5),
            new Element("Carbon", "C", 6),
            new Element("Nitrogen", "N", 7),
            new Element("Oxygen", "O", 8),
            new Element("Fluorine", "F", 9),
            new Element("Neon", "Ne", 10),
            // Add more elements as needed
    };

    private JTextArea infoArea;

    public PeriodicTableGUI() {
        setTitle("Interactive Periodic Table");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Main layout
        setLayout(new BorderLayout());

        // Panel for elements
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayout(0, 10, 5, 5)); // 10 columns for demonstration

        // Add buttons for elements
        for (Element element : elements) {
            JButton btn = new JButton(element.symbol);
            btn.setToolTipText(element.name);
            btn.setBackground(Color.WHITE);
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    displayElementInfo(element);
                }
            });
            tablePanel.add(btn);
        }

        // Text area for displaying info
        infoArea = new JTextArea(5, 20);
        infoArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(infoArea);

        // Add components to frame
        add(new JLabel("Periodic Table (Sample)"), BorderLayout.NORTH);
        add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }

    private void displayElementInfo(Element element) {
        String info = "Name: " + element.name + "\n" +
                      "Symbol: " + element.symbol + "\n" +
                      "Atomic Number: " + element.atomicNumber;
        infoArea.setText(info);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PeriodicTableGUI().setVisible(true);
        });
    }
}
