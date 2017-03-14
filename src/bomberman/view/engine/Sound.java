package bomberman.view.engine;

import javax.sound.sampled.*;
import java.io.InputStream;

public class Sound {

    private Clip clip;

    public Sound(InputStream in) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(in);
            AudioFormat af = audioInputStream.getFormat();
            int size = (int) (af.getFrameSize() * audioInputStream.getFrameLength());
            byte[] audio = new byte[size];
            DataLine.Info info = new DataLine.Info(Clip.class, af, size);
            audioInputStream.read(audio, 0, size);

            this.clip = (Clip) AudioSystem.getLine(info);
            if (clip != null) {
                this.clip.open(af, audio, 0, size);
            }

            audioInputStream.close();
        } catch (Exception e) {
        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
        }
    }

    public void start() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void loop(int count) {
        if (clip != null) {
            clip.loop(count);
        }
    }

}
