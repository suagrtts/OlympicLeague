import java.awt.*;
import javax.swing.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Liga Olympica");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea titleArea = new JTextArea();

        titleArea.setFont(new Font("Consolas", Font.PLAIN, 16));

        titleArea.setEditable(false);
        titleArea.setBackground(Color.BLACK);
        titleArea.setForeground(Color.GREEN);

        titleArea.setLineWrap(false);
        titleArea.setWrapStyleWord(false);

        titleArea.setText(getTitleText());

        add(new JScrollPane(titleArea));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameWindow frame = new GameWindow();
            frame.setVisible(true);
        });
    }

    private String getTitleText() {
        String[] titleLines = {
            "     ╔══════════════════════════════════════════════════════════════════════════════════╗",
            "     ║                                                                                  ║",
            "     ║                      ██╗     ██╗ ██████╗  █████╗                                 ║",
            "     ║                      ██║     ██║██╔════╝ ██╔══██╗                                ║",
            "     ║                      ██║     ██║██║  ███╗███████║                                ║",
            "     ║                      ██║     ██║██║   ██║██╔══██║                                ║",
            "     ║                      ███████╗██║╚██████╔╝██║  ██║                                ║",
            "     ║                      ╚══════╝╚═╝ ╚═════╝ ╚═╝  ╚═╝                                ║",
            "     ║                                                                                  ║",
            "     ║          ██████╗ ██╗     ██╗   ██╗███╗   ███╗██████╗ ██╗ ██████╗ █████╗          ║",
            "     ║         ██╔═══██╗██║     ╚██╗ ██╔╝████╗ ████║██╔══██╗██║██╔════╝██╔══██╗         ║",
            "     ║         ██║   ██║██║      ╚████╔╝ ██╔████╔██║██████╔╝██║██║     ███████║         ║",
            "     ║         ██║   ██║██║       ╚██╔╝  ██║╚██╔╝██║██╔═══╝ ██║██║     ██╔══██║         ║",
            "     ║         ╚██████╔╝███████╗   ██║   ██║ ╚═╝ ██║██║     ██║╚██████╗██║  ██║         ║",
            "     ║              ╚═════╝ ╚══════╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝ ╚═╝╚═╝  ╚═╝         ║",
            "     ║                                                                                  ║",
            "     ╚══════════════════════════════════════════════════════════════════════════════════╝"
        };

        StringBuilder sb = new StringBuilder();
        for (String line : titleLines) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}