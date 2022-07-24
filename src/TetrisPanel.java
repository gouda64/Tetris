import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class TetrisPanel extends JPanel implements ActionListener
{
    static final int UNIT_SIZE = 30;
    static final int SCREEN_WIDTH = 10*UNIT_SIZE;
    static final int SCREEN_HEIGHT = 20*UNIT_SIZE;
    static final int DELAY = 2; //in milliseconds
    static final String CLICK = "./stuff/click.wav";
    static final String SWOOSH = "./stuff/swoosh.wav";
    static final String THUMP = "./stuff/thump.wav";

    private JPanel end = new JPanel();
    private JLabel congrats = new JLabel();
    private JLabel congrats2 = new JLabel();
    private JLabel scoreText = new JLabel();

    private Timer timer;
    Tetris tetris;
    private int lines = 0;
    private int blockY = 0;

    public TetrisPanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH+1, SCREEN_HEIGHT+1));
        this.setLayout(null);
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(new TetrisKeyAdapter());

        end.setLayout(new FlowLayout());
        end.setSize(new Dimension(226, 130));
        end.setBorder(BorderFactory.createLineBorder(Color.black));
        end.add(new JLabel("Game Over"));
        end.add(congrats);
        end.add(congrats2);
        end.add(scoreText);
        end.add(Box.createVerticalGlue());

        JButton restart = new JButton("Restart");
        restart.setActionCommand("Restart");
        restart.addActionListener(this);
        end.add(restart);

        tetris = new Tetris();

        startGame();
    }

    public void startGame() {
        timer = new Timer(DELAY, this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if (tetris.running && !tetris.paused) {
            //past blocks
            g.setColor(Color.gray);
            for (int i = 0; i < 10; i++) {
                for (int k = 0; k < 20; k++) {
                    if (tetris.rows[k][i]) {
                        g.fillRect(UNIT_SIZE*i,SCREEN_HEIGHT - UNIT_SIZE*k, UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }
            //block projection
            switch (tetris.block) {
                case 0 -> g.setColor(new Color(200, 255, 255));
                case 1 -> g.setColor(new Color(255, 255, 200));
                case 2 -> g.setColor(new Color(200, 255, 200));
                case 3 -> g.setColor(new Color(255, 200, 200));
                case 4 -> g.setColor(new Color(255, 200, 255));
                case 5 -> g.setColor(new Color(255, 200, 150));
                case 6 -> g.setColor(new Color(200, 200, 255));
            }
            for(int i = 0; i < 4; i++) {
                g.fillRect(tetris.projX[i],tetris.projY[i], UNIT_SIZE, UNIT_SIZE);
            }
            //block
            switch (tetris.block) {
                case 0 -> g.setColor(new Color(0, 255, 255));
                case 1 -> g.setColor(new Color(255, 255, 0));
                case 2 -> g.setColor(new Color(0, 255, 0));
                case 3 -> g.setColor(new Color(255, 0, 0));
                case 4 -> g.setColor(new Color(255, 0, 255));
                case 5 -> g.setColor(new Color(255, 128, 0));
                case 6 -> g.setColor(new Color(0, 0, 255));
            }
            for(int i = 0; i < 4; i++) {
                g.fillRect(tetris.blockX[i],tetris.blockY[i], UNIT_SIZE, UNIT_SIZE);
            }
            //grid
            g.setColor(Color.black);
            for(int i = 0; i <= SCREEN_HEIGHT/UNIT_SIZE; i++) {
                g.drawLine(i*UNIT_SIZE,0, i*UNIT_SIZE,SCREEN_HEIGHT);
                g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH,i*UNIT_SIZE);
            }
        }
        else if (tetris.paused) {
            int x = (SCREEN_WIDTH - g.getFontMetrics().stringWidth("You're not trying to cheat, are you?"))/2;
            int y = (SCREEN_HEIGHT - g.getFontMetrics().getHeight())/2;
            g.drawString("You're not trying to cheat, are you?", x, y);
        }
        else if (!tetris.running) {
            gameOver(g);
        }
    }

    public void playSound(String filePath) {
        try {
            AudioInputStream inputStream =  AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
        }
        catch (Exception e) {
            System.out.println("Houston we have a problem (with sound files)");
        }
    }
    public void checkLose() {
        if (!tetris.running) {

            end.setLocation(SCREEN_WIDTH/2 - 113, SCREEN_HEIGHT/2 - 65);
            if (0 == tetris.level) {
                congrats.setText("My dead great-grandma could");
                congrats2.setText("do better than you.");
            }
            else if (tetris.level == 1 || tetris.level == 2) {
                congrats.setText("    My grandma could do    ");
                congrats2.setText("  better than you. ");
            }
            else if (3 <= tetris.level && tetris.level <= 5) {
                congrats.setText("   My mom could do better  ");
                congrats2.setText("     than you.     ");
            }
            else if (6 <= tetris.level && tetris.level <= 9) {
                congrats.setText("My brother could do better");
                congrats2.setText("     than you.     ");
            }
            else {
                congrats.setText("Damnnnn, are you hacking?");
            }
            if (tetris.score > tetris.highScore) {
                scoreText.setText("    You beat your high score.   ");
            }
            else {
                scoreText.setText("You didn't beat your high score.");
            }

            this.add(end);

            timer.stop();

            end.revalidate();
        }
    }
    public void gameOver(Graphics g) {
        //past blocks
        g.setColor(Color.gray);
        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 21; k++) {
                if (tetris.rows[k][i]) {
                    g.fillRect(UNIT_SIZE*i,SCREEN_HEIGHT - UNIT_SIZE*k, UNIT_SIZE, UNIT_SIZE);
                }
            }
        }
        //grid
        g.setColor(Color.black);
        for(int i = 0; i <= SCREEN_HEIGHT/UNIT_SIZE; i++) {
            g.drawLine(i*UNIT_SIZE,0, i*UNIT_SIZE,SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH,i*UNIT_SIZE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String c = "Stromboli";
        if (e != null && !(e.getActionCommand() == null)) {
            c = e.getActionCommand();
        }
        if (c.equals("Restart")) {
            tetris.restart();
            this.remove(end);
            timer.start();
        }
        checkLose();

        if (!tetris.paused && tetris.running) {
            if (lines != tetris.linesCleared) {
                playSound(SWOOSH);
                lines = tetris.linesCleared;
            }
            if (blockY != tetris.blockY[0]) {
                blockY = tetris.blockY[0];
                playSound(CLICK);
            }
        }

        repaint();
    }

    public class TetrisKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int s = e.getKeyCode();
            if (!tetris.paused) {
                switch (s) {
                    case KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_Z, KeyEvent.VK_UP -> playSound(CLICK);
                    case KeyEvent.VK_SPACE -> playSound(THUMP);
                }
                tetris.commands.add(s);
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            int s = e.getKeyCode();
            if (s == KeyEvent.VK_DOWN) {
                tetris.commands.add(-1); //released down signal
            }
        }
    }
}
