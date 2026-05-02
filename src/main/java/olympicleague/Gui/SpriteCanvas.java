package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * SpriteCanvas — a JPanel that plays an animated sprite strip in a loop.
 * Used in both the roster cards (small, always looping) and the battle arena.
 */
public class SpriteCanvas extends JPanel {

    private String charName;
    private SpriteLoader.AnimType animType;
    private final int displaySize;
    private final boolean flipped;
    private final int fps;

    private BufferedImage[] frames;
    private int currentFrame = 0;
    private Timer timer;

    public SpriteCanvas(String charName, SpriteLoader.AnimType animType,
                        int displaySize, boolean flipped, int fps) {
        this.charName    = charName;
        this.animType    = animType;
        this.displaySize = displaySize;
        this.flipped     = flipped;
        this.fps         = fps;
        setOpaque(false);
        setPreferredSize(new Dimension(displaySize, displaySize));
        loadFrames();
        startTimer();
    }

    public void setCharacter(String charName, SpriteLoader.AnimType animType) {
        this.charName  = charName;
        this.animType  = animType;
        currentFrame   = 0;
        loadFrames();
        repaint();
    }

    public void setAnimType(SpriteLoader.AnimType animType) {
        if (this.animType == animType) return;
        this.animType = animType;
        currentFrame  = 0;
        loadFrames();
    }

    private void loadFrames() {
        int n = SpriteLoader.getFrameCount(charName, animType);
        frames = new BufferedImage[n];
        for (int i = 0; i < n; i++) {
            BufferedImage f = SpriteLoader.getFrame(charName, animType, i, displaySize);
            frames[i] = flipped ? SpriteLoader.flipH(f) : f;
        }
    }

    private void startTimer() {
        if (timer != null) timer.stop();
        int delay = 1000 / Math.max(1, fps);
        timer = new Timer(delay, e -> {
            if (frames != null && frames.length > 0) {
                currentFrame = (currentFrame + 1) % frames.length;
                repaint();
            }
        });
        timer.start();
    }

    public void stopAnimation() { if (timer != null) timer.stop(); }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        if (frames == null || frames.length == 0) return;
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        BufferedImage f = frames[currentFrame % frames.length];
        int x = (getWidth()  - displaySize) / 2;
        int y = (getHeight() - displaySize) / 2;
        g.drawImage(f, x, y, displaySize, displaySize, null);
        g.dispose();
    }
}
