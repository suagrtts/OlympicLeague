package olympicleague.gui;

import olympicleague.character.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RosterPanel extends JPanel {

    private static final String[] GODS = {
            "Ares", "Zeus", "Poseidon", "Time",
            "Artemis", "Hermes", "Apollo", "Talona", "Loki", "Athena"
    };

    private static final int[] HP = {
            1800, 1800, 1500, 1700,
            1500, 1600, 1600, 1200, 1700, 1650
    };

    private static final int[] MP = {
            1000, 450, 600, 1000,
            950, 700, 1200, 500, 1100, 900
    };

    private static final String[][] SKILLS = {
            {"Spear Thrust",   "Aegis Shield",   "Wrath of Ares"},
            {"Lion's Strike",  "Iron Hide",       "Thunder Wrath"},
            {"Tidal Wave",     "Ocean's Shield",  "Poseidon's Wrath"},
            {"Time Slash",     "Temporal Shift",  "Chrono Mark"},
            {"Piercing Arrow", "Hunter's Reflex", "Moonlit Mark"},
            {"Swift Strike",   "Vanish",          "Hermes' Speed"},
            {"Power Chord",    "Healing Hymn",    "Symphony of Destruction"},
            {"Kit Kit",        "Rat Spot",        "Talona's Might"},
            {"Rage Bait",      "Respawn Shield",  "Loki's Hack"},
            {"Twin Slash",     "Blade Veil",      "Athena's Fury"}
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

    private final Runnable onBack;
    private int selectedIdx = 0;

    private JLabel detailName, detailHp, detailMp, detailGod;
    private JLabel[] skillLabels = new JLabel[3];
    private JLabel[] skillDescLabels = new JLabel[3];
    private SpriteCanvas detailSprite;
    private JPanel cardGrid;

    public RosterPanel(Runnable onBack) {
        this.onBack = onBack;
        setBackground(Theme.BG_DEEP);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);

        updateDetail(0);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JButton backBtn = CharacterSelectPanel.makeButton("← Back", Theme.TEXT_DIM);
        backBtn.addActionListener(e -> onBack.run());

        JLabel title = new JLabel("CHARACTER ROSTER", JLabel.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.GOLD);

        header.add(backBtn, BorderLayout.WEST);
        header.add(title,   BorderLayout.CENTER);
        return header;
    }

    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout(14, 0));
        center.setOpaque(false);
        center.add(buildGrid(),   BorderLayout.WEST);
        center.add(buildDetail(), BorderLayout.CENTER);
        return center;
    }

    private JPanel buildGrid() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(340, 0));

        cardGrid = new JPanel(new GridLayout(4, 3, 6, 6));
        cardGrid.setOpaque(false);

        for (int i = 0; i < CharacterSelectPanel.NAMES.length; i++) {
            cardGrid.add(buildCard(i));
        }
        wrapper.add(cardGrid, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildCard(int idx) {
        JPanel card = new JPanel(new BorderLayout(2, 2));
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createLineBorder(Theme.alpha(Theme.GOLD, 60)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        SpriteCanvas mini = new SpriteCanvas(CharacterSelectPanel.NAMES[idx], SpriteLoader.AnimType.IDLE, 80, false, 8);
        mini.setOpaque(false);

        JLabel name = new JLabel(CharacterSelectPanel.NAMES[idx], JLabel.CENTER);
        name.setFont(Theme.FONT_SMALL);
        name.setForeground(Theme.TEXT_LIGHT);
        name.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        card.add(mini, BorderLayout.CENTER);
        card.add(name, BorderLayout.SOUTH);
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { selectedIdx = idx; updateDetail(idx); refreshHighlights(); }
            @Override public void mouseEntered(MouseEvent e) { card.setBackground(Theme.BG_CARD2); }
            @Override public void mouseExited(MouseEvent e)  { card.setBackground(selectedIdx == idx ? Theme.BG_CARD2 : Theme.BG_CARD); }
        });
        return card;
    }

    private void refreshHighlights() {
        Component[] cs = cardGrid.getComponents();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] instanceof JPanel c) {
                c.setBackground(i == selectedIdx ? Theme.BG_CARD2 : Theme.BG_CARD);
                c.setBorder(BorderFactory.createLineBorder(
                        i == selectedIdx ? Theme.GOLD : Theme.alpha(Theme.GOLD, 60),
                        i == selectedIdx ? 2 : 1
                ));
            }
        }
    }

    private JPanel buildDetail() {
        JPanel detail = new JPanel();
        detail.setBackground(Theme.BG_CARD);
        detail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.alpha(Theme.GOLD, 80)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));

        detailSprite = new SpriteCanvas(CharacterSelectPanel.NAMES[0], SpriteLoader.AnimType.IDLE, 160, false, 8);
        detailSprite.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailSprite.setMaximumSize(new Dimension(160, 160));

        detailName = styledLabel("", Theme.FONT_HEADER, Theme.GOLD);
        detailGod  = styledLabel("", Theme.FONT_BODY,   Theme.TEXT_DIM);
        detailHp   = styledLabel("", Theme.FONT_SKILL,  Theme.HP_GREEN);
        detailMp   = styledLabel("", Theme.FONT_SKILL,  Theme.MP_BLUE);

        JPanel statsRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        statsRow.setOpaque(false);
        statsRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsRow.add(new JLabel("HP:") {{ setFont(Theme.FONT_SKILL); setForeground(Theme.TEXT_DIM); }});
        statsRow.add(detailHp);
        statsRow.add(new JLabel("  MP:") {{ setFont(Theme.FONT_SKILL); setForeground(Theme.TEXT_DIM); }});
        statsRow.add(detailMp);

        JLabel skillsHdr = styledLabel("SKILLS", Theme.FONT_SKILL, Theme.GOLD_DIM);
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

    private JLabel styledLabel(String text, Font f, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(f);
        l.setForeground(c);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private void updateDetail(int idx) {
        detailSprite.setCharacter(CharacterSelectPanel.NAMES[idx], SpriteLoader.AnimType.IDLE);
        detailName.setText(CharacterSelectPanel.NAMES[idx]);
        detailGod .setText("God: " + GODS[idx]);
        detailHp  .setText(String.valueOf(HP[idx]));
        detailMp  .setText(String.valueOf(MP[idx]));
        for (int i = 0; i < 3; i++) {
            skillLabels[i]    .setText("  " + (i+1) + ". " + SKILLS[idx][i]);
            skillDescLabels[i].setText("       " + SKILL_DESC[idx][i]);
        }
        repaint();
    }
}