package olympicleague.gui;

import java.awt.Color;
import java.awt.Font;

/**
 * Centralised design tokens for the Liga Olympica UI.
 * All colours and fonts should be referenced from here — never hard-coded inline.
 */
public final class Theme {

    private Theme() {} // utility class

    // ── Background tones ─────────────────────────────────────────────────────
    public static final Color BG_DEEP  = new Color(0x0A, 0x0A, 0x0F);
    public static final Color BG_CARD  = new Color(0x16, 0x18, 0x25);
    public static final Color BG_CARD2 = new Color(0x1C, 0x1E, 0x30);

    // ── Gold palette ─────────────────────────────────────────────────────────
    public static final Color GOLD       = new Color(0xC9, 0xA8, 0x4C);
    public static final Color GOLD_LIGHT = new Color(0xF0, 0xD0, 0x80);
    public static final Color GOLD_DIM   = new Color(0x7A, 0x62, 0x30);

    // ── Text ─────────────────────────────────────────────────────────────────
    public static final Color TEXT_LIGHT = new Color(0xE8, 0xE0, 0xCC);
    public static final Color TEXT_DIM   = new Color(0x9A, 0x8F, 0x78);

    // ── Accent ───────────────────────────────────────────────────────────────
    public static final Color CRIMSON     = new Color(0x8B, 0x1A, 0x1A);
    public static final Color CRIMSON_BRT = new Color(0xC0, 0x39, 0x2B);

    // ── Bar colours ──────────────────────────────────────────────────────────
    public static final Color HP_GREEN = new Color(0x2E, 0xCC, 0x71);
    public static final Color MP_BLUE  = new Color(0x52, 0x98, 0xD0);
    public static final Color SKILL_CD = new Color(0x44, 0x44, 0x55);

    // ── Borders ──────────────────────────────────────────────────────────────
    public static final Color BORDER = new Color(0xC9, 0xA8, 0x4C, 60);

    // ── Typography ───────────────────────────────────────────────────────────
    public static final Font FONT_TITLE   = new Font("Serif",      Font.BOLD,  28);
    public static final Font FONT_HEADER  = new Font("Serif",      Font.BOLD,  18);
    public static final Font FONT_SUBHEAD = new Font("Serif",      Font.BOLD,  14);
    public static final Font FONT_BODY    = new Font("SansSerif",  Font.PLAIN, 13);
    public static final Font FONT_MONO    = new Font("Monospaced", Font.PLAIN, 12);
    public static final Font FONT_SKILL   = new Font("SansSerif",  Font.BOLD,  12);
    public static final Font FONT_SMALL   = new Font("SansSerif",  Font.PLAIN, 11);

    // ── Utility helpers ──────────────────────────────────────────────────────

    /** Returns {@code color} with the given alpha (0–255). */
    public static Color alpha(Color color, int a) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
    }

    /** Darkens {@code color} by multiplying each channel by {@code factor} (0.0–1.0). */
    public static Color darker(Color color, float factor) {
        return new Color(
            Math.max(0, (int)(color.getRed()   * factor)),
            Math.max(0, (int)(color.getGreen() * factor)),
            Math.max(0, (int)(color.getBlue()  * factor)),
            color.getAlpha()
        );
    }
}