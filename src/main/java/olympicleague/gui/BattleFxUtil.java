package olympicleague.gui;

import java.util.Locale;
import olympicleague.character.GameCharacter;

/**
 * Shared battle UI rules (damage log classification, archer presentation) used by
 * {@link BattlePanel} and {@link BattleSound}.
 */
public final class BattleFxUtil {

    private BattleFxUtil() {}

    /** Characters drawn with bow / arrow ranged attacks in the arena. */
    public static boolean isArcherStyleCharacter(GameCharacter c) {
        if (c == null) return false;
        String n = c.getName();
        return "Atalyn".equals(n) || "Orven".equals(n) || "GoatedKit".equals(n);
    }

    /** True when the battle log describes HP damage to the opponent (matches {@link BattlePanel} offensive branch). */
    public static boolean isOffensiveDamageLog(String resultLog) {
        if (resultLog == null) return false;
        String l = resultLog.toLowerCase(Locale.ROOT);
        return l.contains("dealt") || l.contains("hits for")
                || l.contains("deals") || l.contains("rips through")
                || l.contains("tears") || l.contains("true damage")
                || l.contains("strike 1") || l.contains("hit 1")
                || l.contains("cuts through") || l.contains("infects for")
                || l.contains("crashes for") || l.contains("damage to")
                || l.contains("damage and") || l.contains("strike for");
    }

    public static SpriteLoader.AnimType resolveAttackAnim(GameCharacter actor, String resultLog) {
        String name = actor.getName();
        if ("Atalyn".equals(name) || "Orven".equals(name) || "GoatedKit".equals(name)) {
            return SpriteLoader.AnimType.SHOOT;
        }
        if ("Biji".equals(name) || "Selwyn".equals(name) || "EvilWizard".equals(name)) {
            return SpriteLoader.AnimType.HEAL;
        }
        return SpriteLoader.AnimType.ATTACK;
    }
}