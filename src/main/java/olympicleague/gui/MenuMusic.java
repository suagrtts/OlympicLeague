package olympicleague.gui;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

final class MenuMusic {
    private static Clip clip;

    private MenuMusic() {}

    static synchronized void start() {
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
            // If audio fails (unsupported format / missing mixer), keep the game running silently.
        }
    }

    static synchronized void stop() {
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

