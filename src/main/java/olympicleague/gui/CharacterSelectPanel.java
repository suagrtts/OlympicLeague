package olympicleague.gui;

import olympicleague.character.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class CharacterSelectPanel extends JPanel {

    // ── Playable roster ───────────────────────────────────────────────────────
    public static final String[] NAMES = {
            "Achiron",  "Heralde", "Orris",      "Vor",
            "Atalyn",   "Orven",   "Biji",        "GoatedKit",
            "Selwyn",   "TinySwords"
    };

    private static final String[] GODS = {
            "Ares", "Zeus", "Poseidon", "Time",
            "Artemis", "Hermes", "Apollo", "Talona",
            "Loki", "Athena"
    };

    private static final int[] HP_VALUES = {
            1800, 1800, 1500, 1700,
            1500, 1600, 1600, 1200,
            1700, 1650
    };

    private static final int[] MP_VALUES = {
            1000, 450, 600, 1000,
            950, 700, 1200, 500,
            1100, 900
    };

    private static final String[][] SKILLS = {
            {"Spear Thrust",  "Aegis Shield",    "Wrath of Ares"},
            {"Lion's Strike", "Iron Hide",        "Thunder Wrath"},
            {"Tidal Wave",    "Ocean's Shield",   "Poseidon's Wrath"},
            {"Time Slash",    "Temporal Shift",   "Chrono Mark"},
            {"Piercing Arrow","Hunter's Reflex",  "Moonlit Mark"},
            {"Swift Strike",  "Vanish",           "Hermes' Speed"},
            {"Power Chord",   "Healing Hymn",     "Symphony of Destruction"},
            {"Kit Kit",       "Rat Spot",         "Talona's Might"},
            {"Rage Bait",     "Respawn Shield",   "Loki's Hack"},
            {"Twin Slash",    "Blade Veil",       "Athena's Fury"}
    };

    private static final String[][] SKILL_DESC = {
            {"400 Base Dmg. Cost: 180MP",         "Reduce next damage by 50%. Cost: 320MP",       "+50% damage for 2 turns. Cost: 500MP"},
            {"220 Base Dmg. Cost: 90MP",          "Reduce damage by 30% for 2 turns. Cost: 100MP","400 True Dmg. Cost: 180MP"},
            {"300 Base Dmg. Cost: 130MP",         "Absorb 20% Dmg for 2 turns. Cost: 110MP",      "500 True Dmg. Cost: 180MP"},
            {"300 Base Dmg. Cost: 150MP",         "Evade next attack. Cost: 120MP",                "+25% Dmg for 2 turns. Cost: 500MP"},
            {"360 Base Dmg. Cost: 150MP",         "Evade next attack. Cost: 120MP",                "+50% Damage for 2 turns. Cost: 500MP"},
            {"250 Base Dmg. Cost: 140MP",         "Untargetable next turn. Cost: 120MP",           "Attack twice. Cost: 200MP"},
            {"380 Base Dmg. Cost: 200MP",         "Heals 400 HP. Cost: 300MP",                    "600 Dmg + Stun. Cost: 550MP"},
            {"300 Base Bite Dmg. Cost: 120MP",    "Untargetable for 2 turns. Cost: 250MP",         "+20% bite damage for 3 turns. Cost: 400MP"},
            {"420 Base Dmg. Cost: 220MP",         "Reduce next hit by 60%. Cost: 280MP",           "450 Dmg ignoring defenses. Cost: 480MP"},
            {"350 Base Dmg x2 hits. Cost: 160MP", "Untargetable for 1 turn. Cost: 280MP",          "+60% ATK for 2 turns. Cost: 450MP"}
    };

    private final String label;
    private final Consumer<GameCharacter> onConfirm;
    private final Runnable onBack;

    private int selectedIndex = 0;

    // Detail panel widgets
    private JLabel  detailName, detailGod, detailHpVal, detailMpVal;
    private JLabel[] skillLabels;
    private JLabel[] skillDescLabels;
    private SpriteCanvas detailSprite;
    private JPanel  cardGrid;

    public CharacterSelectPanel(String label, Consumer<GameCharacter> onConfirm, Runnable onBack) {
        this.label     = label;
        this.onConfirm = onConfirm;
        this.onBack    = onBack;

        setBackground(Theme.BG_DEEP);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(buildHeader(),      BorderLayout.NORTH);
        add(buildCenter(),      BorderLayout.CENTER);
        add(buildFooter(),      BorderLayout.SOUTH);

        updateDetail(0);
    }

    // ── Header ───────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JButton backBtn = makeButton("← Back", Theme.TEXT_DIM);
        backBtn.addActionListener(e -> onBack.run());

        JLabel title = new JLabel(label.toUpperCase() + " — SELECT CHARACTER", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.GOLD);

        header.add(backBtn, BorderLayout.WEST);
        header.add(title,   BorderLayout.CENTER);
        return header;
    }

    // ── Center: roster grid + detail panel ──────────────────────────────────
    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(14, 0));
        center.setOpaque(false);
        center.add(buildRosterGrid(),  BorderLayout.WEST);
        center.add(buildDetailPanel(), BorderLayout.CENTER);
        return center;
    }

    private JPanel buildRosterGrid() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(340, 0));

        cardGrid = new JPanel(new GridLayout(4, 3, 6, 6));
        cardGrid.setOpaque(false);

        for (int i = 0; i < NAMES.length; i++) {
            cardGrid.add(buildCharCard(i));
        }
        wrapper.add(cardGrid, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildCharCard(int idx) {
        JPanel card = new JPanel(new BorderLayout(2, 2));
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createLineBorder(Theme.alpha(Theme.GOLD, 60), 1));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        SpriteCanvas mini = new SpriteCanvas(NAMES[idx], SpriteLoader.AnimType.IDLE, 80, false, 8);
        mini.setOpaque(false);

        JLabel nameLbl = new JLabel(NAMES[idx], SwingConstants.CENTER);
        nameLbl.setFont(Theme.FONT_SMALL);
        nameLbl.setForeground(Theme.TEXT_LIGHT);
        nameLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        card.add(mini,    BorderLayout.CENTER);
        card.add(nameLbl, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                selectedIndex = idx;
                updateDetail(idx);
                refreshCardHighlights();
            }
            @Override public void mouseEntered(MouseEvent e) { card.setBackground(Theme.BG_CARD2); }
            @Override public void mouseExited(MouseEvent e)  {
                card.setBackground(selectedIndex == idx ? Theme.BG_CARD2 : Theme.BG_CARD);
            }
        });

        return card;
    }

    private void refreshCardHighlights() {
        Component[] comps = cardGrid.getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof JPanel card) {
                card.setBackground(i == selectedIndex ? Theme.BG_CARD2 : Theme.BG_CARD);
                card.setBorder(BorderFactory.createLineBorder(
                        i == selectedIndex ? Theme.GOLD : Theme.alpha(Theme.GOLD, 60), i == selectedIndex ? 2 : 1
                ));
            }
        }
    }

    private JPanel buildDetailPanel() {
        JPanel detail = new JPanel();
        detail.setBackground(Theme.BG_CARD);
        detail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.alpha(Theme.GOLD, 80), 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));

        detailSprite = new SpriteCanvas(NAMES[0], SpriteLoader.AnimType.IDLE, 160, false, 8);
        detailSprite.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailSprite.setMaximumSize(new Dimension(160, 160));

        detailName = styledLabel("", Theme.FONT_HEADER, Theme.GOLD);
        detailGod  = styledLabel("", Theme.FONT_BODY,   Theme.TEXT_DIM);

        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        statsRow.setOpaque(false);
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailHpVal = styledLabel("", Theme.FONT_SKILL, Theme.HP_GREEN);
        detailMpVal = styledLabel("", Theme.FONT_SKILL, Theme.MP_BLUE);
        statsRow.add(new JLabel("HP:") {{ setFont(Theme.FONT_SKILL); setForeground(Theme.TEXT_DIM); }});
        statsRow.add(detailHpVal);
        statsRow.add(new JLabel("  MP:") {{ setFont(Theme.FONT_SKILL); setForeground(Theme.TEXT_DIM); }});
        statsRow.add(detailMpVal);

        JLabel skillsHdr = styledLabel("SKILLS", Theme.FONT_SKILL, Theme.GOLD_DIM);
        skillLabels     = new JLabel[3];
        skillDescLabels = new JLabel[3];
        JPanel skillsPanel = new JPanel();
        skillsPanel.setOpaque(false);
        skillsPanel.setLayout(new BoxLayout(skillsPanel, BoxLayout.Y_AXIS));
        skillsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 0; i < 3; i++) {
            skillLabels[i]     = styledLabel("", Theme.FONT_SKILL, Theme.TEXT_LIGHT);
            skillDescLabels[i] = styledLabel("", Theme.FONT_SMALL, Theme.TEXT_DIM);
            skillsPanel.add(skillLabels[i]);
            skillsPanel.add(skillDescLabels[i]);
            skillsPanel.add(Box.createVerticalStrut(4));
        }

        detail.add(detailSprite);
        detail.add(Box.createVerticalStrut(10));
        detail.add(detailName);
        detail.add(detailGod);
        detail.add(Box.createVerticalStrut(8));
        detail.add(statsRow);
        detail.add(Box.createVerticalStrut(10));
        detail.add(skillsHdr);
        detail.add(Box.createVerticalStrut(4));
        detail.add(skillsPanel);

        return detail;
    }

    private JLabel styledLabel(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void updateDetail(int idx) {
        if (detailName == null) return;

        detailSprite.setCharacter(NAMES[idx], SpriteLoader.AnimType.IDLE);
        detailName.setText(NAMES[idx]);
        detailGod .setText("God: " + GODS[idx]);
        detailHpVal.setText(String.valueOf(HP_VALUES[idx]));
        detailMpVal.setText(String.valueOf(MP_VALUES[idx]));

        for (int i = 0; i < 3; i++) {
            skillLabels[i]    .setText("  " + (i+1) + ". " + SKILLS[idx][i]);
            skillDescLabels[i].setText("       " + SKILL_DESC[idx][i]);
        }
        repaint();
    }

    // ── Footer: Confirm button ───────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setOpaque(false);

        JButton confirm = makeButton("⚔  CONFIRM SELECTION", Theme.GOLD);
        confirm.setFont(Theme.FONT_SUBHEAD);
        confirm.addActionListener(e -> {
            GameCharacter chosen = makeChar(NAMES[selectedIndex]);
            onConfirm.accept(chosen);
        });
        footer.add(confirm);
        return footer;
    }

    // ── Character Factory ─────────────────────────────────────────────────────
    public static GameCharacter makeChar(String name) {
        return switch (name) {
            case "Atalyn"     -> new Atalyn();
            case "Heralde"    -> new Heralde();
            case "Orris"      -> new Orris();
            case "Orven"      -> new Orven();
            case "Biji"       -> new Biji();
            case "Selwyn"     -> new Selwyn();
            case "GoatedKit"  -> new GoatedKit();
            case "Skeleton"   -> new Skeleton();
            case "EvilWizard" -> new EvilWizard();
            case "Vor"        -> new Vor();
            case "TinySwords" -> new TinySwords();
            default           -> new Achiron();
        };
    }

    // ── Utility ──────────────────────────────────────────────────────────────
    public static JButton makeButton(String text, Color accent) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()  ? Theme.darker(accent, 0.5f)
                        : getModel().isRollover() ? Theme.alpha(accent, 60)
                          : Theme.alpha(accent, 25);
                g.setColor(bg);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g.setColor(getModel().isRollover() ? accent : Theme.darker(accent, 0.7f));
                g.setStroke(new BasicStroke(getModel().isRollover() ? 2 : 1));
                g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 8, 8);
                g.setFont(getFont());
                g.setColor(Color.WHITE);
                FontMetrics fm = g.getFontMetrics();
                g.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2,
                        getHeight()/2 + fm.getAscent()/2 - 2);
                g.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(240, 44));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(Theme.FONT_BODY);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}