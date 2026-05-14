package olympicleague.gui.panels;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.*;
import java.util.function.Consumer;

import olympicleague.assets.SpriteLoader;
import olympicleague.assets.Theme;
import olympicleague.audio.BattleSound;
import olympicleague.character.*;
import olympicleague.gui.components.AnimatedBar;
import olympicleague.gui.components.SpriteCanvas;


public class BattlePanel extends JPanel {

    private final GameCharacter player1;
    private final GameCharacter player2;
    private final boolean isAI;
    /** When true, match victory/defeat stingers are handled by {@link ArcadePanel} (no duplicate grand/gameover). */
    private final boolean arcadeRun;
    private final Consumer<BattleResult> onBattleEnd;

    // State
    private boolean player1Turn = true;
    private boolean battleOver = false;
    private boolean turnInProgress = false;
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
    private static final int ATTACK_PAUSE_MS = 350;

    public BattlePanel(GameCharacter player1, GameCharacter player2, boolean isAI, Consumer<BattleResult> onBattleEnd) {
        this(player1, player2, isAI, onBattleEnd, false);
    }

    public BattlePanel(GameCharacter player1, GameCharacter player2, boolean isAI, Consumer<BattleResult> onBattleEnd,
                       boolean arcadeRun) {
        this.player1 = player1;
        this.player2 = player2;
        this.isAI = isAI;
        this.arcadeRun = arcadeRun;
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
            endBattle(BattleResult.PLAYER_FLED);
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
        turnInProgress = false;

        arenaPanel.roundBanner.setVisible(false);

        updateBars();
        updateRoundLabel();
        log("=== Round " + currentRound + " ===");
        log(player1.getName() + " vs " + player2.getName());
        BattleSound.stopBattleBgm(); // stop previous instance before starting new
        BattleSound.playBattleBgm();
        setPlayerTurn(true);
    }

    private void setPlayerTurn(boolean isPlayer1) {
        player1Turn = isPlayer1;
        turnInProgress = false;

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
        if (battleOver || turnInProgress) return;
        turnInProgress = true;

        GameCharacter target = actor == player1 ? player2 : player1;
        setSkillButtonsEnabled(false);
        actor.updateTurnEffects();

        if (actor.isStunned()) {
            log(actor.getName() + " is stunned and cannot act!");
            actor.setStunned(false);

            boolean nextTurnP1 = !(actor == player1);
            Timer switchTimer = new Timer(700, e -> setPlayerTurn(nextTurnP1));
            switchTimer.setRepeats(false);
            switchTimer.start();
        } else {
            Skill usedSkill = actor.getSkills().get(skillIndex);
            String result = actor.takeTurn(target, skillIndex);
            log(actor.getName() + ": " + result);
            actor.restoreMana(150);
            target.restoreMana(150);
            updateBars();

            BattleSound.playSkillSfx(usedSkill, result, actor);
            playSkillAnimation(actor, target, result, skillIndex);
        }
    }

    private void doAITurn() {
        if (battleOver) return;
        turnInProgress = true;
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

            BattleSound.playSkillSfx(player2.getLastUsedSkill(), result, player2);
            playSkillAnimation(player2, player1, result, -1);
        }
    }

    /** Characters that use a Monk sprite — they use fantasy_spells VFX for skill 1. */
    private static boolean isMonkCharacter(GameCharacter c) {
        String n = c.getName();
        return "Biji".equals(n) || "Selwyn".equals(n) || "TinySwords".equals(n);
    }

    /** Returns a random fantasy_spells VFX name for monk skill 1. */
    private static String randomFantasySpell() {
        String[] spells = {"FS_Death", "FS_AttackUp", "FS_DefenseUp", "FS_Absorb", "FS_Poison", "FS_Haste", "FS_Heal"};
        return spells[(int)(Math.random() * spells.length)];
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

        double r = Math.random();
        if (r < 0.33) return "HolySlash_A";
        else if (r < 0.66) return "HolySlash_B";
        else return "HolySlash_C";
    }

    // ── Floating Text Helpers ────────────────────────────────────────────────
    private String extractDamageFromLog(String log) {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("(\\d+)").matcher(log);
        String last = "";
        while (m.find()) last = m.group(1);
        return last;
    }

    private boolean isTrueDamage(String log) {
        return log.toLowerCase().contains("true");
    }

    private boolean isHeal(String log) {
        return log.toLowerCase().contains("heal") || log.toLowerCase().contains("recovered");
    }

    private void showFloatingText(String text, Color color, SpriteCanvas targetSprite) {
        if (text == null || text.isEmpty()) return;
        Point loc = targetSprite.getLocation();
        int cx = loc.x + targetSprite.getWidth() / 2;
        int cy = loc.y;
        FloatingText ft = new FloatingText(text, color, new Point(cx, cy));
        arenaPanel.add(ft, 0);
        arenaPanel.revalidate();
        arenaPanel.repaint();
    }

    private void playSkillAnimation(GameCharacter actor, GameCharacter target, String resultLog, int skillIndex) {
        String lowerLog = resultLog.toLowerCase();

        if (skillIndex == 2) {                  // index 2 = skill 3 (0-based)
            BattleSound.playUltimate();
        } else if (BattleSound.isSwordSkillLog(resultLog)) {
            BattleSound.playSwordCut();
        } else if (isHeal(resultLog)) {
            BattleSound.playHeal();
        }

        boolean isOffensive = BattleFxUtil.isOffensiveDamageLog(resultLog);

        boolean isEvade = lowerLog.contains("evade") || lowerLog.contains("untargetable") ||
                lowerLog.contains("elusive") || lowerLog.contains("cannot be targeted") ||
                lowerLog.contains("vanish");

        boolean isPowerUp = lowerLog.contains("gained")    || lowerLog.contains("increased") ||
                lowerLog.contains("surges")   || lowerLog.contains("power")    ||
                lowerLog.contains("wrath")    || lowerLog.contains("mark")     ||
                lowerLog.contains("activated")|| lowerLog.contains("damage for");

        boolean actorIsP1 = actor == player1;
        boolean nextTurnP1 = !actorIsP1;

        SpriteCanvas attackerSprite = actorIsP1 ? p1Sprite : p2Sprite;
        SpriteCanvas defenderSprite = actorIsP1 ? p2Sprite : p1Sprite;

        Point startLoc = attackerSprite.getLocation();
        String vfxName = determineVFX(resultLog);

        // ── Fantasy Spells override: monk characters always use FS VFX for skill 1 ──
        if (skillIndex == 0 && isMonkCharacter(actor)) {
            vfxName = randomFantasySpell();
        }

        // ─── 1. OFFENSIVE SKILL ────────────────────────────────────────────────
        if (isOffensive) {
            Point targetLoc = defenderSprite.getLocation();

            if (BattleFxUtil.isArcherStyleCharacter(actor)) {
                attackerSprite.setAnimType(SpriteLoader.AnimType.SHOOT);

                int arrowStartX = actorIsP1 ? startLoc.x + 180 : startLoc.x - 20;
                int arrowStartY = startLoc.y + 60;
                int arrowEndX   = actorIsP1 ? targetLoc.x + 20 : targetLoc.x + 180;
                int arrowEndY   = targetLoc.y + 60;

                String finalVfxName = vfxName;
                Timer launchDelay = new Timer(200, e -> {
                    ArrowCanvas arrow = new ArrowCanvas(actor.getName(), actorIsP1,
                            new Point(arrowStartX, arrowStartY),
                            new Point(arrowEndX,   arrowEndY),
                            500,
                            () -> {
                                defenderSprite.setAnimType(SpriteLoader.AnimType.HURT);

                                // ── floating damage on arrow landing ──
                                String dmgText = extractDamageFromLog(resultLog);
                                Color dmgColor = isTrueDamage(resultLog)
                                        ? new Color(0xFF, 0x66, 0x00)
                                        : new Color(0xFF, 0x44, 0x44);
                                showFloatingText(dmgText, dmgColor, defenderSprite);

                                try {
                                    VFXCanvas vfx = new VFXCanvas(finalVfxName, 250, null, !actorIsP1);
                                    vfx.setLocation(targetLoc.x - 25, targetLoc.y - 25);
                                    arenaPanel.add(vfx, 0);
                                    arenaPanel.repaint();
                                } catch (Exception ex) {
                                    System.err.println("VFX Error: " + finalVfxName);
                                }
                                Timer afterHit = new Timer(ATTACK_PAUSE_MS, e2 -> {
                                    attackerSprite.setAnimType(SpriteLoader.AnimType.IDLE);
                                    if (!target.isAlive()) {
                                        handleRoundEnd();
                                    } else {
                                        defenderSprite.setAnimType(SpriteLoader.AnimType.IDLE);
                                        Timer switchTimer = new Timer(200, e3 -> setPlayerTurn(nextTurnP1));
                                        switchTimer.setRepeats(false);
                                        switchTimer.start();
                                    }
                                });
                                afterHit.setRepeats(false);
                                afterHit.start();
                            });
                    arenaPanel.add(arrow, 0);
                    arenaPanel.repaint();
                });
                launchDelay.setRepeats(false);
                launchDelay.start();

            } else {
                int offset = actorIsP1 ? -100 : 100;
                Point attackPoint = new Point(targetLoc.x + offset, targetLoc.y);

                String finalVfxName1 = vfxName;
                animateMovement(attackerSprite, startLoc, attackPoint, DASH_DURATION_MS, () -> {
                    attackerSprite.setAnimType(BattleFxUtil.resolveAttackAnim(actor, resultLog));
                    defenderSprite.setAnimType(SpriteLoader.AnimType.HURT);

                    // ── floating damage on melee hit ──
                    String dmgText = extractDamageFromLog(resultLog);
                    Color dmgColor = isTrueDamage(resultLog)
                            ? new Color(0xFF, 0x66, 0x00)
                            : new Color(0xFF, 0x44, 0x44);
                    showFloatingText(dmgText, dmgColor, defenderSprite);

                    try {
                        VFXCanvas vfx = new VFXCanvas(finalVfxName1, 250, null, !actorIsP1);
                        vfx.setLocation(targetLoc.x - 25, targetLoc.y - 25);
                        arenaPanel.add(vfx, 0);
                        arenaPanel.repaint();
                    } catch (Exception ex) {
                        System.err.println("VFX Error: " + finalVfxName1);
                    }

                    Timer attackPause = new Timer(ATTACK_PAUSE_MS, e -> {
                        attackerSprite.setAnimType(SpriteLoader.AnimType.IDLE);
                        Point currentAttackPoint = attackerSprite.getLocation();
                        if (!target.isAlive()) {
                            animateMovement(attackerSprite, currentAttackPoint, startLoc, DASH_DURATION_MS, () -> handleRoundEnd());
                        } else {
                            defenderSprite.setAnimType(SpriteLoader.AnimType.IDLE);
                            animateMovement(attackerSprite, currentAttackPoint, startLoc, DASH_DURATION_MS, () -> {
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
        }
        // ─── 2. EVASIVE SKILL ─────────────────────────────────────────────────
        else if (isEvade) {
            int backDir = actorIsP1 ? -60 : 60;
            Point dodgePoint = new Point(startLoc.x + backDir, startLoc.y);

            // ── floating shield indicator ──
            showFloatingText("EVADE", Theme.GOLD, attackerSprite);

            try {
                VFXCanvas vfx = new VFXCanvas(vfxName, 200, null, !actorIsP1);
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
        // ─── 3. POWER UP SKILL ────────────────────────────────────────────────
        else if (isPowerUp) {
            // ── floating buff indicator ──
            showFloatingText("ATK UP!", new Color(0xFF, 0xD7, 0x00), attackerSprite);

            try {
                VFXCanvas vfx = new VFXCanvas(vfxName, 300, null, !actorIsP1);
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
        // ─── 4. HEAL / SHIELD SKILL ───────────────────────────────────────────
        else {
            Point hoverPoint = new Point(startLoc.x, startLoc.y - 60);

            // ── floating heal number ──
            if (isHeal(resultLog)) {
                String healText = "+" + extractDamageFromLog(resultLog);
                showFloatingText(healText, new Color(0x2E, 0xCC, 0x71), attackerSprite);
            } else {
                showFloatingText("SHIELD", new Color(0x52, 0x98, 0xD0), attackerSprite);
            }

            try {
                VFXCanvas vfx = new VFXCanvas(vfxName, 250, null, !actorIsP1);
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
        if (player1.isAlive() && !player2.isAlive()) {
            p1Wins++;
            winner = player1.getName() + " wins Round " + currentRound + "!";
        } else if (!player1.isAlive() && player2.isAlive()) {
            p2Wins++;
            winner = player2.getName() + " wins Round " + currentRound + "!";
        } else {
            winner = "Round " + currentRound + " — Draw!";
        }
        log(winner);
        log("Score: " + p1Wins + " - " + p2Wins);
        updateRoundLabel();

        boolean p1WonRound = player1.isAlive() && !player2.isAlive();
        boolean p2WonRound = !player1.isAlive() && player2.isAlive();
        if (p1WonRound || p2WonRound) {
            if (!isAI || p1WonRound) {
                BattleSound.playRoundWin();
            }
        }

        arenaPanel.roundBanner.setText(winner);
        arenaPanel.roundBanner.setVisible(true);

        currentRound++;

        if (p1Wins >= 2 || p2Wins >= 2 || currentRound > MAX_ROUNDS) {
            Timer endTimer = new Timer(2000, e -> {
                String matchWinner = "Match ends in a Draw!";

                BattleResult result;

                if (p1Wins > p2Wins) {
                    matchWinner = "🏆 " + player1.getName() + " WINS MATCH! 🏆";
                    result = BattleResult.PLAYER_WIN;
                } else {
                    matchWinner = "🏆 " + player2.getName() + " WINS MATCH! 🏆";
                    result = BattleResult.PLAYER_LOSE;
                }

                log(matchWinner);
                arenaPanel.roundBanner.setText(matchWinner);

                if (isAI) {
                    if (p1Wins > p2Wins && !arcadeRun) {
                        BattleSound.playGrandWinner();
                    } else if (p2Wins > p1Wins) {
                        BattleSound.playGameOver();
                    }
                }

                endBattle(result);
            });
            endTimer.setRepeats(false);
            endTimer.start();
        } else {
            Timer nextRound = new Timer(2000, e -> startRound());
            nextRound.setRepeats(false);
            nextRound.start();
        }
    }

    private void endBattle(BattleResult result) {
        battleOver = true;

        setSkillButtonsEnabled(false);

        skillButtonPanel.removeAll();

        JButton menuBtn =
                CharacterSelectPanel.makeButton("Next Opponent", Theme.GOLD);

        menuBtn.addActionListener(e -> onBattleEnd.accept(result));

        skillButtonPanel.add(menuBtn);

        skillButtonPanel.revalidate();
        skillButtonPanel.repaint();

        BattleSound.stopBattleBgm();
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
        Component[] comps = skillButtonPanel.getComponents();
        int skillIdx = 0;
        GameCharacter actor = player1Turn ? player1 : player2;

        for (Component c : comps) {
            if (!(c instanceof JButton btn)) continue;

            if (btn.getText().contains("Flee") || btn.getText().contains("🏃")) {
                btn.setEnabled(true);
                continue;
            }

            if (!enabled) {
                btn.setEnabled(false);
            } else if (skillIdx < actor.getSkills().size()) {
                Skill s = actor.getSkills().get(skillIdx);
                btn.setEnabled(s.isReady());
                btn.setText(s.getName() + (s.isReady() ? "" : " (" + s.getCurrentCooldown() + ")"));
                btn.setBackground(s.isReady() ? Theme.BG_CARD2 : Theme.SKILL_CD);
                btn.setForeground(s.isReady() ? Theme.TEXT_LIGHT : Theme.TEXT_DIM);
            }
            skillIdx++;
        }
    }

    // ── Floating Damage / Heal Numbers ───────────────────────────────────────
    private class FloatingText extends JLabel {
        private float alpha = 1.0f;
        private int yOffset = 0;
        private final Timer timer;

        public FloatingText(String text, Color color, Point location) {
            setText(text);
            setFont(new Font("SansSerif", Font.BOLD, 22));
            setForeground(color);
            setSize(120, 40);
            setHorizontalAlignment(SwingConstants.CENTER);
            setLocation(location.x - 60, location.y - 20);

            timer = new Timer(30, null);
            timer.addActionListener(e -> {
                yOffset -= 3;
                alpha -= 0.04f;
                setLocation(getX(), location.y - 20 + yOffset);
                repaint();

                if (alpha <= 0) {
                    timer.stop();
                    Container parent = getParent();
                    if (parent != null) {
                        parent.remove(this);
                        parent.repaint();
                    }
                }
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, alpha)));

            g2.setColor(new Color(0, 0, 0, 180));
            g2.setFont(getFont());
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = fm.getAscent();
            g2.drawString(getText(), x + 1, y + 1);

            g2.setColor(getForeground());
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    }

    // ── VFX Canvas ───────────────────────────────────────────────────────────
    private class VFXCanvas extends JPanel {
        private final int maxFrames;
        private int currentFrame = 0;
        private final BufferedImage[] frames;
        private final boolean flipped;
        private Timer timer = null;

        public VFXCanvas(String effectName, int size, Runnable onComplete, boolean flipped) {
            this.flipped = flipped;
            setOpaque(false);
            setSize(size, size);

            maxFrames = SpriteLoader.getVFXFrameCount(effectName);
            frames = new BufferedImage[maxFrames];
            for (int i = 0; i < maxFrames; i++) {
                frames[i] = SpriteLoader.getVFXFrame(effectName, i, size);
            }

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
        }

        @Override
        public void addNotify() {
            super.addNotify();
            if (timer != null && !timer.isRunning()) {
                timer.start();
            }
        }
    }

    // ── Arena Panel ──────────────────────────────────────────────────────────
    private static class ArenaPanel extends JPanel {
        private final SpriteCanvas p1Sprite, p2Sprite;
        private BufferedImage bg;
        public final JLabel roundBanner;

        ArenaPanel(SpriteCanvas p1Sprite, SpriteCanvas p2Sprite) {
            this.p1Sprite = p1Sprite;
            this.p2Sprite = p2Sprite;

            int bgIdx = (int) (Math.random() * 4);
            bg = SpriteLoader.getBattleground(bgIdx, 900, 360);

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
            int spriteSize = 200;

            p1Sprite.setBounds(80, h - spriteSize - 20, spriteSize, spriteSize);
            p2Sprite.setBounds(w - 80 - spriteSize, h - spriteSize - 20, spriteSize, spriteSize);

            roundBanner.setBounds(0, h / 2 - 80, w, 120);
        }
    }

    // ── Arrow Canvas ─────────────────────────────────────────────────────────
    private class ArrowCanvas extends JPanel {
        private final BufferedImage arrowImg;
        private Point current;
        private final Point end;
        private final Runnable onArrival;

        ArrowCanvas(String charName, boolean facingRight, Point start, Point end,
                    int durationMs, Runnable onArrival) {
            this.end       = end;
            this.current   = new Point(start);
            this.onArrival = onArrival;

            String arrowPath = resolveArrowPath(charName);
            BufferedImage raw = null;
            try (java.io.InputStream is = SpriteLoader.class.getResourceAsStream(arrowPath)) {
                if (is != null) raw = javax.imageio.ImageIO.read(is);
            } catch (Exception ex) {
                System.err.println("ArrowCanvas: failed to load " + arrowPath);
            }

            if (raw != null) {
                int sz = 48;
                java.awt.image.BufferedImage scaled =
                        new java.awt.image.BufferedImage(sz, sz, java.awt.image.BufferedImage.TYPE_INT_ARGB);
                Graphics2D sg = scaled.createGraphics();
                sg.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                sg.drawImage(raw, 0, 0, sz, sz, null);
                sg.dispose();
                arrowImg = facingRight ? scaled : SpriteLoader.flipH(scaled);
            } else {
                arrowImg = null;
            }

            setOpaque(false);
            setSize(48, 48);
            setLocation(start);

            long t0 = System.currentTimeMillis();
            Timer t = new Timer(16, null);
            t.addActionListener(e -> {
                float pct = Math.min(1f, (float)(System.currentTimeMillis() - t0) / durationMs);
                int cx = (int)(start.x + (end.x - start.x) * pct);
                int cy = (int)(start.y + (end.y - start.y) * pct);
                setLocation(cx, cy);
                repaint();
                if (pct >= 1f) {
                    ((Timer) e.getSource()).stop();
                    Container parent = getParent();
                    if (parent != null) { parent.remove(this); parent.repaint(); }
                    if (onArrival != null) onArrival.run();
                }
            });
            t.start();
        }

        private String resolveArrowPath(String charName) {
            return switch (charName) {
                case "Atalyn"    -> "/sprite/TinyUnits/Red Units/Archer/Arrow.png";
                case "Orven"     -> "/sprite/TinyUnits/Blue Units/Archer/Arrow.png";
                case "GoatedKit" -> "/sprite/TinyUnits/Yellow Units/Archer/Arrow.png";
                default          -> "/sprite/TinyUnits/Red Units/Archer/Arrow.png";
            };
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (arrowImg != null) g.drawImage(arrowImg, 0, 0, null);
        }
    }
}