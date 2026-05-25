package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public static boolean upPressed, downPressed, leftPressed, rightPressed, pausePressed;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if(code ==  KeyEvent.VK_W){
            upPressed = true;
        }
        if(code ==  KeyEvent.VK_S){
            downPressed = true;
        }
        if(code ==  KeyEvent.VK_A){
            leftPressed = true;
        }
        if(code ==  KeyEvent.VK_D){
            rightPressed = true;
        }
        if(code == KeyEvent.VK_SPACE){
            if(pausePressed){
                pausePressed = false;
                GamePanel.music.play(0,true);
                GamePanel.music.loop();
            }
            else{
                pausePressed = true;
                GamePanel.music.stop();
            }
        }
        if (code == KeyEvent.VK_M) {
            if (GamePanel.music.volumeScale > 0) {
                GamePanel.music.volumeScale--;
                GamePanel.music.checkVolume();
            }
        }
        if (code == KeyEvent.VK_N) {
            if (GamePanel.music.volumeScale < 5) {
                GamePanel.music.volumeScale++;
                GamePanel.music.checkVolume();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
