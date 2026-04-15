import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class AdvancedIDE extends JFrame {

    private JTree fileTree;
    private DefaultTreeModel treeModel;
    private JTabbedPane editorTabs;
    private JLabel statusBar;

    public AdvancedIDE() {
        setTitle("Advanced Java 11 IDE (No Libraries)");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        applyDarkTheme();

        setLayout(new BorderLayout());

        add(createMenuBar(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }

    private void applyDarkTheme() {
        UIManager.put("control", new Color(43, 43, 43));
        UIManager.put("info", new Color(43, 43, 43));
        UIManager.put("nimbusBase", new Color(18, 30, 49));
        UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
        UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
        UIManager.put("nimbusFocus", new Color(115,164,209));
        UIManager.put("nimbusGreen", new Color(176,179,50));
        UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
        UIManager.put("nimbusLightBackground", new Color(43, 43, 43));
        UIManager.put("nimbusOrange", new Color(191,98,4));
        UIManager.put("nimbusRed", new Color(169,46,34));
        UIManager.put("nimbusSelectedText", Color.WHITE);
        UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
        UIManager.put("text", Color.WHITE);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem open = new JMenuItem("Open Folder");
        open.addActionListener(e -> chooseDirectory());

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));

        fileMenu.add(open);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        menuBar.add(fileMenu);
        return menuBar;
    }

    private JSplitPane createMainContent() {
        fileTree = new JTree();
        fileTree.setBackground(new Color(60, 63, 65));
        fileTree.setForeground(Color.WHITE);

        fileTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) fileTree.getLastSelectedPathComponent();
            if (node == null) return;

            File file = (File) node.getUserObject();
            if (file.isFile()) {
                openFileAsync(file);
            }
        });

        JScrollPane treeScroll = new JScrollPane(fileTree);

        editorTabs = new JTabbedPane();

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                treeScroll,
                editorTabs
        );
        splitPane.setDividerLocation(300);

        return splitPane;
    }

    private JPanel createStatusBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));

        statusBar = new JLabel("Ready");
        panel.add(statusBar, BorderLayout.WEST);

        return panel;
    }

    private void chooseDirectory() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();
            loadFileTree(dir);
        }
    }

    private void loadFileTree(File root) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
        treeModel = new DefaultTreeModel(rootNode);

        buildTree(root, rootNode);

        fileTree.setModel(treeModel);
    }

    private void buildTree(File file, DefaultMutableTreeNode node) {
        File[] files = file.listFiles();
        if (files == null) return;

        for (File f : files) {
            DefaultMutableTreeNode child = new DefaultMutableTreeNode(f);
            node.add(child);

            if (f.isDirectory()) {
                buildTree(f, child);
            }
        }
    }

    private void openFileAsync(File file) {
        statusBar.setText("Loading: " + file.getName());

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() throws Exception {
                return new String(Files.readAllBytes(file.toPath()));
            }

            @Override
            protected void done() {
                try {
                    String content = get();
                    openEditorTab(file.getName(), content);
                    statusBar.setText("Opened: " + file.getName());
                } catch (Exception e) {
                    statusBar.setText("Error loading file");
                }
            }
        };

        worker.execute();
    }

    private void openEditorTab(String title, String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setBackground(new Color(43, 43, 43));
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(textArea);

        editorTabs.addTab(title, scrollPane);
        editorTabs.setSelectedComponent(scrollPane);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdvancedIDE().setVisible(true);
        });
    }
}