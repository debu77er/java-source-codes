import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class TaskManagerApp extends JFrame {

    // Main components
    private TaskTableModel tableModel;
    private JTable taskTable;
    private JButton addButton, removeButton, startButton, pauseButton, stopButton;
    private JTextField taskNameField;

    public TaskManagerApp() {
        setTitle("Advanced Task Manager");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
        layoutComponents();
        initListeners();
    }

    private void initComponents() {
        tableModel = new TaskTableModel();
        taskTable = new JTable(tableModel);
        taskTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        addButton = new JButton("Add Task");
        removeButton = new JButton("Remove Task");
        startButton = new JButton("Start");
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");

        taskNameField = new JTextField(20);
    }

    private void layoutComponents() {
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Task Name:"));
        topPanel.add(taskNameField);
        topPanel.add(addButton);

        JPanel controlPanel = new JPanel();
        controlPanel.add(startButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(removeButton);

        JScrollPane tableScrollPane = new JScrollPane(taskTable);

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void initListeners() {
        // Add Task
        addButton.addActionListener(e -> {
            String taskName = taskNameField.getText().trim();
            if (!taskName.isEmpty()) {
                Task task = new Task(taskName);
                tableModel.addTask(task);
                taskNameField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a task name.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Remove Task
        removeButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                Task task = tableModel.getTaskAt(selectedRow);
                task.stop(); // stop thread if running
                tableModel.removeTask(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to remove.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Start Task
        startButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                Task task = tableModel.getTaskAt(selectedRow);
                task.start();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to start.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Pause Task
        pauseButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                Task task = tableModel.getTaskAt(selectedRow);
                task.pause();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to pause.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Stop Task
        stopButton.addActionListener(e -> {
            int selectedRow = taskTable.getSelectedRow();
            if (selectedRow != -1) {
                Task task = tableModel.getTaskAt(selectedRow);
                task.stop();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a task to stop.", "Selection Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TaskManagerApp().setVisible(true);
        });
    }

    // Custom Table Model for Tasks
    class TaskTableModel extends AbstractTableModel {
        private final String[] columnNames = { "Task Name", "Progress", "Status" };
        private final List<Task> tasks = new ArrayList<>();

        public void addTask(Task task) {
            tasks.add(task);
            fireTableRowsInserted(tasks.size() - 1, tasks.size() - 1);
        }

        public void removeTask(int index) {
            if (index >= 0 && index < tasks.size()) {
                tasks.remove(index);
                fireTableRowsDeleted(index, index);
            }
        }

        public Task getTaskAt(int index) {
            return tasks.get(index);
        }

        @Override
        public int getRowCount() {
            return tasks.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Task task = tasks.get(rowIndex);
            switch (columnIndex) {
                case 0: return task.getName();
                case 1: return task.getProgress() + "%";
                case 2: return task.getStatus();
                default: return "";
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 1) return String.class;
            return String.class;
        }
    }

    // Task class representing individual tasks
    class Task {
        private final String name;
        private int progress = 0;
        private String status = "Pending";

        private Thread thread;
        private volatile boolean paused = false;
        private volatile boolean stopped = false;

        public Task(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public synchronized int getProgress() {
            return progress;
        }

        public synchronized String getStatus() {
            return status;
        }

        private synchronized void setProgress(int progress) {
            this.progress = progress;
        }

        private synchronized void setStatus(String status) {
            this.status = status;
        }

        public void start() {
            if (thread != null && thread.isAlive()) {
                // Already running
                if (paused) {
                    resume();
                }
                return;
            }
            stopped = false;
            paused = false;
            setStatus("Running");
            thread = new Thread(() -> runTask());
            thread.start();
        }

        public void pause() {
            if (thread != null && thread.isAlive() && !paused) {
                paused = true;
                setStatus("Paused");
            }
        }

        public void resume() {
            if (paused) {
                paused = false;
                setStatus("Running");
                synchronized (this) {
                    notify();
                }
            }
        }

        public void stop() {
            if (thread != null && thread.isAlive()) {
                stopped = true;
                paused = false;
                setStatus("Stopped");
                synchronized (this) {
                    notify();
                }
            }
        }

        private void runTask() {
            while (progress < 100 && !stopped) {
                // Handle pause
                synchronized (this) {
                    while (paused) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                // Simulate work
                try {
                    Thread.sleep(100); // simulate task progress
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                setProgress(progress + 1);
                // Update GUI
                SwingUtilities.invokeLater(() -> tableModel.fireTableDataChanged());
            }
            if (stopped) {
                setStatus("Stopped");
            } else {
                setStatus("Completed");
            }
            SwingUtilities.invokeLater(() -> tableModel.fireTableDataChanged());
        }
    }
}
