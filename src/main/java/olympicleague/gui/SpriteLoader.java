package olympicleague.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * SpriteLoader — all sprites use TinyUnits assets as a prototype.
 *
 * To swap a character's sprite later, only edit the CHAR_SPRITES table below.
 * Each row:  { charName, idlePath, attackPath, nIdle, nAttack, hurtPath, nHurt }
 *
 * All paths are relative to src/main/resources (loaded via getResourceAsStream).
 *
 * TinyUnits frame facts (all sheets are square frames, width/height = frame size):
 *   Warrior_Idle     → 8 frames  (192×192 each)
 *   Warrior_Attack1  → 4 frames
 *   Warrior_Attack2  → 4 frames
 *   Warrior_Guard    → 6 frames  (used as "hurt")
 *   Archer_Idle      → 6 frames
 *   Archer_Shoot     → 8 frames
 *   Monk Idle        → 6 frames
 *   Monk Heal        → 11 frames
 *   Lancer_Idle      → 12 frames (320×320 each)
 *   Lancer_Right_Attack → 3 frames
 */
public class SpriteLoader {

    public enum AnimType { IDLE, ATTACK, HURT }

    // ── Character → sprite mapping ───────────────────────────────────────────
    // { charName, idlePath, attackPath, nIdle, nAttack, hurtPath, nHurt }
    // All paths start from /sprite/TinyUnits/... inside resources root.
    private static final String[][] CHAR_SPRITES = {
        // Player characters — each gets a distinct colour/unit type
        {
            "Achiron",
            "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Idle.png",    "8",
            "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Attack1.png", "4",
            "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Guard.png",   "6",
        },
        {
            "Atalyn",
            "/sprite/TinyUnits/Red Units/Archer/Archer_Idle.png",   "6",
            "/sprite/TinyUnits/Red Units/Archer/Archer_Shoot.png",  "8",
            "/sprite/TinyUnits/Red Units/Archer/Archer_Run.png",    "4",
        },
        {
            "Heralde",
            "/sprite/TinyUnits/Black Units/Lancer/Lancer_Idle.png",         "12",
            "/sprite/TinyUnits/Black Units/Lancer/Lancer_Right_Attack.png", "3",
            "/sprite/TinyUnits/Black Units/Lancer/Lancer_Idle.png",         "4",
        },
        {
            "Vor",
            "/sprite/TinyUnits/Purple Units/Warrior/Warrior_Idle.png",    "8",
            "/sprite/TinyUnits/Purple Units/Warrior/Warrior_Attack1.png", "4",
            "/sprite/TinyUnits/Purple Units/Warrior/Warrior_Guard.png",   "6",
        },
        {
            "Orris",
            "/sprite/TinyUnits/Red Units/Warrior/Warrior_Idle.png",    "8",
            "/sprite/TinyUnits/Red Units/Warrior/Warrior_Attack1.png", "4",
            "/sprite/TinyUnits/Red Units/Warrior/Warrior_Guard.png",   "6",
        },
        {
            "Orven",
            "/sprite/TinyUnits/Blue Units/Archer/Archer_Idle.png",   "6",
            "/sprite/TinyUnits/Blue Units/Archer/Archer_Shoot.png",  "8",
            "/sprite/TinyUnits/Blue Units/Archer/Archer_Run.png",    "4",
        },
        {
            "Biji",
            "/sprite/TinyUnits/Purple Units/Monk/Idle.png", "6",
            "/sprite/TinyUnits/Purple Units/Monk/Heal.png", "11",
            "/sprite/TinyUnits/Purple Units/Monk/Run.png",  "4",
        },
        {
            "Selwyn",
            "/sprite/TinyUnits/Blue Units/Monk/Idle.png", "6",
            "/sprite/TinyUnits/Blue Units/Monk/Heal.png", "11",
            "/sprite/TinyUnits/Blue Units/Monk/Run.png",  "4",
        },
        {
            "GoatedKit",
            "/sprite/TinyUnits/Yellow Units/Archer/Archer_Idle.png",   "6",
            "/sprite/TinyUnits/Yellow Units/Archer/Archer_Shoot.png",  "8",
            "/sprite/TinyUnits/Yellow Units/Archer/Archer_Run.png",    "4",
        },
        // AI / enemy characters
        {
            "Skeleton",
            "/sprite/TinyUnits/Black Units/Warrior/Warrior_Idle.png",    "8",
            "/sprite/TinyUnits/Black Units/Warrior/Warrior_Attack1.png", "4",
            "/sprite/TinyUnits/Black Units/Warrior/Warrior_Guard.png",   "6",
        },
        {
            "EvilWizard",
            "/sprite/TinyUnits/Black Units/Monk/Idle.png", "6",
            "/sprite/TinyUnits/Black Units/Monk/Heal.png", "11",
            "/sprite/TinyUnits/Black Units/Monk/Run.png",  "4",
        },
    };
    //  column layout: [0]=charName, then triplets of (path, path, nFrames, path, nFrames, path, nFrames)
    // Simplify access with constants:
    private static final int IDX_IDLE_PATH   = 1;
    private static final int IDX_IDLE_N      = 2;
    private static final int IDX_ATK_PATH    = 3;
    private static final int IDX_ATK_N       = 4;
    private static final int IDX_HURT_PATH   = 5;
    private static final int IDX_HURT_N      = 6;

    // ── Frame count lookup ────────────────────────────────────────────────────
    public static int getFrameCount(String charName, AnimType anim) {
        for (String[] row : CHAR_SPRITES) {
            if (row[0].equals(charName)) {
                return switch (anim) {
                    case IDLE   -> Integer.parseInt(row[IDX_IDLE_N]);
                    case ATTACK -> Integer.parseInt(row[IDX_ATK_N]);
                    case HURT   -> Integer.parseInt(row[IDX_HURT_N]);
                };
            }
        }
        return 1;
    }

    // ── Per-row raw image cache (path → raw sheet) ────────────────────────────
    private static final Map<String, BufferedImage> rawCache = new HashMap<>();

    // ── Scaled frame cache (path+size+frame → frame image) ───────────────────
    private static final Map<String, BufferedImage> frameCache = new HashMap<>();

    // ── Public API ────────────────────────────────────────────────────────────

    /** Get one scaled frame for the given character and animation state. */
    public static BufferedImage getFrame(String charName, AnimType anim, int frameIdx, int size) {
        String[] row = findRow(charName);
        if (row == null) return makeFallback(size, charName);

        String path = switch (anim) {
            case IDLE   -> row[IDX_IDLE_PATH];
            case ATTACK -> row[IDX_ATK_PATH];
            case HURT   -> row[IDX_HURT_PATH];
        };
        int n = switch (anim) {
            case IDLE   -> Integer.parseInt(row[IDX_IDLE_N]);
            case ATTACK -> Integer.parseInt(row[IDX_ATK_N]);
            case HURT   -> Integer.parseInt(row[IDX_HURT_N]);
        };
        frameIdx = frameIdx % Math.max(1, n);

        String cacheKey = path + ":" + frameIdx + ":" + size;
        BufferedImage cached = frameCache.get(cacheKey);
        if (cached != null) return cached;

        BufferedImage sheet = loadSheet(path);
        if (sheet == null) return makeFallback(size, charName);

        // TinyUnits sheets: frame height = frame width = sheet.height
        int fh = sheet.getHeight();
        int fw = fh;  // square frames always
        int actualN = sheet.getWidth() / fw;
        frameIdx = frameIdx % Math.max(1, actualN);

        BufferedImage rawFrame = sheet.getSubimage(frameIdx * fw, 0, fw, fh);
        BufferedImage scaled   = scaleFrame(rawFrame, size);

        frameCache.put(cacheKey, scaled);
        return scaled;
    }

    /** Convenience: first idle frame — used for portraits. */
    public static BufferedImage getIdleFrame(String charName, int size) {
        return getFrame(charName, AnimType.IDLE, 0, size);
    }

    /** Flip an image horizontally (so right-side fighters face left). */
    public static BufferedImage flipH(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(src, w, 0, -w, h, null);
        g.dispose();
        return out;
    }

    /**
     * Load a background image (main menu).
     * Resources live at: /background/background1.png … background4.png
     */
    public static BufferedImage getBackground(int index, int w, int h) {
        String path = "/background/background" + (index + 1) + ".png";
        return loadAndScale(path, w, h);
    }

    /**
     * Load a battleground image (battle arena).
     * Resources live at: /battleground/battleground1.png … battleground4.png
     */
    public static BufferedImage getBattleground(int index, int w, int h) {
        String path = "/battleground/battleground" + (index + 1) + ".png";
        return loadAndScale(path, w, h);
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private static String[] findRow(String charName) {
        for (String[] row : CHAR_SPRITES) {
            if (row[0].equals(charName)) return row;
        }
        return null;
    }

    private static BufferedImage loadSheet(String path) {
        BufferedImage cached = rawCache.get(path);
        if (cached != null) return cached;
        try (InputStream is = SpriteLoader.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("SpriteLoader: missing resource: " + path);
                return null;
            }
            BufferedImage img = ImageIO.read(is);
            rawCache.put(path, img);
            return img;
        } catch (Exception e) {
            System.err.println("SpriteLoader: failed to load " + path + " — " + e.getMessage());
            return null;
        }
    }

    private static BufferedImage scaleFrame(BufferedImage src, int size) {
        // Maintain aspect ratio inside a square canvas
        double scale = Math.min((double) size / src.getWidth(), (double) size / src.getHeight());
        int dw = (int) (src.getWidth()  * scale);
        int dh = (int) (src.getHeight() * scale);
        int dx = (size - dw) / 2;
        int dy = (size - dh) / 2;

        BufferedImage out = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(src, dx, dy, dw, dh, null);
        g.dispose();
        return out;
    }

    private static BufferedImage loadAndScale(String path, int w, int h) {
        BufferedImage raw = loadSheet(path);
        if (raw == null) return null;
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(raw, 0, 0, w, h, null);
        g.dispose();
        return out;
    }

    private static BufferedImage makeFallback(int size, String charName) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0x30, 0x20, 0x40));
        g.fillRoundRect(0, 0, size, size, 8, 8);
        g.setColor(new Color(0xC9, 0xA8, 0x4C));
        g.setFont(new Font("SansSerif", Font.BOLD, Math.max(8, size / 5)));
        FontMetrics fm = g.getFontMetrics();
        String label = charName.substring(0, Math.min(3, charName.length()));
        g.drawString(label, (size - fm.stringWidth(label)) / 2, size / 2 + fm.getAscent() / 2 - 2);
        g.dispose();
        return img;
    }
}