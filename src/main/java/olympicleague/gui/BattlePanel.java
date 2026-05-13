package olympicleague.gui;

import olympicleague.character.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class BattlePanel extends JPanel {

    private final GameCharacter player1;
    private final GameCharacter player2;
    private final boolean isAI;
    private final Runnable onBattleEnd;

    // State
    private boolean player1Turn = true;
    private boolean battleOver = false;
    private boolean turnInProgress = false; // FIXED: Safety lock to prevent double-click bugs
    private int p1Wins = 0, p2Wins = 0, currentRound = 1;
    private static final int MAX_ROUNDS = 3;

    // GUI widgets
    private AnimatedBar p1HpBar, p1MpBar, p2HpBar, p2MpBar;
    private JTextArea battleLog;
    private JPanel skillButtonPanel;
    private JLabel turnLabel, roundLabel;
    private SpriteCanvas p1Sprite, p2Sprite;
    private ArenaPanel arenaPanel;

    // Animation constants
    private static final int DASH_DURATION_MS = 250;
    private static final int ATTACK_PAUSE_MS = 300;

    public BattlePanel(GameCharacter player1, GameCharacter player2, boolean isAI, Runnable onBattleEnd) {
        this.player1 = player1;
        this.player2 = player2;
        this.isAI = isAI;
        this.onBattleEnd = onBattleEnd;

        setLayout(new BorderLayout(0, 0));
        setBackground(Theme.BG_DEEP);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildArena(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        startRound();
    }

    // ── Top stat bars ────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.BG_CARD);
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.alpha(Theme.GOLD, 60)));

        JPanel p1Stats = buildStatBlock(player1, true);
        JPanel center = buildCenterInfo();
        JPanel p2Stats = buildStatBlock(player2, false);

        top.add(p1Stats, BorderLayout.WEST);
        top.add(center, BorderLayout.CENTER);
        top.add(p2Stats, BorderLayout.EAST);
        return top;
    }

    private JPanel buildStatBlock(GameCharacter gc, boolean isLeft) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        panel.setPreferredSize(new Dimension(220, 80));

        JLabel name = new JLabel(gc.getName());
        name.setFont(Theme.FONT_SUBHEAD);
        name.setForeground(isLeft ? new Color(0xC0, 0x39, 0x2B) : new Color(0x34, 0x98, 0xDB));
        name.setAlignmentX(isLeft ? Component.LEFT_ALIGNMENT : Component.RIGHT_ALIGNMENT);

        AnimatedBar hpBar = new AnimatedBar("HP", Theme.HP_GREEN, gc.getMaxHealth());
        AnimatedBar mpBar = new AnimatedBar("MP", Theme.MP_BLUE, gc.getMaxMana());
        hpBar.setTarget(gc.getHealth(), gc.getMaxHealth());
        mpBar.setTarget(gc.getMana(), gc.getMaxMana());
        hpBar.setMaximumSize(new Dimension(200, 22));
        mpBar.setMaximumSize(new Dimension(200, 22));

        panel.add(name);
        panel.add(Box.createVerticalStrut(4));
        panel.add(hpBar);
        panel.add(Box.createVerticalStrut(2));
        panel.add(mpBar);

        if (isLeft) {
            p1HpBar = hpBar;
            p1MpBar = mpBar;
        } else {
            p2HpBar = hpBar;
            p2MpBar = mpBar;
        }

        return panel;
    }

    private JPanel buildCenterInfo() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Theme.BG_CARD);

        roundLabel = new JLabel("Round 1 / 3  —  0-0");
        roundLabel.setFont(Theme.FONT_SMALL);
        roundLabel.setForeground(Theme.GOLD);

        turnLabel = new JLabel(player1.getName() + "'s Turn");
        turnLabel.setFont(Theme.FONT_SKILL);
        turnLabel.setForeground(Theme.TEXT_DIM);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        center.add(roundLabel, gbc);
        gbc.gridy = 1;
        center.add(turnLabel, gbc);
        return center;
    }

    private ArenaPanel buildArena() {
        p1Sprite = new SpriteCanvas(player1.getName(), SpriteLoader.AnimType.IDLE, 200, false, 8);
        p2Sprite = new SpriteCanvas(player2.getName(), SpriteLoader.AnimType.IDLE, 200, true, 8);

        arenaPanel = new ArenaPanel(p1Sprite, p2Sprite);
        return arenaPanel;
    }

    // ── Bottom: log + skill buttons ──────────────────────────────────────────
    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(8, 0));
        bottom.setBackground(Theme.BG_DEEP);
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        battleLog = new JTextArea(6, 30);
        battleLog.setEditable(false);
        battleLog.setBackground(Theme.BG_CARD);
        battleLog.setForeground(Theme.TEXT_LIGHT);
        battleLog.setFont(Theme.FONT_MONO);
        battleLog.setLineWrap(true);
        battleLog.setWrapStyleWord(true);
        battleLog.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JScrollPane logScroll = new JScrollPane(battleLog);
        logScroll.setBorder(BorderFactory.createLineBorder(Theme.alpha(Theme.GOLD, 50)));
        logScroll.setPreferredSize(new Dimension(320, 120));

        skillButtonPanel = new JPanel(new GridLayout(0, 1, 4, 4));
        skillButtonPanel.setBackground(Theme.BG_DEEP);
        skillButtonPanel.setPreferredSize(new Dimension(280, 120));
        buildSkillButtons(player1);

        bottom.add(logScroll, BorderLayout.CENTER);
        bottom.add(skillButtonPanel, BorderLayout.EAST);
        return bottom;
    }

    private void buildSkillButtons(GameCharacter actor) {
        skillButtonPanel.removeAll();

        List<Skill> skills = actor.getSkills();
        for (int i = 0; i < skills.size(); i++) {
            Skill s = skills.get(i);
            final int idx = i;
            JButton btn = new JButton(s.getName() + (s.isReady() ? "" : " (" + s.getCurrentCooldown() + ")"));
            btn.setFont(Theme.FONT_SKILL);
            btn.setBackground(s.isReady() ? Theme.BG_CARD2 : Theme.SKILL_CD);
            btn.setForeground(s.isReady() ? Theme.TEXT_LIGHT : Theme.TEXT_DIM);
            btn.setBorder(BorderFactory.createLineBorder(s.isReady() ? Theme.alpha(Theme.GOLD, 80) : Theme.alpha(Theme.GOLD, 20)));
            btn.setFocusPainted(false);
            btn.setEnabled(s.isReady());
            btn.setToolTipText(s.getDescription());
            btn.addActionListener(e -> onSkillUsed(actor, idx));
            skillButtonPanel.add(btn);
        }

        JButton fleeBtn = new JButton("🏃 Flee");
        fleeBtn.setFont(Theme.FONT_SKILL);
        fleeBtn.setBackground(new Color(0x30, 0x15, 0x15));
        fleeBtn.setForeground(new Color(0xFF, 0x70, 0x70));
        fleeBtn.setBorder(BorderFactory.createLineBorder(new Color(0x80, 0x20, 0x20)));
        fleeBtn.setFocusPainted(false);
        fleeBtn.addActionListener(e -> {
            log(actor.getName() + " fled the battle!");
            endBattle();
        });
        skillButtonPanel.add(fleeBtn);

        skillButtonPanel.revalidate();
        skillButtonPanel.repaint();
    }

    // ── Battle Logic ─────────────────────────────────────────────────────────
    private void startRound() {
        player1.resetForNewRound();
        player2.resetForNewRound();
        player1Turn = true;
        battleOver = false;
        turnInProgress = false; // FIXED: Reset safety lock

        arenaPanel.roundBanner.setVisible(false);

        updateBars();
        updateRoundLabel();
        log("=== Round " + currentRound + " ===");
        log(player1.getName() + " vs " + player2.getName());
        setPlayerTurn(true);
    }

    private void setPlayerTurn(boolean isPlayer1) {
        player1Turn = isPlayer1;
        turnInProgress = false; // FIXED: Turn logic is done, unlock UI for the next action

        GameCharacter actor = isPlayer1 ? player1 : player2;
        turnLabel.setText(actor.getName() + "'s Turn");

        if (isPlayer1) {
            buildSkillButtons(player1);
            setSkillButtonsEnabled(true);
        } else {
            if (isAI) {
                setSkillButtonsEnabled(false);
                Timer aiTimer = new Timer(800, e -> doAITurn());
                aiTimer.setRepeats(false);
                aiTimer.start();
            } else {
                buildSkillButtons(player2);
                setSkillButtonsEnabled(true);
            }
        }
    }

    private void onSkillUsed(GameCharacter actor, int skillIndex) {
        // FIXED: Prevent double clicks from running overlapping turns
        if (battleOver || turnInProgress) return;
        turnInProgress = true;

        GameCharacter target = actor == player1 ? player2 : player1;
        setSkillButtonsEnabled(false);
        actor.updateTurnEffects();

        if (actor.isStunned()) {
            log(actor.getName() + " is stunned and cannot act!");
            actor.setStunned(false);

            // Determine whose turn is next safely
            boolean nextTurnP1 = !(actor == player1);
            Timer switchTimer = new Timer(700, e -> setPlayerTurn(nextTurnP1));
            switchTimer.setRepeats(false);
            switchTimer.start();
        } else {
            String result = actor.takeTurn(target, skillIndex);
            log(actor.getName() + ": " + result);
            actor.restoreMana(150);
            target.restoreMana(150);
            updateBars();

            playSkillAnimation(actor, target, result);
        }
    }

    private void doAITurn() {
        if (battleOver) return;
        turnInProgress = true; // FIXED: Lock the turn for AI
        player2.updateTurnEffects();

        if (player2.isStunned()) {
            log(player2.getName() + " is stunned!");
            player2.setStunned(false);
            Timer switchTimer = new Timer(700, e -> setPlayerTurn(true));
            switchTimer.setRepeats(false);
            switchTimer.start();
        } else {
            String result = player2.autoTakeTurn(player1);
            log(player2.getName() + ": " + result);
            player1.restoreMana(150);
            player2.restoreMana(150);
            updateBars();

            playSkillAnimation(player2, player1, result);
        }
    }

    private String determineVFX(String log) {
        String l = log.toLowerCase();
        if (l.contains("heal") || l.contains("soul drain")) return "Heal";
        if (l.contains("shield") || l.contains("hide") || l.contains("veil") || l.contains("evade"))
            return "HolyShield";
        if (l.contains("thunder") || l.contains("lightning") || l.contains("bolt")) return "Smite";
        if (l.contains("wrath") || l.contains("fury") || l.contains("blessing") || l.contains("supremacy") || l.contains("mark"))
            return "HeavensFury";
        if (l.contains("sword") || l.contains("judgment") || l.contains("justice") || l.contains("spear"))
            return "SwordOfJustice";
        if (l.contains("nova") || l.contains("magic") || l.contains("arcane")) return "HolyNova";

        // Default physical slashes get a random slash effect
        double r = Math.random();
        if (r < 0.33) return "HolySlash_A";
        else if (r < 0.66) return "HolySlash_B";
        else return "HolySlash_C";
    }

    /**
     * Handles complex movement animations and NEW VFX Overlays.
     */
    private void playSkillAnimation(GameCharacter actor, GameCharacter target, String resultLog) {
        String lowerLog = resultLog.toLowerCase();

        boolean isOffensive = lowerLog.contains("dealt") || lowerLog.contains("hits for") ||
                lowerLog.contains("deals") || lowerLog.contains("rips through") ||
                lowerLog.contains("tears") || lowerLog.contains("true damage") ||
                lowerLog.contains("strike 1") || lowerLog.contains("hit 1") ||
                lowerLog.contains("cuts through") || lowerLog.contains("infects for") ||
                lowerLog.contains("crashes for") || lowerLog.contains("damage to") ||
                lowerLog.contains("damage and") || lowerLog.contains("strike for");

        boolean isEvade = lowerLog.contains("evade") || lowerLog.contains("untargetable") ||
                lowerLog.contains("elusive") || lowerLog.contains("cannot be targeted") ||
                lowerLog.contains("vanish");

        boolean isPowerUp = lowerLog.contains("gained") || lowerLog.contains("increased") ||
                lowerLog.contains("surges") || lowerLog.contains("power");

        boolean actorIsP1 = actor == player1;
        boolean nextTurnP1 = !actorIsP1; // FIXED: Explicitly assign next turn to the opposite player to prevent overrides

        SpriteCanvas attackerSprite = actorIsP1 ? p1Sprite : p2Sprite;
        SpriteCanvas defenderSprite = actorIsP1 ? p2Sprite : p1Sprite;

        Point startLoc = attackerSprite.getLocation();
        String vfxName = determineVFX(resultLog);
        boolean isHealVfx = "Heal".equals(vfxName);

        // ─── 1. OFFENSIVE SKILL (Dash & Attack + VFX) ────────────────────────
        if (isOffensive) {
            Point targetLoc = defenderSprite.getLocation();
            int offset = actorIsP1 ? -100 : 100;
            Point attackPoint = new Point(targetLoc.x + offset, targetLoc.y);

            animateMovement(attackerSprite, startLoc, attackPoint, DASH_DURATION_MS, () -> {
                attackerSprite.setAnimType(SpriteLoader.AnimType.ATTACK);
                defenderSprite.setAnimType(SpriteLoader.AnimType.HURT);

                // Play strike SFX for every offensive hit (player or AI).
                BattleSound.playSwordCut();
                if (isHealVfx) {
                    BattleSound.playHeal();
                }

                // FIXED: Wrapped in try-catch to prevent softlocks from image load errors
                try {
                    VFXCanvas vfx = new VFXCanvas(vfxName, 250, null);
                    vfx.setLocation(targetLoc.x - 25, targetLoc.y - 25);
                    arenaPanel.add(vfx, 0);
                    arenaPanel.repaint();
                } catch (Exception ex) {
                    System.err.println("VFX Error: " + vfxName);
                }

                Timer attackPause = new Timer(ATTACK_PAUSE_MS, e -> {
                    attackerSprite.setAnimType(SpriteLoader.AnimType.IDLE);
                    if (!target.isAlive()) {
                        animateMovement(attackerSprite, attackPoint, startLoc, DASH_DURATION_MS, () -> handleRoundEnd());
                    } else {
                        defenderSprite.setAnimType(SpriteLoader.AnimType.IDLE);
                        animateMovement(attackerSprite, attackPoint, startLoc, DASH_DURATION_MS, () -> {
                            Timer switchTimer = new Timer(200, e2 -> setPlayerTurn(nextTurnP1));
                            switchTimer.setRepeats(false);
                            switchTimer.start();
                        });
                    }
                });
                attackPause.setRepeats(false);
                attackPause.start();
            });
        }
        // ─── 2. EVASIVE SKILL (Backstep + VFX) ───────────────────────────────
        else if (isEvade) {
            int backDir = actorIsP1 ? -60 : 60;
            Point dodgePoint = new Point(startLoc.x + backDir, startLoc.y);

            try {
                VFXCanvas vfx = new VFXCanvas(vfxName, 200, null);
                vfx.setLocation(dodgePoint.x, dodgePoint.y);
                arenaPanel.add(vfx, 0);
                arenaPanel.repaint();
            } catch (Exception ex) {
                System.err.println("VFX Error: " + vfxName);
            }

            animateMovement(attackerSprite, startLoc, dodgePoint, 150, () -> {
                Timer hover = new Timer(400, e -> {
                    animateMovement(attackerSprite, dodgePoint, startLoc, 200, () -> {
                        Timer switchTimer = new Timer(300, e2 -> setPlayerTurn(nextTurnP1));
                        switchTimer.setRepeats(false);
                        switchTimer.start();
                    });
                });
                hover.setRepeats(false);
                hover.start();
            });
        }
        // ─── 3. POWER UP SKILL (Shake + VFX) ─────────────────────────────────
        else if (isPowerUp) {
            try {
                VFXCanvas vfx = new VFXCanvas(vfxName, 300, null);
                vfx.setLocation(startLoc.x - 50, startLoc.y - 50);
                arenaPanel.add(vfx, 0);
                arenaPanel.repaint();
            } catch (Exception ex) {
                System.err.println("VFX Error: " + vfxName);
            }

            Timer shakeTimer = new Timer(30, null);
            long startTime = System.currentTimeMillis();
            shakeTimer.addActionListener(e -> {
                long elapsed = System.currentTimeMillis() - startTime;
                if (elapsed > 600) {
                    ((Timer) e.getSource()).stop();
                    attackerSprite.setLocation(startLoc);
                    Timer switchTimer = new Timer(300, e2 -> setPlayerTurn(nextTurnP1));
                    switchTimer.setRepeats(false);
                    switchTimer.start();
                } else {
                    int randomX = (int) (Math.random() * 10) - 5;
                    attackerSprite.setLocation(startLoc.x + randomX, startLoc.y);
                }
            });
            shakeTimer.start();
        }
        // ─── 4. HEAL / SHIELD SKILL (Magic Float + VFX) ──────────────────────
        else {
            Point hoverPoint = new Point(startLoc.x, startLoc.y - 60);

            try {
                if (isHealVfx) {
                    BattleSound.playHeal();
                }
                VFXCanvas vfx = new VFXCanvas(vfxName, 250, null);
                vfx.setLocation(hoverPoint.x - 25, hoverPoint.y - 25);
                arenaPanel.add(vfx, 0);
                arenaPanel.repaint();
            } catch (Exception ex) {
                System.err.println("VFX Error: " + vfxName);
            }

            animateMovement(attackerSprite, startLoc, hoverPoint, 300, () -> {
                Timer hangTime = new Timer(400, e -> {
                    animateMovement(attackerSprite, hoverPoint, startLoc, 400, () -> {
                        Timer switchTimer = new Timer(200, e2 -> setPlayerTurn(nextTurnP1));
                        switchTimer.setRepeats(false);
                        switchTimer.start();
                    });
                });
                hangTime.setRepeats(false);
                hangTime.start();
            });
        }
    }

    private void animateMovement(Component comp, Point start, Point end, int durationMs, Runnable onComplete) {
        long startTime = System.currentTimeMillis();
        Timer timer = new Timer(16, null);
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float percent = (float) elapsed / durationMs;

            if (percent >= 1.0f) {
                comp.setLocation(end);
                ((Timer) e.getSource()).stop();
                if (onComplete != null) onComplete.run();
            } else {
                int currentX = (int) (start.x + (end.x - start.x) * percent);
                int currentY = (int) (start.y + (end.y - start.y) * percent);
                comp.setLocation(currentX, currentY);
            }
        });
        timer.start();
    }

    private void handleRoundEnd() {
        String winner = "";
        boolean hasRoundWinner = false;
        if (player1.isAlive() && !player2.isAlive()) {
            p1Wins++;
            winner = player1.getName() + " wins Round " + currentRound + "!";
            hasRoundWinner = true;
        } else if (!player1.isAlive() && player2.isAlive()) {
            p2Wins++;
            winner = player2.getName() + " wins Round " + currentRound + "!";
            hasRoundWinner = true;
        } else {
            winner = "Round " + currentRound + " — Draw!";
        }
        if (hasRoundWinner) {
            BattleSound.playGrandWinner();
        }
        log(winner);
        log("Score: " + p1Wins + " - " + p2Wins);
        updateRoundLabel();

        arenaPanel.roundBanner.setText(winner);
        arenaPanel.roundBanner.setVisible(true);

        currentRound++;

        if (p1Wins >= 2 || p2Wins >= 2 || currentRound > MAX_ROUNDS) {
            Timer endTimer = new Timer(2000, e -> {
                String matchWinner = "Match ends in a Draw!";
                if (p1Wins > p2Wins) matchWinner = "🏆 " + player1.getName() + " WINS MATCH! 🏆";
                else if (p2Wins > p1Wins) matchWinner = "🏆 " + player2.getName() + " WINS MATCH! 🏆";
                log(matchWinner);
                arenaPanel.roundBanner.setText(matchWinner);
                endBattle();
            });
            endTimer.setRepeats(false);
            endTimer.start();
        } else {
            Timer nextRound = new Timer(2000, e -> startRound());
            nextRound.setRepeats(false);
            nextRound.start();
        }
    }

    private void endBattle() {
        battleOver = true;
        setSkillButtonsEnabled(false);
        skillButtonPanel.removeAll();
        BattleSound.playGameOver();
        JButton menuBtn = CharacterSelectPanel.makeButton("🏠 Main Menu", Theme.GOLD);
        menuBtn.addActionListener(e -> onBattleEnd.run());
        skillButtonPanel.add(menuBtn);
        skillButtonPanel.revalidate();
        skillButtonPanel.repaint();
    }

    private void updateBars() {
        p1HpBar.setTarget(player1.getHealth(), player1.getMaxHealth());
        p1MpBar.setTarget(player1.getMana(), player1.getMaxMana());
        p2HpBar.setTarget(player2.getHealth(), player2.getMaxHealth());
        p2MpBar.setTarget(player2.getMana(), player2.getMaxMana());
    }

    private void updateRoundLabel() {
        roundLabel.setText("Round " + Math.min(currentRound, MAX_ROUNDS) + " / " + MAX_ROUNDS
                + "   " + p1Wins + " - " + p2Wins);
    }

    private void log(String msg) {
        SwingUtilities.invokeLater(() -> {
            battleLog.append(msg + "\n");
            battleLog.setCaretPosition(battleLog.getDocument().getLength());
        });
    }

    private void setSkillButtonsEnabled(boolean enabled) {
        for (Component c : skillButtonPanel.getComponents()) {
            if (c instanceof JButton btn) {
                if (btn.getText().contains("Flee")) {
                    btn.setEnabled(true);
                    continue;
                }
                if (enabled) {
                    int idx = skillButtonPanel.getComponentZOrder(btn);
                    GameCharacter actor = player1Turn ? player1 : player2;
                    if (idx < actor.getSkills().size()) {
                        btn.setEnabled(actor.getSkills().get(idx).isReady());
                    }
                } else {
                    btn.setEnabled(false);
                }
            }
        }
    }

    // ── NEW: Self-Destructing VFX Canvas ─────────────────────────────────────
    private class VFXCanvas extends JPanel {
        private final int maxFrames;
        private int currentFrame = 0;
        private final BufferedImage[] frames;
        private Timer timer = null;

        public VFXCanvas(String effectName, int size, Runnable onComplete) {
            setOpaque(false);
            setSize(size, size);

            maxFrames = SpriteLoader.getVFXFrameCount(effectName);
            frames = new BufferedImage[maxFrames];
            for (int i = 0; i < maxFrames; i++) {
                frames[i] = SpriteLoader.getVFXFrame(effectName, i, size);
            }

            // Run at a fast 60ms interval (~16 FPS) for snappy magic effects
            timer = new Timer(60, e -> {
                currentFrame++;
                if (currentFrame >= maxFrames) {
                    timer.stop();
                    if (onComplete != null) onComplete.run();
                    Container parent = getParent();
                    if (parent != null) {
                        parent.remove(this);
                        parent.repaint();
                    }
                } else {
                    repaint();
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentFrame < maxFrames && frames[currentFrame] != null) {
                g.drawImage(frames[currentFrame], 0, 0, null);
            }
        }
    }

    // ── Arena Panel ──────────────────────────────────────────────────────────
    private static class ArenaPanel extends JPanel {
        private final SpriteCanvas p1Sprite, p2Sprite;
        private BufferedImage bg;
        private final int bgIdx;
        private int bgW = -1, bgH = -1;
        public final JLabel roundBanner;

        ArenaPanel(SpriteCanvas p1Sprite, SpriteCanvas p2Sprite) {
            this.p1Sprite = p1Sprite;
            this.p2Sprite = p2Sprite;

            bgIdx = (int) (Math.random() * 4);

            setLayout(null);
            setBackground(Theme.BG_DEEP);

            roundBanner = new JLabel("", SwingConstants.CENTER);
            roundBanner.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 42));
            roundBanner.setForeground(Color.WHITE);
            roundBanner.setVisible(false);

            add(roundBanner);
            add(p1Sprite);
            add(p2Sprite);
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            int w = getWidth(), h = getHeight();
            if (w > 0 && h > 0 && (bg == null || w != bgW || h != bgH)) {
                bg = SpriteLoader.getBackground(bgIdx, w, h);
                bgW = w;
                bgH = h;
            }
            if (bg != null) {
                Graphics2D g = (Graphics2D) g0.create();
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setColor(new Color(0, 0, 0, 60));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.dispose();
            }
        }

        @Override
        public void doLayout() {
            int w = getWidth(), h = getHeight();
            int spriteSize = Math.max(140, Math.min(280, Math.min(w, h) / 3));
            int marginX = Math.max(30, w / 14);
            int groundY = h - spriteSize - Math.max(14, h / 18);

            p1Sprite.setDisplaySize(spriteSize);
            p2Sprite.setDisplaySize(spriteSize);

            p1Sprite.setBounds(marginX, groundY, spriteSize, spriteSize);
            p2Sprite.setBounds(w - marginX - spriteSize, groundY, spriteSize, spriteSize);

            roundBanner.setBounds(0, h / 2 - 80, w, 120);
        }
    }
}