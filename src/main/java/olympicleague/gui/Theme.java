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
}