package gui;

import java.awt.*;
import javax.swing.*;

public class SplashScreen extends JWindow {

    public static void show(Runnable onDone) {
        SplashScreen s = new SplashScreen();
        s.setVisible(true);
        Timer t = new Timer(1800, e -> {
            s.setVisible(false);
            s.dispose();
            onDone.run();
        });
        t.setRepeats(false);
        t.start();
    }

    private SplashScreen() {
        setSize(480, 280);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel() {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                g.setColor(Theme.BG_DEEP);
                g.fillRect(0,0,getWidth(),getHeight());
                g.setFont(new Font("Serif", Font.BOLD|Font.ITALIC, 64));
                g.setColor(Theme.GOLD);
                String t = "LIGA";
                FontMetrics fm = g.getFontMetrics();
                g.drawString(t, (getWidth()-fm.stringWidth(t))/2, 140);
                g.setFont(new Font("Serif", Font.BOLD, 28));
                g.setColor(Theme.GOLD_DIM);
                String t2 = "OLYMPICA";
                fm = g.getFontMetrics();
                g.drawString(t2, (getWidth()-fm.stringWidth(t2))/2, 185);
                g.dispose();
            }
        };
        setContentPane(panel);
    }
}
