package Main;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import mino.*;

public class PlayManager {
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int bottom_y;
    public static int top_y;

    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();
    public static int dropInterval = 60;
    boolean gameOver;

    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    int level = 1;
    int lines;
    int score;

    private static PlayManager instance;


    private PlayManager(){

        left_x = (GamePanel.WIDTH/2) - (WIDTH/2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 200;
        NEXTMINO_Y = top_y + 500;

        currentMino = MinoFactory.getRandomMino();
        currentMino.setXY(MINO_START_X,MINO_START_Y);
        nextMino = MinoFactory.getRandomMino();
        nextMino.setXY(NEXTMINO_X,NEXTMINO_Y);
    }

    public static PlayManager getInstance() {
        if (instance == null) {
            instance = new PlayManager();
        }
        return instance;
    }

    public void setNextMino() {

        currentMino = nextMino;

        if (currentMino == null) {
            currentMino = MinoFactory.getRandomMino();
            currentMino.setXY(MINO_START_X, MINO_START_Y);
        }

        nextMino = MinoFactory.getRandomMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }
    public void update(){

        if(currentMino.active == false){
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y){
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(2,false);
            }

            currentMino.deactivating = false;

            currentMino = nextMino;
            currentMino.setXY(MINO_START_X,MINO_START_Y);
            nextMino = MinoFactory.getRandomMino();
            nextMino.setXY(NEXTMINO_X,NEXTMINO_Y);

            checkDelete();
        }else {
            currentMino.update();
        }
    }
    private void checkDelete(){

        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while(x < right_x && y < bottom_y){

            for(int i = 0; i < staticBlocks.size(); i++){
                if(staticBlocks.get(i).x == x && staticBlocks.get(i).y == y){
                    blockCount++;
                }
            }

            x += Block.SIZE;

            if(x == right_x){

                if(blockCount == 12){

                    effectCounterOn = true;
                    effectY.add(y);

                    for(int i = staticBlocks.size()-1; i > -1; i--){
                        if(staticBlocks.get(i).y == y){
                            staticBlocks.remove(i);
                        }
                    }

                    lineCount++;
                    lines++;

                    if(lines % 10 == 0 && dropInterval > 1){
                        level++;
                        if(dropInterval > 10){
                            dropInterval -= 10;
                        }
                        else{
                            dropInterval -= 1;
                        }
                    }
                    for(int i = 0; i < staticBlocks.size(); i++){
                        if(staticBlocks.get(i).y < y){
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }

        if(lineCount > 0){
            GamePanel.se.play(1,false);
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.pink);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);


        g2.setColor(new Color(255, 255, 255, 35));

        for(int i = 1; i < 12; i++) {
            g2.drawLine(left_x + (i * Block.SIZE), top_y, left_x + (i * Block.SIZE), bottom_y);
        }
        for(int i = 1; i < 20; i++) {
            g2.drawLine(left_x, top_y + (i * Block.SIZE), right_x, top_y + (i * Block.SIZE));
        }

        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.setColor(Color.white);
        g2.drawRect(x + 20, y, 200, 200);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.setColor(Color.white);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 80, y + 60);

        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y); y+=70;
        g2.drawString("LINE: " + lines, x, y); y+=70;
        g2.drawString("SCORE: " + score, x, y);

        if (currentMino != null) {
            currentMino.draw(g2);
        }

        nextMino.draw(g2);

        for(int i = 0; i < staticBlocks.size();i++){
            staticBlocks.get(i).draw(g2);
        }
        if(effectCounterOn){
            effectCounter++;
            g2.setColor(Color.red);
            for(int i = 0; i < effectY.size(); i++){
                g2.fillRect(left_x,effectY.get(i),WIDTH,Block.SIZE);
            }
            if(effectCounter == 10){
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(g2.getFont().deriveFont(50f));
        if(gameOver) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("GAME OVER", left_x + 10, top_y + HEIGHT/2);
        }
        else if(KeyHandler.pausePressed){
            x = left_x + 90;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        x = 35;
        y = top_y + 320;
        g2.setColor(Color.white);
        g2.setFont(new Font("Times New Roman", Font.ITALIC, 110));
        g2.drawString("Tetris", x + 60, y - 50);

    }
}
