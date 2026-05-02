package Gui;
import java.awt.Color;
import java.awt.Font;

public class Theme {

    // --- Core Backgrounds ---
    public static final Color BACKGROUND = new Color(10, 10, 15); // Formerly BG_DEEP
    public static final Color PANEL_DARK = new Color(10, 10, 15);
    public static final Color PANEL_LIGHT = new Color(22, 24, 37); // Formerly BG_CARD

    // --- Text & Borders ---
    public static final Color FOREGROUND = new Color(232, 224, 204); // TEXT_LIGHT
    public static final Color TITLE_COLOR = new Color(201, 168, 76); // GOLD
    public static final Color TITLE_LIGHT = new Color(240, 208, 128); // GOLD_LIGHT
    public static final Color BORDER_DIM = new Color(201, 168, 76, 64);

    // --- Action Accents ---
    public static final Color ACCENT_RED = new Color(192, 57, 43);   // CRIMSON_ACCENT
    public static final Color ACCENT_BLUE = new Color(58, 184, 176); // TEAL_ACCENT
    public static final Color ACCENT_PURPLE = new Color(139, 92, 246);

    // --- Fonts ---
    public static final Font CONSOLE = new Font("Consolas", Font.PLAIN, 16);
    public static final Font TITLE = new Font("Consolas", Font.BOLD, 28);
    public static final Font SUBTITLE = new Font("Consolas", Font.BOLD, 18);
    public static final Font SMALL = new Font("Consolas", Font.PLAIN, 12);
    public static final Font HEADER = new Font("Monospaced", Font.BOLD, 20);
    public static final Font BUTTON_FONT = new Font("Serif", Font.BOLD, 14);

    // --- Utility Methods ---
    public static Color highlight(Color base, float factor) {
        int r = Math.min(255, (int)(base.getRed() * factor));
        int g = Math.min(255, (int)(base.getGreen() * factor));
        int b = Math.min(255, (int)(base.getBlue() * factor));
        return new Color(r, g, b);
    }
}