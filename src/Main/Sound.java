package Main;

import  java.net.URL;
import javax.sound.sampled.*;

public class Sound {

    Clip musicClip;
    URL url[] = new URL[10];

    public int volumeScale = 3;
    float volume;
    FloatControl fc;

    public Sound(){
        url[0] = getClass().getResource("/res/game_music.wav");
        url[1] = getClass().getResource("/res/delete_line.wav");
        url[2] = getClass().getResource("/res/gameover.wav");
        url[3] = getClass().getResource("/res/rotation.wav");
        url[4] = getClass().getResource("/res/touch_floor.wav");
    }

    public void checkVolume(){
        if (fc == null) return;

        switch (volumeScale) {
            case 0: volume = -80f; break;
            case 1: volume = -20f; break;
            case 2: volume = -12f; break;
            case 3: volume = -5f;  break;
            case 4: volume = 1f;   break;
            case 5: volume = 6f;   break;
        }
        fc.setValue(volume);
    }

    public void play(int i, boolean music){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();

            if(music){
                musicClip = clip;
            }
            clip.open(ais);

            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();

            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if(event.getType() == LineEvent.Type.STOP)
                        clip.close();
                }
            });
            ais.close();
            clip.start();
        }catch(Exception e){

        }
    }
    public void loop(){
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        musicClip.stop();
        musicClip.close();
    }
}
