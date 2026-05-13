package olympicleague.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;
import java.util.Locale;
import olympicleague.character.GameCharacter;
import olympicleague.character.Skill;
import olympicleague.gui.panels.BattleFxUtil;

public final class BattleSound {

    private BattleSound() {}

    /**
     * True when the battle log corresponds to sword/spear/blade-oriented skills
     * (Twin Slash, Divine Slash, Spear Thrust, Blade Veil text containing "sword", etc.).
     */
    public static boolean isSwordSkillLog(String resultLog) {
        if (resultLog == null) return false;
        String l = resultLog.toLowerCase(Locale.ROOT);
        return l.contains("slash")
                || l.contains("spear")
                || l.contains("sword")
                || l.contains("blade")
                || l.contains("judgment")
                || l.contains("justice");
    }

    /**
     * Heal, shield, damage reduction, untargetable / evade — uses skill metadata with log fallback.
     */
    public static boolean isHealShieldEvadeSkill(Skill skill, String resultLog) {
        String bundle = bundle(skill, resultLog);
        if (bundle.isEmpty()) return false;
        if (matchesHealShieldEvade(bundle)) return true;
        if (resultLog != null) {
            String l = resultLog.toLowerCase(Locale.ROOT);
            return l.contains("evade") || l.contains("untargetable") || l.contains("elusive")
                    || l.contains("cannot be targeted") || l.contains("vanish")
                    || l.contains("incoming damage halved") || l.contains("halved")
                    || l.contains("heal") || l.contains("recovered") || l.contains("shield");
        }
        return false;
    }

    /**
     * Sword / spear / stab / slash attacks — excludes bow shots and pure magic bolts when identifiable by name.
     */
    public static boolean isSwordSpearAttackSkill(Skill skill, String resultLog) {
        if (skill != null) {
            String n = skill.getName().toLowerCase(Locale.ROOT);
            if (n.contains("arrow") || n.contains("bolt") || n.contains("chord")
                    || n.contains("wave") || n.contains("bait") || n.contains("shatter")
                    || (n.contains("wrath") && n.contains("thunder"))
                    || n.contains("hack") || n.contains("symphony") || n.contains("kit kit")
                    || n.contains("drain")) {
                return false;
            }
            String d = skill.getDescription().toLowerCase(Locale.ROOT);
            if (n.contains("slash") || n.contains("spear") || n.contains("thrust")
                    || n.contains("stab") || n.contains("sword")
                    || (n.contains("blade") && !n.contains("veil"))) {
                return true;
            }
            if ((n.contains("strike") || n.contains("cut"))
                    && !n.contains("thunder") && !n.contains("lightning")) {
                return true;
            }
            if (d.contains("spear") || d.contains("sword") || d.contains("slash") || d.contains("stab")) {
                return true;
            }
        }
        if (resultLog != null) {
            String l = resultLog.toLowerCase(Locale.ROOT);
            if (l.contains("spear thrust") || l.contains("twin slash") || l.contains("time slash")
                    || l.contains("lion's strike") || l.contains("swift strike")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Plays appropriate battle SFX from the resolved skill (or log-only fallback). Skips failed casts.
     */
    public static void playSkillSfx(Skill skill, String resultLog) {
        playSkillSfx(skill, resultLog, null);
    }

    /**
     * Same as {@link #playSkillSfx(Skill, String)}; when {@code source} is set, archer-style characters
     * playing an offensive damage skill get {@link #playArrowShoot()} instead of sword SFX.
     */
    public static void playSkillSfx(Skill skill, String resultLog, GameCharacter source) {
        if (resultLog != null && resultLog.toLowerCase(Locale.ROOT).contains("not enough mana")) {
            return;
        }
        if (isHealShieldEvadeSkill(skill, resultLog)) {
            playHeal();
            return;
        }
        if (source != null
                && BattleFxUtil.isArcherStyleCharacter(source)
                && BattleFxUtil.isOffensiveDamageLog(resultLog)) {
            playArrowShoot();
            return;
        }
        if (isSwordSpearAttackSkill(skill, resultLog) || isSwordSkillLog(resultLog)) {
            playSwordCut();
        }
    }

    public static void playArrowShoot() {
        playResource("/sound/arrow-shoot.wav", "OlympicLeague-arrow");
    }

    public static void playSwordCut() {
        playResource("/sound/swordcut.wav", "OlympicLeague-swordcut");
    }

    public static void playGameOver() {
        playResource("/sound/gameover.wav", "OlympicLeague-gameover");
    }

    public static void playHeal() {
        playResource("/sound/heal.wav", "OlympicLeague-heal");
    }

    /** Full campaign / VS-AI match victory (not a single round). */
    public static void playGrandWinner() {
        playResource("/sound/grandwinner.wav", "OlympicLeague-grandwinner");
    }

    /** A single round win (best-of-3 round, or any non–grand-finale round win). */
    public static void playRoundWin() {
        playResource("/sound/won.wav", "OlympicLeague-roundwin");
    }

    private static String bundle(Skill skill, String resultLog) {
        StringBuilder sb = new StringBuilder();
        if (skill != null) {
            sb.append(skill.getName()).append(' ').append(skill.getDescription());
        }
        if (resultLog != null) {
            sb.append(' ').append(resultLog);
        }
        return sb.toString().toLowerCase(Locale.ROOT);
    }

    private static boolean matchesHealShieldEvade(String b) {
        if (b.contains("heal") || b.contains("hymn") || (b.contains("blessing") && b.contains("heal"))) {
            return true;
        }
        if (b.contains("soul drain") || (b.contains("drain") && b.contains("hp"))) {
            return true;
        }
        if (b.contains("shield") || b.contains("hide") || b.contains("aegis")
                || b.contains("ocean's shield") || b.contains("iron hide")
                || b.contains("respawn shield") || b.contains("undying resolve")) {
            return true;
        }
        if (b.contains("blade veil") || b.contains("temporal shift") || b.contains("hunter's reflex")
                || b.contains("vanish") || b.contains("rat spot") || b.contains("eternal guardian")) {
            return true;
        }
        if (b.contains("evade") || b.contains("untargetable") || b.contains("reflex")) {
            return true;
        }
        return b.contains("absorb") && b.contains("dmg");
    }

    private static void playResource(String resourcePath, String threadName) {
        URL url = BattleSound.class.getResource(resourcePath);
        if (url == null) {
            System.err.println("BattleSound: missing classpath resource " + resourcePath);
            return;
        }
        Thread sfx = new Thread(() -> openAndPlay(url, resourcePath), threadName);
        sfx.setDaemon(true);
        sfx.start();
    }

    private static void openAndPlay(URL url, String label) {
        AudioInputStream fileIn = null;
        AudioInputStream decoded = null;
        try {
            fileIn = AudioSystem.getAudioInputStream(url);
            AudioFormat raw = fileIn.getFormat();
            if (needsPcmConversion(raw)) {
                AudioFormat pcm = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        raw.getSampleRate(),
                        16,
                        raw.getChannels(),
                        raw.getChannels() * 2,
                        raw.getSampleRate(),
                        false);
                decoded = AudioSystem.getAudioInputStream(pcm, fileIn);
            } else {
                decoded = fileIn;
            }

            Clip clip = AudioSystem.getClip();
            clip.open(decoded);
            clip.addLineListener(ev -> {
                if (ev.getType() == LineEvent.Type.STOP) {
                    try {
                        ev.getLine().close();
                    } catch (Exception ignored) {
                    }
                }
            });
            clip.start();
        } catch (Exception e) {
            System.err.println("BattleSound(" + label + "): " + e.getMessage());
        } finally {
            try {
                if (decoded != null && decoded != fileIn) {
                    decoded.close();
                } else if (fileIn != null) {
                    fileIn.close();
                }
            } catch (Exception ignored) {
            }
        }
    }

    private static boolean needsPcmConversion(AudioFormat f) {
        return !AudioFormat.Encoding.PCM_SIGNED.equals(f.getEncoding())
                || f.getSampleSizeInBits() != 16
                || f.isBigEndian();
    }
}
