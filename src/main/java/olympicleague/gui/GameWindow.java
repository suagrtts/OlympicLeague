import java.awt.*;
import javax.swing.*;

public class GameWindow extends JFrame {

    private JTextArea textArea;

    public GameWindow() {
        setTitle("Liga Olympica");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setFont(new Font("Consolas", Font.PLAIN, 16));
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.GREEN);
        textArea.setLineWrap(false);

        add(new JScrollPane(textArea));

        SwingUtilities.invokeLater(this::startAnimation);
    }

    private void startAnimation() {
        textArea.setText(getTitleText() + "\n");
        new Timer(300, new OlympicAnimation()).start();
    }

    class OlympicAnimation implements java.awt.event.ActionListener {
        String[] ringLines = {
            "                         ████      ████      ████",
            "                       ██    ██  ██    ██  ██    ██",
            "                       ██    ████    ████    ████",
            "                         ████  ██  ██  ██  ██  ████",
            "                             ██████████████████    ██",
            "                               ████      ████      ██",
            "                                            ██████████"
        };

        int index = 0;

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (index < ringLines.length) {
                textArea.append(ringLines[index] + "\n");
                index++;
            } else {
                ((Timer) e.getSource()).stop();
                new Timer(300, new WarriorAnimation()).start();
            }
        }
    }

    class WarriorAnimation implements java.awt.event.ActionListener {

        String[] warriorArt = {
            "\n                                THE ARENA AWAITS",
            "",
            "                                  /\\    /\\",
            "                                 /  \\__/  \\",
            "                                |   o  o   |",
            "                                |     >    |",
            "                                 \\  \\__/  /",
            "                                  |____|",
            "                               ___/|  |\\___",
            "                              /   /|  |\\   \\",
            "                             |___| |  | |___|",
            "                                  ||  ||",
            "                                  ||  ||",
            "                                 _||  ||_",
            "                                |__________|"
        };

        int index = 0;

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (index < warriorArt.length) {
                textArea.append(warriorArt[index] + "\n");
                index++;
            } else {
                ((Timer) e.getSource()).stop();
            }
        }
    }

    // TITLE TEXT
    private String getTitleText() {
        return """
     ╔══════════════════════════════════════════════════════════════════════════════════╗
     ║                                                                                  ║
     ║                      ██╗     ██╗ ██████╗  █████╗                                 ║
     ║                      ██║     ██║██╔════╝ ██╔══██╗                                ║
     ║                      ██║     ██║██║  ███╗███████║                                ║
     ║                      ██║     ██║██║   ██║██╔══██║                                ║
     ║                      ███████╗██║╚██████╔╝██║  ██║                                ║
     ║                      ╚══════╝╚═╝ ╚═════╝ ╚═╝  ╚═╝                                ║
     ║                                                                                  ║
     ║          ██████╗ ██╗     ██╗   ██╗███╗   ███╗██████╗ ██╗ ██████╗ █████╗          ║
     ║         ██╔═══██╗██║     ╚██╗ ██╔╝████╗ ████║██╔══██╗██║██╔════╝██╔══██╗         ║
     ║         ██║   ██║██║      ╚████╔╝ ██╔████╔██║██████╔╝██║██║     ███████║         ║
     ║         ██║   ██║██║       ╚██╔╝  ██║╚██╔╝██║██╔═══╝ ██║██║     ██╔══██║         ║
     ║         ╚██████╔╝███████╗   ██║   ██║ ╚═╝ ██║██║     ██║╚██████╗██║  ██║         ║
     ║              ╚═════╝ ╚══════╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝ ╚═╝╚═╝  ╚═╝         ║
     ║                                                                                  ║
     ╚══════════════════════════════════════════════════════════════════════════════════╝
    """;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow frame = new GameWindow();
            frame.setVisible(true);
        });
    }
}