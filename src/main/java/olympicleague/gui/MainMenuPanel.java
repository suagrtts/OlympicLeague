package olympicleague.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainMenuPanel extends JPanel {

    private final Runnable onPvP, onPvE, onArcade, onRoster;
    private BufferedImage bgImage;

    public MainMenuPanel(Runnable onPvP, Runnable onPvE, Runnable onArcade, Runnable onRoster) {
        this.onPvP    = onPvP;
        this.onPvE    = onPvE;
        this.onArcade = onArcade;
        this.onRoster = onRoster;

        // Load a random background from the background package
        int bgIdx = (int)(Math.random() * 4);
        bgImage = SpriteLoader.getBackground(bgIdx, 940, 720);

        setLayout(new BorderLayout());
        setBackground(Theme.BG_DEEP);
        buildUI();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        if (bgImage != null) {
            Graphics2D g = (Graphics2D) g0.create();
            // Draw background with darkening overlay
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
            g.setColor(new Color(0, 0, 0, 160));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.dispose();
        }
    }

    private void buildUI() {
        // ── Title area ─────────────────────────────────────────────────────
        JPanel titleArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g0) {
                super.paintComponent(g0);
                setOpaque(false);
                Graphics2D g = (Graphics2D) g0.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // "LIGA" — big golden gradient
                g.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 72));
                String liga = "LIGA";
                FontMetrics fm = g.getFontMetrics();
                int ligaX = (w - fm.stringWidth(liga)) / 2;
                int ligaY = h / 2 - 20;

                // Shadow
                g.setColor(new Color(0, 0, 0, 140));
                g.drawString(liga, ligaX + 3, ligaY + 3);

                // Gold gradient fill
                GradientPaint gp1 = new GradientPaint(ligaX, ligaY - 60, Theme.GOLD_LIGHT, ligaX, ligaY + 10, Theme.GOLD_DIM);
                g.setPaint(gp1);
                g.drawString(liga, ligaX, ligaY);

                // "OLYMPICA"
                g.setFont(new Font("Serif", Font.BOLD, 40));
                String sub = "OLYMPICA";
                fm = g.getFontMetrics();
                int subX = (w - fm.stringWidth(sub)) / 2;
                int subY = ligaY + 52;

                g.setColor(new Color(0, 0, 0, 120));
                g.drawString(sub, subX + 2, subY + 2);
                GradientPaint gp2 = new GradientPaint(subX, subY - 30, Theme.GOLD, subX, subY + 10, Theme.GOLD_DIM);
                g.setPaint(gp2);
                g.drawString(sub, subX, subY);

                // Decorative line under title
                g.setColor(Theme.alpha(Theme.GOLD, 120));
                g.setStroke(new BasicStroke(1.5f));
                int lineY = subY + 14;
                g.drawLine(w / 2 - 160, lineY, w / 2 + 160, lineY);

                // Tagline
                g.setFont(Theme.FONT_SMALL);
                g.setColor(Theme.TEXT_DIM);
                String tag = "Only ONE shall claim the throne";
                fm = g.getFontMetrics();
                g.drawString(tag, (w - fm.stringWidth(tag)) / 2, lineY + 22);

                g.dispose();
            }
        };
        titleArea.setOpaque(false);
        titleArea.setPreferredSize(new Dimension(700, 300));
        add(titleArea, BorderLayout.CENTER);

        // ── Buttons ────────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 50, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.weightx = 1.0;

        JButton pvpBtn    = makeBigButton("Player vs Player",  "Two warriors face each other",                new Color(0xC0, 0x39, 0x2B), onPvP);
        JButton pveBtn    = makeBigButton("Player vs AI",      "Test your skill against the computer",        new Color(0x34, 0x98, 0xDB), onPvE);
        JButton arcadeBtn = makeBigButton("Arcade Mode",       "Fight through a gauntlet of champions",       new Color(0x8E, 0x44, 0xAD), onArcade);
        JButton rosterBtn = makeBigButton("View Characters",   "Browse all warriors and their abilities",     Theme.GOLD_DIM,              onRoster);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1; btnPanel.add(pvpBtn,    gbc);
        gbc.gridx = 1; gbc.gridy = 0;                    btnPanel.add(pveBtn,    gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; btnPanel.add(arcadeBtn, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; btnPanel.add(rosterBtn, gbc);

        JLabel credits = new JLabel("Forged by: Rovpoli · kd · biji · selwyn", JLabel.CENTER);
        credits.setFont(Theme.FONT_SMALL);
        credits.setForeground(Theme.TEXT_DIM);
        gbc.gridy = 3; btnPanel.add(credits, gbc);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private JButton makeBigButton(String title, String subtitle, Color accent, Runnable action) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = getModel().isPressed()  ? Theme.darker(accent, 0.5f)
                         : getModel().isRollover() ? Theme.alpha(accent, 80)
                         : Theme.alpha(accent, 40);
                g.setColor(bg);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g.setColor(getModel().isRollover() ? accent : Theme.darker(accent, 0.7f));
                g.setStroke(new BasicStroke(getModel().isRollover() ? 2 : 1));
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g.setFont(Theme.FONT_SUBHEAD);
                g.setColor(Color.WHITE);
                FontMetrics fm = g.getFontMetrics();
                g.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, getHeight() / 2 - 4);

                g.setFont(Theme.FONT_SMALL);
                g.setColor(Theme.TEXT_DIM);
                fm = g.getFontMetrics();
                g.drawString(subtitle, (getWidth() - fm.stringWidth(subtitle)) / 2, getHeight() / 2 + 16);

                g.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(240, 70));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        return btn;
    }
}
