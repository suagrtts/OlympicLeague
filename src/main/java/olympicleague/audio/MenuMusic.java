package olympicleague.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

// 1. Change 'final class' to 'public final class'
public final class MenuMusic {
    private static Clip clip;

    // 2. Make the constructor public
    public MenuMusic() {}

    // 3. Change 'static' to 'public static'
    public static synchronized void start() {
        if (clip != null && clip.isRunning()) return;

        stop();

        try {
            URL url = MenuMusic.class.getResource("/sound/menu.wav");
            if (url == null) return;

            try (AudioInputStream in = AudioSystem.getAudioInputStream(url)) {
                Clip c = AudioSystem.getClip();
                c.open(in);
                c.loop(Clip.LOOP_CONTINUOUSLY);
                c.start();
                clip = c;
            }
        } catch (Exception ignored) {
        }
    }

    // 4. Change 'static' to 'public static'
    public static synchronized void stop() {
        if (clip == null) return;
        try {
            clip.stop();
            clip.close();
        } catch (Exception ignored) {
        } finally {
            clip = null;
        }
    }
}