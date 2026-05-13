package olympicleague.gui;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;

public final class BattleSound {

    private BattleSound() {}

    /**
     * True when the battle log corresponds to sword/spear/blade-oriented skills
     * (Twin Slash, Divine Slash, Spear Thrust, Blade Veil text containing "sword", etc.).
     */
    public static boolean isSwordSkillLog(String resultLog) {
        String l = resultLog.toLowerCase();
        return l.contains("slash")
                || l.contains("spear")
                || l.contains("sword")
                || l.contains("blade")
                || l.contains("judgment")
                || l.contains("justice");
    }

    /**
     * Plays sword SFX off the EDT. Decodes to PCM_SIGNED 16-bit LE when needed — many
     * WAVs (float / 24-bit / big-endian) fail {@link Clip#open(AudioInputStream)} otherwise.
     */
    public static void playSwordCut() {
        playResource("/sound/swordcut.wav", "OlympicLeague-swordcut");
    }

    public static void playGameOver() {
        playResource("/sound/gameover.wav", "OlympicLeague-gameover");
    }

    public static void playHeal() {
        playResource("/sound/heal.wav", "OlympicLeague-heal");
    }

    public static void playGrandWinner() {
        playResource("/sound/won.wav", "OlympicLeague-won");
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
