import java.awt.event.*;
import java.util.*;

public class Tetris implements ActionListener
{
    static final int UNIT_SIZE = 30;
    static final int SCREEN_WIDTH = 10*UNIT_SIZE;
    static final int SCREEN_HEIGHT = 20*UNIT_SIZE;
    static final int DELAY = 2; //in milliseconds
    static final String CLICK = "./stuff/click.wav";
    static final String SWOOSH = "./stuff/swoosh.wav";
    static final String THUMP = "./stuff/thump.wav";
    private int down = 40;
    private boolean downPressed = false;
    private int downDelayDummy = 0;
    private int softDropDummy = 0;
    int[] blockX = new int[4];
    int[] blockY = new int[4];
    int[] projX = new int[4];
    int[] projY = new int[4];
    int block;
    int nextBlock = (int) (Math.random()*7);
    int linesCleared = 0;
    int score = 0;
    int highScore = 0;
    int level = 0;
    private int levelLines = 0;
    boolean[][] rows = new boolean[23][12];
    boolean running = false;
    boolean paused = false;
    private javax.swing.Timer timer;

    ArrayList<Integer> commands = new ArrayList<Integer>();


    public Tetris() {
        startGame();
    }

    public void startGame() {
        running = true;
        newBlock();
        timer = new javax.swing.Timer(DELAY, this);
        timer.start();
    }

    public void newBlock() {
        block = nextBlock;
        nextBlock = (int) (Math.random()*7);
        //0: I block, 1: O block, 2: S block, 3: Z block, 4: T block, 5: L block, 6: J block
        //I block cyan, O block yellow, S block green, Z block red, T block purple, L block orange, J block blue
        //first coord is center of rotation
        switch (block) {
            case 0 -> {
                blockX = new int[]{SCREEN_WIDTH / 2 - UNIT_SIZE, SCREEN_WIDTH / 2 - 2 * UNIT_SIZE, SCREEN_WIDTH / 2, SCREEN_WIDTH / 2 + UNIT_SIZE};
                blockY = new int[]{0, 0, 0, 0};
            }
            case 1 -> {
                blockX = new int[]{SCREEN_WIDTH / 2 - UNIT_SIZE, SCREEN_WIDTH / 2 - UNIT_SIZE, SCREEN_WIDTH / 2, SCREEN_WIDTH / 2,};
                blockY = new int[]{0, UNIT_SIZE, 0, UNIT_SIZE};
            }
            case 2 -> {
                blockX = new int[]{SCREEN_WIDTH / 2, SCREEN_WIDTH / 2 - UNIT_SIZE, SCREEN_WIDTH / 2, SCREEN_WIDTH / 2 + UNIT_SIZE};
                blockY = new int[]{UNIT_SIZE, UNIT_SIZE, 0, 0};
            }
            case 3 -> {
                blockX = new int[]{SCREEN_WIDTH / 2, SCREEN_WIDTH / 2 - UNIT_SIZE, SCREEN_WIDTH / 2, SCREEN_WIDTH / 2 + UNIT_SIZE};
                blockY = new int[]{0, 0, UNIT_SIZE, UNIT_SIZE};
            }
            case 4 -> {
                blockX = new int[]{SCREEN_WIDTH / 2, SCREEN_WIDTH / 2 - UNIT_SIZE, SCREEN_WIDTH / 2, SCREEN_WIDTH / 2 + UNIT_SIZE};
                blockY = new int[]{UNIT_SIZE, UNIT_SIZE, 0, UNIT_SIZE};
            }
            case 5 -> {
                blockX = new int[]{SCREEN_WIDTH / 2 + UNIT_SIZE, SCREEN_WIDTH / 2 - UNIT_SIZE, SCREEN_WIDTH / 2, SCREEN_WIDTH / 2 + UNIT_SIZE};
                blockY = new int[]{UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, 0};
            }
            case 6 -> {
                blockX = new int[]{SCREEN_WIDTH / 2 - UNIT_SIZE, SCREEN_WIDTH / 2 - UNIT_SIZE, SCREEN_WIDTH / 2, SCREEN_WIDTH / 2 + UNIT_SIZE};
                blockY = new int[]{UNIT_SIZE, 0, UNIT_SIZE, UNIT_SIZE};
            }
        }
        adjustProjection();
    }


    public void move() {
        for (int i = 0; i < 4; i++) {
            blockY[i] = blockY[i] + UNIT_SIZE;
        }
    }
    public void rotate(boolean clockwise) {
        int[] newX = new int[4];
        int[] newY = new int[4];
        boolean canRotate = true;
        boolean fixed = false;
        for (int i = 0; i < 4; i++) {
            if (clockwise) {
                newX[i] = (blockY[i] - blockY[0]) + blockX[0];
                newY[i] = -(blockX[i] - blockX[0]) + blockY[0];
            }
            else {
                newX[i] = -(blockY[i] - blockY[0]) + blockX[0];
                newY[i] = (blockX[i] - blockX[0]) + blockY[0];
            }
            if (!fixed) {
                canRotate = !(newX[i] < 0 || newX[i] >= SCREEN_WIDTH || newY[i] >= SCREEN_HEIGHT || rows[(SCREEN_HEIGHT - newY[i])/UNIT_SIZE][newX[i]/UNIT_SIZE]);
                fixed = !canRotate;
            }
        }

        if (!canRotate) {
            if (Math.min(Math.min(newX[0], newX[1]), Math.min(newX[2], newX[3])) < 0) {
                canRotate = true;
                for (int i = 0; i < 4; i++) {
                    newX[i] += UNIT_SIZE;
                    canRotate = !(newX[i] < 0 || rows[(SCREEN_HEIGHT - newY[i])/UNIT_SIZE][newX[i]/UNIT_SIZE]);
                    if (!canRotate) {
                        break;
                    }
                }
                if (!canRotate) {
                    canRotate = true;
                    for (int i = 0; i < 4; i++) {
                        newX[i] += UNIT_SIZE;
                        canRotate = !(newX[i] < 0 || rows[(SCREEN_HEIGHT - newY[i])/UNIT_SIZE][newX[i]/UNIT_SIZE]);
                        if (!canRotate) {
                            break;
                        }
                    }
                }
            }
            else if (Math.max(Math.max(newX[0], newX[1]), Math.max(newX[2], newX[3])) >= SCREEN_WIDTH) {
                canRotate = true;
                for (int i = 0; i < 4; i++) {
                    newX[i] -= UNIT_SIZE;
                    canRotate = !(newX[i] >= SCREEN_WIDTH || rows[(SCREEN_HEIGHT - newY[i])/UNIT_SIZE][newX[i]/UNIT_SIZE]);
                    if (!canRotate) {
                        break;
                    }
                }
                if (!canRotate) {
                    canRotate = true;
                    for (int i = 0; i < 4; i++) {
                        newX[i] -= UNIT_SIZE;
                        canRotate = !(newX[i] >= SCREEN_WIDTH || rows[(SCREEN_HEIGHT - newY[i])/UNIT_SIZE][newX[i]/UNIT_SIZE]);
                        if (!canRotate) {
                            break;
                        }
                    }
                }
            }
            else if (Math.max(Math.max(newY[0], newY[1]), Math.max(newY[2], newY[3])) >= SCREEN_HEIGHT) {
                canRotate = true;
                for (int i = 0; i < 4; i++) {
                    newY[i] -= UNIT_SIZE;
                    canRotate = !(newY[i] >= SCREEN_HEIGHT || rows[(SCREEN_HEIGHT - newY[i])/UNIT_SIZE][newX[i]/UNIT_SIZE]);
                    if (!canRotate) {
                        break;
                    }
                }
                if (!canRotate) {
                    canRotate = true;
                    for (int i = 0; i < 4; i++) {
                        newY[i] -= UNIT_SIZE;
                        canRotate = !(newY[i] >= SCREEN_HEIGHT || rows[(SCREEN_HEIGHT - newY[i])/UNIT_SIZE][newX[i]/UNIT_SIZE]);
                        if (!canRotate) {
                            break;
                        }
                    }
                }
            }
        }

        if (block != 1 && canRotate) {
            for (int i = 0; i < 4; i++) {
                blockX[i] = newX[i];
                blockY[i] = newY[i];
            }
        }

        adjustProjection();
    }
    public void moveRight() {
        boolean notBlockedRight = true;
        for (int i = 0; i < 4; i++) {
            if (blockX[i] == SCREEN_WIDTH - UNIT_SIZE || rows[(SCREEN_HEIGHT-blockY[i])/UNIT_SIZE][blockX[i]/UNIT_SIZE + 1]) {
                notBlockedRight = false;
                break;
            }
        }
        if (notBlockedRight) {
            for (int i = 0; i < 4; i++) {
                blockX[i] = blockX[i] + UNIT_SIZE;
            }
            adjustProjection();
        }
    }
    public void moveLeft() {
        boolean notBlockedLeft = true;
        for (int i = 0; i < 4; i++) {
            if (blockX[i] == 0 || rows[(SCREEN_HEIGHT-blockY[i])/UNIT_SIZE][blockX[i]/UNIT_SIZE - 1]) {
                notBlockedLeft = false;
                break;
            }
        }
        if (notBlockedLeft) {
            for (int i = 0; i < 4; i++) {
                blockX[i] = blockX[i] - UNIT_SIZE;
            }
            adjustProjection();
        }
    }
    public void hardDrop() {
        score += ((SCREEN_HEIGHT - blockY[0])/UNIT_SIZE)*2;
        blockX = projX.clone();
        blockY = projY.clone();
    }
    public boolean checkLanded() {
        for (int i = 0; i < 4; i++) {
            if (blockY[i] >= SCREEN_HEIGHT - UNIT_SIZE || rows[(SCREEN_HEIGHT-blockY[i])/UNIT_SIZE-1][blockX[i]/UNIT_SIZE]) {
                for (int z = 0; z < 4; z++) {
                    rows[(SCREEN_HEIGHT - blockY[z])/UNIT_SIZE][blockX[z]/UNIT_SIZE] = true;
                }
                newBlock();
                return true;
            }
        }
        return false;
    }
    public void adjustProjection() {
        projX = blockX.clone();
        projY = blockY.clone();
        unicorn:
        while (true) {
            for (int i = 0; i < 4; i++) {
                if (projY[i] == SCREEN_HEIGHT-UNIT_SIZE || rows[(SCREEN_HEIGHT-projY[i])/UNIT_SIZE - 1][projX[i]/UNIT_SIZE]) {
                    break unicorn;
                }
            }
            for (int i = 0; i < 4; i++) {
                projY[i] += UNIT_SIZE;
            }
        }
    }
    public void checkRows() {
        int linesAtOnce = 0;
        for (int i = 0; i < 20; i++) {
            boolean isFull = true;
            for (int k = 0; k < 10; k++) {
                if (!rows[i][k]) {
                    isFull = false;
                    break;
                }
            }
            if (isFull) {
                System.arraycopy(rows, i + 1, rows, i, 19 - i);
                rows[19] = new boolean[10];
                linesCleared++;
                linesAtOnce++;
                i--;
            }
        }
        levelLines += linesAtOnce;
        if (levelLines >= 10) {
            level++;
            levelLines -= 10;
            if (down-3 > 0) {
                down -= 3;
            }
            downDelayDummy = 0;
        }
        switch (linesAtOnce) {
            case 1 -> score += 40 * (level + 1);
            case 2 -> score += 100 * (level + 1);
            case 3 -> score += 300 * (level + 1);
            case 4 -> score += 1200 * (level + 1);
        }
    }
    public void checkLose() {
        if (rows[19][4] && rows[19][5]) {
            timer.stop();
            running = false;
        }
    }
    public void restart() {
        System.out.println("restarting");
        if (score > highScore) {
            highScore = score;
        }
        score = 0;
        linesCleared = 0;
        level = 0;
        levelLines = 0;
        down = 40;
        downDelayDummy = 0;
        rows = new boolean[23][12];
        running = true;
        paused = false;
        nextBlock = (int) (Math.random()*7);
        newBlock();
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int s : commands) {
            if (!paused) {
                if (s == KeyEvent.VK_LEFT) {
                    moveLeft();
                }
                else if (s == KeyEvent.VK_RIGHT) {
                    moveRight();
                }
                else if (s == KeyEvent.VK_Z) {
                    rotate(true);
                }
                else if (s == KeyEvent.VK_UP) {
                    rotate(false);
                }
                else if (s == KeyEvent.VK_DOWN) {
                    downPressed = true;
                }
                else if (s == KeyEvent.VK_SPACE) {
                    hardDrop();
                }
                else if (s == KeyEvent.VK_P) {
                    paused = true;
                }
                else if (s == -1) {
                    downPressed = false;
                }
            }
            else {
                if (s == KeyEvent.VK_P) {
                    paused = false;
                }
            }
        }
        commands = new ArrayList<Integer>();


        if (running && !paused) {
            if (!checkLanded()) {
                if (downPressed) {
                    if (softDropDummy == Math.min(down/2, 3)) {
                        move();
                        softDropDummy = 0;
                        score++;
                    }
                    else {
                        softDropDummy++;
                    }
                }
                else {
                    if (downDelayDummy == down) {
                        move();
                        downDelayDummy = 0;
                    } else {
                        downDelayDummy++;
                    }
                }
                checkLanded();
            }
            checkRows();
            checkLose();
        }
    }
}

