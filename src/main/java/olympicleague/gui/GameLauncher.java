package olympicleague.gui;

import javax.swing.SwingUtilities;

public class GameLauncher {
    public static void main(String[] args) {
        // Show splash screen for 2 seconds
        SplashScreen.show(2000);

        // Wait for splash then launch main window
        javax.swing.Timer launchTimer = new javax.swing.Timer(2100, e -> {
            SwingUtilities.invokeLater(GameWindow::new);
        });
        launchTimer.setRepeats(false);
        launchTimer.start();
    }
}
