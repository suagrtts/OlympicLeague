import javax.swing.*;
import java.awt.*;

//AnimatedBar: HP and MP bars with value label
public class AnimatedBar extends JPanel {

    private final Color fillColor;
    private final Color bgColor = new Color(0x20, 0x20, 0x30);
    private final String label;

    private int current, max, displayed;
    private Timer animTimer;

    public AnimatedBar(String label, Color fillColor, int max) {
        this.label     = label;
        this.fillColor = fillColor;
        this.max       = max;
        this.current   = max;
        this.displayed = max;

        setOpaque(false);
        setPreferredSize(new Dimension(200, 22));
    }

    public void setTarget(int current, int max) {
        this.current = current;
        this.max     = max;
        if (animTimer != null) animTimer.stop();
        animTimer = new Timer(16, e -> {
            if (displayed == current) { ((Timer) e.getSource()).stop(); return; }
            displayed += (current > displayed) ? 1 : -1;
            // fast-forward big jumps
            int diff = Math.abs(displayed - current);
            if (diff > 20) displayed += (current > displayed) ? diff / 4 : -diff / 4;
            repaint();
        });
        animTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();
        int barH = h - 10, barY = 10;

        // Label
        g.setFont(Theme.FONT_SMALL);
        g.setColor(Theme.TEXT_DIM);
        g.drawString(label, 0, barY - 1);

        // Background
        g.setColor(bgColor);
        g.fillRoundRect(0, barY, w, barH, 4, 4);

        // Fill
        if (max > 0) {
            int fillW = (int)((double) displayed / max * w);
            fillW = Math.max(0, Math.min(fillW, w));
            g.setColor(fillColor);
            g.fillRoundRect(0, barY, fillW, barH, 4, 4);
        }

        // Border
        g.setColor(Theme.alpha(fillColor, 80));
        g.drawRoundRect(0, barY, w - 1, barH - 1, 4, 4);

        // Value text
        String val = displayed + "/" + max;
        g.setFont(Theme.FONT_SMALL);
        FontMetrics fm = g.getFontMetrics();
        int tx = (w - fm.stringWidth(val)) / 2;
        g.setColor(new Color(0, 0, 0, 120));
        g.drawString(val, tx + 1, barY + barH - 3);
        g.setColor(Color.WHITE);
        g.drawString(val, tx, barY + barH - 4);

        g.dispose();
    }
}
