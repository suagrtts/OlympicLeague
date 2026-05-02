package gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * SpriteLoader — loads real PNG sprite strips from the JAR resources folder.
 *
 * Each sprite strip is a horizontal sequence of frames (all same width).
 * Call getFrame(name, frameIndex) to get one frame as a BufferedImage.
 * Call getIdleFrame(characterName) for the static portrait used in roster/select.
 *
 * Character → sprite mapping (Vor and Samurai excluded):
 *   Achiron   → lancer_yellow  (Yellow Lancer = Spartan vibe)
 *   Atalyn    → archer_red     (Red Archer = Hunter)
 *   Heralde   → lancer_black   (Black Lancer = Zeus champion)
 *   TinySwords→ tinyswords_blue (Blue Warrior = dual blade)
 *   Orris     → tinyswords_red  (Red Warrior = sea warrior)
 *   Orven     → tinyswords_purple (Purple Warrior = swift herald)
 *   Biji      → monk_purple    (Purple Monk = musician/healer)
 *   Selwyn    → monk_blue      (Blue Monk = trickster)
 *   GoatedKit → archer_blue    (Blue Archer = ranger)
 *   Skeleton  → skeleton       (actual Skeleton sprites)
 *   EvilWizard→ evilwizard     (actual Evil Wizard 2 sprites)
 *   SirKhai   → lancer_red     (Red Lancer = champion)
 */
public class SpriteLoader {

    private static final Map<String, BufferedImage[]> cache = new HashMap<>();

    // Maps character class name → (idle_strip_resource, attack_strip_resource, n_idle, n_attack)
    private static final String[][] CHAR_SPRITES = {
        // {charName, idleRes, attackRes, nIdle, nAttack, hurtRes, nHurt}
        {"Achiron",    "lancer_yellow_idle",   "lancer_yellow_attack", "12","3","lancer_yellow_idle","2"},
        {"Atalyn",     "archer_red_idle",       "archer_red_attack",    "6", "8","archer_red_idle",   "2"},
        {"Heralde",    "lancer_black_idle",     "lancer_black_attack",  "12","3","lancer_black_idle", "2"},
        {"TinySwords", "tinyswords_blue_idle",  "tinyswords_blue_attack1","8","4","tinyswords_blue_guard","6"},
        {"Orris",      "tinyswords_red_idle",   "tinyswords_red_attack1","8","4","tinyswords_red_guard","6"},
        {"Orven",      "tinyswords_purple_idle","tinyswords_purple_attack1","8","4","tinyswords_purple_guard","6"},
        {"Biji",       "monk_purple_idle",      "monk_purple_heal",     "6","11","monk_purple_idle",  "2"},
        {"Selwyn",     "monk_blue_idle",        "monk_blue_heal",       "6","11","monk_blue_idle",    "2"},
        {"GoatedKit",  "archer_blue_idle",      "archer_blue_attack",   "6", "8","archer_blue_idle",  "2"},
        {"Skeleton",   "skeleton_idle",         "skeleton_attack",      "8","18","skeleton_hurt",     "8"},
        {"EvilWizard", "evilwizard_idle",       "evilwizard_attack1",   "8", "8","evilwizard_hurt",   "3"},
        {"SirKhai",    "lancer_red_idle",       "lancer_red_attack",    "12","3","lancer_red_idle",   "2"},
    };

    public enum AnimType { IDLE, ATTACK, HURT }

    public static int getFrameCount(String charName, AnimType anim) {
        for (String[] row : CHAR_SPRITES) {
            if (row[0].equals(charName)) {
                return switch (anim) {
                    case IDLE   -> Integer.parseInt(row[3]);
                    case ATTACK -> Integer.parseInt(row[4]);
                    case HURT   -> Integer.parseInt(row[6]);
                };
            }
        }
        return 1;
    }

    /** Returns a single frame from the given animation type */
    public static BufferedImage getFrame(String charName, AnimType anim, int frameIdx, int size) {
        String res = getResName(charName, anim);
        if (res == null) return makeFallback(size);
        BufferedImage[] frames = loadStrip(res, getFrameCount(charName, anim), size);
        if (frames == null || frames.length == 0) return makeFallback(size);
        return frames[frameIdx % frames.length];
    }

    /** Convenience: idle frame 0 for portrait use */
    public static BufferedImage getIdleFrame(String charName, int size) {
        return getFrame(charName, AnimType.IDLE, 0, size);
    }

    private static String getResName(String charName, AnimType anim) {
        for (String[] row : CHAR_SPRITES) {
            if (row[0].equals(charName)) {
                return switch (anim) {
                    case IDLE   -> row[1];
                    case ATTACK -> row[2];
                    case HURT   -> row[5];
                };
            }
        }
        return null;
    }

    private static final Map<String, BufferedImage> rawCache = new HashMap<>();

    private static BufferedImage[] loadStrip(String resName, int n, int size) {
        BufferedImage raw = rawCache.get(resName);
        if (raw == null) {
            String path = "/sprites/" + resName + ".png";
            try (InputStream is = SpriteLoader.class.getResourceAsStream(path)) {
                if (is == null) return null;
                raw = ImageIO.read(is);
                rawCache.put(resName, raw);
            } catch (Exception e) {
                return null;
            }
        }
        return splitStrip(raw, n, size);
    }

    private static BufferedImage[] splitStrip(BufferedImage strip, int n, int size) {
        if (strip == null) return null;
        int fw = strip.getWidth() / n;
        int fh = strip.getHeight();
        BufferedImage[] frames = new BufferedImage[n];
        for (int i = 0; i < n; i++) {
            BufferedImage frame = strip.getSubimage(i * fw, 0, fw, fh);
            // Scale to square
            BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = scaled.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                               RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            // Fit maintaining aspect ratio
            double scale = Math.min((double)size/fw, (double)size/fh);
            int dw = (int)(fw*scale), dh = (int)(fh*scale);
            int dx = (size-dw)/2, dy = (size-dh)/2;
            g.drawImage(frame, dx, dy, dw, dh, null);
            g.dispose();
            frames[i] = scaled;
        }
        return frames;
    }

    /** Flip a frame horizontally for the right-side fighter */
    public static BufferedImage flipH(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight();
        BufferedImage flipped = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = flipped.createGraphics();
        g.drawImage(src, w, 0, -w, h, null);
        g.dispose();
        return flipped;
    }

    private static BufferedImage makeFallback(int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(0x40, 0x40, 0x60));
        g.fillRect(0, 0, size, size);
        g.setColor(new Color(0xC9, 0xA8, 0x4C));
        g.setFont(new Font("SansSerif", Font.BOLD, size / 5));
        g.drawString("?", size/2 - size/12, size/2 + size/10);
        g.dispose();
        return img;
    }

    /** Load a battleground background scaled to given dimensions */
    public static BufferedImage getBattleground(int index, int w, int h) {
        String path = "/backgrounds/battleground" + (index + 1) + ".png";
        try (InputStream is = SpriteLoader.class.getResourceAsStream(path)) {
            if (is == null) return null;
            BufferedImage raw = ImageIO.read(is);
            BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = scaled.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(raw, 0, 0, w, h, null);
            g.dispose();
            return scaled;
        } catch (Exception e) {
            return null;
        }
    }

    /** Load effect frames for a skill animation */
    public static BufferedImage[] getEffectFrames(String effectName, int size) {
        java.util.List<BufferedImage> frames = new java.util.ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            String num = String.format("%02d", i);
            String path = "/effects/" + effectName + "/" + effectName + "_" + num + ".png";
            try (InputStream is = SpriteLoader.class.getResourceAsStream(path)) {
                if (is == null) break;
                BufferedImage raw = ImageIO.read(is);
                BufferedImage scaled = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = scaled.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(raw, 0, 0, size, size, null);
                g.dispose();
                frames.add(scaled);
            } catch (Exception e) { break; }
        }
        return frames.toArray(new BufferedImage[0]);
    }
}
