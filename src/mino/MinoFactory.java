package mino;

import java.util.Random;

public class MinoFactory {

    public static Mino getRandomMino() {
        Mino mino = null;
        int i = new Random().nextInt(7);

        switch(i) {
            case 0: mino = new Mino_L1(); break;
            case 1: mino = new Mino_L2(); break;
            case 2: mino = new Mino_Square(); break;
            case 3: mino = new Mino_Bar(); break;
            case 4: mino = new Mino_T(); break;
            case 5: mino = new Mino_Z1(); break;
            case 6: mino = new Mino_Z2(); break;
        }
        return mino;
    }
}