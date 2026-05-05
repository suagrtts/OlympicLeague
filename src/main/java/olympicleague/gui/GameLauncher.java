package gui;

import javax.swing.SwingUtilities;

public class GameLauncher {
    public static void launch() {
        SwingUtilities.invokeLater(GameWindow::new);
    }
}
