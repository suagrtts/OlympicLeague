package olympicleague.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class SpriteLoader {

    /**
     * AnimType — one entry per distinct animation a character can play.
     *
     * IDLE    — standing loop
     * ATTACK  — melee strike (warriors, lancers)
     * SHOOT   — ranged fire animation (archers); falls back to ATTACK if absent
     * HEAL    — heal/cast animation (monks); falls back to ATTACK if absent
     * HURT    — hit-stagger / guard
     */
    public enum AnimType { IDLE, ATTACK, SHOOT, HEAL, HURT }

    // ── Character sprite table ────────────────────────────────────────────────
    // Layout per row:
    //   [0]  charName
    //   [1]  idlePath       [2]  nIdle
    //   [3]  attackPath     [4]  nAttack
    //   [5]  shootPath      [6]  nShoot   ← same as attackPath when no ranged anim
    //   [7]  healPath       [8]  nHeal    ← same as attackPath when no heal anim
    //   [9]  hurtPath       [10] nHurt
    private static final String[][] CHAR_SPRITES = {
            {
                    "Achiron",
                    "/sprite/TinyUnits/Black Units/Lancer/Lancer_Idle.png",          "12",
                    "/sprite/TinyUnits/Black Units/Lancer/Lancer_Right_Attack.png",  "3",
                    "/sprite/TinyUnits/Black Units/Lancer/Lancer_Right_Attack.png",  "3",  // no ranged
                    "/sprite/TinyUnits/Black Units/Lancer/Lancer_Right_Attack.png",  "3",  // no heal
                    "/sprite/TinyUnits/Black Units/Lancer/Lancer_Idle.png",          "4",
            },
            {
                    "Atalyn",
                    "/sprite/TinyUnits/Red Units/Archer/Archer_Idle.png",   "6",
                    "/sprite/TinyUnits/Red Units/Archer/Archer_Shoot.png",  "8",   // ATTACK uses shoot
                    "/sprite/TinyUnits/Red Units/Archer/Archer_Shoot.png",  "8",   // SHOOT
                    "/sprite/TinyUnits/Red Units/Archer/Archer_Shoot.png",  "8",   // no heal
                    "/sprite/TinyUnits/Red Units/Archer/Archer_Run.png",    "4",
            },
            {
                    "Heralde",
                    "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Idle.png",    "8",
                    "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Attack1.png", "4",
                    "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Attack1.png", "4",  // no ranged
                    "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Attack1.png", "4",  // no heal
                    "/sprite/TinyUnits/Yellow Units/Warrior/Warrior_Guard.png",   "6",
            },
            {
                    "Vor",
                    "/sprite/TinyUnits/Purple Units/Warrior/Warrior_Idle.png",    "8",
                    "/sprite/TinyUnits/Purple Units/Warrior/Warrior_Attack1.png", "4",
                    "/sprite/TinyUnits/Purple Units/Warrior/Warrior_Attack1.png", "4",
                    "/sprite/TinyUnits/Purple Units/Warrior/Warrior_Attack1.png", "4",
                    "/sprite/TinyUnits/Purple Units/Warrior/Warrior_Guard.png",   "6",
            },
            {
                    "Orris",
                    "/sprite/TinyUnits/Red Units/Warrior/Warrior_Idle.png",    "8",
                    "/sprite/TinyUnits/Red Units/Warrior/Warrior_Attack1.png", "4",
                    "/sprite/TinyUnits/Red Units/Warrior/Warrior_Attack1.png", "4",
                    "/sprite/TinyUnits/Red Units/Warrior/Warrior_Attack1.png", "4",
                    "/sprite/TinyUnits/Red Units/Warrior/Warrior_Guard.png",   "6",
            },
            {
                    "Orven",
                    "/sprite/TinyUnits/Blue Units/Archer/Archer_Idle.png",   "6",
                    "/sprite/TinyUnits/Blue Units/Archer/Archer_Shoot.png",  "8",
                    "/sprite/TinyUnits/Blue Units/Archer/Archer_Shoot.png",  "8",
                    "/sprite/TinyUnits/Blue Units/Archer/Archer_Shoot.png",  "8",
                    "/sprite/TinyUnits/Blue Units/Archer/Archer_Run.png",    "4",
            },
            {
                    "Biji",
                    "/sprite/TinyUnits/Purple Units/Monk/Idle.png", "6",
                    "/sprite/TinyUnits/Purple Units/Monk/Heal.png", "11",  // ATTACK plays heal (monks cast)
                    "/sprite/TinyUnits/Purple Units/Monk/Heal.png", "11",  // no ranged
                    "/sprite/TinyUnits/Purple Units/Monk/Heal.png", "11",  // HEAL
                    "/sprite/TinyUnits/Purple Units/Monk/Run.png",  "4",
            },
            {
                    "Selwyn",
                    "/sprite/TinyUnits/Blue Units/Monk/Idle.png", "6",
                    "/sprite/TinyUnits/Blue Units/Monk/Heal.png", "11",
                    "/sprite/TinyUnits/Blue Units/Monk/Heal.png", "11",
                    "/sprite/TinyUnits/Blue Units/Monk/Heal.png", "11",
                    "/sprite/TinyUnits/Blue Units/Monk/Run.png",  "4",
            },
            {
                    "GoatedKit",
                    "/sprite/TinyUnits/Yellow Units/Archer/Archer_Idle.png",   "6",
                    "/sprite/TinyUnits/Yellow Units/Archer/Archer_Shoot.png",  "8",
                    "/sprite/TinyUnits/Yellow Units/Archer/Archer_Shoot.png",  "8",
                    "/sprite/TinyUnits/Yellow Units/Archer/Archer_Shoot.png",  "8",
                    "/sprite/TinyUnits/Yellow Units/Archer/Archer_Run.png",    "4",
            },
            {
                    "Skeleton",
                    "/sprite/TinyUnits/Black Units/Warrior/Warrior_Idle.png",    "8",
                    "/sprite/TinyUnits/Black Units/Warrior/Warrior_Attack1.png", "4",
                    "/sprite/TinyUnits/Black Units/Warrior/Warrior_Attack1.png", "4",
                    "/sprite/TinyUnits/Black Units/Warrior/Warrior_Attack1.png", "4",
                    "/sprite/TinyUnits/Black Units/Warrior/Warrior_Guard.png",   "6",
            },
            {
                    "EvilWizard",
                    "/sprite/TinyUnits/Black Units/Monk/Idle.png", "6",
                    "/sprite/TinyUnits/Black Units/Monk/Heal.png", "11",
                    "/sprite/TinyUnits/Black Units/Monk/Heal.png", "11",
                    "/sprite/TinyUnits/Black Units/Monk/Heal.png", "11",
                    "/sprite/TinyUnits/Black Units/Monk/Run.png",  "4",
            },
    };

    // Column index constants — makes the table easy to extend
    private static final int IDX_IDLE_PATH   = 1;
    private static final int IDX_IDLE_N      = 2;
    private static final int IDX_ATK_PATH    = 3;
    private static final int IDX_ATK_N       = 4;
    private static final int IDX_SHOOT_PATH  = 5;
    private static final int IDX_SHOOT_N     = 6;
    private static final int IDX_HEAL_PATH   = 7;
    private static final int IDX_HEAL_N      = 8;
    private static final int IDX_HURT_PATH   = 9;
    private static final int IDX_HURT_N      = 10;

    // ── VFX table ─────────────────────────────────────────────────────────────
    private static final String[][] VFX_SPRITES = {
            {"Heal",           "/effects/Heal/Spritesheet/Heal_spritesheet.png",                     "12"},
            {"HeavensFury",    "/effects/HeavensFury/Spritesheet/HeavensFury_spritesheet.png",       "12"},
            {"HolyNova",       "/effects/HolyNova/Spritesheet/HolyNova_spritesheet.png",             "10"},
            {"HolyShield",     "/effects/HolyShield/Spritesheet/HolyShield_spritesheet.png",         "11"},
            {"HolySlash_A",    "/effects/HolySlash_A/Spritesheet/HolySlash_A_spritesheet.png",       "5"},
            {"HolySlash_B",    "/effects/HolySlash_B/Spritesheet/HolySlash_B_spritesheet.png",       "4"},
            {"HolySlash_C",    "/effects/HolySlash_C/Spritesheet/HolySlash_C_spritesheet.png",       "7"},
            {"Smite",          "/effects/Smite/Spritesheet/Smite_spritesheet.png",                   "11"},
            {"SwordOfJustice", "/effects/SwordOfJustice/Spritesheet/SwordOfJustice_spritesheet.png", "13"},
    };

    // ── Caches ────────────────────────────────────────────────────────────────
    private static final Map<String, BufferedImage> rawCache   = new HashMap<>();
    private static final Map<String, BufferedImage> frameCache = new HashMap<>();

    // ── Public character frame API ────────────────────────────────────────────

    public static int getFrameCount(String charName, AnimType anim) {
        String[] row = findRow(charName);
        if (row == null) return 1;
        return Integer.parseInt(row[animToNIndex(anim)]);
    }

    public static BufferedImage getFrame(String charName, AnimType anim, int frameIdx, int size) {
        String[] row = findRow(charName);
        if (row == null) return makeFallback(size, charName);

        String path = row[animToPathIndex(anim)];

        String cacheKey = path + ":" + frameIdx + ":" + size;
        BufferedImage cached = frameCache.get(cacheKey);
        if (cached != null) return cached;

        BufferedImage sheet = loadSheet(path);
        if (sheet == null) return makeFallback(size, charName);

        int fh     = sheet.getHeight();
        int fw     = fh;
        int actualN = sheet.getWidth() / fw;
        frameIdx   = frameIdx % Math.max(1, actualN);

        BufferedImage rawFrame = sheet.getSubimage(frameIdx * fw, 0, fw, fh);
        BufferedImage scaled   = scaleFrame(rawFrame, size);

        frameCache.put(cacheKey, scaled);
        return scaled;
    }

    public static BufferedImage getIdleFrame(String charName, int size) {
        return getFrame(charName, AnimType.IDLE, 0, size);
    }

    // ── Public VFX API ────────────────────────────────────────────────────────

    public static int getVFXFrameCount(String effectName) {
        for (String[] row : VFX_SPRITES)
            if (row[0].equals(effectName)) return Integer.parseInt(row[2]);
        return 1;
    }

    public static BufferedImage getVFXFrame(String effectName, int frameIdx, int size) {
        for (String[] row : VFX_SPRITES) {
            if (!row[0].equals(effectName)) continue;
            String path  = row[1];
            int    n     = Integer.parseInt(row[2]);
            String cKey  = "VFX:" + path + ":" + frameIdx + ":" + size;
            BufferedImage cached = frameCache.get(cKey);
            if (cached != null) return cached;

            BufferedImage sheet = loadSheet(path);
            if (sheet == null) return null;

            int fh      = sheet.getHeight();
            int actualN = Math.max(1, sheet.getWidth() / fh);
            frameIdx    = (frameIdx % Math.max(1, n)) % actualN;

            BufferedImage raw    = sheet.getSubimage(frameIdx * fh, 0, fh, fh);
            BufferedImage scaled = scaleFrame(raw, size);
            frameCache.put(cKey, scaled);
            return scaled;
        }
        return null;
    }

    // ── Image utilities ───────────────────────────────────────────────────────

    public static BufferedImage flipH(BufferedImage src) {
        int w = src.getWidth(), h = src.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(src, w, 0, -w, h, null);
        g.dispose();
        return out;
    }

    public static BufferedImage getBackground(int index, int w, int h) {
        return loadAndScale("/background/background"   + (index + 1) + ".png", w, h);
    }

    public static BufferedImage getBattleground(int index, int w, int h) {
        return loadAndScale("/battleground/battleground" + (index + 1) + ".png", w, h);
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    private static int animToPathIndex(AnimType anim) {
        return switch (anim) {
            case IDLE   -> IDX_IDLE_PATH;
            case ATTACK -> IDX_ATK_PATH;
            case SHOOT  -> IDX_SHOOT_PATH;
            case HEAL   -> IDX_HEAL_PATH;
            case HURT   -> IDX_HURT_PATH;
        };
    }

    private static int animToNIndex(AnimType anim) {
        return switch (anim) {
            case IDLE   -> IDX_IDLE_N;
            case ATTACK -> IDX_ATK_N;
            case SHOOT  -> IDX_SHOOT_N;
            case HEAL   -> IDX_HEAL_N;
            case HURT   -> IDX_HURT_N;
        };
    }

    private static String[] findRow(String charName) {
        for (String[] row : CHAR_SPRITES)
            if (row[0].equals(charName)) return row;
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
        int dw = (int)(src.getWidth()  * scale);
        int dh = (int)(src.getHeight() * scale);
        BufferedImage out = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.drawImage(src, (size - dw) / 2, (size - dh) / 2, dw, dh, null);
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