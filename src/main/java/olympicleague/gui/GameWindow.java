import java.awt.*;
import javax.swing.*;

public class GameWindow extends JFrame {

    public GameWindow() {
        setTitle("Liga Olympica");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea titleArea = new JTextArea();
        titleArea.setFont(new Font("Monospaced", Font.BOLD, 12));
        titleArea.setEditable(false);
        titleArea.setBackground(Color.BLACK);
        titleArea.setForeground(Color.GREEN);

        // NOW using the new OOP class
        titleArea.setText(TitleBanner.getTitleText());

        add(new JScrollPane(titleArea));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameWindow().setVisible(true);
        });
    }
}