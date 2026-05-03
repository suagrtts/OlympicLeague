package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;


//MainMenuPanel — the home screen.
 
public class MainMenuPanel extends JPanel {

    private final Runnable onPvP, onPvE, onRoster;
    private float glowPhase = 0f;
    private Timer glowTimer;

    public MainMenuPanel(Runnable onPvP, Runnable onPvE, Runnable onRoster) {
        this.onPvP    = onPvP;
        this.onPvE    = onPvE;
        this.onRoster = onRoster;

        setLayout(new BorderLayout());
        setBackground(Theme.BG_DEEP);
        buildUI();
    }

    private void buildUI() {
        // ── Title area (painted) ─────────────────────────────────────────────
        JPanel titleArea = new JPanel() {
            @Override protected void paintComponent(Graphics g0) {
                super.paintComponent(g0);
                Graphics2D g = (Graphics2D) g0.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // LIGA
                g.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 64));
                String liga = "LIGA";
                FontMetrics fm = g.getFontMetrics();
                int x = (w - fm.stringWidth(liga)) / 2;


                // OLYMPICA
                g.setFont(new Font("Serif", Font.BOLD, 36));
                String sub = "OLYMPICA";
                fm = g.getFontMetrics();
                int x2 = (w - fm.stringWidth(sub)) / 2;
                g.setColor(new Color(0,0,0,100));
                g.drawString(sub, x2+2, h/2 + 28);
                GradientPaint gp2 = new GradientPaint(x2, h/2, Theme.GOLD, x2, h/2+40, Theme.GOLD_DIM);
                g.setPaint(gp2);
                g.drawString(sub, x2, h/2 + 26);

                //tagline
                g.setFont(Theme.FONT_SMALL);
                g.setColor(Theme.TEXT_DIM);
                String tag = "Only ONE shall claim the throne";
                fm = g.getFontMetrics();
                g.drawString(tag, (w - fm.stringWidth(tag))/2, h/2 + 55);

                g.dispose();
            }
        };
        titleArea.setBackground(Theme.BG_DEEP);
        titleArea.setPreferredSize(new Dimension(700, 280));
        add(titleArea, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new GridBagLayout());
        btnPanel.setBackground(Theme.BG_DEEP);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 60, 50, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.weightx = 1.0;

        JButton pvpBtn = makeBigButton("Player vs Player",
            "Two warriors face each other", new Color(0xC0,0x39,0x2B), onPvP);
        JButton pveBtn = makeBigButton("Player vs AI",
            "Test your skill against the computer", new Color(0x34,0x98,0xDB), onPvE);
        JButton rosterBtn = makeBigButton("View Characters",
            "Browse all warriors and their abilities", Theme.GOLD_DIM, onRoster);

        gbc.gridx = 0; gbc.gridy = 0; btnPanel.add(pvpBtn,    gbc);
        gbc.gridx = 1; gbc.gridy = 0; btnPanel.add(pveBtn,    gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        btnPanel.add(rosterBtn, gbc);

        // Creator credits
        JLabel credits = new JLabel("Forged by: Rovpoli · kd · biji · selwyn", JLabel.CENTER);
        credits.setFont(Theme.FONT_SMALL);
        credits.setForeground(Theme.TEXT_DIM);
        gbc.gridy = 2; btnPanel.add(credits, gbc);

        add(btnPanel, BorderLayout.SOUTH);
    }

    private JButton makeBigButton(String title, String subtitle, Color accent, Runnable action) {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color bg = getModel().isPressed()  ? Theme.darker(accent, 0.5f)
                         : getModel().isRollover() ? Theme.alpha(accent, 60)
                         : Theme.alpha(accent, 30);
                g.setColor(bg);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                g.setColor(getModel().isRollover() ? accent : Theme.darker(accent, 0.7f));
                g.setStroke(new BasicStroke(getModel().isRollover() ? 2 : 1));
                g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);

                // Title
                g.setFont(Theme.FONT_SUBHEAD);
                g.setColor(Color.WHITE);
                FontMetrics fm = g.getFontMetrics();
                g.drawString(title, (getWidth()-fm.stringWidth(title))/2, getHeight()/2 - 4);

                // Subtitle
                g.setFont(Theme.FONT_SMALL);
                g.setColor(Theme.TEXT_DIM);
                fm = g.getFontMetrics();
                g.drawString(subtitle, (getWidth()-fm.stringWidth(subtitle))/2, getHeight()/2 + 16);

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
