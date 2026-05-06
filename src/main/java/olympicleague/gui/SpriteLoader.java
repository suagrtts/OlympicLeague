package olympicleague.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class SpriteLoader {

    public enum AnimType { IDLE, ATTACK, HURT }

    // ── Character → sprite mapping ───────────────────────────────────────────
    private static final String[][] CHAR_SPRITES = {
        {
            "Achiron",
            "/sprite/TinyUnits/Black Units/Lancer/Lancer_Idle.png",         "12",
            "/sprite/TinyUnits/Black Units/Lancer/Lancer_Right_Attack.png", "3",
            "/sprite/TinyUnits/Black Units/Lancer/Lancer_Idle.png",         "4",
        },
        {
            "Atalyn",
            "/sprite/TinyUnits/Red Units/Archer/Archer_Idle.png",   "6",
            "/sprite/TinyUnits/Red Units/Archer/Archer_Shoot.png",  "8",
            "/sprite/TinyUnits/Red Units/Archer/Archer_Run.png",    "4",
        },
        {
            "Heralde",
            "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Idle.png",    "8",
            "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Attack1.png", "4",
            "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Guard.png",   "6",
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
        }
    };

    // ── NEW: VFX → sprite mapping ───────────────────────────────────────────
    private static final String[][] VFX_SPRITES = {
        {"Heal", "/effects/Heal/Spritesheet/Heal_spritesheet.png", "12"},
        {"HeavensFury", "/effects/HeavensFury/Spritesheet/HeavensFury_spritesheet.png", "12"},
        {"HolyNova", "/effects/HolyNova/Spritesheet/HolyNova_spritesheet.png", "10"},
        {"HolyShield", "/effects/HolyShield/Spritesheet/HolyShield_spritesheet.png", "11"},
        {"HolySlash_A", "/effects/HolySlash_A/Spritesheet/HolySlash_A_spritesheet.png", "5"},
        {"HolySlash_B", "/effects/HolySlash_B/Spritesheet/HolySlash_B_spritesheet.png", "4"},
        {"HolySlash_C", "/effects/HolySlash_C/Spritesheet/HolySlash_C_spritesheet.png", "7"},
        {"Smite", "/effects/Smite/Spritesheet/Smite_spritesheet.png", "11"},
        {"SwordOfJustice", "/effects/SwordOfJustice/Spritesheet/SwordOfJustice_spritesheet.png", "13"}
    };

    private static final int IDX_IDLE_PATH   = 1;
    private static final int IDX_IDLE_N      = 2;
    private static final int IDX_ATK_PATH    = 3;
    private static final int IDX_ATK_N       = 4;
    private static final int IDX_HURT_PATH   = 5;
    private static final int IDX_HURT_N      = 6;

    private static final Map<String, BufferedImage> rawCache = new HashMap<>();
    private static final Map<String, BufferedImage> frameCache = new HashMap<>();

    // ── Character Frame API ──────────────────────────────────────────────────
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

        int fh = sheet.getHeight();
        int fw = fh;  
        int actualN = sheet.getWidth() / fw;
        frameIdx = frameIdx % Math.max(1, actualN);

        BufferedImage rawFrame = sheet.getSubimage(frameIdx * fw, 0, fw, fh);
        BufferedImage scaled   = scaleFrame(rawFrame, size);

        frameCache.put(cacheKey, scaled);
        return scaled;
    }

    // ── NEW: VFX Frame API ───────────────────────────────────────────────────
    public static int getVFXFrameCount(String effectName) {
        for (String[] row : VFX_SPRITES) {
            if (row[0].equals(effectName)) return Integer.parseInt(row[2]);
        }
        return 1;
    }

    public static BufferedImage getVFXFrame(String effectName, int frameIdx, int size) {
        String path = "";
        int n = 1;
        for (String[] row : VFX_SPRITES) {
            if (row[0].equals(effectName)) {
                path = row[1];
                n = Integer.parseInt(row[2]);
                break;
            }
        }
        if (path.isEmpty()) return null;

        frameIdx = frameIdx % Math.max(1, n);
        String cacheKey = "VFX:" + path + ":" + frameIdx + ":" + size;
        BufferedImage cached = frameCache.get(cacheKey);
        if (cached != null) return cached;

        BufferedImage sheet = loadSheet(path);
        if (sheet == null) return null; 

        int fh = sheet.getHeight();
        int fw = fh;
        int actualN = Math.max(1, sheet.getWidth() / fw);
        frameIdx = frameIdx % actualN;

        BufferedImage rawFrame = sheet.getSubimage(frameIdx * fw, 0, fw, fh);
        BufferedImage scaled = scaleFrame(rawFrame, size);

        frameCache.put(cacheKey, scaled);
        return scaled;
    }

    // ── Utility Methods ──────────────────────────────────────────────────────
    public static BufferedImage getIdleFrame(String charName, int size) {
        return getFrame(charName, AnimType.IDLE, 0, size);
    }

    public static BufferedImage flipH(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(src, w, 0, -w, h, null);
        g.dispose();
        return out;
    }

    public static BufferedImage getBackground(int index, int w, int h) {
        String path = "/background/background" + (index + 1) + ".png";
        return loadAndScale(path, w, h);
    }

    public static BufferedImage getBattleground(int index, int w, int h) {
        String path = "/battleground/battleground" + (index + 1) + ".png";
        return loadAndScale(path, w, h);
    }

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
        double scale = Math.min((double) size / src.getWidth(), (double) size / src.getHeight());
        int dw = (int) (src.getWidth()  * scale);
        int dh = (int) (src.getHeight() * scale);
        int dx = (size - dw) / 2;
        int dy = (size - dh) / 2;

        BufferedImage out = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(src, dx, dy, dw, dh, null);
        g.dispose();
        return out;
    }

    private static BufferedImage loadAndScale(String path, int w, int h) {
        BufferedImage raw = loadSheet(path);
        if (raw == null) return null;
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
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