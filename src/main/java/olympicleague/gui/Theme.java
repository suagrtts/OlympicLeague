package gui;

import java.awt.Color;
import java.awt.Font;

public class Theme {

    public static final Color BACKGROUND = Color.BLACK;
    public static final Color FOREGROUND = new Color(0, 255, 0); // neon green (terminal style)

    public static final Color TITLE_COLOR = new Color(255, 215, 0); // gold
    public static final Color ACCENT_BLUE = new Color(0, 170, 255);
    public static final Color ACCENT_RED = new Color(255, 80, 80);
    public static final Color ACCENT_PURPLE = new Color(180, 120, 255);

    public static final Color PANEL_DARK = new Color(15, 15, 15);
    public static final Color PANEL_LIGHT = new Color(30, 30, 30);

    // Main terminal / ASCII font
    public static final Font CONSOLE = new Font("Consolas", Font.PLAIN, 16);

    // Titles / banners
    public static final Font TITLE = new Font("Consolas", Font.BOLD, 28);

    // Subtitles
    public static final Font SUBTITLE = new Font("Consolas", Font.BOLD, 18);

    // Small UI text
    public static final Font SMALL = new Font("Consolas", Font.PLAIN, 12);

    // Decorative bold ASCII headers
    public static final Font HEADER = new Font("Monospaced", Font.BOLD, 20);

    //ds
    public static Color highlight(Color base, float factor) {
        int r = Math.min(255, (int)(base.getRed() * factor));
        int g = Math.min(255, (int)(base.getGreen() * factor));
        int b = Math.min(255, (int)(base.getBlue() * factor));
        return new Color(r, g, b);
    }

    public static final Color BG_DEEP      = new Color(0x0A, 0x0A, 0x0F);
    public static final Color BG_CARD      = new Color(0x16, 0x18, 0x25);
    public static final Color BG_CARD2     = new Color(0x1C, 0x1E, 0x30);
    public static final Color GOLD         = new Color(0xC9, 0xA8, 0x4C);
    public static final Color GOLD_LIGHT   = new Color(0xF0, 0xD0, 0x80);
    public static final Color GOLD_DIM     = new Color(0x7A, 0x62, 0x30);
    public static final Color TEXT_LIGHT   = new Color(0xE8, 0xE0, 0xCC);
    public static final Color TEXT_DIM     = new Color(0x9A, 0x8F, 0x78);
    public static final Color CRIMSON      = new Color(0x8B, 0x1A, 0x1A);
    public static final Color CRIMSON_BRT  = new Color(0xC0, 0x39, 0x2B);
    public static final Color HP_GREEN     = new Color(0x2E, 0xCC, 0x71);
    public static final Color MP_BLUE      = new Color(0x52, 0x98, 0xD0);
    public static final Color SKILL_CD     = new Color(0x44, 0x44, 0x55);
    public static final Color BORDER       = new Color(0xC9, 0xA8, 0x4C, 60);

    public static final Font FONT_TITLE    = new Font("Serif",       Font.BOLD,  28);
    public static final Font FONT_HEADER   = new Font("Serif",       Font.BOLD,  18);
    public static final Font FONT_SUBHEAD  = new Font("Serif",       Font.BOLD,  14);
    public static final Font FONT_BODY     = new Font("SansSerif",   Font.PLAIN, 13);
    public static final Font FONT_MONO     = new Font("Monospaced",  Font.PLAIN, 12);
    public static final Font FONT_SKILL    = new Font("SansSerif",   Font.BOLD,  12);
    public static final Font FONT_SMALL    = new Font("SansSerif",   Font.PLAIN, 11);

    public static Color alpha(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }
}