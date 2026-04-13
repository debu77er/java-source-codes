import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimplePiano extends JFrame {

    private final int[] WHITE_KEYS = {60, 62, 64, 65, 67, 69, 71, 72}; // MIDI notes for C4 to C5
    private final int[] BLACK_KEYS = {61, 63, 66, 68, 70}; // C#4, D#4, F#4, G#4, A#4
    private Synthesizer synthesizer;
    private MidiChannel channel;

    public SimplePiano() {
        setTitle("Simple Java Piano");
        setSize(700, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Use absolute positioning

        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channel = synthesizer.getChannels()[0];
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "MIDI system is unavailable");
            System.exit(1);
        }

        // Create white keys
        int whiteKeyWidth = 60;
        int whiteKeyHeight = 200;
        for (int i = 0; i < WHITE_KEYS.length; i++) {
            int note = WHITE_KEYS[i];
            JButton key = new JButton();
            key.setBackground(Color.WHITE);
            key.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            key.setBounds(i * whiteKeyWidth, 0, whiteKeyWidth, whiteKeyHeight);
            final int midiNote = note;
            key.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    playNote(midiNote);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    stopNote(midiNote);
                }
            });
            add(key);
        }

        // Create black keys
        int blackKeyWidth = 40;
        int blackKeyHeight = 120;
        // Position black keys over the white keys
        int[] blackKeyOffsets = {45, 105, 225, 285, 345}; // positions relative to white keys
        for (int i = 0; i < BLACK_KEYS.length; i++) {
            int note = BLACK_KEYS[i];
            JButton blackKey = new JButton();
            blackKey.setBackground(Color.BLACK);
            blackKey.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            // Position black keys between white keys
            int x = blackKeyOffsets[i];
            blackKey.setBounds(x, 0, blackKeyWidth, blackKeyHeight);
            final int midiNote = note;
            blackKey.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    playNote(midiNote);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    stopNote(midiNote);
                }
            });
            add(blackKey);
        }

        setVisible(true);
    }

    private void playNote(int midiNote) {
        channel.noteOn(midiNote, 600); // velocity 600
    }

    private void stopNote(int midiNote) {
        channel.noteOff(midiNote);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimplePiano());
    }
}
