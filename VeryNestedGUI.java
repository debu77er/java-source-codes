import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VeryNestedGUI extends JFrame {
    public VeryNestedGUI() {
        setTitle("Bardzo Zagnieżdżony GUI");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Główny panel z BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createNorthPanel(), BorderLayout.NORTH);
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        mainPanel.add(createSouthPanel(), BorderLayout.SOUTH);
        mainPanel.add(createEastPanel(), BorderLayout.EAST);
        mainPanel.add(createWestPanel(), BorderLayout.WEST);

        setContentPane(mainPanel);
    }

    private JPanel createNorthPanel() {
        JPanel northPanel = new JPanel(new GridLayout(2, 1));
        northPanel.setBorder(BorderFactory.createTitledBorder("Pasek górny"));

        JPanel topSubPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topSubPanel.add(new JLabel("Witamy w GUI!"));
        topSubPanel.add(createButtonWithAction("Przycisk 1"));

        JPanel bottomSubPanel = new JPanel(new GridLayout(1, 2));
        bottomSubPanel.add(createButtonWithAction("Przycisk 2"));
        bottomSubPanel.add(createButtonWithAction("Przycisk 3"));

        northPanel.add(topSubPanel);
        northPanel.add(bottomSubPanel);
        return northPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(2, 2));
        centerPanel.setBorder(BorderFactory.createTitledBorder("Środek"));

        // Zagnieżdżone panele
        centerPanel.add(createNestedPanel("Panel 1", new String[]{"Opcja A", "Opcja B"}));
        centerPanel.add(createNestedPanel("Panel 2", new String[]{"Opcja C", "Opcja D"}));
        centerPanel.add(createNestedPanel("Panel 3", new String[]{"Opcja E", "Opcja F"}));
        centerPanel.add(createNestedPanel("Panel 4", new String[]{"Opcja G", "Opcja H"}));

        return centerPanel;
    }

    private JPanel createNestedPanel(String title, String[] options) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(title));

        JPanel optionsPanel = new JPanel(new GridLayout(options.length, 1));
        for (String option : options) {
            JCheckBox checkBox = new JCheckBox(option);
            checkBox.addItemListener(e -> {
                String state = e.getStateChange() == ItemEvent.SELECTED ? "zaznaczony" : "odznaczony";
                System.out.println(option + " został " + state);
            });
            optionsPanel.add(checkBox);
        }

        JTextArea textArea = new JTextArea(3, 20);
        textArea.setText("Tutaj możesz wpisać tekst...");
        textArea.setLineWrap(true);
        textArea.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                System.out.println("Wpisano: " + textArea.getText());
            }
        });

        JButton button = new JButton("Kliknij mnie");
        button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Przycisk w " + title + " został kliknięty!"));

        // Zagnieżdżone panele
        JPanel nestedPanel = new JPanel(new BorderLayout());
        nestedPanel.add(optionsPanel, BorderLayout.NORTH);
        nestedPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        nestedPanel.add(button, BorderLayout.SOUTH);

        panel.add(nestedPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btn1 = createButtonWithAction("Przycisk Dolny 1");
        JButton btn2 = createButtonWithAction("Przycisk Dolny 2");
        JButton btn3 = createButtonWithAction("Przycisk Dolny 3");
        southPanel.add(btn1);
        southPanel.add(btn2);
        southPanel.add(btn3);
        return southPanel;
    }

    private JPanel createEastPanel() {
        JPanel eastPanel = new JPanel(new GridLayout(3, 1));
        eastPanel.setBorder(BorderFactory.createTitledBorder("Pasek boczny"));

        for (int i = 1; i <= 3; i++) {
            JButton btn = createButtonWithAction("Przycisk E" + i);
            eastPanel.add(btn);
        }
        return eastPanel;
    }

    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel(new GridLayout(2, 1));
        westPanel.setBorder(BorderFactory.createTitledBorder("Panel lewy"));

        JTextField textField = new JTextField("Wpisz coś...");
        textField.addActionListener(e -> System.out.println("Wpisano: " + textField.getText()));

        JButton toggleButton = new JButton("Przełącz");
        toggleButton.addActionListener(e -> {
            boolean enabled = textField.isEnabled();
            textField.setEnabled(!enabled);
            System.out.println("Pole tekstowe jest teraz " + (enabled ? "wyłączone" : "włączone"));
        });

        westPanel.add(textField);
        westPanel.add(toggleButton);
        return westPanel;
    }

    private JButton createButtonWithAction(String text) {
        JButton button = new JButton(text);
        button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Kliknięto " + text));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VeryNestedGUI().setVisible(true);
        });
    }
}
