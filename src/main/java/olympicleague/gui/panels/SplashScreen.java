package olympicleague.gui.panels;

import olympicleague.assets.SpriteLoader;
import olympicleague.assets.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SplashScreen extends JWindow {

    public SplashScreen() {
        setSize(700, 420);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout()) {
            private BufferedImage bg = SpriteLoader.getBackground(0, 700, 420);

            @Override
            protected void paintComponent(Graphics g0) {
                super.paintComponent(g0);
                Graphics2D g = (Graphics2D) g0.create();
                if (bg != null) {
                    g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                    g.setColor(new Color(0, 0, 0, 140));
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g.setColor(Theme.BG_DEEP);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                g.dispose();
            }
        };
        content.setOpaque(false);
        content.setBorder(BorderFactory.createLineBorder(Theme.GOLD, 2));

        // Center text
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel title = new JLabel("LIGA OLYMPICA", JLabel.CENTER);
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 52));
        title.setForeground(Theme.GOLD);

        JLabel sub = new JLabel("Only ONE shall claim the throne", JLabel.CENTER);
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_DIM);

        JLabel loading = new JLabel("Loading...", JLabel.CENTER);
        loading.setFont(Theme.FONT_MONO);
        loading.setForeground(Theme.GOLD_DIM);

        center.add(title,   gbc);
        center.add(sub,     gbc);
        center.add(loading, gbc);

        content.add(center, BorderLayout.CENTER);
        setContentPane(content);
    }

    /** Show splash for the given duration in milliseconds, then dispose. */
    public static void show(int durationMs) {
        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);
        Timer timer = new Timer(durationMs, e -> splash.dispose());
        timer.setRepeats(false);
        timer.start();
    }
}
