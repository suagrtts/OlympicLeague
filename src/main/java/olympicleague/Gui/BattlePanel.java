package Gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BattlePanel extends JPanel {

    // Dynamically create the transparent border using the Theme's TITLE_COLOR
    private final Color BORDER_DIM = new Color(
            Theme.TITLE_COLOR.getRed(),
            Theme.TITLE_COLOR.getGreen(),
            Theme.TITLE_COLOR.getBlue(),
            64
    );

    private ArenaCanvas canvas;
    private JPanel buttonPanel;

    public BattlePanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.PANEL_DARK); // Replaced BG_DEEP

        canvas = new ArenaCanvas();
        add(canvas, BorderLayout.CENTER);

        buttonPanel = new JPanel();
        buttonPanel.setBackground(Theme.PANEL_DARK);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Replaced hardcoded accent colors with Theme constants
        JButton attackBtn = createThemeButton("ATTACK", Theme.ACCENT_RED);
        JButton healBtn = createThemeButton("HEAL", Theme.ACCENT_BLUE);
        JButton skill3Btn = createThemeButton("SKILL 3", Theme.ACCENT_PURPLE);
        JButton stunBtn = createThemeButton("STUN", Theme.TITLE_COLOR);

        attackBtn.addActionListener(e -> canvas.triggerAction("ATTACK"));
        healBtn.addActionListener(e -> canvas.triggerAction("HEAL"));
        skill3Btn.addActionListener(e -> canvas.triggerAction("SKILL3"));
        stunBtn.addActionListener(e -> canvas.triggerAction("STUN"));

        buttonPanel.add(attackBtn);
        buttonPanel.add(healBtn);
        buttonPanel.add(skill3Btn);
        buttonPanel.add(stunBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createThemeButton(String text, Color accentColor) {
        JButton btn = new JButton(text);
        btn.setBackground(Theme.PANEL_LIGHT); // Replaced BG_CARD
        btn.setForeground(accentColor);
        btn.setFont(Theme.SUBTITLE); // Replaced hardcoded Serif font
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DIM, 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Theme.TITLE_COLOR, 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                // Uses your Theme.highlight() method to make the button text glow!
                btn.setForeground(Theme.highlight(accentColor, 1.5f));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_DIM, 1),
                        BorderFactory.createEmptyBorder(10, 20, 10, 20)
                ));
                btn.setForeground(accentColor);
            }
        });

        return btn;
    }

    public void startBattle() {
        canvas.start();
    }

    private class ArenaCanvas extends JPanel implements Runnable {
        private Thread thread;
        private boolean running;
        private List<Particle> particles;
        private String p1State = "IDLE";
        private String p2State = "IDLE";

        public ArenaCanvas() {
            setPreferredSize(new Dimension(800, 400));
            setBackground(Theme.PANEL_DARK);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_DIM));
            particles = new ArrayList<>();
        }

        public void start() {
            if (thread == null) {
                running = true;
                thread = new Thread(this);
                thread.start();
            }
        }

        public void triggerAction(String action) {
            p1State = action;
            if (action.equals("ATTACK") || action.equals("SKILL3")) {
                particles.add(new Projectile(150, 250, 600, 250, action.equals("ATTACK") ? Theme.ACCENT_RED : Theme.ACCENT_PURPLE));
            } else if (action.equals("HEAL")) {
                particles.add(new Spark(125, 250, Theme.ACCENT_BLUE));
            }
        }

        private void update() {
            Iterator<Particle> iterator = particles.iterator();
            while (iterator.hasNext()) {
                Particle p = iterator.next();
                p.update();
                if (!p.isActive()) {
                    iterator.remove();
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setFont(Theme.SUBTITLE); // Consistent thematic font

            // Text color highlighted for better readability against dark backgrounds
            g2d.setColor(Theme.highlight(Theme.TITLE_COLOR, 1.2f));
            g2d.drawString("PLAYER 1: " + p1State, 50, 40);
            g2d.drawString("PLAYER 2: " + p2State, 620, 40);

            // Player 1 Box
            g2d.setColor(Theme.PANEL_LIGHT);
            g2d.fillRoundRect(100, 180, 70, 120, 10, 10);
            g2d.setColor(Theme.TITLE_COLOR);
            g2d.drawRoundRect(100, 180, 70, 120, 10, 10);

            // Player 2 Box
            g2d.setColor(Theme.PANEL_LIGHT);
            g2d.fillRoundRect(630, 180, 70, 120, 10, 10);
            g2d.setColor(Theme.ACCENT_RED);
            g2d.drawRoundRect(630, 180, 70, 120, 10, 10);

            Iterator<Particle> iterator = particles.iterator();
            while (iterator.hasNext()) {
                Particle p = iterator.next();
                p.draw(g2d);
            }
        }

        @Override
        public void run() {
            long lastTime = System.nanoTime();
            double amountOfTicks = 60.0;
            double ns = 1000000000 / amountOfTicks;
            double delta = 0;

            while (running) {
                long now = System.nanoTime();
                delta += (now - lastTime) / ns;
                lastTime = now;

                while (delta >= 1) {
                    update();
                    delta--;
                }
                repaint();

                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private abstract class Particle {
        protected int x;
        protected int y;
        protected Color color;
        protected boolean active = true;

        public abstract void update();
        public abstract void draw(Graphics2D g2d);
        public boolean isActive() {
            return active;
        }
    }

    private class Spark extends Particle {
        private int lifeSpan = 40;

        public Spark(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        @Override
        public void update() {
            y -= 3;
            lifeSpan--;
            if (lifeSpan <= 0) {
                active = false;
            }
        }

        @Override
        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillOval(x, y, 10, 10);
        }
    }

    private class Projectile extends Particle {
        private int targetX;

        public Projectile(int startX, int startY, int targetX, int targetY, Color color) {
            this.x = startX;
            this.y = startY;
            this.targetX = targetX;
            this.color = color;
        }

        @Override
        public void update() {
            x += 15;
            if (x >= targetX) {
                active = false;
            }
        }

        @Override
        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.fillRoundRect(x, y + 40, 25, 8, 4, 4);
        }
    }
}