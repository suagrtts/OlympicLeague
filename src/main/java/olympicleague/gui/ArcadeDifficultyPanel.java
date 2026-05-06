package olympicleague.gui;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * ArcadeDifficultyPanel — lets the player choose Easy/Medium/Hard before Arcade Mode.
 */
public class ArcadeDifficultyPanel extends JPanel {

    private final Consumer<String> onDiffChosen;
    private final Runnable         onBack;

    private static final String[][] DIFFS = {
        {"EASY",   "Easy",   "Fight 4 opponents",  "0xC0, 0x39, 0x2B"},
        {"MEDIUM", "Medium", "Fight 6 opponents",  "0x34, 0x98, 0xDB"},
        {"HARD",   "Hard",   "Fight ALL opponents","0x8E, 0x44, 0xAD"},
    };

    private static final Color[] COLORS = {
        new Color(0x2E, 0xCC, 0x71),  // Easy — green
        new Color(0xF3, 0x9C, 0x12),  // Medium — orange
        new Color(0xC0, 0x39, 0x2B),  // Hard — red
    };

    public ArcadeDifficultyPanel(Consumer<String> onDiffChosen, Runnable onBack) {
        this.onDiffChosen = onDiffChosen;
        this.onBack       = onBack;

        setBackground(Theme.BG_DEEP);
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(30, 60, 40, 60));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCards(),  BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JButton backBtn = CharacterSelectPanel.makeButton("← Back", Theme.TEXT_DIM);
        backBtn.addActionListener(e -> onBack.run());

        JLabel title = new JLabel("ARCADE MODE — SELECT DIFFICULTY", JLabel.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.GOLD);

        JLabel sub = new JLabel("Defeat every opponent in sequence to claim glory", JLabel.CENTER);
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_DIM);

        JPanel titleGroup = new JPanel(new GridLayout(2, 1, 0, 4));
        titleGroup.setOpaque(false);
        titleGroup.add(title);
        titleGroup.add(sub);

        header.add(backBtn, BorderLayout.WEST);
        header.add(titleGroup, BorderLayout.CENTER);
        return header;
    }

    private JPanel buildCards() {
        JPanel cards = new JPanel(new GridLayout(1, 3, 20, 0));
        cards.setOpaque(false);

        String[] labels    = {"Easy",   "Medium", "Hard"};
        String[] subtitles = {"4 Opponents\nHandicapped AI",
                              "6 Opponents\nBalanced AI",
                              "All Opponents\nFull power AI"};
        String[] keys      = {"EASY",   "MEDIUM", "HARD"};

        for (int i = 0; i < 3; i++) {
            cards.add(buildDiffCard(keys[i], labels[i], subtitles[i], COLORS[i]));
        }
        return cards;
    }

    private JPanel buildDiffCard(String key, String label, String subtitle, Color accent) {
        JPanel card = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Theme.alpha(accent, 30));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g.setColor(Theme.alpha(accent, 100));
                g.setStroke(new BasicStroke(2));
                g.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);
                g.dispose();
                super.paintComponent(g0);
            }
        };
        card.setOpaque(false);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setPreferredSize(new Dimension(200, 260));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lbl = new JLabel(label, JLabel.CENTER);
        lbl.setFont(new Font("Serif", Font.BOLD, 28));
        lbl.setForeground(accent);

        JLabel sub = new JLabel("<html><center>" + subtitle.replace("\n","<br>") + "</center></html>", JLabel.CENTER);
        sub.setFont(Theme.FONT_BODY);
        sub.setForeground(Theme.TEXT_DIM);

        JButton btn = CharacterSelectPanel.makeButton("SELECT", accent);
        btn.addActionListener(e -> onDiffChosen.accept(key));

        card.add(lbl, gbc);
        card.add(sub, gbc);
        card.add(btn, gbc);

        // Hover glow
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) { onDiffChosen.accept(key); }
        });

        return card;
    }
}
